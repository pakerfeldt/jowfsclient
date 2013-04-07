package org.owfs.jowfsclient;

/**
 * @author Tom Kucharski
 */
public class TestNGGroups {

	/**
	 * Unit test that do not need any external enviromnet
	 */
	public static final String UNIT = "unit";

	/**
	 * Integration tests use real owfs server and in most cases expect devices to be connected to OneWire.
	 * More information can be found in README.md file
	 */
	public static final String INTEGRATION = "integration";

	/**
	 * Integration tests that needs your interaction to exit successfully.
	 */
	public static final String INTEGRATION_MANUAL = "integration_manual";

	/**
	 * Unit tests of external libraries. Mostly for learning purpose, but can also verify contract between jowfsclient and external library
	 */
	public static final String LEARNING = "learning";
}
