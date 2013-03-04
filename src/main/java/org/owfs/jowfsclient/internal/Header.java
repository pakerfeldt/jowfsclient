/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient.internal;

/**
 * This class represents the header of a packet to/from owserver.
 *
 * @author Patrik Akerfeldt
 */
public class Header {

	private int version;
	private int payloadLength;
	private int function;
	//private int returnValue;
	private Flags flags;
	private int dataLength;
	private int offset;

	//SUPPRESS CHECKSTYLE ParameterNumber
	public Header(int version, int payloadLength, int function, Flags flags, int dataLength, int offset) {
		this.version = version;
		this.payloadLength = payloadLength;
		this.function = function;
		this.flags = flags;
		this.dataLength = dataLength;
		this.offset = offset;
	}

	/**
	 * Returns the version field in the header
	 *
	 * @return the version field in the header
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Returns the length of the payload
	 *
	 * @return the payload length field in the header
	 */
	public int getPayloadLength() {
		return payloadLength;
	}

	/**
	 * If the header comes from a request packet this returns the message type.
	 * If the header comes from a response packet this returns the return value.
	 *
	 * @return the function field in the header
	 */
	public int getFunction() {
		return function;
	}

	/**
	 * Returns a Flags representation of the flags field in the header.
	 *
	 * @return a Flags representation of the flags field in the header.
	 */
	public Flags getFlags() {
		return flags;
	}

	/**
	 * Returns the data length field in the header.
	 *
	 * @return the data length field in the header.
	 */
	public int getDataLength() {
		return dataLength;
	}

	/**
	 * Returns the offset field in the header.
	 *
	 * @return the offset field in the header.
	 */
	public int getOffset() {
		return offset;
	}

	public String toString() {
		return "Version: " + version + "\nPayloadLength: " + payloadLength
				+ "\nFunction: " + function + "\nflags: " + flags
				+ "\nDataLength: " + dataLength + "\nOffset: " + offset;
	}

}
