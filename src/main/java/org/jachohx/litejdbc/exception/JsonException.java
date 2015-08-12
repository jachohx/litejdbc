package org.jachohx.litejdbc.exception;


public class JsonException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public JsonException() {
        super();
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(Throwable cause) {
        super(cause);   
    }
}
