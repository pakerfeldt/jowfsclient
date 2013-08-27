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
import org.owfs.jowfsclient.internal.OwfsConnectionImpl;
import org.owfs.jowfsclient.internal.OwfsConnectionThreadSafeProxy;
import org.owfs.jowfsclient.internal.regularfs.OwfsConnectionRegularFs;

/**
 * This is a factory client for {@link OwfsConnection}s.
 * Here's an example of how {@code OwfsConnectionFactory} and {@link OwfsConnection} can
 * be used:
 * <pre>
 * OwfsConnection client = OwfsConnectionFactory.newOwfsClient(&quot;127.0.0.1&quot;, 3001, true);
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

public class OwfsConnectionFactory {

	private OwfsConnectionConfig connectionConfig;

	private AlarmingDevicesScanner alarmingScanner;

	public OwfsConnectionFactory(String hostName, int portNumber) {
		connectionConfig = new OwfsConnectionConfig(hostName, portNumber);
		connectionConfig.setDeviceDisplayFormat(Enums.OwDeviceDisplayFormat.F_DOT_I);
		connectionConfig.setTemperatureScale(Enums.OwTemperatureScale.CELSIUS);
		connectionConfig.setPersistence(Enums.OwPersistence.ON);
		connectionConfig.setBusReturn(Enums.OwBusReturn.ON);
	}

	public OwfsConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}

	public void setConnectionConfig(OwfsConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
	}

	public void setAlarmingScanner(AlarmingDevicesScanner alarmingScanner) {
		this.alarmingScanner = alarmingScanner;
	}

	public AlarmingDevicesScanner getAlarmingScanner() {
		if (alarmingScanner == null) {
			alarmingScanner = new AlarmingDevicesScanner(new AlarmingDevicesReader(this));
		}
		return alarmingScanner;
	}

	public OwfsConnection createNewConnection() {
		return new OwfsConnectionImpl(connectionConfig);
	}

	/**
	 * Creates a new {@link OwfsConnection} instance.
	 *
	 * @return a new {@link OwfsConnection} instance.
	 */
	public static OwfsConnection newOwfsClient(OwfsConnectionConfig config) {
		return new OwfsConnectionImpl(config);
	}

	/**
	 * Thread safe {@link OwfsConnection}
	 *
	 * @return
	 */
	public static OwfsConnection newOwfsClientThreadSafe(OwfsConnectionConfig config) {
		return new OwfsConnectionThreadSafeProxy().decorate(newOwfsClient(config));
	}

	/**
	 * Creates a new {@link OwfsConnection} instance.
	 *
	 * @param rootPath A file path to the root directory of the 1-wire file system, e.g. <strong>&quot;/mnt/1wire&quot;</strong> or
	 *                 <strong>&quot;/var/1wire/simulated-fs&quot;</strong>
	 * @return a new {@link OwfsConnection} instance.
	 */
	public static OwfsConnection newOwfsClient(String rootPath) {
		return new OwfsConnectionRegularFs(rootPath);
	}
}
