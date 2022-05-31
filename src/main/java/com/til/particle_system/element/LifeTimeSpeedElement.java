package com.til.particle_system.element;

import com.til.particle_system.element.main.ParticleCell;
import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.util.ITime;

/***
 * 生命周期内速度
 * @author til
 */
public class LifeTimeSpeedElement implements IElement.IParticleElement {

    /***
     * 生命周期内的速度控制
     * -起始速度*该值
     */
    @UseField
    public ITime.ITimeV3 speed;

    /***
     * 轨道
     * 将轨道速度应用于粒子，使其围绕系统中心旋转
     */
    @UseField
    public ITime.ITimeV3 track;

    /***
     * 偏移
     * 将偏移应用于旋转中心
     */
    @UseField
    public ITime.ITimeV3 deviation;

    @Override
    public void up(ParticleCell particleCell) {
        //TODO
    }
}
