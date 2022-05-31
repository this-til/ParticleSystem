package com.til.particle_system.util;

/***
 * @author til
 */
public class V3 {
    /***
     * pitch
     */
    public final double x;
    /**
     * yaw
     */
    public final double y;
    /***
     * roll
     */
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

}
