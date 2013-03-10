package org.owfs.jowfsclient;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmingDevicesReader implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(AlarmingDevicesReader.class);
	public static final String ALARM_FOLDER_FOR_OWFS_SERVER = "/alarm";

	private OwfsClient client;

	private OwfsClientFactory factory;

	private AlarmListener listener;

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
			List<String> read = client.listDirectoryAll(ALARM_FOLDER_FOR_OWFS_SERVER);
			if (!read.isEmpty()) {
				listener.alarmForDevices(read);
			}
		} catch (OwfsException e) {
			log.warn("owfsClientNotAvailable", e);
		} catch (IOException e) {
			log.warn("owfsClientNotAvailable", e);
		}
	}

	void connectIfNecessary() {
		if (client == null) {
			client = factory.createNewConnection();
		}
	}

	public void setAlarmListener(AlarmListener listener) {
		this.listener = listener;
	}
}
