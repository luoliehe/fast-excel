package org.utils.fastexcel.exception;

/**
 * 映射异常
 * 
 * @author luoliehe
 */
public class MappingException extends EaseExcelException {

	private static final long serialVersionUID = 1L;

	public MappingException() {
		super();
	}

	public MappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MappingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MappingException(String message) {
		super(message);
	}

	public MappingException(Throwable cause) {
		super(cause);
	}

}
