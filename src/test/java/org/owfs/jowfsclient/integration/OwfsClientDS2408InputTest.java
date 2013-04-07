package org.owfs.jowfsclient.integration;

import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;
import java.util.List;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @author Tom Kucharski
 */
public class OwfsClientDS2408InputTest extends OwfsClientTest {
	private static final Logger log = LoggerFactory.getLogger(OwfsClientDS2408InputTest.class);

	public static final String OWFS_DEVICE_DS2408_INPUT = "owfs.device.ds2408.input";

	public static final String ALARM_FORMAT_CHANGE_ON_ANY_INPUT = "133333333";
	public static final String ALARM_FORMAT_CHANGE_ON_ANY_INPUT_WEIRD_RESULT = "   122222230";

	protected String deviceDs2408;

	@BeforeClass
	@Parameters(OWFS_DEVICE_DS2408_INPUT)
	public void setOwfsDeviceDs2408(String deviceDs2408) {
		log.info("setOwfsDeviceDs2408:" + deviceDs2408);
		this.deviceDs2408 = deviceDs2408;
	}

	@Test(groups = TestNGGroups.INTEGRATION)
	public void shouldConfigureAlarmWith_WeirdResult() throws Exception {
		//given
		setDefaultAlarmFormat();

		//when
		String read = client.read(deviceDs2408 + "/set_alarm");

		//then
		assertEquals(ALARM_FORMAT_CHANGE_ON_ANY_INPUT_WEIRD_RESULT, read);
	}


	protected boolean checkIfDeviceIsAlarming(String device) throws OwfsException, IOException {
		List<String> alarmingDevices = client.listDirectoryAll("/alarm");
		for (String devicePath : alarmingDevices) {
			if (devicePath.contains(device)) {
				return true;
			}
		}
		return false;
	}

	protected void disableAlarming() throws IOException, OwfsException {
		client.write(deviceDs2408 + "/latch.BYTE", "0");
	}

	protected void setDefaultAlarmFormat() throws IOException, OwfsException {
		client.write(deviceDs2408 + "/set_alarm", ALARM_FORMAT_CHANGE_ON_ANY_INPUT);
	}
}
