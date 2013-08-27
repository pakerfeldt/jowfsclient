/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient.internal;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.owfs.jowfsclient.Enums;
import org.owfs.jowfsclient.OwfsConnection;
import org.owfs.jowfsclient.OwfsConnectionConfig;
import org.owfs.jowfsclient.OwfsException;

/**
 * This class is used by applications that wants to communicate with the
 * owserver over the owserver protocol.
 *
 * @author Patrik Akerfeldt
 */
public class OwfsConnectionImpl implements OwfsConnection {

	private static final int OWNET_DEFAULT_DATALEN = 4096; // default data
	// length

	private OwfsConnectionConfig config;
	private Socket owSocket;
	private DataInputStream owIn;
	private DataOutputStream owOut;

	/**
	 * Constructs a new OwClient with the specified connection details.
	 *
	 * @param config connection configuration including owfs host location and protocol specific settings such as device name format etc.
	 */
	public OwfsConnectionImpl(OwfsConnectionConfig config) {
		this.config = config;
	}

	@Override
	public void setConfiguration(OwfsConnectionConfig config) {
		this.config = config;
	}

	/**
	 * Creates the socket and two data streams used when communicating with the
	 * owserver and returns the result as {@code boolean}.
	 *
	 * @param force true if the connect should be forced.
	 * @return true if the socket and the two data streams (in and out) was
	 *         created, otherwise false.
	 * @throws IOException
	 */
	private boolean connect(boolean force) throws IOException {
		if (force) {
			closeSocketAndDataStreams();
		}
		if (owSocket == null || !owSocket.isConnected()) {
			return tryToSocketConnectionAndStreamsInitialization();
		} else {
			return false;
		}
	}

	private boolean tryToSocketConnectionAndStreamsInitialization() throws IOException {
		owSocket = new Socket();
		try {
			owSocket.connect(new InetSocketAddress(config.getHostName(), config.getPortNumber()), config.getConnectionTimeout());
			owIn = new DataInputStream(owSocket.getInputStream());
			owOut = new DataOutputStream(owSocket.getOutputStream());
			return true;
		} catch (SocketTimeoutException ste) {
			owSocket = null;
			return false;
		}
	}

	@Override
	public void disconnect() throws IOException {
		closeSocketAndDataStreams();
	}

	private void closeSocketAndDataStreams() throws IOException {
		if (owSocket != null) {
			owSocket.close();
		}
		owSocket = null;
		close(owOut);
		owOut = null;
		close(owIn);
		owIn = null;
	}

	private void close(Closeable closeable) throws IOException {
		if (closeable != null) {
			closeable.close();
		}
	}

	private void establishConnectionIfNeeded() throws IOException {
		if (owSocket == null || !owSocket.isConnected() || !isPersistenceEnabled()) {
			connect(true);
		}
	}

	private void disconnectIfConfigured() throws IOException {
		if (!isPersistenceEnabled()) {
			disconnect();
		}
	}

	private boolean isPersistenceEnabled() {
		return config.getFlags().getPersistence() == Enums.OwPersistence.ON;
	}

	@Override
	public String read(String path) throws IOException, OwfsException {
		ResponsePacket response;
		RequestPacket request = new RequestPacket(Enums.OwMessageType.READ, OWNET_DEFAULT_DATALEN, config.getFlags(), path);
		sendRequest(request);
		do {
			response = readPacket();
			// Ignore PING messages (i.e. messages with payload length -1)
		} while (response.getHeader().getPayloadLength() == -1);

		disconnectIfConfigured();
		return response.getPayload();
	}

	@Override
	public void write(String path, String dataToWrite) throws IOException, OwfsException {
		RequestPacket request = new RequestPacket(Enums.OwMessageType.WRITE, config.getFlags(), path, dataToWrite);
		sendRequest(request);
		/*
		* Even if we're not interested in the result of the response packet
		* we must read the packet from the socket. Partly to clean incoming
		* bytes but also in order to throw exceptions on error.
		*/
		readPacket();
		disconnectIfConfigured();
	}

	@Override
	public Boolean exists(String path) throws IOException, OwfsException {
		ResponsePacket response;
		RequestPacket request = new RequestPacket(Enums.OwMessageType.PRESENCE, 0, config.getFlags(), path);
		sendRequest(request);
		response = readPacket();

		disconnectIfConfigured();
		/*
		* FIXME: This must be taken care of, method should return false if
		* device does not exist, not throw an exception as it does no, in
		* readPacket(). Check what message are returned on error and what
		* are return when device does not exist.
		*/
		//TODO Refactor readPacket() <- more information there
		return response.getHeader().getFunction() >= 0;

	}

