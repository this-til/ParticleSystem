package com.til.math;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.DefaultNew;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import net.minecraft.world.phys.Vec3;

/***
 * 一个三维向量
 * @author til
 */
@BaseClass(sonClass = V3.class)
@SonClass()
@DefaultNew(newExample = V3.class)
public class V3 {
    /***
     * pitch
     */
    @JsonField
    public final double x;
    /**
     * yaw
     */
    @JsonField
    public final double y;
    /***
     * roll
     */
    @JsonField
    public final double z;

    public V3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public V3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public V3(V2 v2) {
        this(v2.x, v2.y, 0);
    }

    public V3(Vec3 vec3) {
        this(vec3.x, vec3.y, 0);
    }

    public V3(Quaternion quaternion) {
        double w = quaternion.w;
        double x = quaternion.x;
        double y = quaternion.y;
        double z = quaternion.z;
        double sinrCosp = 2 * (w * x + y * z);
        double cosrCosp = 1 - 2 * (x * x + y * y);
        this.z = (float) Math.atan2(sinrCosp, cosrCosp);
        double sinp = 2 * (w * y - z * x);
        if (Math.abs(sinp) >= 1) {
            this.x = Math.copySign(1.57075f, sinp);
        } else {
            this.x = (float) Math.asin(sinp);
        }
        double sinyCosp = 2 * (w * z + x * y);
        double cosyCosp = 1 - 2 * (y * y + z * z);
        this.y = (float) Math.atan2(sinyCosp, cosyCosp);

    }

    public V3 multiply(double d) {
        return new V3(x * d, y * d, z * d);
    }

    public V3 multiply(V3 v3) {
        return new V3(x * v3.x, y * v3.y, z * v3.z);
    }

    public V3 add(double d) {
        return new V3(x + d, y + d, z + d);
    }

    public V3 add(V3 v3) {
        return new V3(x + v3.x, y + v3.y, z + v3.z);
    }

    public V3 reduce(double d) {
        return new V3(x - d, y - d, z - d);
    }

    /***
     * 返回目标指向自身的向量
     */
    public V3 reduce(V3 v3) {
        return new V3(x - v3.x, y - v3.y, z - v3.z);
    }

    /***
     * 模的平方
     */
    public double sqrMagnitude() {
        return x * x + y * y + z * z;
    }

    /***
     * 模
     */
    public double magnitude() {
        return Math.sqrt(sqrMagnitude());
    }

    /***
     * 获取量向量间距
     */
    public double distance(V3 v3) {
        return v3.reduce(this).magnitude();
    }

    public boolean isEmpty() {
        return x == 0 && y == 0 && z == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof V3) {
            V3 v3 = (V3) obj;
            return Double.compare(v3.x, this.x) == 0
                    && Double.compare(v3.y, this.y) == 0
                    && Double.compare(v3.z, this.z) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        long j = Double.doubleToLongBits(this.x);
        int i = (int) (j ^ j >>> 32);
        j = Double.doubleToLongBits(this.y);
        i = 31 * i + (int) (j ^ j >>> 32);
        j = Double.doubleToLongBits(this.z);
        return 31 * i + (int) (j ^ j >>> 32);
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
