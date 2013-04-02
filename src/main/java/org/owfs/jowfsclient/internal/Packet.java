/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient.internal;

/**
 * This abstract class represents a owserver protocol packet.
 *
 * @author Patrik Akerfeldt
 */
public abstract class Packet {

	private Header header;

	/**
	 * Constructs a new {@code Packet}.
	 *
	 * @param version       the value of the version field.
	 * @param payloadLength the value of payload length field.
	 * @param function      the value of the message type / return value field.
	 * @param dataLength    the value of the data length field.
	 * @param flags         a {@link Flags} representation of the value of the flags field.
	 * @param offset        the value of the offset field.
	 */
	//SUPPRESS CHECKSTYLE ParameterNumber
	public Packet(int version, int payloadLength, int function, int dataLength, Flags flags, int offset) {
		header = new Header(version, payloadLength, function, flags, dataLength, offset);
	}

	public Header getHeader() {
		return header;
	}
}
