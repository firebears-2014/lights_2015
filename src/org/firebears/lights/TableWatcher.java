package org.firebears.lights;

import opc.Animation;
import opc.PixelStrip;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

import java.util.*;

/**
 * Watches for changes to a given {@link NetworkTable} where the key starts with
 * a specific prefix.
 * <p>
 * Probably you will create one of these watchers for each {@link PixelStrip}.
 * Then you will add named animations to each watcher.  When the animation named
 * in the {@code NetworkTable} is changed, it triggers an animation change.
 * Setting the animation name to a blank causes animation to stop.
 * <p>
 * You can also change numbers in table entries to push number values into
 * the animations.  This lets you modify a running animation to correspond
 * to something hapening on the robot.
 */
public class TableWatcher implements ITableListener {

	public static final boolean VERBOSE 
		= "true".equals(System.getProperty("verbose", "false"));
	
	private final String prefix;
	private final PixelStrip strip;
	private final Map<String,Animation> animationMap;

	public TableWatcher(String prefix, PixelStrip strip) {
		this.prefix = prefix;
		this.strip = strip;
		this.animationMap = new HashMap<String,Animation>();
	}

	/**
	 * Handle every change in the {@link NetworkTable}.
	 * Ignore changes whose key doesn't match the prefix.
	 */
	@Override
	public void valueChanged(ITable source, String key, Object value, boolean isNew) {
		if (! key.startsWith(prefix)) { return; } 
		String relativeKey = key.substring(prefix.length());
		if (contains(relativeKey, NetworkTable.PATH_SEPARATOR)) { return; }
		if (relativeKey.equals(".value"))  {
			changeAnimationValue((Double)value);
		} else {
			changeAnimation((String)value);
		}
	}

	/**
	 * Change the number value of the current animation.
	 */
	private void changeAnimationValue(Double value) {
		if (strip.getAnimation() == null) {
			if (VERBOSE) {
				System.err.println("Error: no animation running on " + strip);
			}
			return;
		}
		if (VERBOSE) {
			System.out.println("changeAnimationValue on " + strip + " and "
					+ strip.getAnimation() + " to " + value);
		}
		strip.getAnimation().setValue((Double) value);
	}

	/**
	 * Change the animation currently running on this strip.
	 * If the animationName is blank, turn off animation on this strip.
	 */
	private void changeAnimation(String animationName) {
		if (animationName==null || animationName.trim().length()==0) {
			strip.setAnimation(null);
			if (VERBOSE) {
				System.out.println("changeAnimation on " + strip 
						+ " to nothing ");
			}
			return;
		}
		Animation newAnimation = animationMap.get(animationName.toString());
		if (newAnimation == null) {
			System.err.println("Error: unknown animation " + animationName + " for " + strip);
			return;
		}
		if (newAnimation == strip.getAnimation()) {
			return;
		} else {
			if (VERBOSE) {
				System.out.println("changeAnimation on " + strip + " to "
						+ animationName);
			}
			strip.setAnimation(newAnimation);
		}
	}

	/**
	 * @return whether the key contains the separator character.
	 */
	private static boolean contains(String key, char separator) {
		for (int i = 0; i < key.length(); ++i)  {
			if (key.charAt(i) == separator) { return true; }
		}
		return false;
	}
	
	/**
	 * Add a named animation to this watcher.
	 */
	public void addAnimation(String animationName, Animation animation)  {
		this.animationMap.put(animationName, animation);
	}
}
