package org.bharath.captchaservice.exception;

public class CustomException extends RuntimeException {

	private static final long serialVersionUID = -7009146299497113747L;

	public CustomException(String message) {
		super(message);
	}
}
