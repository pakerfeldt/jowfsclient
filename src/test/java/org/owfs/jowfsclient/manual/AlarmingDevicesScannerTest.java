package org.owfs.jowfsclient.manual;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.owfs.jowfsclient.alarm.AlarmingDevicesReader;
import org.owfs.jowfsclient.alarm.AlarmingDevicesScanner;
import org.owfs.jowfsclient.device.SwitchAlarmingDeviceEvent;
import org.owfs.jowfsclient.device.SwitchAlarmingDeviceListener;
import org.owfs.jowfsclient.integration.TestNGIntegrationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @author Tom Kucharski
 */
@Test(groups = TestNGGroups.INTEGRATION_MANUAL)
public class AlarmingDevicesScannerTest {
	private static final Logger log = LoggerFactory.getLogger(AlarmingDevicesScannerTest.class);

	private Thread waitingThread = Thread.currentThread();
	boolean messageReceived = false;

	@Parameters({
			TestNGIntegrationProperties.OWFS_HOSTNAME,
			TestNGIntegrationProperties.OWFS_PORT,
			TestNGIntegrationProperties.OWFS_DEVICE_DS2408_INPUT
	})
	@Test
	public void shouldReceiveAtLeastOneMessage(String hostName, int port, String inputDevice) throws InterruptedException, IOException, OwfsException {

		OwfsClientFactory factory = new OwfsClientFactory(hostName, port);
		AlarmingDevicesReader alarmingDevicesReader = new AlarmingDevicesReader(factory);
		SwitchAlarmingDeviceListener ds2408AlarmingDeviceHandler = new SwitchAlarmingDeviceListener(
				inputDevice,
				SwitchAlarmingDeviceListener.ALARMING_MASK_8_SWITCHES
		) {
			@Override
			public void handleAlarm(SwitchAlarmingDeviceEvent event) {
				log.info("Alarm '" + getDeviceName() + "' : latch:;" + event.latchStatus + "', sensed:'" + event.sensedStatus + "'");
				cancelTestSuccesfully();
			}
		};
		alarmingDevicesReader.addAlarmingDeviceHandler(ds2408AlarmingDeviceHandler);
		AlarmingDevicesScanner alarmingDevicesScanner = new AlarmingDevicesScanner(alarmingDevicesReader);

		alarmingDevicesScanner.init();
		doManualAction(inputDevice);
		alarmingDevicesScanner.shutdown();
		assertTrue(messageReceived);
	}

	private void cancelTestSuccesfully() {
		messageReceived = true;
		waitingThread.interrupt();
	}

	private void doManualAction(String inputDevice) throws InterruptedException {
		log.info("set '" + inputDevice + "' in alarm mode, for example by set high on any input. You've got 60 seconds to do that!");
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			log.info("Supposedly you set high on input and everything works properly!");
		}
	}
}
