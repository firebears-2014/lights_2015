package org.firebears.lights;

import opc.OpcClient;
import opc.OpcDevice;
import opc.PixelStrip;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import examples.Binary;
import examples.Caterpillar;
import examples.Fire;
import examples.LiftLights;
import examples.MovingPixel;
import examples.Pulsing;
import examples.Spark;
//import examples.bulb;
import examples.crazy;

/**
 * This program allows the robot to control lights connected
 * to the Fadecandy server.  The robot will make changes into
 * the "lights" network table.  This program will detect those
 * changes and cause the animations to change on the pixel
 * strips.  
 * <p>
 * This program can run on any computer in the robot's 
 * subnet.  It may run on the same Raspberry Pi where the
 * Fadecandy server is running.
 */
public class LightsMain {

	// Constants for pixel strips 
	public static final String STRIP_LIFT1 = "lift1";
	public static final String STRIP_LIFT2 = "lift2";
	public static final String STRIP_BOX = "box";
	public static final String STRIP_UNDERGLOW = "underglow";
	public static final String STRIP_CELEBRATE = "celebrate";
	
	// Constants for  animations
	public static final String ANIM_PULSE = "PULSING_GREEN_ANIM";
	public static final String ANIM_MOVE = "MOVING_BLUE_ANIM";
	public static final String ANIM_FIRE = "FIRE_ANIM";
	public static final String ANIM_LIFT = "LIFT";
	public static final String ANIM_CRAZY = "CRAZY";
	public static final String ANIM_BINARY = "BIN_ANIM";
	public static final String ANIM_BULB = "BULB";
	public static final String ANIM_CATERPILLAR = "ANIM_CATERPILLAR";
	public static final String ANIM_SPARK = "SPARK";
	
	//Color Schemes
	public static final int CS_RED = 0;
	public static final int CS_BLUE = 1;
	public static final int CS_YELLOW = 2;
	public static final int CS_RED_YELLOW = 3;
	public static final int CS_RED_WHITE = 4;
	public static final int CS_WHITE = 5;

	/** Host name or IP address of the Network Table server. */
	public static final String NT_SERVER_HOST 
//		= System.getProperty("network_table.server", "roborio-2846.local");
		= System.getProperty("network_table.server", "roborio-2846.local");
	
	/** Host name or IP address of the Fadecandy server. */
	public static final String FC_SERVER_HOST 
		= System.getProperty("fadecandy.server", "raspberrypi.local");
	
	/** Port number of the Fadecandy server. */
	public static final int FC_SERVER_PORT 
		= Integer.parseInt(System.getProperty("fadecandy.port", "7890"));
	
	/** Whether to display extra information about internal processes. */
	public static final boolean VERBOSE 
		= "true".equals(System.getProperty("verbose", "false"));
	
	private static void init_pix_strip(
		OpcDevice fadeCandy, NetworkTable table,
		int pin, int len, String name)
	{
		PixelStrip strip = fadeCandy.addPixelStrip(pin, len, name); 
		TableWatcher watcher = new TableWatcher(name, strip);
		
		watcher.addAnimation(ANIM_PULSE, new Pulsing());
		watcher.addAnimation(ANIM_MOVE, new MovingPixel(0x0000FF));
		watcher.addAnimation(ANIM_LIFT, new LiftLights());
		watcher.addAnimation(ANIM_FIRE, new Fire());
		watcher.addAnimation(ANIM_CRAZY, new crazy());
		watcher.addAnimation(ANIM_BINARY, new Binary());
		watcher.addAnimation(ANIM_CATERPILLAR, new Caterpillar());
		watcher.addAnimation(ANIM_SPARK, new Spark());

//		watcher.addAnimation(BULB, new bulb());

		table.addTableListener(watcher, true);
	}
	
	public static void main(String[] args) {
		
		// Initialize the NetworkTables
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress(NT_SERVER_HOST);
		NetworkTable table = NetworkTable.getTable("lights");
		if (VERBOSE) System.out.println("# network_table.server=" + NT_SERVER_HOST);
		
		// Initialize Fadecandy server
		OpcClient server = new OpcClient(FC_SERVER_HOST, FC_SERVER_PORT);
		OpcDevice fadeCandy = server.addDevice();
		if (VERBOSE) System.out.println("# fadecandy.server=" + FC_SERVER_HOST);
		if (VERBOSE) System.out.println("# fadecandy.port=" + FC_SERVER_PORT);
		
		// Initialize pixel strips

		init_pix_strip(fadeCandy, table, 0, 64, STRIP_LIFT1);

/*
		init_pix_strip(fadeCandy, table, 0, 60, STRIP_LIFT1);
		init_pix_strip(fadeCandy, table, 1, 60, STRIP_LIFT2);
		//TODO: Find actual pixel count
		init_pix_strip(fadeCandy, table, 2, 64, STRIP_BOX);
		//TODO: Find actual pixel count
		init_pix_strip(fadeCandy, table, 3, 64, STRIP_UNDERGLOW);
		//TODO: Find actual pixel count
		init_pix_strip(fadeCandy, table, 4, 64, STRIP_CELEBRATE);
*/
		
//		init_pix_strip(fadeCandy, table, 0, 16, STRIP_LIFT1);
//		init_pix_strip(fadeCandy, table, 0, 16, STRIP_LIFT2);
//		init_pix_strip(fadeCandy, table, 0, 16, STRIP_BOX);
//		init_pix_strip(fadeCandy, table, 0, 16, STRIP_UNDERGLOW);
//		init_pix_strip(fadeCandy, table, 1, 8, "nothing");
//		init_pix_strip(fadeCandy, table, 2, 16, STRIP_CELEBRATE);
		
		// Wait forever while Client Connection Reader thread runs
		System.out.println(server.getConfig());
		while (true) {
			server.animate();
			try {
				Thread.sleep(25);
			} catch (InterruptedException e ) {
 				if (VERBOSE) { System.err.println(e.getMessage()); }
			}
		}
	}

}
