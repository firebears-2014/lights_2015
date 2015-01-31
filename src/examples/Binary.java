package examples;

import opc.Animation;
import opc.OpcClient;
import opc.OpcDevice;
import opc.PixelStrip;


/**
 * Example animation that makes a pixel move across the strip.
 */
public class Binary extends Animation {
	
	public static final int FC_SERVER_PORT = 7890;
	public static final String FC_SERVER_HOST = "raspberrypi.local";
	
	int timepass = 0;
	long prevtime = 0;
	
	int binaryCount = 0;

	int currentPixel;
	long timePerPixel = 200L;
	//Red, Yellow, Orange, Purple, White, 
	int colors[] = new int[] {
			0x000000, 0xFFFFFF
	};
	
	/** Time for the next state change. */
	long changeTime;
	
	public Binary() {
	}

	@Override
	public void reset(PixelStrip strip) {

		currentPixel = 0;
		changeTime = millis();
	}
	
	public int mixColor(int c1, int c2, float percentOfOne) {
		float percentOfTwo = 1.f - percentOfOne;
		return makeColor(
			(int)((getRed(c1) * percentOfOne) + (getRed(c2) * percentOfTwo)) / 2,
			(int)((getGreen(c1) * percentOfOne) + (getGreen(c2) * percentOfTwo)) / 2,
			(int)((getBlue(c1) * percentOfOne) + (getBlue(c2) * percentOfTwo)) / 2);
	}
	
	public int getBit(int p, int s) {
		int rtn = binaryCount;
		rtn = rtn << 1;
		rtn = rtn >> 1;
		rtn = rtn >> (p-1);
		rtn = rtn << (s-p);
		if(rtn == 1) {
			return 0xFFFFFF;
		}else{
			return 0x000000;
		}
	}

	@Override
	public boolean draw(PixelStrip strip) {
		int a, ct1, ct2;

		if(prevtime + 100 < millis()) {
			prevtime = millis();
			binaryCount++;
			for (int p = 0; p < strip.getPixelCount(); p++) {
				strip.setPixelColor(p, getBit(p, 31));
			}
			return true;	
		}else{
			return false;
		}
	}
	
	public static void main(String[] args) throws Exception {
		OpcClient server = new OpcClient(FC_SERVER_HOST, FC_SERVER_PORT);
		OpcDevice fadeCandy = server.addDevice();
		
		// Configure for three separate pixel strips
		PixelStrip strip1 = fadeCandy.addPixelStrip(0, 64);  // 8 x 8 grid on pin 0
		PixelStrip strip2 = fadeCandy.addPixelStrip(1, 8);   // 8 pixel strip on pin 1
		PixelStrip strip3 = fadeCandy.addPixelStrip(2, 16);  // 16 pixel ring on pin 2

		// Since the pixels are not uniform strips of 64, customize 
		// the server config JSON file with the following:
		System.out.println(server.getConfig());
		
		// Set each strip to show a different animation
		strip1.setAnimation(new Binary());
		strip2.setAnimation(new Binary());
		strip3.setAnimation(new Binary());

		System.out.println("starting anim...");
		
		while (true) {
			server.animate();
		}
		
//		server.clear();
//		server.show();
//		server.close();
	}

}