	/**
	 * This method is not working properly for alarming directory.
	 */
	@Override
	public List<String> listDirectoryAll(String path) throws OwfsException, IOException {
		RequestPacket request = new RequestPacket(Enums.OwMessageType.DIRALL, 0, config.getFlags(), path);
		sendRequest(request);
		ResponsePacket response = readPacket();
		List<String> list = new ArrayList<String>();
		if (response != null && response.getPayload() != null) {
			String[] arr = response.getPayload().split(",");
			Collections.addAll(list, arr);
		}
		disconnectIfConfigured();
		return list;
	}

	@Override
	public List<String> listDirectory(String path) throws OwfsException, IOException {
		RequestPacket request = new RequestPacket(Enums.OwMessageType.DIR, 0, config.getFlags(), path);
		sendRequest(request);
		ResponsePacket response;
		List<String> list = new ArrayList<String>();
		while ((response = readPacket()) != null && response.getHeader().getPayloadLength() != 0) {
			list.add(response.getPayload());
		}
		disconnectIfConfigured();
		return list;
	}

	private void sendRequest(RequestPacket packet) throws IOException {
		establishConnectionIfNeeded();
		/*
		* TODO: Should we check that there is no data in the DataInputStream before sending our request?
		* / Send header
		*/
		owOut.writeInt(packet.getHeader().getVersion());
		owOut.writeInt(packet.getHeader().getPayloadLength());
		owOut.writeInt(packet.getHeader().getFunction());
		owOut.writeInt(packet.getHeader().getFlags().intValue());
		owOut.writeInt(packet.getHeader().getDataLength());
		owOut.writeInt(packet.getHeader().getOffset());
		/* Send payload */
		owOut.write(packet.getPayload());
		/* Send data if needed */
		if (packet.isWritingData()) {
			owOut.write(packet.getDataToWrite());
		}

	}

	/**
	 * Reads a owserver protocol packet from the tcp socket and returns the result as a {@link ResponsePacket}.
	 *
	 * @return a {@link ResponsePacket} read from the tcp socket, null if no packet could be read.
	 * @throws IOException   if an I/O error occurs
	 * @throws OwfsException if owserver returns an error.
	 */
	private ResponsePacket readPacket() throws IOException, OwfsException {
		int[] rawHeader = new int[6];
		try {
			rawHeader[0] = owIn.readInt(); // version
			rawHeader[1] = owIn.readInt(); // payload length
			rawHeader[2] = owIn.readInt(); // function return value
			rawHeader[3] = owIn.readInt(); // flags
			rawHeader[4] = owIn.readInt(); // data length
			rawHeader[5] = owIn.readInt(); // offset
		} catch (EOFException e) {
			return null;
		}
		grantOrDenyPersistence(rawHeader[3]);
		String payload = null;
		if (rawHeader[2] >= 0) { /* Check return value */
			if (rawHeader[1] >= 0) { /* Bytes to read */
				byte[] payloadBytes = new byte[rawHeader[1]];
				owIn.readFully(payloadBytes, 0, payloadBytes.length);
				/* TODO: why is not offset header field used here? */
				if (rawHeader[1] > 0) {
					// Remove ending zero byte if any
					if (payloadBytes[rawHeader[1] - 1] == 0) {
						payload = new String(payloadBytes, 0, rawHeader[1] - 1);
					} else {
						payload = new String(payloadBytes, 0, rawHeader[1]);
					}
				} else {
					payload = null;
				}
			}
		} else {
			//TODO Always return ResponsePacket and interpret its attributes to get if there is exception thrown or not.
			/* Error received */
			throw new OwfsException("Error " + rawHeader[2], rawHeader[2]);
		}
		//TODO Flags obeject is reduntantly created
		return new ResponsePacket(rawHeader[0], rawHeader[1], rawHeader[2], new Flags(rawHeader[3]), rawHeader[4], rawHeader[5], payload);
	}

	private void grantOrDenyPersistence(int value) {
		Flags returnFlags = new Flags(value);
		if (returnFlags.getPersistence() == Enums.OwPersistence.ON) {
			config.getFlags().setPersistence(Enums.OwPersistence.ON);
		} else {
			config.getFlags().setPersistence(Enums.OwPersistence.OFF);
		}
	}
}
