package com.til.math;

import com.til.json_read_write.annotation.*;
import com.til.util.UseString;
import com.til.util.Util;

import java.awt.*;

/***
 * 颜色类
 * @author til
 */
@BaseClass(sonClass = Colour.class)
@SonClass()
@DefaultNew(newExample = Colour.class)
@UsePrefab(route = {UseString.PREFAB, "colour"})
public class Colour {

    @JsonField
    public final double r;
    @JsonField
    public final double g;
    @JsonField
    public final double b;
    @JsonField
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
        return new Color((float) Util.limit(r, 1, 0),
                (float) Util.limit(g, 1, 0),
                (float) Util.limit(b, 1, 0),
                (float) Util.limit(a, 1, 0));
    }

    public Colour multiply(double d) {
        return new Colour(r * d, g * d, b * d, a * d);
    }

    public Colour multiply(Colour c) {
        return new Colour(r * c.r, g * c.g, b * c.b, a * c.a);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Colour v3) {
            return Double.compare(v3.r, this.r) == 0
                    && Double.compare(v3.g, this.g) == 0
                    && Double.compare(v3.b, this.b) == 0
                    && Double.compare(v3.a, this.a) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + this.r + ", " + this.g + ", " + this.b + ", " + this.a + ")";
    }

}
