package org.owfs.jowfsclient.device;

/**
 * @author Tom Kucharski <kucharski.tom@gmail.com>
 * @since 08.04.13 23:56
 */
public class SwitchAlarmingDeviceEvent {

	public String latchStatus;
	public String sensedStatus;

	public SwitchAlarmingDeviceEvent(String latchStatus, String sensedStatus) {
		this.latchStatus = latchStatus;
		this.sensedStatus = sensedStatus;
	}

	public boolean[] getLatchStatusAsArray() {
		return convertToArray(latchStatus);
	}

	public boolean[] getSensedStatusAsArray() {
		return convertToArray(sensedStatus);
	}

	public String getLatchStatus() {
		return latchStatus;
	}

	public String getSensedStatus() {
		return sensedStatus;
	}

	private boolean[] convertToArray(String arrayAsString) {
		String[] split = arrayAsString.split(",");
		boolean[] result = new boolean[split.length];
		for (int i = 0; i < split.length; i++) {
			String value = split[i];
			result[i] = "1".equals(value);
		}
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("AlarmEvent{");
		sb.append("latchStatus='").append(latchStatus).append('\'');
		sb.append(", sensedStatus='").append(sensedStatus).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
