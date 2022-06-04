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
    public final double w;
    @JsonField
    public final double x;
    @JsonField
    public final double y;
    @JsonField
    public final double z;

    public Quaternion() {
        this.w = 1;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
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
        w = cy * cp * cr + sy * sp * sr;
        x = cy * cp * sr - sy * sp * cr;
        y = sy * cp * sr + cy * sp * cr;
        z = sy * cp * cr - cy * sp * sr;
    }

    public Quaternion multiply(Quaternion quaternion) {
        double w = this.w * quaternion.w - this.x * quaternion.x - this.y * quaternion.y - this.z * quaternion.z;
        double x = this.w * quaternion.x + this.x * quaternion.w + this.y * quaternion.z - this.z * quaternion.y;
        double y = this.w * quaternion.y + this.y * quaternion.w + this.z * quaternion.x - this.x * quaternion.z;
        double z = this.w * quaternion.z + this.z * quaternion.w + this.x * quaternion.y - this.y * quaternion.x;
        return new Quaternion(w, x, y, z);
    }

    /***
     * 求逆
     */
    public Quaternion inverse() {
        return new Quaternion(w, -x, -y, -z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Quaternion) {
            Quaternion v3 = (Quaternion) obj;
            return Double.compare(v3.w, this.w) == 0
                    && Double.compare(v3.x, this.x) == 0
                    && Double.compare(v3.y, this.y) == 0
                    && Double.compare(v3.z, this.z) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + this.w + ", " + this.x + ", " + this.y + ", " + this.z + ")";
    }

}
