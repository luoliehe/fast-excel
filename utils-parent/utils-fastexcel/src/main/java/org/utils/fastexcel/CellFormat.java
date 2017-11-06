package org.utils.fastexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.utils.fastexcel.annotation.EaseCell;

/**
 * 
 * Cell内容自定操作处理接口
 * 
 * @author luoliehe
 * @see EaseCell#format()
 */
public interface CellFormat {

	/**
	 * 用户自定义处理某个字段值，返回值将反射进这个属性
	 * 
	 * @param cell
	 * @return
	 */
	Object format(Cell cell);

}
