package org.owfs.jowfsclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmingDevicesReader implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(AlarmingDevicesReader.class);
	public static final String ALARM_FOLDER_FOR_OWFS_SERVER = "/alarm";
	public static final String ALARM_FOLDER_FOR_OWFS_SERVER_TO_REMOVE = ALARM_FOLDER_FOR_OWFS_SERVER+"/";

	private OwfsClient client;

	private OwfsClientFactory factory;

	private AlarmingDevicesListener listener;

	public AlarmingDevicesReader(OwfsClientFactory factory) {
		this.factory = factory;
	}

	@Override
	public void run() {
		connectIfNecessary();
		tryToReadAlarmingDirectory();
	}

	void tryToReadAlarmingDirectory() {
		try {
			readAlarmingDirectory();
		} catch (OwfsException e) {
			log.warn("owfsClientNotAvailable", e);
		} catch (IOException e) {
			log.warn("owfsClientNotAvailable", e);
		}
	}

	private void readAlarmingDirectory() throws OwfsException, IOException {
		List<String> read = client.listDirectoryAll(ALARM_FOLDER_FOR_OWFS_SERVER);
		if (!read.isEmpty()) {
			ArrayList<String> strings = rebuildDevicesListWithoutDirectoryPath(read);
			listener.alarmForDevices(strings);
		}
	}

	ArrayList<String> rebuildDevicesListWithoutDirectoryPath(List<String> read) {
		ArrayList<String> strings = new ArrayList<String>();
		for (String s : read) {
			strings.add(s.substring(ALARM_FOLDER_FOR_OWFS_SERVER_TO_REMOVE.length()));
		}
		return strings;
	}

	void connectIfNecessary() {
		if (client == null) {
			client = factory.createNewConnection();
		}
	}

	public void setAlarmListener(AlarmingDevicesListener listener) {
		this.listener = listener;
	}
}
