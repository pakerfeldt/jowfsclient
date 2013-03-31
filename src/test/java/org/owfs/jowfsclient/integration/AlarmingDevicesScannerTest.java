package org.owfs.jowfsclient.integration;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.List;
import org.owfs.jowfsclient.AlarmingDevicesListener;
import org.owfs.jowfsclient.AlarmingDevicesReader;
import org.owfs.jowfsclient.AlarmingDevicesScanner;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
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
	public void shouldReceiveAtLeastOneMessage(String hostName,int port, String inputDevice) throws InterruptedException, IOException, OwfsException {
		OwfsClientFactory factory = new OwfsClientFactory(hostName,port);

		AlarmingDevicesReader alarmingDevicesReader = new AlarmingDevicesReader(factory);
		alarmingDevicesReader.setAlarmListener(new AlarmingDevicesListener() {
			@Override
			public void alarmForDevices(List<String> devices) {
				log.info("Alarming devices: "+devices);
			}
		});
		AlarmingDevicesScanner alarmingDevicesScanner = new AlarmingDevicesScanner(alarmingDevicesReader);

		OwfsClient connection = factory.createNewConnection();
		connection.write(inputDevice+"/set_alarm","133333333");

		alarmingDevicesScanner.init();
		doManualAction();
		alarmingDevicesScanner.shutdown();
	}

	private void doManualAction() throws InterruptedException {
		log.info("set OwfsClientDS2408InputTest.OWFS_DEVICE_DS2408_INPUT in alarm mode, for example by set high on any input");
		Thread.sleep(60000);
	}
}
