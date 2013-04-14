package org.owfs.jowfsclient.internal;

import org.owfs.jowfsclient.Enums;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomasz Kucharski <kucharski.tom@gmail.com>
 * @since 14.04.13 15:04
 */
public class FlagsTest {

	@Test
	public void shouldReturnPersistence() {
		//given
		Flags flags = new Flags();

		//when
		flags.setPersistence(Enums.OwPersistence.ON);

		//then
		Assert.assertEquals(flags.getPersistence(), Enums.OwPersistence.ON);
	}

	@Test
	public void shouldReturnTemperatureScale() {
		//given
		Flags flags = new Flags();

		//when
		flags.setTemperatureScale(Enums.OwTemperatureScale.CELSIUS);

		//then
		Assert.assertEquals(flags.getTemperatureScale(), Enums.OwTemperatureScale.CELSIUS);
	}

	@Test
	public void shouldReturnProperValueOnComplexSettings() {
		//given
		Flags flags = new Flags();

		//when
		flags.setPersistence(Enums.OwPersistence.ON);
		flags.setBusReturn(Enums.OwBusReturn.ON);
		flags.setTemperatureScale(Enums.OwTemperatureScale.CELSIUS);
		flags.setDeviceDisplayFormat(Enums.OwDeviceDisplayFormat.F_DOT_I);

		//then
		Assert.assertEquals(flags.getPersistence(), Enums.OwPersistence.ON);
		Assert.assertEquals(flags.getBusReturn(), Enums.OwBusReturn.ON);
		Assert.assertEquals(flags.getDeviceDisplayFormat(), Enums.OwDeviceDisplayFormat.F_DOT_I);
		Assert.assertEquals(flags.getTemperatureScale(), Enums.OwTemperatureScale.CELSIUS);
	}


}
