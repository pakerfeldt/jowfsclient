package org.owfs.jowfsclient.integration;

import static org.mockito.Mockito.mock;

import java.util.List;
import org.owfs.jowfsclient.AlarmingDevicesListener;
import org.owfs.jowfsclient.AlarmingDevicesReader;
import org.owfs.jowfsclient.AlarmingDevicesScanner;
import org.owfs.jowfsclient.OwfsClientFactory;
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

	public static final String OWFS_HOSTNAME = "owfs.hostname";
	public static final String OWFS_PORT = "owfs.port";

	@Parameters({OWFS_HOSTNAME,OWFS_PORT})
	public void shouldReceiveAtLeastOneMessage(String hostName,int port) throws InterruptedException {
		OwfsClientFactory factory = new OwfsClientFactory();
		factory.setHostName(hostName);
		factory.setPortNumber(port);

		AlarmingDevicesReader alarmingDevicesReader = new AlarmingDevicesReader(factory);
		alarmingDevicesReader.setAlarmListener(new AlarmingDevicesListener() {
			@Override
			public void alarmForDevices(List<String> devices) {
				log.info("Alarming devices: "+devices);
			}
		});
		AlarmingDevicesScanner alarmingDevicesScanner = new AlarmingDevicesScanner(alarmingDevicesReader);

		//set OwfsClientDS2408InputTest.OWFS_DEVICE_DS2408_INPUT in alarm mode
		alarmingDevicesScanner.init();
		Thread.sleep(10000);
		alarmingDevicesScanner.shutdown();
	}
}
