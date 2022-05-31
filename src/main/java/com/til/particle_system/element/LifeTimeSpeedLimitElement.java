package com.til.particle_system.element;

import com.til.particle_system.element.main.ParticleCell;
import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.util.ITime;

/***
 * 生命周期类速度限制
 * @author til
 */
public class LifeTimeSpeedLimitElement implements IElement.IParticleElement {

    /***
     * 速度阈值
     * 速度超过部分进行抑制操作
     */
    @UseField
    public ITime.ITimeV3 speedThreshold;

    /***
     * 抑制（0~1）之间
     * 例如0.5将超过阈值速度的超过部分减去50%
     */
    @UseField
    public ITime.ITimeNumber threshold;

    /***
     * 乘以大小
     * 计算阈值速度时将原速度乘粒子大小作为输出
     */
    @UseField
    public Boolean multiplySize;

    /***
     * 乘以速度
     * 计算阈值速度时将原速度乘粒子速度作为输出
     */
    @UseField
    public Boolean multiplySpeed;

    @Override
    public void up(ParticleCell particleCell) {
        //TODO
    }
}
