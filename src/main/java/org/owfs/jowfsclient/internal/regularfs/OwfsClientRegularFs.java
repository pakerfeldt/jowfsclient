/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient.internal.regularfs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsException;

/**
 * This is an implementation of {@link OwfsClient} that reads from a file
 * system, instead of communicating directly with the owserver.
 * It could be used to read the mounted 1-wire file system created by OWFS but
 * also as a simulated version that reads from a static pre-defined file system
 * that mimics an active OWFS.
 *
 * @author Patrik Akerfeldt
 */
public class OwfsClientRegularFs implements OwfsClient {

	private File root;
	private final int root_length;

	public OwfsClientRegularFs(String rootPath) {
		root = new File(rootPath);
		if (!root.isDirectory()) {
			throw new IllegalArgumentException(rootPath = " is not a directory.");
		}
		root_length = root.getAbsolutePath().length();

	}

	@Override
	public void disconnect() throws IOException {
		// no need to do nothing
	}

	@Override
	public Boolean exists(String path) throws IOException, OwfsException {
		File f = new File(root.getAbsolutePath() + path);
		return f.exists();
	}

	@Override
	public List<String> listDirectory(String path) throws OwfsException, IOException {
		List<String> contents = new ArrayList<String>();
		File directory = new File(root.getAbsolutePath() + path);
		if (!directory.isDirectory()) {
			/*
			 * FIXME: Check what happends when trying to list dir contents of an
			 * element. Fix arguments to OwfsException
			 */
			throw new OwfsException("Error", 1);
		}
		for (File file : directory.listFiles()) {
			contents.add(file.getAbsolutePath().substring(root_length));
		}
		return contents;
	}

	@Override
	public List<String> listDirectoryAll(String path) throws OwfsException, IOException {
		return listDirectory(path);
	}

	@Override
	public String read(String path) throws IOException, OwfsException {
		File file = new File(root.getAbsolutePath() + path);
		if (file.isFile() && file.canRead()) {
			String value = "";
			BufferedReader reader = new BufferedReader(new FileReader(file));
			/*
			 * Reading file as done below is not entirely correct. What if the
			 * file ends with a line feed / carriage return?
			 */
			while (reader.ready()) {
				value += reader.readLine();
				if (reader.ready()) {
					value += "\n";
				}
			}
			return value;
		} else {
			throw new OwfsException("Error", 1);
		}
	}

	@Override
	public void write(String path, String dataToWrite) throws IOException, OwfsException {
		File file = new File(root.getAbsolutePath() + path);
		if (file.isFile() && file.canWrite()) {
			DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file));
			outputStream.writeBytes(dataToWrite);
		} else {
			throw new OwfsException("Error", 1);
		}
	}
}
