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

public interface OwfsClient {

	/**
	 * Setup configuration for Owfs connection defining device display format, measuring metrics, persistence, aliasing etc.
	 * Normally this configuration is done once per connection.
	 *
	 * @param config configuration
	 */
	void setConfiguration(OwfsClientConfig config);

	/**
	 * Tears down the connection to the owserver if open.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	void disconnect() throws IOException;

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
