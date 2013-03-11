package org.owfs.jowfsclient;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tomasz Kucharski <kucharski.tom@gmail.com>
 * @since 11.03.13 00:36
 */
public class AlarmingDevicesScanner {
	private static final Logger log = LoggerFactory.getLogger(AlarmingDevicesScanner.class);

	private static final int INITIAL_DELAY = 1;
	private static final int PERIOD = 1;
	private static final int THREAD_POOL_SIZE = 1;

	private AlarmingDevicesReader reader;

	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

	public AlarmingDevicesScanner(AlarmingDevicesReader reader) {
		this.reader = reader;
	}

	public void init() {
		log.info("Alarming devices scanner initialization...");
		scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);
		scheduledThreadPoolExecutor.scheduleAtFixedRate(reader, INITIAL_DELAY, PERIOD, TimeUnit.MILLISECONDS);
	}

	public void shutdown() {
		log.info("Alarming devices scanner stopped");
		scheduledThreadPoolExecutor.shutdown();
	}
}
