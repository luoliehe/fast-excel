package org.utils.fastexcel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;
import org.utils.fastexcel.annotation.EaseCell;
import org.utils.fastexcel.annotation.EaseRow;
import org.utils.fastexcel.exception.IndexDuplicationException;
import org.utils.fastexcel.exception.MappingException;
import org.utils.fastexcel.exception.ValidateException;

import com.sun.media.sound.InvalidFormatException;

/**
 * 
 * 将行转换对象，核心处理类
 * 
 * @author luoliehe
 * @param <T>
 */
public class CoreMapper<T> {

	/** 数据校验 */
	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	private final Class<T> beanClass;
	private final Field[] fieldArray;
	private final org.utils.fastexcel.annotation.EaseRow excelRow;
	private final EaseCell[] excelCellArray;
	private final Map<Class<?>[], CellType> classMap = new HashMap<>();
	private final Map<Annotation, CellFormat> formatClassMap = new HashMap<>();
	
	public final int maxColumnNum;

	public CoreMapper(Class<T> beanClass) throws MappingException {
		if (!beanClass.isAnnotationPresent(EaseRow.class)) {
			throw new MappingException("非映射对象");
		}
		this.excelRow = beanClass.getAnnotation(EaseRow.class);
		this.beanClass = beanClass;

		Map<Integer, Field> map = new HashMap<>();
		for (Field field : beanClass.getDeclaredFields()) {
			if (!field.isAnnotationPresent(EaseCell.class)) {
				continue;
			}
			EaseCell excelCell = field.getAnnotation(EaseCell.class);
			if (map.containsKey(excelCell.index())) {
				Field f = map.get(excelCell.index());
				throw new IndexDuplicationException("字段[" + f.getName() + "]与字段[ " + field.getName()+" ] index 重复");
			}
			map.put(excelCell.index(), field);
		}

		this.maxColumnNum = map.size();
		this.fieldArray = new Field[maxColumnNum];
		this.excelCellArray = new EaseCell[maxColumnNum];
		for (int index = 0; index < maxColumnNum; index++) {
			if (!map.containsKey(index)) {
				throw new MappingException("下标不连续，请确保存在  EaseCell 设置了 index = " + index + " 的值");
			}
			Field field = map.get(index);
			EaseCell excelCell = field.getAnnotation(EaseCell.class);
			if (excelCell.format() != CellFormat.class) {
				Class<?> formatClass = excelCell.format();
				try {
					formatClassMap.put(excelCell, (CellFormat) formatClass.newInstance());
				} catch (ReflectiveOperationException e) {
					throw new MappingException("自定义格式类 [ " + formatClass.getName() + " ] 不能实例化", e);
				}
			}
			fieldArray[index] = field;
			excelCellArray[index] = excelCell;
		}

		init();
	}

	public EaseCell[] getExcelCellArray() {
		return excelCellArray;
	}

	/**
	 * 校验表头是否正确，作为判断Excel文档的正确性
	 * 
	 * @param sheet
	 * @throws MappingException
	 */
	public void validateSheetHeader(Row firstRow) throws MappingException {
		for (int index = 0; index < maxColumnNum; index++) {
			Cell cell = CellUtil.getCell(firstRow, index);
			cell.setCellType(CellType.STRING);
			String title = cell.getStringCellValue();
			EaseCell cm = excelCellArray[index];
			if (StringUtils.equals(cm.name(), title) || StringUtils.contains(cm.label(), title)
					|| StringUtils.contains(title, cm.label())) {
				continue;
			}
			throw new MappingException(String.format("第 [ %s ]列，请按照模板填写 [ %s ]", index + 1, cm.name()));
		}
	}
	
	public void validateSheetHeader(Sheet sheet) throws MappingException {
		validateSheetHeader(CellUtil.getRow(0, sheet));
	}

