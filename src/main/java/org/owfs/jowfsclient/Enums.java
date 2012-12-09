/*******************************************************************************
* Copyright (c) 2009,2010 Patrik Akerfeldt
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the BSD license
* which accompanies this distribution, see the COPYING file.
*
*******************************************************************************/
package org.owfs.jowfsclient;

/**
 * This class contains different types of {@code enum}s used by the OwfsClient when
 * communicating with owserver.
 * 
 * @author Patrik Akerfeldt
 * 
 */
public class Enums {

	/**
	 * Defines the different types of message type that owserver supports.
	 * 
	 */
	public static enum OwMessageType {
		OWNET_MSG_ERROR (0),
		OWNET_MSG_NOP (1),
		OWNET_MSG_READ (2),
		OWNET_MSG_WRITE (3),
		OWNET_MSG_DIR (4),
		OWNET_MSG_SIZE (5),
		OWNET_MSG_PRESENCE (6),
		OWNET_MSG_DIRALL (7),
		OWNET_MSG_GET (8),
		OWNET_MSG_READ_ANY (99999);
		
		public final int intValue;

		OwMessageType(int value) {
			this.intValue = value;
		}

		public static OwMessageType getEnum(int intValue) {
			for(OwMessageType mt : OwMessageType.values()) {
				if(mt.intValue == intValue) {
					return mt;
				}
			}
			return null;
		}
	
	}
	
	/**
	 * Defines the different display formats that owserver supports.
	 * F = family, I = identity, C = CRC
	 *
	 */
	public static enum OwDeviceDisplayFormat {
		OWNET_DDF_F_DOT_I 		(0x00000000),
		OWNET_DDF_FI 			(0x01000000),
		OWNET_DDF_F_DOT_I_DOT_C (0x02000000),
		OWNET_DDF_F_DOT_IC 		(0x03000000),
		OWNET_DDF_FI_DOT_C 		(0x04000000),
		OWNET_DDF_FIC 			(0x05000000);
		
		public final int intValue;

		private OwDeviceDisplayFormat(int value) {
			this.intValue = value;
		}
		
		public static int getBitmask() {
			return 0x0F000000;
		}
	}
	
	/**
	 * Used to define which temperature scale owserver should return temperatures in.
	 */
	public static enum OwTemperatureScale {
		OWNET_TS_CELSIUS 		(0x00000000),
		OWNET_TS_FAHRENHEIT		(0x00010000),
		OWNET_TS_KELVIN			(0x00020000),
		OWNET_TS_RANKINE		(0x00030000);
		
		public final int intValue;
		
		private OwTemperatureScale(int value) {
			this.intValue = value;
		}
		
		public static int getBitmask() {
			return 0x000F0000;
		}
	}
	
	/**
	 * Whether or not to request/grant persistent connections.
	 */
	public static enum OwPersistence {
		OWNET_PERSISTENCE_OFF	(0x00000000),
		OWNET_PERSISTENCE_ON	(0x00000004);
		
		public final int intValue;
		
		private OwPersistence(int value) {
			this.intValue = value;
		}
		
		public static int getBitmask() {
			return 0x00000004;
		}
		
		public static OwPersistence getEnum(int intValue) {
			for(OwPersistence p : OwPersistence.values()) {
				if(p.intValue == intValue) {
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
		OWNET_ALIAS_OFF			(0x00000000),
		OWNET_ALIAS_ON			(0x00000008);
		
		public final int intValue;
		
		private OwAlias(int value) {
			this.intValue = value;
		}
		
		public static int getBitmask() {
			return 0x00000008;
		}		
	}
	
	/**
	 * Whether or not to include special directories (settings, statistics,
	 * uncached,...)
	 */
	public static enum OwBusReturn {
		OWNET_BUSRETURN_OFF		(0x00000000),
		OWNET_BUSRETURN_ON		(0x00000002);

		public final int intValue;
		
		private OwBusReturn(int value) {
			this.intValue = value;
		}
		
		public static int getBitmask() {
			return 0x00000002;
		}	
	}
}
