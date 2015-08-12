package org.jachohx.litejdbc.exception;


public class InitException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public InitException() {
        super();
    }

    public InitException(String message) {
        super(message);
    }

    public InitException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitException(Throwable cause) {
        super(cause);   
    }
}
