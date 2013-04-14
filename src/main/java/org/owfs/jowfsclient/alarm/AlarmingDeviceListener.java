package org.owfs.jowfsclient.alarm;

import java.io.IOException;
import org.owfs.jowfsclient.OwfsConnection;
import org.owfs.jowfsclient.OwfsException;

/**
 * @author Tom Kucharski
 */
public interface AlarmingDeviceListener {

	String getDeviceName();

	void onInitialize(OwfsConnection client) throws IOException, OwfsException;

	void onAlarm(OwfsConnection client) throws IOException, OwfsException;
}
