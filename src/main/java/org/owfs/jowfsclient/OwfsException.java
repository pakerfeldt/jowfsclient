/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient;

/**
 * This exception represents errors returned by the owserver.
 *
 * @author Patrik Akerfeldt
 */
public class OwfsException extends Exception {

	private static final long serialVersionUID = 2322213126593948880L;
	private final int errorCode;

	/**
	 * Constructs a new {@code OwfsException}.
	 *
	 * @param message   a descriptive message of the error occurred.
	 * @param errorCode the error code received from owserver.
	 */
	public OwfsException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Returns the error code associated with this exception.
	 *
	 * @return the error code associated with this exception.
	 */
	public int getErrorCode() {
		return errorCode;
	}
}
