package org.owfs.jowfsclient;

import org.owfs.jowfsclient.alarm.OWFSUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Tom Kucharski
 */
@Test(groups = TestNGGroups.UNIT)
public class OWFSUtilsTest {

	public static final String SAMPLE_DEVICE_NAME = "19.BLEBLEBLE";

	@DataProvider(name = "names")
	public Object[][] createData1() {
		return new Object[][]{
				{"/alarm/" + SAMPLE_DEVICE_NAME, SAMPLE_DEVICE_NAME},
				{SAMPLE_DEVICE_NAME, SAMPLE_DEVICE_NAME},
				{"/" + SAMPLE_DEVICE_NAME, SAMPLE_DEVICE_NAME},
				{SAMPLE_DEVICE_NAME + "/", SAMPLE_DEVICE_NAME},
		};
	}

	@Test(dataProvider = "names")
	public void shouldExtractDeviceNameFromDevicePath(String deviceNameBefore, String expectedDeviceName) {
		//given
		String devicePath = deviceNameBefore;

		//when
		String deviceName = OWFSUtils.extractDeviceNameFromDevicePath(devicePath);

		//then
		Assert.assertEquals(deviceName, expectedDeviceName);
	}
}
