package opc;

public abstract class Foreground extends Animation {

	@Override
	public abstract void reset(PixelStrip strip);

	@Override
	public abstract boolean draw(PixelStrip strip);

}
