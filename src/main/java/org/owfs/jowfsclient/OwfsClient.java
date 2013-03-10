/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient;

import java.io.IOException;
import java.util.List;
import org.owfs.jowfsclient.Enums.OwAlias;
import org.owfs.jowfsclient.Enums.OwBusReturn;
import org.owfs.jowfsclient.Enums.OwDeviceDisplayFormat;
import org.owfs.jowfsclient.Enums.OwPersistence;
import org.owfs.jowfsclient.Enums.OwTemperatureScale;

public interface OwfsClient {

	/**
	 * Tears down the connection to the owserver if open.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	void disconnect() throws IOException;

	/**
	 * Sets the timeout on the TCP connection with owserver.
	 *
	 * @param timeout timeout in milliseconds.
	 */
	void setTimeout(int timeout);

	/**
	 * Sets the format in which devices will be displayed.
	 *
	 * @param deviceDisplay the new {@link OwDeviceDisplayFormat}.
	 */
	void setDeviceDisplayFormat(OwDeviceDisplayFormat deviceDisplay);

	/**
	 * Sets the temperature scale that owserver should return temperature values
	 * in.
	 *
	 * @param tempScale the new {@link OwTemperatureScale} value.
	 */
	void setTemperatureScale(OwTemperatureScale tempScale);

	/**
	 * Set whether or not persistent connection should be requested. Note that
	 * this does not necessarily mean that a persistent connection will be
	 * established with owserver. This will only happen if owserver grants that
	 * request.
	 *
	 * @param persistence the new {@link OwPersistence} value
	 */
	void setPersistence(OwPersistence persistence);

	/**
	 * Sets whether or not to use aliases for known slaves.
	 *
	 * @param alias the new {@link OwAlias} value.
	 */
	void setAlias(OwAlias alias);

	/**
	 * Sets whether or not to include special directories on directory listing.
	 *
	 * @param busReturn the new {@link OwBusReturn} value.
	 */
	void setBusReturn(OwBusReturn busReturn);

	/**
	 * Reads the value from the specified entity.
	 *
	 * @param path the entity to read value from.
	 * @return the value of the entity.
	 * @throws IOException   if an I/O error occurs.
	 * @throws OwfsException if owserver returns an error,
	 */
	String read(String path) throws IOException, OwfsException;

	/**
	 * Writes a value to the specified entity.
	 *
	 * @param path        the entity to write to.
	 * @param dataToWrite the value to write.
	 * @throws IOException   if an I/O error occurs.
	 * @throws OwfsException if the value couldn't be written and if owserver returns an
	 *                       error.
	 */
	void write(String path, String dataToWrite) throws IOException, OwfsException;

	/**
	 * Returns true if the specified path exists.
	 *
	 * @param path the path/entity to check for existence.
	 * @return true if the specified path/device exists.
	 * @throws IOException   if an I/O error occurs.
	 * @throws OwfsException if owserver returns an error.
	 */
	Boolean exists(String path) throws IOException, OwfsException;

	/**
	 * Returns a list of elements inside the specified path. {@code
	 * listDirectoryAll} and {@code listDirectory} returns the same {@link List}
	 * although {@code listDirectoryAll} will get the result in one owserver
	 * response packet (instead of one packet per element).
	 *
	 * @param path the path to list elements from
	 * @return a {@code List}<String> of elements found inside the specified path.
	 * @throws OwfsException if owserver returns an error.
	 * @throws IOException   if an I/O error occurs.
	 */
	List<String> listDirectoryAll(String path) throws OwfsException, IOException;

	/**
	 * Returns a list of elements inside the specified path. {@code
	 * listDirectory} and {@code listDirectoryAll} returns the same {@link List}
	 * although {@code listDirectory} will get the result in one owserver
	 * response packet per found element (instead of one packet for all
	 * elements).
	 *
	 * @param path the path to list elements from
	 * @return a {@code List}<String> of elements found inside the specified path.
	 * @throws OwfsException if owserver returns an error.
	 * @throws IOException   if an I/O error occurs.
	 */
	List<String> listDirectory(String path) throws OwfsException, IOException;
}
