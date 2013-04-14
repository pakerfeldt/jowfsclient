package org.owfs.jowfsclient.device;

import java.io.IOException;
import org.owfs.jowfsclient.OwfsConnection;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.alarm.AlarmingDeviceListener;

/**
 * @author Tom Kucharski
 */
public abstract class SwitchAlarmingDeviceListener implements AlarmingDeviceListener {
	public static final String ALARMING_MASK_8_SWITCHES = "133333333";
	public static final String ALARMING_MASK_2_SWITCHES = "133";

	private static final String COMMAND_SET_ALARM = "/set_alarm";
	private static final String COMMAND_POWER_ON_RESET = "/por";
	private static final String COMMAND_POWER_ON_RESET_CLEARING_VALUE = "0";
	private static final String COMMAND_LATCH_ALL = "/latch.ALL";
	private static final String COMMAND_SENSED_ALL = "/sensed.ALL";
	private static final String COMMAND_LATCH_1 = "/latch.1";
	private static final String ANY_LATCH_ON = "1";
	private static final String ANY_LATCH_OFF = "0";

	private final String deviceName;
	private String alarmingMask;

	public SwitchAlarmingDeviceListener(String deviceName, String alarmingMask) {
		this.alarmingMask = alarmingMask;
		this.deviceName = deviceName;
	}

	@Override
	public String getDeviceName() {
		return deviceName;
	}

	@Override
	public void onInitialize(OwfsConnection client) throws IOException, OwfsException {
		setAlarmMaskTrigger(client);
		clearDeviceJustPoweredFlag(client);
	}

	@Override
	public void onAlarm(OwfsConnection client) throws IOException, OwfsException {
		String latchStatus = readWhatIsLatched(client);
		String sensedStatus = readWhatIsActuallySensed(client);
		if (noneLatchOn(latchStatus)) {
			clearDeviceJustPoweredFlag(client);
		} else {
			clearDeviceLatchAllStatus(client);
			handleAlarm(new SwitchAlarmingDeviceEvent(latchStatus, sensedStatus));
		}
	}

	private void clearDeviceLatchAllStatus(OwfsConnection client) throws IOException, OwfsException {
		//clearing any latch will clear all latches according to dallas spec
		client.write(deviceName + COMMAND_LATCH_1, ANY_LATCH_OFF);
	}

	public abstract void handleAlarm(SwitchAlarmingDeviceEvent event);

	private String readWhatIsActuallySensed(OwfsConnection client) throws IOException, OwfsException {
		return client.read(deviceName + COMMAND_SENSED_ALL);
	}

	private boolean noneLatchOn(String latchAll) {
		return !latchAll.contains(ANY_LATCH_ON);
	}

	private String readWhatIsLatched(OwfsConnection client) throws IOException, OwfsException {
		return client.read(deviceName + COMMAND_LATCH_ALL);
	}

	private void clearDeviceJustPoweredFlag(OwfsConnection client) throws IOException, OwfsException {
		client.write(deviceName + COMMAND_POWER_ON_RESET, COMMAND_POWER_ON_RESET_CLEARING_VALUE);
	}

	private void setAlarmMaskTrigger(OwfsConnection client) throws IOException, OwfsException {
		client.write(deviceName + COMMAND_SET_ALARM, alarmingMask);
	}

}
