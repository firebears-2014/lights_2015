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

	public TheaterLights(int c) {
		color[0] = c;
	}

	public int color[] = {
			//row 0
			makeColor(255,100 ,0),
	};
	int N = 6;
	int rotat = 2;
	int state;
	long timePerCycle = 100L;

	
	
	
	/** Time for the next state change. */
	long changeTime;

	@Override
	public void reset(PixelStrip strip) {
		state = 0;
		changeTime = millis();
	}
	public void setValue(double n) {
		// Override this in your Animation class
		rotat = (int)n;
		
		

	}

	

	@Override
	public boolean draw(PixelStrip strip) {
		if (millis() < changeTime) { return false;}
			
		state = (state - 1) % (N * rotat);
		for (int i=0; i<strip.getPixelCount(); i++)  {
			int j = (i+state) % (N * rotat);
			int c1 = color[0];
			strip.setPixelColor(i, j>=N ? color[0] : BLACK);
		}
		
		changeTime = millis() + timePerCycle;
		return true;
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		OpcClient server = new OpcClient(FC_SERVER_HOST, FC_SERVER_PORT);
		OpcDevice fadeCandy = server.addDevice();
		PixelStrip strip1 = fadeCandy.addPixelStrip(0, 64);
		System.out.println(server.getConfig());
		
		TheaterLights a = new TheaterLights(0x0000DD);
		a.setValue(600);
		strip1.setAnimation(a);
		
		for (int i=0; i<1000; i++) {
			server.animate();
			Thread.sleep(100);
		}
		
		strip1.clear();
		server.show();
		server.close();
	}

}
