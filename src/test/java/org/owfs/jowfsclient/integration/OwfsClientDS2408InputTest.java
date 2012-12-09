package org.owfs.jowfsclient.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.FileAssert.fail;

/**
 * @author Tom Kucharski
 * @since 12/8/12 11:29 PM
 */
public class OwfsClientDS2408InputTest extends OwfsClientTest {
    private static final Log log = LogFactory.getLog(OwfsClientDS2408InputTest.class);

    public static final String OWFS_DEVICE_DS2408_INPUT = "owfs.device.ds2408.input";

    public static final String ALARM_FORMAT_CHANGE_ON_ANY_INPUT = "133333333";
    public static final String ALARM_FORMAT_CHANGE_ON_ANY_INPUT_WEIRD_RESULT = "   122222230";

    private String deviceDs2408;

    @BeforeClass
    @Parameters(OWFS_DEVICE_DS2408_INPUT)
    public void setOwfsDeviceDs2408(String deviceDs2408) {
        log.info("setOwfsDeviceDs2408:" + deviceDs2408);
        this.deviceDs2408 = deviceDs2408;
    }

    @Test(groups = TestNGGroups.INTEGRATION)
    public void shouldConfigureAlarmWith_WeirdResult() throws Exception {
        //given
        setDefaultAlarmFormat();

        //when
        String read = client.read(deviceDs2408 + "/set_alarm");

        //then
        assertEquals(ALARM_FORMAT_CHANGE_ON_ANY_INPUT_WEIRD_RESULT, read);
    }

    @Test(groups = TestNGGroups.INTEGRATION_MANUAL,
            description = "This test needs your manual action to sense any input in your DS2408 device"
    )
    public void testManuallyTestHowAlarmWorks() throws Exception {
        setDefaultAlarmFormat();

        //I know, this is weird, but simply waits max 1000 loops until you physically sense any input in your device
        for (int i = 0; i < 1000; i++) {
            log.info("Waiting " + i + "/1000 loops until you physically sense any input in device " + deviceDs2408 + "...");
            if (checkIfDeviceIsAlarming(deviceDs2408)) {
                log.info("Well done! Input sensed: " + client.read("/alarm/" + deviceDs2408 + "/latch.ALL"));
                disableAlarming();
                return;
            }
        }
        fail();
    }

    private boolean checkIfDeviceIsAlarming(String device) throws OwfsException, IOException {
        List<String> alarmingDevices = client.listDirectoryAll("/alarm");
        for (String devicePath : alarmingDevices) {
            if (devicePath.contains(device)) {
                return true;
            }
        }
        return false;
    }

    private void disableAlarming() throws IOException, OwfsException {
        client.write(deviceDs2408 + "/latch.BYTE", "0");
    }

    private void setDefaultAlarmFormat() throws IOException, OwfsException {
        client.write(deviceDs2408 + "/set_alarm", ALARM_FORMAT_CHANGE_ON_ANY_INPUT);
    }
}
