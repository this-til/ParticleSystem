package com.til.math;

import com.mojang.math.Vector3f;
import com.til.json_read_write.annotation.*;
import com.til.util.UseString;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/***
 * 一个三维向量
 * @author til
 */
@BaseClass(sonClass = V3.class)
@SonClass()
@DefaultNew(newExample = V3.class)
@UsePrefab(route = {UseString.PREFAB, "v3"})
public class V3 {

    public static final V3 XN = new V3(-1.0F, 0.0F, 0.0F);
    public static final V3 XP = new V3(1.0F, 0.0F, 0.0F);
    public static final V3 YN = new V3(0.0F, -1.0F, 0.0F);
    public static final V3 YP = new V3(0.0F, 1.0F, 0.0F);
    public static final V3 ZN = new V3(0.0F, 0.0F, -1.0F);
    public static final V3 ZP = new V3(0.0F, 0.0F, 1.0F);
    public static final V3 ZERO = new V3(0.0F, 0.0F, 0.0F);

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
        this(vec3.x, vec3.y, vec3.z);
    }

    public V3(Quaternion quaternion) {
        double w = quaternion.i;
        double x = quaternion.j;
        double y = quaternion.k;
        double z = quaternion.r;
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

    public V3 add(double x, double y, double z) {
        return new V3(this.x + x, this.y + y, this.z + z);
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

    public V3 reduce(Vec3 vec3) {
        return new V3(x - vec3.x, y - vec3.y, z - vec3.z);
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

    public V3 transform(Quaternion quaternion) {
        Quaternion q1 = quaternion.multiply(new Quaternion(this.x, this.y, this.z, 0));
        q1 = q1.multiply(quaternion.inverse());
        return new V3(q1.i, q1.j, q1.k);
    }

    public Quaternion rotation(double z) {
        return new Quaternion(this, z, false);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof V3 v3) {
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

    public static V3 lerp(V3 v3, V3 oldV3, double time) {
        return v3.add(v3.reduce(oldV3).multiply(time));
    }
}
