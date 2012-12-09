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
 * 
 * 
 * Here's an example of how {@code OwfsClientFactory} and {@link OwfsClient} can
 * be used:
 * 
 * <blockquote>
 * 
 * <pre>
 * OwfsClient client = OwfsClientFactory.newOwfsClient(&quot;127.0.0.1&quot;, 3001, true);
 * 
 * client.setPersistence(OwPersistence.OWNET_PERSISTENCE_ON);
 * client.setTemperatureScale(OwTemperatureScale.OWNET_TS_CELSIUS);
 * client.setBusReturn(OwBusReturn.OWNET_BUSRETURN_ON);
 * try {
 * 	List&lt;String&gt; dirs = client.listDirectoryAll(&quot;/&quot;);
 * 	for (String d : dirs) {
 * 		System.out.println(d);
 * 	}
 * } catch (OwfsException e) {
 * 	// Handle OwfsException
 * } catch (IOException e) {
 * 	// Handle IOException
 * }
 *</pre>
 * 
 * </blockquote>
 * 
 * @author Patrik Akerfeldt
 * 
 */

public class OwfsClientFactory {

	private OwfsClientFactory() {
	};

	/**
	 * Creates a new {@link OwfsClient} instance.
	 * 
	 * @param hostname
	 *            A {@link String} representation of the hostname to connect to.
	 * @param port
	 *            The port to connect to.
	 * @param threadSafe
	 *            Whether or not the {@link OwfsClient} instance should be
	 *            thread safe. Set to true if unsure.
	 * @return a new {@link OwfsClient} instance.
	 */
	public static OwfsClient newOwfsClient(String hostname, Integer port,
			boolean threadSafe) {
		return new OwfsClientImpl(hostname, port, threadSafe);
	}

	/**
	 * Creates a new {@link OwfsClient} instance.
	 * 
	 * @param rootPath
	 *            A file path to the root directory of the 1-wire file system,
	 *            e.g. <strong>&quot;/mnt/1wire&quot;</strong> or
	 *            <strong>&quot;/var/1wire/simulated-fs&quot;</strong>
	 * 
	 * @return a new {@link OwfsClient} instance.
	 */
	public static OwfsClient newOwfsClient(String rootPath) {
		return new OwfsClientRegularFs(rootPath);
	}
}
