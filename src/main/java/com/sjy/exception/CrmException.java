/**
 * 
 */
package com.sjy.exception;

import java.text.MessageFormat;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * 
 * @since 2017年1月10日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@SuppressWarnings("serial")
public class CrmException extends RuntimeException {
	protected int code = CrmExceptionType.Customize_Error;
	protected Throwable cause;

	/**
	 * Constructor for MagicException.
	 */
	public CrmException() {
		super();
	}

	/**
	 * Constructor for MagicException.
	 */
	public CrmException(int code) {
		super();
		this.code = code;
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
	 */
	public CrmException(int code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * Constructor for MagicException.
	 * 
	 * @param message
	 * @param objs
	 */
	public CrmException(String message, Object... objs) {
		super(MessageFormat.format(message, objs));
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
		if (super.getMessage() == null)
			return cause == null ? "" : "原因:" + cause.getMessage();
		else
			return super.getMessage();

	}

	public String toString() {
		return getClass().getSimpleName() + ": " + getMessage();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
