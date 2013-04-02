package org.owfs.jowfsclient.integration;

import java.io.IOException;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.owfs.jowfsclient.scanner.AlarmingDeviceEvent;
import org.owfs.jowfsclient.scanner.AlarmingDevicesListener;
import org.owfs.jowfsclient.scanner.AlarmingDevicesReader;
import org.owfs.jowfsclient.scanner.AlarmingDevicesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @author Tomasz Kucharski <kucharski.tom@gmail.com>
 * @since 11.03.13 00:42
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
		alarmingDevicesReader.addObservableDevice(inputDevice);
		alarmingDevicesReader.setAlarmListener(new AlarmingDevicesListener() {
			@Override
			public void alarmForDevices(AlarmingDeviceEvent event) {
				log.info("Alarming devices: " + event);
			}
		});
		AlarmingDevicesScanner alarmingDevicesScanner = new AlarmingDevicesScanner(alarmingDevicesReader);

		OwfsClient connection = factory.createNewConnection();
		connection.write(inputDevice + "/set_alarm", "133333333");
		connection.write(inputDevice + "/por", "0");

		alarmingDevicesScanner.init();
		doManualAction();
		alarmingDevicesScanner.shutdown();
	}

	private void doManualAction() throws InterruptedException {
		log.info("set OwfsClientDS2408InputTest.OWFS_DEVICE_DS2408_INPUT in alarm mode, for example by set high on any input");
		Thread.sleep(60000);
	}
}
