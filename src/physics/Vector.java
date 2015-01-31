package physics;

/**
 * a class representing a 2D vector
 */
public class Vector {
	double x, y;
	
	public Vector() {
		this(0, 0);
	}
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static Vector fromPolar(double r, double theta) {
        return new Vector(Math.cos(theta) * r, Math.sin(theta) * r);
	}
	
	public static Vector fromPolarInverted(double r, double theta) {
        return new Vector(Math.cos(theta) * r, -Math.sin(theta) * r);
	}

    public Vector copy() {
        return new Vector(x, y);
    }
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setPolar(double r, double theta) {
		this.x = Math.cos(theta) * r;
		this.y = Math.sin(theta) * r;
	}
	
	public void setPolarInverted(double r, double theta) {
		this.x = Math.cos(theta) * r;
		this.y = Math.sin(theta) * -r;
	}
	
	public void add(double x, double y) {
		this.x += x;
		this.y += y;
	}
	
	public static double fastInvSqrt(double x) {
	    double xhalf = 0.5 * x;
	    long i = Double.doubleToLongBits(x);
	    i = 0x5fe6ec85e7de30daL - (i >> 1);
	    x = Double.longBitsToDouble(i);
	    x = x * (1.5 - xhalf * x * x);
	    return x;
	}
	
	public Vector plus(Vector other) {
		return new Vector(x + other.x, y + other.y);
	}
	
	public Vector minus(Vector other) {
		return new Vector(x - other.x, y - other.y);
	}
	
	public Vector times(double fac) {
		return new Vector(x * fac, y * fac);
	}
	
	public Vector over(double fac) {
		return new Vector(x / fac, y / fac);
	}
	
	public double dot(Vector other) {
		return x * other.x + y * other.y;
	}
	
	public Vector rotated(double theta) {
		double cos = Math.cos(theta);
		double sin = Math.sin(theta);
		return new Vector(x * cos - y * sin, x * sin + y * cos);
	}
	
	public double magnitude() {
		return Math.sqrt(x * x + y * y);
	}
	
	public Vector normalize() {
		double fac = fastInvSqrt(x * x + y * y);
		// Math.sqrt() may be faster if optimized
		return new Vector(x * fac, y * fac);
	}
}
