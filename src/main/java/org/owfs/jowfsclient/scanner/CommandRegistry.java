package org.owfs.jowfsclient.scanner;

import java.util.ArrayList;
import java.util.Collection;

public class CommandRegistry {
	private Collection<ReadCommand> registeredCommands = new ArrayList<ReadCommand>();

	public Collection<ReadCommand> getRegisteredCommands() {
		return registeredCommands;
	}

	public void register(ReadCommand command) {
		registeredCommands.add(command);
	}
}
