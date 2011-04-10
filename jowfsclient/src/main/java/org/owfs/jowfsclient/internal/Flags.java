/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient.internal;

import org.owfs.jowfsclient.Enums.OwAlias;
import org.owfs.jowfsclient.Enums.OwBusReturn;
import org.owfs.jowfsclient.Enums.OwDeviceDisplayFormat;
import org.owfs.jowfsclient.Enums.OwPersistence;
import org.owfs.jowfsclient.Enums.OwTemperatureScale;

/**
 * This class encapsulates the flag header word and has members to get and set
 * the supporting fields.
 * 
 * @author Patrik Akerfeldt
 * 
 */
public class Flags {

	private int flags;

	/**
	 * Constructs a new Flags object.
	 * 
	 * @param value
	 *            the initial flag value.
	 */
	public Flags(int value) {
		this.flags = value;
	}

	/**
	 * Returns the {@code int} representation of this instance.
	 * 
	 * @return the {@code int} representation of this instance.
	 */
	public int intValue() {
		return flags;
	}

	/**
	 * Sets the device display format.
	 * 
	 * @param deviceDisplay
	 *            the new display format.
	 */
	public void setDeviceDisplayFormat(OwDeviceDisplayFormat deviceDisplay) {
		/* Reset device display bits */
		flags &= ~OwDeviceDisplayFormat.getBitmask();
		/* Set new device display */
		flags |= deviceDisplay.intValue;
	}

	/**
	 * Sets the temperature scale.
	 * 
	 * @param tempScale
	 *            the new temperature scale.
	 */
	public void setTemperatureScale(OwTemperatureScale tempScale) {
		/* Reset temperature scale bits */
		flags &= ~OwTemperatureScale.getBitmask();
		/* Set new temperature scale */;
		flags |= tempScale.intValue;
	}

	/**
	 * Set the persistence bits.
	 * 
	 * @param persistence
	 *            the new persistence value
	 */
	public void setPersistence(OwPersistence persistence) {
		/* Reset persistence */
		flags &= ~OwPersistence.getBitmask();
		/* Set persistence bits */;
		flags |= persistence.intValue;
	}

	/**
	 * Returns the value of the persistence represented as a
	 * {@link OwPersistence}.
	 * 
	 * @return the value of the persistence represented as a
	 *         {@link OwPersistence}.
	 */
	public OwPersistence getPersistence() {
		return OwPersistence.getEnum(OwPersistence.getBitmask() | flags);
	}

	/**
	 * Sets the alias bits.
	 * 
	 * @param alias
	 *            the new alias value.
	 */
	public void setAlias(OwAlias alias) {
		/* Reset alias */
		flags &= ~OwAlias.getBitmask();
		/* Set persistence bits */;
		flags |= alias.intValue;
	}

	/**
	 * Sets the bus return bits.
	 * 
	 * @param busReturn
	 *            the new busreturn value.
	 */
	public void setBusReturn(OwBusReturn busReturn) {
		/* Reset bus return */
		flags &= ~OwBusReturn.getBitmask();
		/* Set persistence bits */;
		flags |= busReturn.intValue;
	}
}
