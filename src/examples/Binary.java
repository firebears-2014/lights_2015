package examples;

import opc.Animation;
import opc.OpcClient;
import opc.OpcDevice;
import opc.PixelStrip;

public class Binary extends Animation {

	long timePerPixel = 50L;
	int currentNumber;
	long changeTime;
	
	@Override
	public void reset(PixelStrip strip) {
		strip.clear();
		currentNumber = 0;
		changeTime = millis();
	}

	@Override
	public boolean draw(PixelStrip strip) {
		if (millis() < changeTime) { return false; }
		int n = currentNumber++;
		for (int p = 0; p < strip.getPixelCount(); p++) {
			int color = (n%2==1) ? WHITE : BLACK;
			strip.setPixelColor(p, color);
			n = n / 2;
		}
		changeTime = millis() + timePerPixel;
		return true;
	}


	
	public static void main(String[] args) throws Exception {
		
		final String FC_SERVER_HOST 
			= System.getProperty("fadecandy.server", "raspberrypi.local");
		final int FC_SERVER_PORT 
			= Integer.parseInt(System.getProperty("fadecandy.port", "7890"));

		OpcClient server = new OpcClient(FC_SERVER_HOST, FC_SERVER_PORT);
		OpcDevice fadeCandy = server.addDevice();
		PixelStrip strip1 = fadeCandy.addPixelStrip(0, 72);
		System.out.println(server.getConfig());

		Binary a = new Binary();
		strip1.setAnimation(a);

		while ( a.currentNumber < 1024) {
			server.animate();
			Thread.sleep(100);
		}

		server.clear();
		server.show();
		server.close();
	}
	
}
