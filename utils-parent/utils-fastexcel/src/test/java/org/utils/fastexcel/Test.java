package org.utils.fastexcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.utils.fastexcel.exception.ValidateException;

import com.sun.media.sound.InvalidFormatException;

public class Test {
	
	public  void importTest() throws FileNotFoundException, IOException, InvalidFormatException, EncryptedDocumentException, org.apache.poi.openxml4j.exceptions.InvalidFormatException{
		File xls = new File("C:\\Users\\admin\\Desktop\\【线索量名单】上汽大众朗行10.11.xls");

		StringBuffer sb = new StringBuffer();
		long l = System.currentTimeMillis();
		CoreMapper<Bean> mapper = new CoreMapper<>(Bean.class);
		try (InputStream is = new FileInputStream(xls)) {
			
			Sheet sheet = WorkbookFactory.create(is).getSheetAt(0);
			Iterator<Row> iterator = sheet.rowIterator();
			mapper.validateSheetHeader(sheet);
			for (int rowNum = 0; iterator.hasNext(); rowNum++) {
				Row row = iterator.next();
				if (rowNum == 0) {
					continue;
				}
				Bean bean = mapper.map(row);
				try {
					System.out.println(bean);
				} catch (ValidateException e) {
					sb.append(e.getMessage()).append(System.lineSeparator());
				}
			}
			System.out.println(sb);
			File file = new File("C:\\Users\\admin\\Desktop\\repot.txt");
			if(!file.exists()) {
				file.createNewFile();
			}
			IOUtils.write(sb.toString(), new FileOutputStream(file));
		}
		System.out.println("系统"+(System.currentTimeMillis() - l));
	}
	
	public void createTemplateTest() throws InvalidFormatException, IOException {
		CoreMapper<Bean> mapper = new CoreMapper<>(Bean.class);
		System.out.println(mapper.createTemplateFile(new File(".")));
	}
}
