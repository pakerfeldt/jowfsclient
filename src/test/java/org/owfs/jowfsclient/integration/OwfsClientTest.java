package org.owfs.jowfsclient.integration;

import org.owfs.jowfsclient.Enums;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.TestNGGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * This is integration test class and you have to configure it to run these tests correctly.
 * These tests use owfs server that has to be deployed and running.
 * All you have to do configure it is to add simple properties file testng-integration.properties to project base directory.
 * Your file has to contain variables listed below:
 * owfs.hostname=192.168.1.2
 * owfs.port=4304
 * owfs.device.ds2408.output=29.07960B00000019
 * owfs.device.ds2408.input=29.DD940B00000091
 *
 * @author Tom Kucharski
 * @since 12/6/12 11:56 PM
 */
@Test(groups = TestNGGroups.INTEGRATION)
public class OwfsClientTest {
	private static final Logger log = LoggerFactory.getLogger(OwfsClientTest.class);

	public static final String OWFS_HOSTNAME = "owfs.hostname";
	public static final String OWFS_PORT = "owfs.port";

	protected OwfsClient client;

	private String owfsHostname;
	private int owfsPort;

	@BeforeClass
	@Parameters(OWFS_HOSTNAME)
	public void setOwfsHostname(String owfsHostname) {
		log.info("setOwfsHostname:" + owfsHostname);
		this.owfsHostname = owfsHostname;
	}

	@BeforeClass
	@Parameters(OWFS_PORT)
	public void setOwfsPort(int owfsPort) {
		log.info("setOwfsPort:" + owfsPort);
		this.owfsPort = owfsPort;
	}

	@BeforeMethod
	public void constructOwfsClient() {
		OwfsClientFactory owfsClientFactory = new OwfsClientFactory(owfsHostname,owfsPort);
		client = owfsClientFactory.createNewConnection();
	}
}