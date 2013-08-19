package org.gatein.lwwcm;

/**
 * Exception coming from wcm system.
 */
public class WcmException extends Exception {

	public WcmException() {
		super();
	}

	public WcmException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WcmException(String message, Throwable cause) {
		super(message, cause);
	}

	public WcmException(String message) {
		super(message);
	}

	public WcmException(Throwable cause) {
		super(cause);
	}
	
}
