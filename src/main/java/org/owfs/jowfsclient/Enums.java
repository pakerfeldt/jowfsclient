/*******************************************************************************
 * Copyright (c) 2009,2010 Patrik Akerfeldt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD license
 * which accompanies this distribution, see the COPYING file.
 *
 *******************************************************************************/
package org.owfs.jowfsclient;

/**
 * This class contains different types of {@code enum}s used by the OwfsConnection when communicating with owserver.
 * http://owfs.org/index.php?page=owserver-flag-word
 *
 * @author Patrik Akerfeldt
 */
public class Enums {

	/**
	 * Defines the different types of message type that owserver supports.
	 */
	public static enum OwMessageType {
		ERROR(0),
		NOP(1),
		READ(2),
		WRITE(3),
		DIR(4),
		SIZE(5),
		PRESENCE(6),
		DIRALL(7),
		GET(8),
		READ_ANY(99999);

		public final int intValue;

		OwMessageType(int value) {
			this.intValue = value;
		}

		public static OwMessageType getEnum(int intValue) {
			for (OwMessageType mt : OwMessageType.values()) {
				if (mt.intValue == intValue) {
					return mt;
				}
			}
			return null;
		}

	}

	/**
	 * Defines the different display formats that owserver supports.
	 * F = family, I = identity, C = CRC
	 * Explanation here: http://owfs.org/uploads/owpresent.html#sect27
	 */
	public static enum OwDeviceDisplayFormat {
		F_DOT_I(0x00000000),
		FI(0x01000000),
		F_DOT_I_DOT_C(0x02000000),
		F_DOT_IC(0x03000000),
		FI_DOT_C(0x04000000),
		FIC(0x05000000);

		public final int intValue;

		private OwDeviceDisplayFormat(int value) {
			this.intValue = value;
		}

		public static int getBitmask() {
			return 0x0F000000;
		}

		public static OwDeviceDisplayFormat getEnum(int intValue) {
			for (OwDeviceDisplayFormat p : OwDeviceDisplayFormat.values()) {
				if (p.intValue == intValue) {
					return p;
				}
			}
			return null;
		}
	}

	/**
	 * Used to define which temperature scale owserver should return temperatures in.
	 */
	public static enum OwTemperatureScale {
		CELSIUS(0x00000000),
		FAHRENHEIT(0x00010000),
		KELVIN(0x00020000),
		RANKINE(0x00030000);

		public final int intValue;

		private OwTemperatureScale(int value) {
			this.intValue = value;
		}

		public static int getBitmask() {
			return 0x000F0000;
		}

		public static OwTemperatureScale getEnum(int intValue) {
			for (OwTemperatureScale p : OwTemperatureScale.values()) {
				if (p.intValue == intValue) {
					return p;
				}
			}
			return null;
		}
	}

	/**
	 * Whether or not to request/grant persistent connections.
	 */
	public static enum OwPersistence {
		OFF(0x00000000),
		ON(0x00000004);

		public final int intValue;

		private OwPersistence(int value) {
			this.intValue = value;
		}

		public static int getBitmask() {
			return 0x00000004;
		}

		public static OwPersistence getEnum(int intValue) {
			for (OwPersistence p : OwPersistence.values()) {
				if (p.intValue == intValue) {
					return p;
				}
			}
			return null;
		}
	}

	/**
	 * Whether or not to use aliases for known slaves (human readable names)
	 */
	public static enum OwAlias {
		OFF(0x00000000),
		ON(0x00000008);

		public final int intValue;

		private OwAlias(int value) {
			this.intValue = value;
		}

		public static int getBitmask() {
			return 0x00000008;
		}

		public static OwAlias getEnum(int intValue) {
			for (OwAlias p : OwAlias.values()) {
				if (p.intValue == intValue) {
					return p;
				}
			}
			return null;
		}

	}

	/**
	 * Whether or not to include special directories (settings, statistics, uncached,...)
	 */
	public static enum OwBusReturn {
		OFF(0x00000000),
		ON(0x00000002);

		public final int intValue;

		private OwBusReturn(int value) {
			this.intValue = value;
		}

		public static int getBitmask() {
			return 0x00000002;
		}

		public static OwBusReturn getEnum(int intValue) {
			for (OwBusReturn p : OwBusReturn.values()) {
				if (p.intValue == intValue) {
					return p;
				}
			}
			return null;
		}
	}
}
