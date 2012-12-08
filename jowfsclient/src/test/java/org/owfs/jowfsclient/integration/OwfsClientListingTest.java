package org.owfs.jowfsclient.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author Tom Kucharski
 * @since 12/8/12 11:32 PM
 */
@Test(groups = TestNGGroups.INTEGRATION)
public class OwfsClientListingTest extends OwfsClientTest {

    private static final Log log = LogFactory.getLog(OwfsClientListingTest.class);

    @Test
    public void shouldListDirectiories() throws Exception {
        List<String> directories = client.listDirectoryAll("/");
        for (String dir : directories) {
            log.info(dir);
            List<String> subdirectories = client.listDirectoryAll(dir);
            for (String subdir : subdirectories) {
                try {
                    log.info("\t" + subdir + "\t:" + client.read(subdir));
                } catch (OwfsException e) {
                    log.info("\t" + subdir + "\t: DIRECTORY");
                }
            }
        }
        client.disconnect();
    }
}

