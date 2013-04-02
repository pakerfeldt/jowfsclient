package org.owfs.jowfsclient.scanner;

/**
 * @author Tomasz Kucharski <kucharski.tom@gmail.com>
 * @since 10.03.13 00:27
 */
public class ReadCommand {

	public String name;
	public String returnValue;

	public ReadCommand(String name, String returnValue) {
		this.name = name;
		this.returnValue = returnValue;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ReadCommand");
		sb.append("{name='").append(name).append('\'');
		sb.append(", returnValue='").append(returnValue).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
