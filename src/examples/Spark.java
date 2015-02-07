package examples;

import opc.Animation;
import opc.OpcClient;
import opc.OpcDevice;
import opc.PixelStrip;

/**
 * Display a moving white pixel with trailing orange/red flames
 * This looks pretty good with a ring of pixels.
 */
public class Spark extends Animation {

	public static final int FC_SERVER_PORT = 7890;
//	public static final String FC_SERVER_HOST = "localhost";
	public static final String FC_SERVER_HOST = "raspberrypi.local";

	/** Colors of the chasing pixel. */
	int color[] = { makeColor(196, 196, 196), // White
			makeColor(128, 128, 0), // Yellow
			makeColor(96, 64, 0), // Yellow-orange
			makeColor(64, 32, 0), // orange
			makeColor(32, 0, 0), // red
			makeColor(16, 0, 0), // red
			makeColor(0, 0, 0), // black
	};

	int currentPixel;
	int numPixels;
	long changeTime;
	long timePerPixel = 200L;

	@Override
	public void reset(PixelStrip strip) {
		currentPixel = 0;
		numPixels = strip.getPixelCount();
		changeTime = millis();
	}

	@Override
	public boolean draw(PixelStrip strip) {
		if (millis() < changeTime) { return false; }
		for (int i = 0; i < color.length; i++) {
			strip.setPixelColor(pixNum(currentPixel, i), color[i]);
		}
		currentPixel = pixNum(currentPixel + 1, 0);
		changeTime = millis() + timePerPixel;
		return true;
	}

	/**
	 * Return the pixel number that is i steps behind number p.
	 */
	int pixNum(int p, int i) {
		return (p + numPixels - i) % numPixels;
	}
	
	/**
	 * @param n value between 0.0 and 1.0;
	 */
	public void setValue(double n) { 
		double v = 50 * n;
		v = Math.min(v, 50);
		v = Math.max(v, 1);
		timePerPixel = Math.round(4000.0 / v);
	}
	
	
	public static void main(String[] args) throws Exception {
		String FC_SERVER_HOST = System.getProperty("fadecandy.server", "raspberrypi.local");
		int FC_SERVER_PORT = Integer.parseInt(System.getProperty("fadecandy.port", "7890"));
		
		OpcClient server = new OpcClient(FC_SERVER_HOST, FC_SERVER_PORT);
		OpcDevice fadeCandy = server.addDevice();
		PixelStrip strip1 = fadeCandy.addPixelStrip(0, 64);
//		PixelStrip strip1 = fadeCandy.addPixelStrip(2, 16);
		System.out.println(server.getConfig());
		
		Animation a = new Spark();
		a.setValue(0.5);
		strip1.setAnimation(a);
		
		for (int i=0; i<10000; i++) {
			server.animate();
			Thread.sleep(5);
		}
		
		server.close();
	}
}
