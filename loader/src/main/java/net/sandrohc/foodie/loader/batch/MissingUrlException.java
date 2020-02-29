/*
 * Copyright (c) 2020. Authored by SandroHc
 */

package net.sandrohc.foodie.loader.batch;

public class MissingUrlException extends Exception {

	public MissingUrlException() {
	}

	public MissingUrlException(String message) {
		super(message);
	}

	public MissingUrlException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingUrlException(Throwable cause) {
		super(cause);
	}

	public MissingUrlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
