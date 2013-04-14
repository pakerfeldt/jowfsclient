package org.owfs.jowfsclient;

import org.owfs.jowfsclient.internal.Flags;

/**
 * @author Tom Kucharski
 */
public class OwfsConnectionConfig {

	private String hostName;

	private int portNumber;

	private Flags flags = new Flags();

	private int connectionTimeout = 4000; // default to 4s timeout

	public OwfsConnectionConfig(String hostName, int portNumber) {
		this.hostName = hostName;
		this.portNumber = portNumber;
	}

	public String getHostName() {
		return hostName;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public Flags getFlags() {
		return flags;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * Sets the timeout on the TCP connection with owserver.
	 *
	 * @param timeout timeout in milliseconds.
	 */
	public void setTimeout(int timeout) {
		connectionTimeout = timeout;
	}

	/**
	 * Sets the format in which devices will be displayed.
	 *
	 * @param deviceDisplay the new {@link org.owfs.jowfsclient.Enums.OwDeviceDisplayFormat}.
	 */
	public void setDeviceDisplayFormat(Enums.OwDeviceDisplayFormat deviceDisplay) {
		flags.setDeviceDisplayFormat(deviceDisplay);
	}

	public Enums.OwDeviceDisplayFormat getOwDeviceDisplayFormat() {
		return flags.getDeviceDisplayFormat();
	}


	/**
	 * Sets the temperature scale that owserver should return temperature values
	 * in.
	 *
	 * @param tempScale the new {@link org.owfs.jowfsclient.Enums.OwTemperatureScale} value.
	 */
	public void setTemperatureScale(Enums.OwTemperatureScale tempScale) {
		flags.setTemperatureScale(tempScale);
	}

	public Enums.OwTemperatureScale getTemperatureScale() {
		return flags.getTemperatureScale();
	}


	/**
	 * Set whether or not persistent connection should be requested. Note that
	 * this does not necessarily mean that a persistent connection will be
	 * established with owserver. This will only happen if owserver grants that
	 * request.
	 *
	 * @param persistence the new {@link org.owfs.jowfsclient.Enums.OwPersistence} value
	 */
	public void setPersistence(Enums.OwPersistence persistence) {
		flags.setPersistence(persistence);
	}

	public Enums.OwPersistence getPersistence() {
		return flags.getPersistence();
	}

	/**
	 * Sets whether or not to use aliases for known slaves.
	 *
	 * @param alias the new {@link org.owfs.jowfsclient.Enums.OwAlias} value.
	 */
	public void setAlias(Enums.OwAlias alias) {
		flags.setAlias(alias);
	}

	public Enums.OwAlias getAlias() {
		return flags.getAlias();
	}

	/**
	 * Sets whether or not to include special directories on directory listing.
	 *
	 * @param busReturn the new {@link org.owfs.jowfsclient.Enums.OwBusReturn} value.
	 */
	public void setBusReturn(Enums.OwBusReturn busReturn) {
		flags.setBusReturn(busReturn);
	}

	public Enums.OwBusReturn getBusReturn() {
		return flags.getBusReturn();
	}
}
