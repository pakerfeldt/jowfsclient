package org.owfs.jowfsclient.alarm;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.owfs.jowfsclient.OwfsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tom Kucharski
 */
public class AlarmingDevicesScanner {
	private static final Logger log = LoggerFactory.getLogger(AlarmingDevicesScanner.class);

	private static final int INITIAL_DELAY = 1;
	private static final int PERIOD = 50;
	public static final int THREAD_POOL_SIZE = 1;

	private AlarmingDevicesReader reader;

	private int period;

	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

	public AlarmingDevicesScanner(AlarmingDevicesReader reader) {
		this(reader, PERIOD);
	}

	public AlarmingDevicesScanner(AlarmingDevicesReader reader, int period) {
		this.reader = reader;
		this.period = period;
	}

	public void init() {
		log.debug("AlarmingDeviceScanner initialization");
		scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);
		scheduledThreadPoolExecutor.setMaximumPoolSize(THREAD_POOL_SIZE);
		scheduledThreadPoolExecutor.scheduleAtFixedRate(reader, INITIAL_DELAY, period, TimeUnit.MILLISECONDS);
	}

	public void addAlarmingDeviceHandler(AlarmingDeviceListener commander) throws IOException, OwfsException {
		reader.addAlarmingDeviceHandler(commander);
		verifyIfShouldBeStartedOrStopped();
	}

	public boolean isAlarmingDeviceOnList(String deviceName) {
		return reader.isAlarmingDeviceHandlerInstalled(deviceName);
	}

	public void removeAlarmingDeviceHandler(String deviceName) {
		reader.removeAlarmingDeviceHandler(deviceName);
		verifyIfShouldBeStartedOrStopped();
	}

	public void verifyIfShouldBeStartedOrStopped() {
		if (reader.isWorthToWork()) {
			init();
		} else {
			shutdown();
		}
	}

	public void shutdown() {
		log.debug("AlarmingDeviceScanner shutdown");
		scheduledThreadPoolExecutor.shutdown();
	}
}
