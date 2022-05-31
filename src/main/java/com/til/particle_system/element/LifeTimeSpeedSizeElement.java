package com.til.particle_system.element;

import com.til.particle_system.element.main.ParticleCell;
import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.util.ITime;
import com.til.particle_system.util.V2;

/***
 * 速度决定大小
 * @author til
 */
public class LifeTimeSpeedSizeElement implements IElement.IParticleElement {

    /***
     * 大小变换
     * -根据速度范围计算time
     * -粒子大小*改值
     */
    @UseField
    public ITime.ITimeV3 size;

    /***
     * 速度范围
     * -根据移动速度的模计算time
     */
   @UseField
   public V2 speedRange;

    @Override
    public void up(ParticleCell particleCell) {
        particleCell.size = particleCell.size.multiply(size.as(speedRange.getProportion(particleCell.move.magnitude())));
    }
}
