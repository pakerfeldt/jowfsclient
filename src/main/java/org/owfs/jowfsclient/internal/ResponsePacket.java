/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient.internal;

/**
 * {@code ResponsePacket} represents response packets received from the
 * owserver.
 * 
 * @author Patrik Akerfeldt
 * 
 */
public class ResponsePacket extends Packet {

	String payload;

	/*
	 * Should have a field for return code
	 */

	/**
	 * Constructs a new {@code ResponsePacket}.
	 * 
	 * @param version
	 *            the value of the version header field.
	 * @param payloadLength
	 *            the value of the payload length header field.
	 * @param returnValue
	 *            the value of the function / return value header field.
	 * @param flags
	 *            a {@link Flags} representation of the flags header field.
	 * @param dataLength
	 *            the value of the data length header field.
	 * @param offset
	 *            the value of the offset header field.
	 * @param payload
	 *            the payload of the packet.
	 */
	public ResponsePacket(int version, int payloadLength, int returnValue,
			Flags flags, int dataLength, int offset, String payload) {
		super(version, payloadLength, returnValue, dataLength, flags, 0);
		this.payload = payload;
	}

	/**
	 * Returns the payload of this packet.
	 * 
	 * @return the payload of this packet.
	 */
	public String getPayload() {
		return payload;
	}
}
