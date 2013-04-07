package org.owfs.jowfsclient.manual;

import static org.testng.FileAssert.fail;

import org.owfs.jowfsclient.TestNGGroups;
import org.owfs.jowfsclient.integration.OwfsClientDS2408InputTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author Tomasz Kucharski <kucharski.tom@gmail.com>
 * @since 08.04.13 00:30
 */
@Test(groups = TestNGGroups.INTEGRATION_MANUAL)
public class OwfsDS2408AlarmingTest extends OwfsClientDS2408InputTest {
	private static final Logger log = LoggerFactory.getLogger(OwfsDS2408AlarmingTest.class);

	public void testManuallyTestHowAlarmWorks() throws Exception {
		setDefaultAlarmFormat();

		//I know, this is weird, but simply waits max 1000 loops until you physically sense any input in your device
		for (int i = 0; i < 1000; i++) {
			log.info("Waiting " + i + "/1000 loops until you physically sense any input in device " + deviceDs2408 + "...");
			if (checkIfDeviceIsAlarming(deviceDs2408)) {
				log.info("Well done! Input sensed: " + client.read("/alarm/" + deviceDs2408 + "/latch.ALL"));
				disableAlarming();
				return;
			}
		}
		fail();
	}

}
