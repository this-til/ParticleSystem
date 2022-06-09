package com.til.math;

import org.jetbrains.annotations.Nullable;

/***
 * 变换
 * @author til
 */
public class Transform {

    public V3 pos;

    public Quaternion rotate;


    public V3 size;


    /***
     * 父类变换
     */
    @Nullable
    public Transform parent;

    public Transform(V3 pos, Quaternion rotate, V3 size, @Nullable Transform parent) {
        this.pos = pos;
        this.rotate = rotate;
        this.size = size;
        this.parent = parent;
    }

    public Transform(Transform transform) {
        this(transform.pos, transform.rotate, transform.size, transform.parent);
    }

    /***
     * 获取全局位置
     */
    public V3 getWorldPos() {
        if (parent == null) {
            return pos;
        }
        return parent.getWorldPos().multiply(pos.multiply(parent.size).transform(parent.rotate));
    }

    /***
     * 获取全局旋转
     */
    public Quaternion getWorldRotate() {
        if (parent == null) {
            return rotate;
        }
        return parent.getWorldRotate().multiply(rotate);
    }

    /***
     * 获取全局旋转
     */
    public V3 getWorldSize() {
        if (parent == null) {
            return size;
        }
        return parent.getWorldSize().multiply(size);
    }


    public TransformRecord as() {
        return new TransformRecord(pos, rotate, size);
    }

    public record TransformRecord(V3 pos, Quaternion rotate, V3 size) {
    }


}
