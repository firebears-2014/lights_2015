package examples;

import opc.Animation;
import opc.PixelStrip;

public class LiftLights extends Animation{
	
	public static final int FC_SERVER_PORT = 7890;
	public static final String FC_SERVER_HOST = "raspberrypi.local";

	int currentHeight = 0;
	
	Animation background;

	int colors[] = new int[] {
			0x000000, 0x00FF00
	};
	
	PixelStrip g_strip;
	
	public LiftLights() {
	}

	@Override
	public void reset(PixelStrip strip) {
		g_strip = strip;
		setBg(new Fire());
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
	
	public void setBg(Animation anim) {
		background = anim;
		background.reset(g_strip);
		background.g_fade = 255;
	}
	
	public void setDimness(int dim) {
		background.g_fade = dim;
	}

	@Override
	public boolean draw(PixelStrip strip) {
		
		int limit = strip.getPixelCount();
		int i;
		
		background.draw(strip);
		
		for(i = 0; i < limit; i++) {
			if(i > currentHeight-3 && i < currentHeight+3) {
				strip.setPixelColor(limit_a(i, limit), colors[1]);
			}else{
//				strip.setPixelColor(limit_a(i, limit), colors[0]);
			}
		}
		return true;
	}

}
