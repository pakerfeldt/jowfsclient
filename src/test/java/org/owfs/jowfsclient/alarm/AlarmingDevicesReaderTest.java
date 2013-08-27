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
import org.owfs.jowfsclient.OwfsConnection;
import org.owfs.jowfsclient.OwfsConnectionFactory;
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
		OwfsConnectionFactory factory = mock(OwfsConnectionFactory.class);
		when(factory.createNewConnection()).thenReturn(mock(OwfsConnection.class));
		AlarmingDevicesReader alarmingDevicesReader = new AlarmingDevicesReader(factory);

		//when
		alarmingDevicesReader.connectIfNecessary();
		alarmingDevicesReader.connectIfNecessary();

		//then
		verify(factory, times(1)).createNewConnection();
	}

	public void shouldNotifySeveralTimesThatAlarmArise() throws IOException, OwfsException {
		//given
		OwfsConnection mock = mock(OwfsConnection.class);
		when(mock.listDirectory(anyString())).thenReturn(Arrays.asList(DEVICE_NAME_1, DEVICE_NAME_2));
		OwfsConnectionFactory factory = mock(OwfsConnectionFactory.class);
		when(factory.createNewConnection()).thenReturn(mock);
		AlarmingDevicesReader alarmingDevicesReader = spy(new AlarmingDevicesReader(factory));

		//when
		alarmingDevicesReader.run();

		//then
		verify(alarmingDevicesReader, times(2)).processAlarmingDevice(anyString());

	}

	public void shouldProcessAlarmingDeviceCommander() throws IOException, OwfsException {
		//given
		OwfsConnectionFactory owfsConnectionFactory = mock(OwfsConnectionFactory.class);
		AlarmingDevicesReader alarmingDevicesReader = new AlarmingDevicesReader(owfsConnectionFactory);
		AlarmingDeviceListener alarmingDeviceListener = mock(AlarmingDeviceListener.class);
		when(alarmingDeviceListener.getDeviceName()).thenReturn(DEVICE_NAME_1);
		alarmingDevicesReader.addAlarmingDeviceHandler(alarmingDeviceListener);

		//when
		alarmingDevicesReader.processAlarmingDevices(Arrays.asList(DEVICE_NAME_1, DEVICE_NAME_2));

		//then
		verify(alarmingDeviceListener, times(1)).onAlarm(any(OwfsConnection.class));
		verify(alarmingDeviceListener, times(1)).onInitialize(any(OwfsConnection.class));
	}

}
