package examples;

import opc.Animation;
import opc.OpcClient;
import opc.OpcDevice;
import opc.PixelStrip;

/**
 * 
 */
public class TheaterLights extends Animation {
	
	public static final int FC_SERVER_PORT = 7890;
	public static final String FC_SERVER_HOST = "raspberrypi.local";

	int N = 2;
	int state;
	int color;
	long timePerCycle = 300L;
	
	/** Time for the next state change. */
	long changeTime;
	
	public TheaterLights(int c) {
		color = c;
	}

	@Override
	public void reset(PixelStrip strip) {
		state = 0;
		changeTime = millis();
	}
	public void setValue(double n) {
		// Override this in your Animation class
		

	}

	

	@Override
	public boolean draw(PixelStrip strip) {
		if (millis() < changeTime) { return false;}
			
		state = (state + 1) % (N * 2);
		for (int i=0; i<strip.getPixelCount(); i++)  {
			int j = (i+state) % (N * 2);
			strip.setPixelColor(i, j>=N ? color : BLACK);
		}
		
		changeTime = millis() + timePerCycle;
		return true;
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		OpcClient server = new OpcClient(FC_SERVER_HOST, FC_SERVER_PORT);
		OpcDevice fadeCandy = server.addDevice();
		PixelStrip strip1 = fadeCandy.addPixelStrip(0, 64);
		System.out.println(server.getConfig());
		
		strip1.setAnimation(new TheaterLights(0xFF0000));
		
		for (int i=0; i<1000; i++) {
			server.animate();
			Thread.sleep(100);
		}
		
		strip1.clear();
		server.show();
		server.close();
	}

}
