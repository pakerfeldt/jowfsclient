/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient.internal;

import org.owfs.jowfsclient.Enums.*;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is used by applications that wants to communicate with the
 * owserver over the owserver protocol.
 *
 * @author Patrik Akerfeldt
 */
public class OwfsClientImpl implements OwfsClient {

    private static final int OWNET_REQUEST = 0x00000100; // default flag
    private static final int OWNET_DEFAULT_DATALEN = 4096; // default data
    // length

    private String hostname;
    private Integer port;
    private Socket owSocket;
    private DataInputStream owIn;
    private DataOutputStream owOut;
    private Flags flags = new Flags(OWNET_REQUEST);
    private int connectionTimeout = 4000; // default to 4s timeout

    /**
     * Constructs a new OwClient with the specified connection details.
     *
     * @param hostname A {@link String} representation of the hostname to connect to.
     * @param port     The port to connect to, which owserver listens to.
     */
    public OwfsClientImpl(String hostname, Integer port) {
        this.hostname = hostname;
        this.port = port;
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
            if (owIn != null) {
                owIn.close();
                owIn = null;
            }
            if (owOut != null) {
                owOut.close();
                owOut = null;
            }
            if (owSocket != null) {
                owSocket.close();
                owSocket = null;
            }
        }
        if (owSocket == null || !owSocket.isConnected()) {
            owSocket = new Socket();
            try {
                owSocket.connect(new InetSocketAddress(hostname, port), connectionTimeout);
            } catch (SocketTimeoutException ste) {
                owSocket = null;
                return false;
            }
            owIn = new DataInputStream(owSocket.getInputStream());
            owOut = new DataOutputStream(owSocket.getOutputStream());
            return true;
        } else
            return false;
    }

    @Override
    public void disconnect() throws IOException {
        if (owIn != null) {
            owIn.close();
            owIn = null;
        }
        if (owOut != null) {
            owOut.close();
            owOut = null;
        }
        if (owSocket != null) {
            owSocket.close();
            owSocket = null;
        }
    }

    /**
     * Establishes a connection to the owserver if needed, i.e. if no {@code
     * Socket} exists, {@code Socket}. Returns true if connection is
     * established.
     *
     * @throws IOException if an I/O error occurs.
     */
    private void establishConnection() throws IOException {
        if (owSocket == null
                || !owSocket.isConnected()
                || !(flags.getPersistence() == OwPersistence.OWNET_PERSISTENCE_ON))
            connect(true);
    }

    @Override
    public void setTimeout(int timeout) {
        connectionTimeout = timeout;
        if (owSocket != null && !owSocket.isClosed()) {
            try {
                owSocket.setSoTimeout(connectionTimeout);
            } catch (SocketException e) {
                // ignore
            }
        }
    }

    @Override
    public void setDeviceDisplayFormat(OwDeviceDisplayFormat deviceDisplay) {
        flags.setDeviceDisplayFormat(deviceDisplay);
    }

    @Override
    public void setTemperatureScale(OwTemperatureScale tempScale) {
        flags.setTemperatureScale(tempScale);
    }

    @Override
    public void setPersistence(OwPersistence persistence) {
        flags.setPersistence(persistence);
    }

    @Override
    public void setAlias(OwAlias alias) {
        flags.setAlias(alias);
    }

    @Override
    public void setBusReturn(OwBusReturn busReturn) {
        flags.setBusReturn(busReturn);
    }

    @Override
    public String read(String path) throws IOException, OwfsException {
        ResponsePacket response;
        RequestPacket request = new RequestPacket(OwMessageType.OWNET_MSG_READ, OWNET_DEFAULT_DATALEN, flags, path);
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
        RequestPacket request = new RequestPacket(OwMessageType.OWNET_MSG_WRITE, flags, path, dataToWrite);
        sendRequest(request);
        /*
        * Even if we're not interested in the result of the response packet
        * we must read the packet from the socket. Partly to clean incoming
        * bytes but also in order to throw exceptions on error.
        */
        readPacket();

        disconnectIfConfigured();
    }

    private void disconnectIfConfigured() throws IOException {
        if (flags.getPersistence() != OwPersistence.OWNET_PERSISTENCE_ON) {
            disconnect();
        }
    }

    @Override
    public Boolean exists(String path) throws IOException, OwfsException {
        ResponsePacket response;
        RequestPacket request = new RequestPacket(OwMessageType.OWNET_MSG_PRESENCE, 0, flags, path);
        sendRequest(request);
        response = readPacket();

        disconnectIfConfigured();
        /*
        * FIXME: This must be taken care of, method should return false if
        * device does not exist, not throw an exception as it does no, in
        * readPacket(). Check what message are returned on error and what
        * are return when device does not exist.
        */
        return response.getHeader().getFunction() >= 0;

    }

    @Override
    public List<String> listDirectoryAll(String path) throws OwfsException, IOException {
        List<String> list = new ArrayList<String>();
        RequestPacket request = new RequestPacket(OwMessageType.OWNET_MSG_DIRALL, 0, flags, path);
        sendRequest(request);
        ResponsePacket response = readPacket();
        if (response != null && response.getPayload() != null) {
            String[] arr = response.getPayload().split(",");
            Collections.addAll(list, arr);
        }

        disconnectIfConfigured();
        return list;
    }

    @Override
    public List<String> listDirectory(String path) throws OwfsException, IOException {
        List<String> list = new ArrayList<String>();
        RequestPacket request = new RequestPacket(OwMessageType.OWNET_MSG_DIR, 0, flags, path);

        sendRequest(request);

        ResponsePacket response;
        while ((response = readPacket()) != null && response.getHeader().getPayloadLength() != 0) {
            list.add(response.getPayload());
        }

        disconnectIfConfigured();
        return list;
    }

    /**
     * Sends a request to the owserver.
     *
     * @param packet the {@link RequestPacket} to send.
     * @throws IOException
     */
    private void sendRequest(RequestPacket packet) throws IOException {
        establishConnection();

        /*
           * TODO: Should we check that there is no data in the DataInputStream
           * before sending our request? System.out.println(packet.getHeader());
           *
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
     * Reads a owserver protocol packet from the tcp socket and returns the
     * result as a {@link ResponsePacket}.
     *
     * @return a {@link ResponsePacket} read from the tcp socket, null if no
     *         packet could be read.
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

        Flags returnFlags = new Flags(rawHeader[3]);

        /* Grant/deny persistence */
        if (returnFlags.getPersistence() == OwPersistence.OWNET_PERSISTENCE_ON) {
            flags.setPersistence(OwPersistence.OWNET_PERSISTENCE_ON);
        } else
            flags.setPersistence(OwPersistence.OWNET_PERSISTENCE_OFF);

        String payload = null;
        if (rawHeader[2] >= 0) { /* Check return value */
            if (rawHeader[1] >= 0) { /* Bytes to read */
                byte[] payloadBytes = new byte[rawHeader[1]];
                owIn.readFully(payloadBytes, 0, payloadBytes.length);

                /* TODO: why is not offset header field used here? */
                if (rawHeader[1] > 0) {
                    // Remove ending zero byte if any
                    if (payloadBytes[rawHeader[1] - 1] == 0)
                        payload = new String(payloadBytes, 0, rawHeader[1] - 1);
                    else
                        payload = new String(payloadBytes, 0, rawHeader[1]);
                } else
                    payload = null;

            }
        } else {
            /* Error received */
            throw new OwfsException("Error " + rawHeader[2], rawHeader[2]);
        }

        return new ResponsePacket(rawHeader[0], rawHeader[1], rawHeader[2],
                new Flags(rawHeader[3]), rawHeader[4], rawHeader[5], payload);
    }
}
