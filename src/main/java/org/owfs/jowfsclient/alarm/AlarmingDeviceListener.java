package org.owfs.jowfsclient.alarm;

import java.io.IOException;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsException;

/**
 * @author Tom Kucharski
 */
public interface AlarmingDeviceListener {

	String getDeviceName();

	void onInitialize(OwfsClient client) throws IOException, OwfsException;

	void onAlarm(OwfsClient client) throws IOException, OwfsException;
}
