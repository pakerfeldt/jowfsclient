package org.owfs.jowfsclient.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Vector;
import org.owfs.jowfsclient.Enums.OwMessageType;
import org.owfs.jowfsclient.TestNGGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = TestNGGroups.UNIT)
public class RequestPacketTest {

	public static final Vector<OwMessageType> writingTypes = new Vector<OwMessageType>();

	RequestPacket packet1;
	public static final OwMessageType p1_function = OwMessageType.DIR;
	public static final Flags p1_flags = new Flags(100);
	public static final String p1_payload = "P1-Payload";
	public static final int p1_datalen = 4096;

	RequestPacket packet2;
	public static final OwMessageType p2_function = OwMessageType.WRITE;
	public static final Flags p2_flags = new Flags(100);
	public static final String p2_payload = "P2-Payload";
	public static final String p2_write = "P2-ToWrite";

	Vector<RequestPacket> packets = new Vector<RequestPacket>();

	@BeforeClass
	protected void setUp() throws Exception {
		writingTypes.add(OwMessageType.WRITE);

		packet1 = new RequestPacket(p1_function, p1_datalen, p1_flags,
				p1_payload);
		packets.add(packet1);
		packet2 = new RequestPacket(p2_function, p2_flags, p2_payload, p2_write);
		packets.add(packet2);
	}

	@Test
	public void testGetPayload() {
		byte[] payload = packet1.getPayload();
		assertTrue(payload.length == p1_payload.length() + 1);
		assertTrue(payload[payload.length - 1] == 0);
		assertTrue(payload[payload.length - 2] == p1_payload.charAt(p1_payload
				.length() - 1));

		payload = packet2.getPayload();
		assertTrue(payload.length == p2_payload.length() + 1);
		assertTrue(payload[payload.length - 1] == 0);
		assertTrue(payload[payload.length - 2] == p2_payload.charAt(p2_payload
				.length() - 1));

	}

	@Test
	public void testGetFunction() {
		assertTrue(packet1.getFunction().equals(p1_function));
		assertTrue(packet2.getFunction().equals(p2_function));
	}

	@Test
	public void testWritingData() {
		for (RequestPacket p : packets) {
			if (writingTypes.contains(p.getFunction())) {
				assertTrue(p.isWritingData());
			} else {
				assertFalse(p.isWritingData());
			}
		}
	}

	@Test
	public void testDataToWrite() {
		assertNull(packet1.getDataToWrite());
		assertArrayEquals(p2_write.getBytes(), packet2.getDataToWrite());
	}
}
