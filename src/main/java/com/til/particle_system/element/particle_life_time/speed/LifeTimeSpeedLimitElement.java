package com.til.particle_system.element.particle_life_time.speed;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.json_read_write.util.math.ITime;
import com.til.particle_system.element.IElement;
import com.til.particle_system.element.cell.ParticleCell;

/***
 * 生命周期类速度限制
 * @author til
 */
@BaseClass(sonClass = LifeTimeSpeedLimitElement.class)
@SonClass
public class LifeTimeSpeedLimitElement implements IElement.IParticleElement {

    /***
     * 速度阈值
     * 速度超过部分进行抑制操作
     */
    @JsonField
    public ITime.ITimeNumber speedThreshold;

    /***
     * 抑制（0~1）之间
     * 例如0.5将超过阈值速度的超过部分减去50%
     */
    @JsonField
    public ITime.ITimeNumber threshold;

    /***
     * 乘以大小
     * 计算阈值速度时将原速度乘粒子大小作为输出
     */
    @JsonField
    public Boolean multiplySize;

    /***
     * 乘以速度
     * 计算阈值速度时将原速度乘粒子速度作为输出
     */
    @JsonField
    public Boolean multiplySpeed;

}
