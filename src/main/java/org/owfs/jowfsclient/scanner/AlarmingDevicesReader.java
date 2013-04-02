package org.owfs.jowfsclient.scanner;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.time.StopWatch;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.OwfsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmingDevicesReader implements Runnable {
	public static final String ALARM_FOLDER_FOR_OWFS_SERVER = "/alarm/";
	public static final String ALARM_FOLDER_FOR_OWFS_SERVER_TO_REMOVE = ALARM_FOLDER_FOR_OWFS_SERVER + "/";
	private static final Logger log = LoggerFactory.getLogger(AlarmingDevicesReader.class);
	private OwfsClientFactory factory;
	private OwfsClient client;
	private AlarmingDevicesListener listener;

	private Set<String> observableDevices = new HashSet();

	public AlarmingDevicesReader(OwfsClientFactory factory) {
		this.factory = factory;
	}

	public void setAlarmListener(AlarmingDevicesListener listener) {
		this.listener = listener;
	}

	public void addObservableDevice(String deviceName) {
		observableDevices.add(deviceName);
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
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<String> read = client.listDirectory(ALARM_FOLDER_FOR_OWFS_SERVER);
		stopWatch.stop();
		log.info("" + stopWatch);
		processAlarmingDevices(read);
	}

	private void processAlarmingDevices(List<String> read) throws IOException, OwfsException {
		if (!read.isEmpty()) {
			for (String devicePath : read) {
				String deviceName = extractDeviceNameFromDevicePath(devicePath);
				if (observableDevices.contains(deviceName)) {
					processAlarmingAndObservableDevice(devicePath);
				}
			}
		}
	}

	private void processAlarmingAndObservableDevice(String devicePath) throws IOException, OwfsException {
		String pow = client.read(devicePath + "/por");
		String sensedAll = client.read(devicePath + "/sensed.ALL");
		String latchAll = client.read(devicePath + "/latch.ALL");

		client.write(devicePath + "/latch.ALL", "0,0,0,0,0,0,0,0");
		client.write(devicePath + "/por.", "0");

		AlarmingDeviceEvent alarmingDeviceEvent = new AlarmingDeviceEvent(
				extractDeviceNameFromDevicePath(devicePath),
				new ReadCommand("sensed.ALL", sensedAll),
				new ReadCommand("latch.ALL", latchAll)
		);
		listener.alarmForDevices(alarmingDeviceEvent);
	}

	private void handleConnectionException(Exception e) {
		log.warn("Exception occur", e);
		if (client != null) {
			client = null;
		}
	}

	String extractDeviceNameFromDevicePath(String devicePath) {
		return devicePath.substring(ALARM_FOLDER_FOR_OWFS_SERVER_TO_REMOVE.length() - 1);
	}

	void connectIfNecessary() {
		if (client == null) {
			client = factory.createNewConnection();
		}
	}
}
