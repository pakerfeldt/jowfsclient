/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient;

import org.owfs.jowfsclient.internal.OwfsClientImpl;
import org.owfs.jowfsclient.internal.regularfs.OwfsClientRegularFs;

/**
 * This is a factory client for {@link OwfsClient}s.
 * <p/>
 * <p/>
 * Here's an example of how {@code OwfsClientFactory} and {@link OwfsClient} can
 * be used:
 * <p/>
 * <blockquote>
 * <p/>
 * <pre>
 * OwfsClient client = OwfsClientFactory.newOwfsClient(&quot;127.0.0.1&quot;, 3001, true);
 *
 * client.setPersistence(OwPersistence.OWNET_PERSISTENCE_ON);
 * client.setTemperatureScale(OwTemperatureScale.OWNET_TS_CELSIUS);
 * client.setBusReturn(OwBusReturn.OWNET_BUSRETURN_ON);
 * try {
 * 	List&lt;String&gt; dirs = client.listDirectoryAll(&quot;/&quot;);
 * 	for (String d : dirs) {
 * 		log.info(d);
 *     }
 * } catch (OwfsException e) {
 * 	// Handle OwfsException
 * } catch (IOException e) {
 * 	// Handle IOException
 * }
 * </pre>
 * <p/>
 * </blockquote>
 *
 * @author Patrik Akerfeldt
 */

public class OwfsClientFactory {

	private String hostName;
	private int portNumber;

	/**
	 * Creates a new {@link OwfsClient} instance.
	 *
	 * @param hostname A {@link String} representation of the hostname to connect to.
	 * @param port     The port to connect to.
	 * @return a new {@link OwfsClient} instance.
	 */
	public static OwfsClient newOwfsClient(String hostname, Integer port) {
		return new OwfsClientImpl(hostname, port);
	}

	/**
	 * Whether or not the {@link OwfsClient} instance should be thread safe. If the client will only be used by one
	 * thread, this can be turned off to increase performance, even though the performance increase will probably be
	 * minimal.
	 *
	 * @param hostname
	 * @param port
	 * @return
	 */
	public static OwfsClient newOwfsClientThreadSafe(String hostname, Integer port) {
		return new OwfsClientThreadSafeFactory().decorate(newOwfsClient(hostname, port));
	}

	/**
	 * Creates a new {@link OwfsClient} instance.
	 *
	 * @param rootPath A file path to the root directory of the 1-wire file system,
	 *                 e.g. <strong>&quot;/mnt/1wire&quot;</strong> or
	 *                 <strong>&quot;/var/1wire/simulated-fs&quot;</strong>
	 * @return a new {@link OwfsClient} instance.
	 */
	public static OwfsClient newOwfsClient(String rootPath) {
		return new OwfsClientRegularFs(rootPath);
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public OwfsClient createNewConnection() {
		return OwfsClientFactory.newOwfsClient(hostName, portNumber);
	}
}
