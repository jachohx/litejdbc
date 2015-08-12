package org.jachohx.litejdbc.exception;

public class InternalException  extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public InternalException(Throwable cause) {
        super(cause);  
    }

    public InternalException(String message) {
        super(message);
    }
}
