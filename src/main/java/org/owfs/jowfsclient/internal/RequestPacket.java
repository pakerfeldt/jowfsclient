/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient.internal;

import org.owfs.jowfsclient.Enums.OwMessageType;

/**
 * {@code RequestPacket} represents request packets sent to the owserver. The
 * class has two constructors depending on the request is a write request or
 * not.
 *
 * @author Patrik Akerfeldt
 */
public class RequestPacket extends Packet {

	byte[] payload;
	byte[] dataToWrite;
	boolean writingData = false;

	/**
	 * Constructs a new {@code RequestPacket} that is not supposed to write
	 * anything to any elements in the owserver.
	 *
	 * @param function   the value of the message type header field.
	 * @param dataLength value of the data length header field.
	 * @param flags      the {@link Flags} to associate with this owserver protocol
	 *                   packet.
	 * @param payload    the payload to include in the packet.
	 */
	public RequestPacket(OwMessageType function, int dataLength, Flags flags, String payload) {
		super(0, payload.length() + 1, function.intValue, dataLength, flags, 0);

		byte[] bytes = payload.getBytes();
		this.payload = new byte[bytes.length + 1];
		int i = 0;
		for (; i < bytes.length; i++) {
			this.payload[i] = bytes[i];
		}
		//TODO can be removed since arrays are already initialized
		this.payload[i] = 0;

	}

	/**
	 * Constructs a new {@code RequestPacket} that is supposed to write
	 * something to an element in the owserver.
	 *
	 * @param function    the message type
	 * @param flags       the {@link Flags} to associate with this owserver protocol
	 *                    packet.
	 * @param payload     the payload to include in the packet.
	 * @param dataToWrite the new value to write to the owserver element (defined by the
	 *                    payload).
	 */
	/* TODO: Check that datalength really should be dataToWrite.length() here */
	public RequestPacket(OwMessageType function, Flags flags, String payload, String dataToWrite) {
		super(0, payload.length() + 1 + dataToWrite.length(), function.intValue, dataToWrite.length(), flags, 0);

		byte[] bytes = payload.getBytes();
		this.payload = new byte[bytes.length + 1];
		int i = 0;
		for (; i < bytes.length; i++) {
			this.payload[i] = bytes[i];
		}
		this.payload[i] = 0;

		bytes = dataToWrite.getBytes();
		this.dataToWrite = new byte[bytes.length];
		for (i = 0; i < bytes.length; i++) {
			this.dataToWrite[i] = bytes[i];
		}
		writingData = true;

	}

	/**
	 * Returns the payload as a {@code byte} array.
	 *
	 * @return the payload as a {@code byte} array.
	 */
	public byte[] getPayload() {
		return payload;
	}

	/**
	 * Returns true if the packet is supposed to write something to an owserver
	 * element, otherwise false.
	 *
	 * @return true if the packet is supposed to write something to an owserver
	 *         element, otherwise false.
	 */
	public boolean isWritingData() {
		return writingData;
	}

	/**
	 * Returns the {@code OwMessageType} from the packet.
	 *
	 * @return the {@code OwMessageType} from the packet.
	 */
	public OwMessageType getFunction() {
		return OwMessageType.getEnum(getHeader().getFunction());
	}

	/**
	 * Returns the data, as a {@code byte} array that is to be written.
	 *
	 * @return the data, as a {@code byte} array that is to be written.
	 */
	public byte[] getDataToWrite() {
		return dataToWrite;
	}

}
