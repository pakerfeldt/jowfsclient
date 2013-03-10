package org.owfs.jowfsclient.integration;

import java.io.IOException;
import java.util.List;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author Tom Kucharski
 * @since 12/8/12 11:32 PM
 */
@Test(groups = TestNGGroups.INTEGRATION)
public class OwfsClientListingTest extends OwfsClientTest {

	private static final Logger log = LoggerFactory.getLogger(OwfsClientListingTest.class);

	@Test
	public void shouldListDirectiories() throws Exception {
		List<String> directories = client.listDirectoryAll("/");
		for (String dir : directories) {
			log.info(dir);
			List<String> subdirectories = client.listDirectoryAll(dir);
			for (String subdir : subdirectories) {
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

