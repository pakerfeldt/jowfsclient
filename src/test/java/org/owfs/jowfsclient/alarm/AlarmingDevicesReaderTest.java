package org.owfs.jowfsclient.alarm;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.testng.annotations.Test;

/**
 * @author Tom Kucharski
 */
@Test(groups = TestNGGroups.UNIT)
public class AlarmingDevicesReaderTest {

	public static final String DEVICE_NAME_1 = "DEVICE1";
	public static final String DEVICE_NAME_2 = "DEVICE2";

	public void shouldReuseExistingConnection() {
		//given
		OwfsClientFactory factory = mock(OwfsClientFactory.class);
		when(factory.createNewConnection()).thenReturn(mock(OwfsClient.class));
		AlarmingDevicesReader alarmingDevicesReader = new AlarmingDevicesReader(factory);

		//when
		alarmingDevicesReader.connectIfNecessary();
		alarmingDevicesReader.connectIfNecessary();

		//then
		verify(factory, times(1)).createNewConnection();
	}

	public void shouldNotifySeveralTimesThatAlarmArise() throws IOException, OwfsException {
		//given
		OwfsClient mock = mock(OwfsClient.class);
		when(mock.listDirectory(anyString())).thenReturn(Arrays.asList(DEVICE_NAME_1, DEVICE_NAME_2));
		OwfsClientFactory factory = mock(OwfsClientFactory.class);
		when(factory.createNewConnection()).thenReturn(mock);
		AlarmingDevicesReader alarmingDevicesReader = spy(new AlarmingDevicesReader(factory));

		//when
		alarmingDevicesReader.run();

		//then
		verify(alarmingDevicesReader, times(2)).processAlarmingDevice(anyString());

	}

	public void shouldProcessAlarmingDeviceCommander() throws IOException, OwfsException {
		//given
		OwfsClientFactory owfsClientFactory = mock(OwfsClientFactory.class);
		AlarmingDevicesReader alarmingDevicesReader = new AlarmingDevicesReader(owfsClientFactory);
		AlarmingDeviceHandler alarmingDeviceHandler = mock(AlarmingDeviceHandler.class);
		when(alarmingDeviceHandler.getDeviceName()).thenReturn(DEVICE_NAME_1);
		alarmingDevicesReader.addAlarmingDeviceHandler(alarmingDeviceHandler);

		//when
		alarmingDevicesReader.processAlarmingDevices(Arrays.asList(DEVICE_NAME_1, DEVICE_NAME_2));

		//then
		verify(alarmingDeviceHandler, times(1)).onAlarm(any(OwfsClient.class));
		verify(alarmingDeviceHandler, times(1)).onInitialize(any(OwfsClient.class));
	}

}
