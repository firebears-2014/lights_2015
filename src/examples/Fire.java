package examples;

import opc.Animation;
import opc.OpcClient;
import opc.OpcDevice;
import opc.PixelStrip;

/**
 * Example animation that makes a pixel move across the strip.
 */
public class Fire extends Animation {
	
	public static final int FC_SERVER_PORT = 7890;
	public static final String FC_SERVER_HOST = "raspberrypi.local";
	
	int timepass = 0;

	int currentPixel;
	long timePerPixel = 200L;
	//Red, Yellow, Orange, Purple, White, 
	int colors[] = new int[] {
			0xFF0000, 0xFFAA00, 0xFF5500, 0xFF1100, 0xFF1111, 0xFFAAAA, 0x000000
	};
	
	//every 4
	int temp_colors[] = new int[5]; //every other line, alternate blended
	
	/** Time for the next state change. */
	long changeTime;
	
	public Fire() {
	}

	@Override
	public void reset(PixelStrip strip) {
		for(int i = 0; i < 4; i++) {
			temp_colors[i] = (int)(Math.random()*7);
		}
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
	
	public int limit_a(int a, int limit) {
		if(a >= limit) {
			return a - limit;
		}else{
			return a;
		}
	}

	@Override
	public boolean draw(PixelStrip strip) {
//		long currentTime = millis() % timeCycle;
		int a;
		for (int p = 0; p < (strip.getPixelCount() / 4); p++) {
			a = (p*4) + timepass;
			strip.setPixelColor(limit_a(a, strip.getPixelCount()),
				mixColor(colors[temp_colors[p%4]], colors[temp_colors[(p%4)+1]], 1.f)
			);
			strip.setPixelColor(limit_a(a+1, strip.getPixelCount()), 
					mixColor(colors[temp_colors[p%4]], colors[temp_colors[(p%4)+1]], .75f)
				);
			strip.setPixelColor(limit_a(a+2, strip.getPixelCount()), 
					mixColor(colors[temp_colors[p%4]], colors[temp_colors[(p%4)+1]], .5f)
				);
			strip.setPixelColor(limit_a(a+3, strip.getPixelCount()), 
				mixColor(colors[temp_colors[p%4]], colors[temp_colors[(p%4)+1]], .25f)
			);
		}
		timepass++;
		if(timepass > strip.getPixelCount()) {
			timepass = 0;
		}
		if((int)(Math.random()*2) == 1) {
			temp_colors[(int)(Math.random() * 5)] = (int)(Math.random()*7); 
		}
		return true;
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
		strip1.setAnimation(new Fire());
		strip2.setAnimation(new Fire());
		strip3.setAnimation(new Fire());

		System.out.println("starting anim...");
		
		while (true) {
			server.animate();
		}
		
//		server.clear();
//		server.show();
//		server.close();
	}

}
