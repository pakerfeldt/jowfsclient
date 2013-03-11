package org.owfs.jowfsclient;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomasz Kucharski <kucharski.tom@gmail.com>
 * @since 10.03.13 01:51
 */
@Test(groups = TestNGGroups.UNIT)
public class AlarmingDevicesReaderTest {

	public static final List<String> DEVICES = Arrays.asList("XXX", "YYY");

	public void shouldConnectIfNecessary() {
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
		AlarmingDevicesListener alarmingDevicesListener = mock(AlarmingDevicesListener.class);
		alarmingDevicesReader.setAlarmListener(alarmingDevicesListener);

		//when
		alarmingDevicesReader.run();

		//then
		verify(alarmingDevicesListener, times(1)).alarmForDevices(DEVICES);

	}

	private OwfsClientFactory createListingDirsOwfsClientFactory() throws OwfsException, IOException {
		OwfsClientFactory factory = mock(OwfsClientFactory.class);
		OwfsClient mock = mock(OwfsClient.class);
		when(factory.createNewConnection()).thenReturn(mock);
		when(mock.listDirectoryAll(anyString())).thenReturn(DEVICES);
		return factory;
	}

	public void shouldRebuildDevicesListWithoutDirectoryPath() {
		//given
		List<String> strings = Arrays.asList("/alarm/T1", "/alarm/T2");

		//when
		ArrayList<String> fixed = new AlarmingDevicesReader(null).rebuildDevicesListWithoutDirectoryPath(strings);

		//then
		Assert.assertEquals(Arrays.asList("T1", "T2"),fixed);
	}



}
