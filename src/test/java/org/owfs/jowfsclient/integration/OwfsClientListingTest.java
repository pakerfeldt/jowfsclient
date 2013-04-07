package org.owfs.jowfsclient.integration;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author Tom Kucharski
 */
@Test(groups = TestNGGroups.INTEGRATION)
public class OwfsClientListingTest extends OwfsClientTest {
	private static final Logger log = LoggerFactory.getLogger(OwfsClientListingTest.class);

	@Test
	public void shouldShowDirNotExist() throws IOException, OwfsException {
		Boolean exists = client.exists("/alarmingXXX");
		assertTrue(exists);
	}

	@Test
	public void shouldListAlarmDirectioryWithoutException() throws Exception {
		listDirectory("/alarm");
	}

	@Test
	public void shouldListMainDirectioryWithoutException() throws Exception {
		listDirectory("/");
	}

	private void listDirectory(String path) throws OwfsException, IOException {
		List<String> directories = client.listDirectory(path);
		for (String dir : directories) {
			log.info("DIR-> " + dir);
			List<String> subdirectories = client.listDirectory(dir);
			for (String subdir : subdirectories) {
				log.info("\t SUBDIR-> " + dir);
				tryToReadAndLogPathValue(subdir);
			}
		}
		client.disconnect();
	}

	private void tryToReadAndLogPathValue(String subdir) throws IOException {
		try {
			log.info("\t" + subdir + "\t:" + client.read(subdir));
		} catch (OwfsException e) {
			log.info("\t" + subdir + "\t: DIRECTORY");
		}
	}
}