	/**
	 * 将行转成对象
	 * 
	 * @param row
	 * @return
	 * @throws MappingException
	 * @throws ValidateException
	 */
	public T map(Row row) throws MappingException, ValidateException {
		T bean;
		try {
			bean = beanClass.newInstance();
		} catch (Exception e) {
			throw new MappingException("Bean不能被实例化[ " + beanClass.getName() + " ]", e);
		}

		Map<String, Object> properties = new HashMap<>();
		for (int index = 0; index < maxColumnNum; index++) {
			Cell cell = CellUtil.getCell(row, index);
			Field field = fieldArray[index];
			EaseCell excelCell = field.getAnnotation(EaseCell.class);

			CellFormat format = formatClassMap.get(excelCell);
			if (format != null) {
				// 自定义格式化
				properties.put(field.getName(), format.format(cell));
			} else {
				// 程序默认处理
				CellType cellType = getCellMappingType(field.getGenericType());
				
				if (cell.getCellTypeEnum() != cellType) {
					cell.setCellType(cellType);
				}
				switch (cellType) {
				case STRING:
					properties.put(field.getName(), cell.getStringCellValue());
					break;
				case NUMERIC:
					properties.put(field.getName(), cell.getNumericCellValue());
					break;
				case BOOLEAN:
					properties.put(field.getName(), cell.getBooleanCellValue());
					break;
				default:
					properties.put(field.getName(), null);
				}
			}
		}
		// 设置属性
		try {
			BeanUtils.populate(bean, properties);
		} catch (ReflectiveOperationException e) {
			throw new MappingException("设置值出错", e);
		}
		// 校验数据遵循 JSR303校验规则
		try {
			validateData(bean);
		} catch (ValidateException e) {
			throw new ValidateException("第[ " + (row.getRowNum() + 1) + " ]行出错： " + e.getMessage());
		}
		return bean;
	}

	/**
	 * 创建一个模板文件
	 * 
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public File createTemplateFile(File dir) throws InvalidFormatException, IOException {
		String name = StringUtils.isBlank(excelRow.value()) ? "template.xlsx" : excelRow.value() + ".xlsx";
		File file = new File(new File(dir, "temp"), name);
		FileUtils.touch(file);
		try (OutputStream os = new FileOutputStream(file)) {
			createTemplate(os);
		}
		return file;
	}

	/**
	 * 创建一个模板文件
	 * 
	 * @param os
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public void createTemplate(OutputStream os) throws InvalidFormatException, IOException {
		CoreMapper<T> rowMapper = new CoreMapper<>(beanClass);
		try (HSSFWorkbook book = new HSSFWorkbook();) {
			Sheet sheet = book.createSheet("模板");
			Row row = sheet.createRow(1);
			EaseCell[] array = rowMapper.getExcelCellArray();
			for (int i = 0; i < array.length; i++) {
				CellUtil.createCell(row, i, array[i].label());
			}
			book.write(os);
		}
	}

	private void init() {
		Class<?>[] stringClass = { String.class, Character.class };
		classMap.put(stringClass, CellType.STRING);
		

		Class<?>[] numberClass = { short.class, int.class, long.class, float.class, double.class, Short.class,
				Integer.class, Long.class, Float.class, Double.class, Date.class };

		classMap.put(numberClass, CellType.NUMERIC);

		Class<?>[] booleanClass = { boolean.class, Boolean.class };
		classMap.put(booleanClass, CellType.BOOLEAN);
	}

	private CellType getCellMappingType(Type type) {
		for (Map.Entry<Class<?>[], CellType> entry : classMap.entrySet()) {
			if (ArrayUtils.contains(entry.getKey(), type)) {
				return entry.getValue();
			}
		}
		throw new IllegalArgumentException("未找到类型" + type.getTypeName());
	}

	public void validateData(T bean) {
		Set<ConstraintViolation<T>> set = validator.validate(bean);
		
		java.util.List<String> error = new ArrayList<>();
		for (ConstraintViolation<T> constraintViolation : set) {
			error.add(constraintViolation.getMessage());
		}
		if(!error.isEmpty()) {
			throw new ValidateException(StringUtils.join(error, ","));
		}
	}
	
	public List<String> getExtend(Row row, int size) {
		List<String> list = new ArrayList<>();
		for (int columnIndex = 0; columnIndex < size; columnIndex++) {
			Cell cell = CellUtil.getCell(row, maxColumnNum + columnIndex);
			cell.setCellType(CellType.STRING);
			list.add(cell.getStringCellValue());
		}
		return list;
	}

}
