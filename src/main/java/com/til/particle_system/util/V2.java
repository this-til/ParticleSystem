package com.til.particle_system.util;

/***
 * @author til
 */
public class V2 {
    public final double x;
    public final double y;

    public V2() {
        x = 0;
        y = 0;
    }

    public V2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getProportion(double d) {
        return getProportionStatic(x, y, d);
    }

    public static double getProportionStatic(double a, double b, double d) {
        double max = Math.max(a, b);
        double min = Math.min(a, b);
        if (d < min) {
            return 0;
        }
        if (d > max) {
            return 1;
        }
        return (d - min) / max;
    }

}
