package org.owfs.jowfsclient.alarm;

/**
 * @author Tom Kucharski
 */
public class OWFSUtils {
	public static final String SLASH = "/";
	public static final String ALARM_FOLDER_FOR_OWFS_SERVER = "/alarm/";

	public static String extractDeviceNameFromDevicePath(String devicePath) {
		if (devicePath.endsWith(SLASH)) {
			devicePath = devicePath.substring(0, devicePath.length() - 1);
		}
		if (devicePath.contains(SLASH)) {
			return devicePath.substring(devicePath.lastIndexOf(SLASH) + 1);
		} else {
			return devicePath;
		}
	}
}
