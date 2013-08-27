package org.owfs.jowfsclient.integration;

import org.owfs.jowfsclient.TestNGGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @author Tom Kucharski
 */
@Test(groups = TestNGGroups.INTEGRATION)
public class OwfsClientDS2408OutputTest extends OwfsClientTest {
	private static final Logger log = LoggerFactory.getLogger(OwfsClientDS2408OutputTest.class);

	public static final String OWFS_DEVICE_DS2408_OUTPUT = "owfs.device.ds2408.output";

	private String deviceDs2408;

	@BeforeClass
	@Parameters(OWFS_DEVICE_DS2408_OUTPUT)
	public void setOwfsDeviceDs2408(String deviceDs2408) {
		log.info("setOwfsDeviceDs2408:" + deviceDs2408);
		this.deviceDs2408 = deviceDs2408;
	}

	@Test
	public void switch1TurnOnAndOff() throws Exception {
		client.write(deviceDs2408 + "/PIO.0", "1");
		client.write(deviceDs2408 + "/PIO.0", "0");
	}

	@Test
	public void turnOnAllSwitches() throws Exception {
		client.write(deviceDs2408 + "/PIO.ALL", "0,0,0,0,0,0,0,0");
	}
}
