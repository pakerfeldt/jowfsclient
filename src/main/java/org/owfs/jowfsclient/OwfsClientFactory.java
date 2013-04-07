/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient;

import org.owfs.jowfsclient.alarm.AlarmingDevicesReader;
import org.owfs.jowfsclient.alarm.AlarmingDevicesScanner;
import org.owfs.jowfsclient.internal.OwfsClientImpl;
import org.owfs.jowfsclient.internal.OwfsClientThreadSafeFactory;
import org.owfs.jowfsclient.internal.regularfs.OwfsClientRegularFs;

/**
 * This is a factory client for {@link OwfsClient}s.
 * Here's an example of how {@code OwfsClientFactory} and {@link OwfsClient} can
 * be used:
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
 *
 * @author Patrik Akerfeldt
 */

public class OwfsClientFactory {

	private OwfsClientConfig config;

	public OwfsClientFactory(String hostName, int portNumber) {
		config = new OwfsClientConfig(hostName, portNumber);
		config.setDeviceDisplayFormat(Enums.OwDeviceDisplayFormat.OWNET_DDF_F_DOT_I);
		config.setTemperatureScale(Enums.OwTemperatureScale.OWNET_TS_CELSIUS);
		config.setPersistence(Enums.OwPersistence.OWNET_PERSISTENCE_ON);
		config.setBusReturn(Enums.OwBusReturn.OWNET_BUSRETURN_ON);
	}

	public OwfsClient createNewConnection() {
		return new OwfsClientImpl(config);
	}

	public AlarmingDevicesScanner createNewAlarmingScanner() {
		return new AlarmingDevicesScanner(new AlarmingDevicesReader(this));
	}

	/**
	 * Creates a new {@link OwfsClient} instance.	 *
	 *
	 * @return a new {@link OwfsClient} instance.
	 */
	public static OwfsClient newOwfsClient(OwfsClientConfig config) {
		return new OwfsClientImpl(config);
	}

	/**
	 * Thread safe {@link OwfsClient}	 *
	 *
	 * @return
	 */
	public static OwfsClient newOwfsClientThreadSafe(OwfsClientConfig config) {
		return new OwfsClientThreadSafeFactory().decorate(newOwfsClient(config));
	}

	/**
	 * Creates a new {@link OwfsClient} instance.
	 *
	 * @param rootPath A file path to the root directory of the 1-wire file system, e.g. <strong>&quot;/mnt/1wire&quot;</strong> or
	 *                 <strong>&quot;/var/1wire/simulated-fs&quot;</strong>
	 * @return a new {@link OwfsClient} instance.
	 */
	public static OwfsClient newOwfsClient(String rootPath) {
		return new OwfsClientRegularFs(rootPath);
	}
}
