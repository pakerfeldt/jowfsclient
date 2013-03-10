package org.owfs.jowfsclient.internal;

import static org.testng.Assert.assertTrue;

import org.owfs.jowfsclient.TestNGGroups;
import org.testng.annotations.Test;

@Test(groups = TestNGGroups.UNIT)
public class HeaderTest {

	private static final int h1_version = 45;
	private static final int h1_paylen = 45;
	private static final int h1_function = 45;
	private static final Flags h1_flags = new Flags(100);
	private static final int h1_datalen = 45;
	private static final int h1_offset = 45;

	Header header1 = new Header(h1_version, h1_paylen, h1_function, h1_flags, h1_datalen, h1_offset);

	@Test
	public void testGetVersion() {
		assertTrue(h1_version == header1.getVersion());
	}

	@Test
	public void testGetPayloadLength() {
		assertTrue(h1_paylen == header1.getPayloadLength());
	}

	@Test
	public void testGetFlags() {
		assertTrue(h1_flags.equals(header1.getFlags()));
	}

	@Test
	public void testGetDataLength() {
		assertTrue(h1_datalen == header1.getDataLength());
	}

	@Test
	public void testGetOffset() {
		assertTrue(h1_offset == header1.getOffset());
	}

	@Test
	public void testGetFunction() {
		assertTrue(h1_function == header1.getFunction());
	}


}
