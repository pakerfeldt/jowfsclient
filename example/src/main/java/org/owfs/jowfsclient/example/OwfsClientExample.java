package org.owfs.jowfsclient.example;

import java.io.IOException;
import java.util.List;

import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.Enums.OwBusReturn;
import org.owfs.jowfsclient.Enums.OwDeviceDisplayFormat;
import org.owfs.jowfsclient.Enums.OwPersistence;
import org.owfs.jowfsclient.Enums.OwTemperatureScale;

public class OwfsClientExample {

	public static void main(String[] args) {

		/* Construct the client */
		OwfsClient client = OwfsClientFactory.newOwfsClient("192.168.1.10",
				3001, false);

		/* Configure client */
		client.setDeviceDisplayFormat(OwDeviceDisplayFormat.OWNET_DDF_F_DOT_I);
		client.setBusReturn(OwBusReturn.OWNET_BUSRETURN_ON);
		client.setPersistence(OwPersistence.OWNET_PERSISTENCE_ON);
		client.setTemperatureScale(OwTemperatureScale.OWNET_TS_CELSIUS);

		try {
			List<String> directories = client.listDirectoryAll("/");
			for (String dir : directories) {
				System.out.println(dir);
				List<String> subdirectories = client.listDirectoryAll(dir);
				for (String subdir : subdirectories) {
					System.out.println("\t" + subdir);
				}
			}
			client.disconnect();

		} catch (OwfsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
