/**
 * 
 */
package com.sjy.exception;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2017年1月10日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@SuppressWarnings("serial")
public class CrmException extends RuntimeException {
	protected Throwable cause;

	/**
	 * Constructor for MagicException.
	 */
	public CrmException() {
		super();
	}

	/**
	 * Constructor for MagicException.
	 * 
	 * @param message
	 */
	public CrmException(String message) {
		super(message);
	}

	/**
	 * Constructor for MagicException.
	 * 
	 * @param message
	 * @param cause
	 */
	public CrmException(String message, Throwable cause) {
		super(message);
		this.cause = cause;
	}

	/**
	 * Constructor for MagicException.
	 * 
	 * @param cause
	 */
	public CrmException(Throwable cause) {
		super();
		this.cause = cause;
	}

	/**
	 * @return Throwable
	 */
	public Throwable getCause() {
		return cause;
	}

	public String getMessage() {
		if (super.getMessage() == null) return cause == null ? "" : "原因:" + cause.getMessage();
		else return super.getMessage();

	}

	public String toString() {
		return getClass().getSimpleName() + ": " + getMessage();
	}

}
