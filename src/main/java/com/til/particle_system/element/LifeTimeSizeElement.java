package com.til.particle_system.element;

import com.til.particle_system.element.main.ParticleCell;
import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.util.ITime;

/***
 * 生命周期大小
 * @author til
 */
public class LifeTimeSizeElement implements IElement.IParticleElement {

    /***
     * 大小
     * -起始大小*该值
     */
    @UseField
    public ITime.ITimeV3 size;

    @Override
    public void up(ParticleCell particleCell) {
        particleCell.size = particleCell.size.multiply(size.as(particleCell.time));
    }
}
