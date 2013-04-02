package org.owfs.jowfsclient.scanner;

/**
 * @author Tomasz Kucharski <kucharski.tom@gmail.com>
 * @since 10.03.13 03:13
 */
public interface AlarmingDevicesListener {

	void alarmForDevices(AlarmingDeviceEvent event);
}
