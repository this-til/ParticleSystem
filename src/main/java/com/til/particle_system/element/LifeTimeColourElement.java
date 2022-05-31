package com.til.particle_system.element;

import com.til.particle_system.element.main.ParticleCell;
import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.util.ITime;

/***
 * 生命周期颜色
 * @author til
 */
public class LifeTimeColourElement implements IElement.IParticleElement {

    /***
     * 颜色变换
     * -粒子颜色*该颜色
     */
    @UseField
    public ITime.ITimeColour colour;

    @Override
    public void up(ParticleCell particleCell) {
        particleCell.color = particleCell.color.multiply(colour.as(particleCell.time));
    }
}
