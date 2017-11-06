package org.utils.fastexcel.exception;

public class IndexDuplicationException extends MappingException {

	private static final long serialVersionUID = 1L;

	public IndexDuplicationException() {
		super();
	}

	public IndexDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IndexDuplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public IndexDuplicationException(String message) {
		super(message);
	}

	public IndexDuplicationException(Throwable cause) {
		super(cause);
	}

}
