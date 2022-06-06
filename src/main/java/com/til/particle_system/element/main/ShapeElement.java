package com.til.particle_system.element.main;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.DefaultNew;
import com.til.json_read_write.annotation.SonClass;
import com.til.math.Quaternion;
import com.til.math.V3;
import com.til.particle_system.element.IElement;
import com.til.particle_system.client.cell.ParticleCell;

/**
 * 粒子系统的发射形状
 *
 * @author til
 */
@BaseClass(sonClass = {ShapeElement.EmptyShapeElement.class})
@DefaultNew(newExample = ShapeElement.EmptyShapeElement.class)
public abstract class ShapeElement implements IElement {

    /***
     * 获取起始位置
     */
    public abstract V3 getStartPos(ParticleCell particleCell);

    /***
     * 获取起始位移
     */
    public abstract V3 getStartMove(ParticleCell particleCell);

    /***
     * 获取开始时的旋转
     */
    public abstract Quaternion getStartRotate(ParticleCell particleCell);

    @SonClass(name = EmptyShapeElement.NAME)
    public static class EmptyShapeElement extends ShapeElement {
        public static final String NAME = "empty";

        @Override
        public V3 getStartPos(ParticleCell particleCell) {
            return new V3();
        }

        @Override
        public V3 getStartMove(ParticleCell particleCell) {
            return new V3();
        }

        @Override
        public Quaternion getStartRotate(ParticleCell particleCell) {
            return new Quaternion();
        }
    }

}
