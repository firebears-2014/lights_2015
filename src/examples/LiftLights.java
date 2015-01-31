package examples;

import opc.Animation;
import opc.PixelStrip;

public class LiftLights extends Animation{
	
	public static final int FC_SERVER_PORT = 7890;
	public static final String FC_SERVER_HOST = "raspberrypi.local";

	int currentHeight = 0;

	int colors[] = new int[] {
			0xFFFFFF, 0x00FF00
	};
	
	public LiftLights() {
	}

	@Override
	public void reset(PixelStrip strip) {
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
			return limit - 1;
		}else if (limit < 0){
			return 0;
		}else{
			return a;
		}
	}
	
	public void setValue(double n) {
		currentHeight = (int)n;
	}

	@Override
	public boolean draw(PixelStrip strip) {
		
		int limit = strip.getPixelCount();
		int i;
		
		for(i = 0; i < limit; i++) {
			if(i > currentHeight-1 && i < currentHeight+1) {
				strip.setPixelColor(limit_a(i, limit), colors[1]);
			}else{
				strip.setPixelColor(limit_a(i, limit), colors[0]);
			}
		}
		return true;
	}

}
