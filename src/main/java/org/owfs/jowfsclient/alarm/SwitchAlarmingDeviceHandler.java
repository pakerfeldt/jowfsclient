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

	private final String deviceName;
	private String alarmingMask;

	public SwitchAlarmingDeviceHandler(String deviceName, String alarmingMask) {
		this.alarmingMask = alarmingMask;
		this.deviceName = deviceName;
	}

	@Override
	public String getDeviceName() {
		return deviceName;
	}

	@Override
	public void onInitialize(OwfsClient client) throws IOException, OwfsException {
		setAlarmMaskTrigger(client);
		clearDeviceJustPoweredFlag(client);
	}

	@Override
	public void onAlarm(OwfsClient client) throws IOException, OwfsException {
		String latchStatus = readWhatIsLatched(client);
		if (noneLatchOn(latchStatus)) {
			clearDeviceJustPoweredFlag(client);
		} else {
			clearDeviceLatchAllStatus(client);
			String sensedStatus = readWhatIsActuallySensed(client);
			handleAlarm(latchStatus, sensedStatus);
		}
	}

	private void clearDeviceLatchAllStatus(OwfsClient client) throws IOException, OwfsException {
		//clearing any latch will clear all latches according to dallas spec
		client.write(deviceName + COMMAND_LATCH_1, ANY_LATCH_OFF);
	}

	public abstract void handleAlarm(String latchStatus, String sensedStatus);

	private String readWhatIsActuallySensed(OwfsClient client) throws IOException, OwfsException {
		return client.read(deviceName + COMMAND_SENSED_ALL);
	}

	private boolean noneLatchOn(String latchAll) {
		return !latchAll.contains(ANY_LATCH_ON);
	}

	private String readWhatIsLatched(OwfsClient client) throws IOException, OwfsException {
		return client.read(deviceName + COMMAND_LATCH_ALL);
	}

	private void clearDeviceJustPoweredFlag(OwfsClient client) throws IOException, OwfsException {
		client.write(deviceName + COMMAND_POWER_ON_RESET, COMMAND_POWER_ON_RESET_CLEARING_VALUE);
	}

	private void setAlarmMaskTrigger(OwfsClient client) throws IOException, OwfsException {
		client.write(deviceName + COMMAND_SET_ALARM, alarmingMask);
	}

}
