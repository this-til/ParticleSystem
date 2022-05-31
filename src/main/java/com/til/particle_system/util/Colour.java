package com.til.particle_system.util;

import java.awt.*;

/***
 *
 * @author til
 */
public class Colour {

    public final double r;
    public final double g;
    public final double b;
    public final double a;

    public Colour() {
        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.a = 1;
    }

    public Colour(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color as() {
        return new Color((float) r, (float) g, (float) b, (float) a);
    }

    public Colour multiply(double d) {
        return new Colour(r * d, g * d, b * d, a * d);
    }

    public Colour multiply(Colour c) {
        return new Colour(r * c.r, g * c.g, b * c.b, a * c.a);
    }

}
