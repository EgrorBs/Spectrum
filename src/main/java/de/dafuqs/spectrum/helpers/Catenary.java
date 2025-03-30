package de.dafuqs.spectrum.helpers;

import net.minecraft.util.math.*;

//I DO NOT CARE HOW MUCH YOU PAY I AM NOT CALCULATING A
// Also but btw this is my son, he is loved
public class Catenary {
	private final Vec2f slack, start;
	private final float span, yDiff, a;
	
	public Catenary(Vec2f start, Vec2f end, float a) {
		span = end.x - start.x;
		yDiff = end.y - start.y;
		
		var sX = start.x + span / 2 + a * Support.asinh(
				(yDiff * Math.exp(span / (2 * a)))
				/ (a * (1 - Math.exp(span / a)))
		);
		
		var sY = start.y - a * (Math.cosh((sX - start.x) / a) - 1);
		
		slack = new Vec2f((float) sX, (float) sY);
		this.start = start;
		this.a = a;
	}
	
	public double getAt(double x) {
		return start.y + a * (Math.cosh((x - slack.x) / a) - Math.cosh((start.x - slack.x) / a));
	}
}