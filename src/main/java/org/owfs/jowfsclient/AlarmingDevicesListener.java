package org.owfs.jowfsclient;

import java.util.List;

/**
 * @author Tomasz Kucharski <kucharski.tom@gmail.com>
 * @since 10.03.13 03:13
 */
public interface AlarmingDevicesListener {

	void alarmForDevices(List<String> devices);
}
