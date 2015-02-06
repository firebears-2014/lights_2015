package opc;

import examples.Fire;

public abstract class Foreground extends Animation {

	protected Animation background;
	protected PixelStrip g_strip;
	
	@Override
	public abstract void reset(PixelStrip strip);

	@Override
	public abstract boolean draw(PixelStrip strip);
	
	public void setBg(Animation anim) {
		background = anim;
		background.reset(g_strip);
		background.g_fade = 255;
	}
	
	public void setDimness(int dim) {
		background.g_fade = dim;
	}

	public void prepare(PixelStrip strip) {
		g_strip = strip;
		setBg(new Fire());
	}
	
	public void draw_bg() {
		background.draw(g_strip);
	}
	
}
