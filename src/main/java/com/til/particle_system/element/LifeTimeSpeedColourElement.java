package com.til.particle_system.element;

import com.til.particle_system.element.main.ParticleCell;
import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.util.ITime;
import com.til.particle_system.util.V2;

/***
 * 速度颜色
 * @author til
 */
public class LifeTimeSpeedColourElement implements IElement.IParticleElement {

    /***
     * 眼色变换
     * -根据速度范围计算time
     * -粒子颜色*该颜色
     */
    @UseField
    public ITime.ITimeColour colour;

    /***
     * 速度范围
     * -根据移动速度的模计算time
     */
    @UseField
    public V2 speedRange;

    @Override
    public void up(ParticleCell particleCell) {
        particleCell.color = particleCell.color.multiply(colour.as(speedRange.getProportion(particleCell.move.magnitude())));
    }
}
