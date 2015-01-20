package org.firebears.main;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.networktables.NetworkTableListenerAdapter;

public class LightsMain {

	public static void main(String[] args) {
		
		// Initialize the NetworkTables
		String SERVER_HOST = System.getProperty("nt.server.host",
				"roborio-2846.local");
		System.out.println("nt.server.host=" + SERVER_HOST);
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress(SERVER_HOST);
		NetworkTable table = NetworkTable.getTable("lights");
		
		// TODO More Stuff

	}

}
