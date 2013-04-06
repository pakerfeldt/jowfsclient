package org.owfs.jowfsclient.alarm;

import java.io.IOException;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsException;

/**
 * @author Tom Kucharski
 */
public abstract class SwitchAlarmingDeviceHandler implements AlarmingDeviceHandler {

	public static final String COMMAND_SET_ALARM = "/set_alarm";
	public static final String COMMAND_POWER_ON_RESET = "/por";
	public static final String COMMAND_POWER_ON_RESET_CLEARING_VALUE = "0";
	public static final String COMMAND_LATCH_ALL = "/latch.ALL";
	public static final String COMMAND_SENSED_ALL = "/sensed.ALL";
	public static final String COMMAND_LATCH_1 = "/latch.1";
	public static final String ANY_LATCH_ON = "1";
	public static final String ANY_LATCH_OFF = "0";

	private String alarmingMask;

	public SwitchAlarmingDeviceHandler(String alarmingMask) {
		this.alarmingMask = alarmingMask;
	}

	@Override
	public void onInitialize(OwfsClient client, String deviceName) throws IOException, OwfsException {
		setAlarmTrigger(client, deviceName);
		clearDeviceJustPoweredFlag(client, deviceName);
	}

	@Override
	public void onAlarm(OwfsClient client, String devicePath, String device) throws IOException, OwfsException {
		String latchStatus = readWhatIsLatched(client, devicePath);
		if (noneLatchOn(latchStatus)) {
			clearDeviceJustPoweredFlag(client, device);
		} else {
			clearDeviceLatchAllStatus(client, device);
			String sensedStatus = readWhatIsActuallySensed(client, devicePath);
			handleAlarm(devicePath, latchStatus, sensedStatus);
		}
	}

	private void clearDeviceLatchAllStatus(OwfsClient client, String devicePath) throws IOException, OwfsException {
		//clearing any latch will clear all latches according to dallas spec
		client.write(devicePath + COMMAND_LATCH_1, ANY_LATCH_OFF);
	}

	public abstract void handleAlarm(String deviceName, String latchStatus, String sensedStatus);

	private String readWhatIsActuallySensed(OwfsClient client, String devicePath) throws IOException, OwfsException {
		return client.read(devicePath + COMMAND_SENSED_ALL);
	}

	private boolean noneLatchOn(String latchAll) {
		return !latchAll.contains(ANY_LATCH_ON);
	}

	private String readWhatIsLatched(OwfsClient client, String devicePath) throws IOException, OwfsException {
		return client.read(devicePath + COMMAND_LATCH_ALL);
	}

	private void clearDeviceJustPoweredFlag(OwfsClient client, String deviceName) throws IOException, OwfsException {
		client.write(deviceName + COMMAND_POWER_ON_RESET, COMMAND_POWER_ON_RESET_CLEARING_VALUE);
	}

	private void setAlarmTrigger(OwfsClient client, String deviceName) throws IOException, OwfsException {
		client.write(deviceName + COMMAND_SET_ALARM, alarmingMask);
	}

}
