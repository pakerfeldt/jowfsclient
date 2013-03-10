package org.owfs.jowfsclient;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tomasz Kucharski <kucharski.tom@gmail.com>
 * @since 10.03.13 00:33
 */
public class CommandExecutor {
	private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);

	public static final int INITIAL_DELAY = 1;
	public static final int PERIOD = 1;

	public void init() {
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
//		scheduledThreadPoolExecutor.scheduleAtFixedRate(alarmingDevicesReader.createRunnable(), INITIAL_DELAY, PERIOD, TimeUnit.MILLISECONDS);
	}

	private Runnable createRunnable() {
		return null;
	}
}
