package org.owfs.jowfsclient.alarm;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.OwfsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmingDevicesReader implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(AlarmingDevicesReader.class);

	private OwfsClientFactory factory;
	private OwfsClient client;

	private Map<String, AlarmingDeviceHandler> observableDevices = new HashMap<String, AlarmingDeviceHandler>();

	public AlarmingDevicesReader(OwfsClientFactory factory) {
		this.factory = factory;
	}

	public void addObservableDevice(String deviceName, AlarmingDeviceHandler commander) {
		observableDevices.put(deviceName, commander);
	}

	@Override
	public void run() {
		connectIfNecessary();
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
		List<String> read = client.listDirectory(OWFSUtils.ALARM_FOLDER_FOR_OWFS_SERVER);
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
		AlarmingDeviceHandler alarmingDeviceHandler = observableDevices.get(deviceName);
		if (alarmingDeviceHandler != null) {
			alarmingDeviceHandler.onAlarm(client, devicePath, deviceName);
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

	public void initialize() {
		connectIfNecessary();
		try {
			for (Map.Entry<String, AlarmingDeviceHandler> entry : observableDevices.entrySet()) {
				log.info("Initializing: '" + entry.getKey() + "'...");
				entry.getValue().onInitialize(client, entry.getKey());
			}
		} catch (Exception e) {
			handleConnectionException(e);
		}
	}
}
