package org.owfs.jowfsclient.scanner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomasz Kucharski <kucharski.tom@gmail.com>
 * @since 10.03.13 01:51
 */
@Test(groups = TestNGGroups.UNIT)
public class AlarmingDevicesReaderTest {

	public static final String DEVICE = "XXX";

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

	public void shouldNotifyIfAlarmingDevicesFound() throws IOException, OwfsException {
		//given
		OwfsClientFactory factory = createListingDirsOwfsClientFactory();
		AlarmingDevicesReader alarmingDevicesReader = new AlarmingDevicesReader(factory);
		alarmingDevicesReader.addObservableDevice(DEVICE);
		AlarmingDevicesListener alarmingDevicesListener = mock(AlarmingDevicesListener.class);
		alarmingDevicesReader.setAlarmListener(alarmingDevicesListener);

		//when
		alarmingDevicesReader.run();

		//then
		verify(alarmingDevicesListener, times(1)).alarmForDevices(any(AlarmingDeviceEvent.class));

	}

	private OwfsClientFactory createListingDirsOwfsClientFactory() throws OwfsException, IOException {
		OwfsClientFactory factory = mock(OwfsClientFactory.class);
		OwfsClient mock = mock(OwfsClient.class);
		when(factory.createNewConnection()).thenReturn(mock);
		ArrayList arrayList = new ArrayList();
		arrayList.add("/alarm/" + DEVICE);
		when(mock.listDirectory(anyString())).thenReturn(arrayList);
		return factory;
	}

	public void shouldRebuildDevicesListWithoutDirectoryPath() {
		//given
		String devicePath = "/alarm/T1";

		//when
		String deviceName = new AlarmingDevicesReader(null).extractDeviceNameFromDevicePath(devicePath);

		//then
		Assert.assertEquals("T1", deviceName);
	}


}
