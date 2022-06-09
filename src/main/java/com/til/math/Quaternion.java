package com.til.math;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.DefaultNew;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;

/***
 * 四元数
 * 用来表示旋转
 * @author til
 */
@BaseClass(sonClass = Quaternion.class)
@SonClass()
@DefaultNew(newExample = Quaternion.class)
public class Quaternion {
    @JsonField
    public final double i;
    @JsonField
    public final double j;
    @JsonField
    public final double k;
    @JsonField
    public final double r;

    public Quaternion() {
        this.i = 1;
        this.j = 0;
        this.k = 0;
        this.r = 0;
    }

    public Quaternion(double i, double j, double k, double r) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.r = r;
    }

    public Quaternion(V3 v3) {
        double pitch = v3.x;
        double yaw = v3.y;
        double roll = v3.z;
        double cy = Math.cos(yaw * 0.5f);
        double sy = Math.sin(yaw * 0.5f);
        double cp = Math.cos(pitch * 0.5f);
        double sp = Math.sin(pitch * 0.5f);
        double cr = Math.cos(roll * 0.5f);
        double sr = Math.sin(roll * 0.5f);
        i = cy * cp * cr + sy * sp * sr;
        j = cy * cp * sr - sy * sp * cr;
        k = sy * cp * sr + cy * sp * cr;
        r = sy * cp * cr - cy * sp * sr;
    }

    public Quaternion(com.mojang.math.Quaternion rotation) {
        this(rotation.i(), rotation.j(), rotation.k(), rotation.r());
    }

    public Quaternion(V3 v3, double d, boolean b) {
        if (b) {
            d *= Math.PI / 180F;
        }
        double f = Math.sin(d / 2.0F);
        this.i = v3.x * f;
        this.j = v3.y * f;
        this.k = v3.z * f;
        this.r = Math.cos(d / 2.0F);
    }

    public Quaternion multiply(Quaternion quaternion) {
        double i = this.i * quaternion.i - this.j * quaternion.j - this.k * quaternion.k - this.r * quaternion.r;
        double j = this.i * quaternion.j + this.j * quaternion.i + this.k * quaternion.r - this.r * quaternion.k;
        double k = this.i * quaternion.k + this.k * quaternion.i + this.r * quaternion.j - this.j * quaternion.r;
        double r = this.i * quaternion.r + this.r * quaternion.i + this.j * quaternion.k - this.k * quaternion.j;
        return new Quaternion(i, j, k, r);
    }

    public Quaternion multiply(double d) {
        return new Quaternion(i, j * d, k * d, r * d);
    }

    /***
     * 求逆
     */
    public Quaternion inverse() {
        return multiply(-1);
    }

    public boolean isEmpty() {
        return i == 1 && j == 0 && k == 0 && r == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Quaternion v3) {
            return Double.compare(v3.i, this.i) == 0
                    && Double.compare(v3.j, this.j) == 0
                    && Double.compare(v3.k, this.k) == 0
                    && Double.compare(v3.r, this.r) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + this.i + ", " + this.j + ", " + this.k + ", " + this.r + ")";
    }

    public static Quaternion lerp(Quaternion quaternion, Quaternion oldQuaternion, double time) {
        return quaternion.multiply(quaternion.multiply(oldQuaternion.inverse()).multiply(time));
    }

}
