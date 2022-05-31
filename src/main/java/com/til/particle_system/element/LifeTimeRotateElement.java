package com.til.particle_system.element;

import com.til.particle_system.element.main.ParticleCell;
import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.util.ITime;
import com.til.particle_system.util.Quaternion;

/***
 * 基于粒子生命周期的旋转
 * @author til
 */
public class LifeTimeRotateElement implements IElement.IParticleElement {

    /***
     * 角速度
     */
    @UseField
    public ITime.ITimeV3 angularVelocity;

    @Override
    public void up(ParticleCell particleCell) {
        particleCell.rotateMove = particleCell.rotateMove.multiply(new Quaternion(angularVelocity.as(particleCell.time)));
    }
}
