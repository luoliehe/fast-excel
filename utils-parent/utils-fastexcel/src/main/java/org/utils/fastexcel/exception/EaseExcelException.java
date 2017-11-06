package org.utils.fastexcel.exception;

public class EaseExcelException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EaseExcelException() {
		super();
	}

	public EaseExcelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EaseExcelException(String message, Throwable cause) {
		super(message, cause);
	}

	public EaseExcelException(String message) {
		super(message);
	}

	public EaseExcelException(Throwable cause) {
		super(cause);
	}
	
}
