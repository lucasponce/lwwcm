package org.gatein.lwwcm;

public class WcmAuthorizationException extends Exception {

	public WcmAuthorizationException() {
		super();
	}

	public WcmAuthorizationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WcmAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public WcmAuthorizationException(String message) {
		super(message);
	}

	public WcmAuthorizationException(Throwable cause) {
		super(cause);
	}

}
