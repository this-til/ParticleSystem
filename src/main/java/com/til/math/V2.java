package com.til.math;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.DefaultNew;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;

/***
 * 二维向量
 * @author til
 */
@BaseClass(sonClass = V2.class)
@SonClass()
@DefaultNew(newExample = V2.class)
public class V2 {
    @JsonField
    public final double x;
    @JsonField
    public final double y;

    public V2() {
        x = 0;
        y = 0;
    }

    public V2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public V2(V3 v3) {
        this(v3.x, v3.y);
    }


    public V2 multiply(double d) {
        return new V2(x * d, y * d);
    }

    public V2 multiply(V2 v3) {
        return new V2(x * v3.x, y * v3.y);
    }

    public V2 add(double d) {
        return new V2(x + d, y + d);
    }

    public V2 add(V2 v3) {
        return new V2(x + v3.x, y + v3.y);
    }

    public V2 reduce(double d) {
        return new V2(x - d, y - d);
    }

    /***
     * 返回目标指向自身的向量
     */
    public V2 reduce(V2 v3) {
        return new V2(x - v3.x, y - v3.y);
    }

    /***
     * 模的平方
     */
    public double sqrMagnitude() {
        return x * x + y * y;
    }

    public boolean isEmpty() {
        return x == 0 && y == 0;
    }

    /***
     * 模
     */
    public double magnitude() {
        return Math.sqrt(sqrMagnitude());
    }

    /***
     * 获取d在改向量最小值到最大值之间的比例
     */
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof V3) {
            V3 v3 = (V3) obj;
            return Double.compare(v3.x, this.x) == 0
                    && Double.compare(v3.y, this.y) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

}
