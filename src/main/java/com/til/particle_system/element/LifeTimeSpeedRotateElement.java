package com.til.particle_system.element;

import com.til.particle_system.element.main.ParticleCell;
import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.util.ITime;
import com.til.particle_system.util.Quaternion;
import com.til.particle_system.util.V2;

/***
 * 按照速度旋转
 * @author til
 */
public class LifeTimeSpeedRotateElement implements IElement.IParticleElement {

    /***
     * 角速度
     */
    @UseField
    public ITime.ITimeV3 angularVelocity;

    /***
     * 速度范围
     */
    @UseField
    public V2 speedRange;

    @Override
    public void up(ParticleCell particleCell) {
        particleCell.rotateMove =  particleCell.rotateMove.multiply(new Quaternion(angularVelocity.as(speedRange.getProportion( particleCell.move.magnitude()))));
    }
}
