/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.batch;

public class MissingIdException extends Exception {

	public MissingIdException() {
	}

	public MissingIdException(String message) {
		super(message);
	}

	public MissingIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingIdException(Throwable cause) {
		super(cause);
	}

	public MissingIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
