package org.owfs.jowfsclient.integration;

import java.io.IOException;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.owfs.jowfsclient.alarm.AlarmingDevicesReader;
import org.owfs.jowfsclient.alarm.AlarmingDevicesScanner;
import org.owfs.jowfsclient.alarm.SwitchAlarmingDeviceHandler;
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

	@Parameters({
			TestNGIntegrationProperties.OWFS_HOSTNAME,
			TestNGIntegrationProperties.OWFS_PORT,
			TestNGIntegrationProperties.OWFS_DEVICE_DS2408_INPUT
	})
	@Test
	public void shouldReceiveAtLeastOneMessage(String hostName, int port, String inputDevice) throws InterruptedException, IOException, OwfsException {
		OwfsClientFactory factory = new OwfsClientFactory(hostName, port);

		AlarmingDevicesReader alarmingDevicesReader = new AlarmingDevicesReader(factory);
		SwitchAlarmingDeviceHandler ds2408AlarmingDeviceHandler = new SwitchAlarmingDeviceHandler("133333333") {
			@Override
			public void handleAlarm(String deviceName, String latchStatus, String sensedStatus) {
				log.info("Alarm '" + deviceName + "' : latch:;" + latchStatus + "', sensed:'" + sensedStatus + "'");
			}
		};
		alarmingDevicesReader.addObservableDevice(inputDevice, ds2408AlarmingDeviceHandler);
		AlarmingDevicesScanner alarmingDevicesScanner = new AlarmingDevicesScanner(alarmingDevicesReader);


		alarmingDevicesScanner.init();
		doManualAction(inputDevice);
		alarmingDevicesScanner.shutdown();
	}

	private void doManualAction(String inputDevice) throws InterruptedException {
		log.info("set '" + inputDevice + "' in alarm mode, for example by set high on any input");
		Thread.sleep(60000);
	}
}
