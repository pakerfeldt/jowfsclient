package org.owfs.jowfsclient.alarm;

import java.io.IOException;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsException;

/**
 * @author Tom Kucharski
 */
public interface AlarmingDeviceHandler {

	void onInitialize(OwfsClient client, String deviceName) throws IOException, OwfsException;

	void onAlarm(OwfsClient client, String devicePath, String device) throws IOException, OwfsException;
}
