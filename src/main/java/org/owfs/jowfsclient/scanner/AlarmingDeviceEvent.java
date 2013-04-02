package org.owfs.jowfsclient.scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tomasz Kucharski <kucharski.tom@gmail.com>
 * @since 02.04.13 01:38
 */
public class AlarmingDeviceEvent {

	private String name;

	private List<ReadCommand> commands = new ArrayList<ReadCommand>();

	public AlarmingDeviceEvent(String name, ReadCommand... commands) {
		this.name = name;
		this.commands.addAll(Arrays.asList(commands));
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("AlarmingDeviceEvent");
		sb.append("{name='").append(name).append('\'');
		sb.append(", commands=").append(commands);
		sb.append('}');
		return sb.toString();
	}
}
