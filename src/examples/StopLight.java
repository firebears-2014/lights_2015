package examples;

import opc.Animation;
import opc.OpcClient;
import opc.OpcDevice;
import opc.PixelStrip;

public class StopLight extends Animation {

	public static final int FC_SERVER_PORT = 7890;
	public static final String FC_SERVER_HOST = "localhost";
	
	int red = makeColor(64, 0, 0);
	int yellow = makeColor(32, 32, 0);
	int green = makeColor(0, 64, 0);
	int black = makeColor(0, 0, 0);
	
	/** Milliseconds for the yellow cycle. */
	long cycleTime = 800;

	/** Current state of the stoplight, either red, yellow, or green. */
	int state = green;

	/** Time for the next state change. */
	long changeTime;

	@Override
	public void reset(PixelStrip strip) {
		changeTime = millis();
		strip.clear();
	}

	/**
	 * Wait until after "changeTime". After changeTime, switch to the
	 * light, and then recalculate the changeTime.
	 * 
	 * @return whether a redraw is needed.
	 */
	@Override
	public boolean draw(PixelStrip strip) {
		long timeNow = millis();
		if (timeNow < changeTime) {
			return false;
		} else {
			strip.clear();
			if (state == red) {
				state = green;
				strip.setPixelColor(0, green);
				strip.setPixelColor(1, green);
				changeTime = timeNow + 3 * cycleTime;
			} else if (state == green) {
				state = yellow;
				strip.setPixelColor(3, yellow);
				strip.setPixelColor(4, yellow);
				changeTime = timeNow + 1 * cycleTime;
			} else {
				state = red;
				strip.setPixelColor(6, red);
				strip.setPixelColor(7, red);
				changeTime = timeNow + 3 * cycleTime;
			}
			return true;
		}
	}

	
	
	
	public static void main(String[] args) throws Exception {
		OpcClient server = new OpcClient(FC_SERVER_HOST, FC_SERVER_PORT);
		OpcDevice fadeCandy = server.addDevice();
//		PixelStrip strip1 = fadeCandy.addPixelStrip(0, 64);
		PixelStrip strip2 = fadeCandy.addPixelStrip(1, 8);
		System.out.println(server.getConfig());
		
//		strip1.setAnimation(new MovingPixel(0x0000FF));
		strip2.setAnimation(new StopLight());
		
		for (int i=0; i<1000; i++) {
			server.animate();
			Thread.sleep(100);
		}
		
		server.close();
	}

}
