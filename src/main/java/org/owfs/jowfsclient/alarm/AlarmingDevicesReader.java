package org.owfs.jowfsclient.alarm;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.owfs.jowfsclient.OwfsConnection;
import org.owfs.jowfsclient.OwfsConnectionFactory;
import org.owfs.jowfsclient.OwfsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmingDevicesReader implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(AlarmingDevicesReader.class);

	private OwfsConnectionFactory factory;
	private OwfsConnection client;

	private Map<String, AlarmingDeviceListener> alarmingDevices = new HashMap<String, AlarmingDeviceListener>();

	public AlarmingDevicesReader(OwfsConnectionFactory factory) {
		this.factory = factory;
	}

	public void addAlarmingDeviceHandler(AlarmingDeviceListener commander) throws IOException, OwfsException {
		commander.onInitialize(getClient());
		alarmingDevices.put(commander.getDeviceName(), commander);
	}

	public boolean isAlarmingDeviceHandlerInstalled(String deviceName) {
		return alarmingDevices.containsKey(deviceName);
	}

	public void removeAlarmingDeviceHandler(String deviceName) {
		alarmingDevices.remove(deviceName);
	}

	private OwfsConnection getClient() {
		connectIfNecessary();
		return client;
	}

	public boolean isWorthToWork() {
		return alarmingDevices.size() > 0;
	}

	@Override
	public void run() {
		tryToReadAlarmingDirectory();
	}

	void tryToReadAlarmingDirectory() {
		try {
			readAlarmingDirectory();
		} catch (Exception e) {
			handleConnectionException(e);
		}
	}

	private void readAlarmingDirectory() throws OwfsException, IOException {
		List<String> read = getClient().listDirectory(OWFSUtils.ALARM_FOLDER_FOR_OWFS_SERVER);
		processAlarmingDevices(read);
	}

	void processAlarmingDevices(List<String> read) throws IOException, OwfsException {
		if (!read.isEmpty()) {
			for (String devicePath : read) {
				processAlarmingDevice(devicePath);
			}
		}
	}

	void processAlarmingDevice(String devicePath) throws IOException, OwfsException {
		String deviceName = OWFSUtils.extractDeviceNameFromDevicePath(devicePath);
		AlarmingDeviceListener alarmingDeviceListener = alarmingDevices.get(deviceName);
		if (alarmingDeviceListener != null) {
			alarmingDeviceListener.onAlarm(getClient());
		}
	}

	private void handleConnectionException(Exception e) {
		log.warn("Exception occured", e);
		if (client != null) {
			client = null;
		}
	}

	void connectIfNecessary() {
		if (client == null) {
			client = factory.createNewConnection();
		}
	}
}
