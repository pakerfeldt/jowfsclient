package org.owfs.jowfsclient;

import java.util.ArrayList;
import java.util.Collection;

public class CommandRegistry {
	private Collection<Command> registeredCommands = new ArrayList<Command>();

	public Collection<Command> getRegisteredCommands() {
		return registeredCommands;
	}

	public void register(Command command) {
		registeredCommands.add(command);
	}
}
