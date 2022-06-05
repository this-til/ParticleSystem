package com.til.particle_system.element.main;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.json_read_write.JsonTransform;
import com.til.math.ITime;
import com.til.math.IValue;
import com.til.math.Quaternion;
import com.til.math.V3;
import com.til.particle_system.element.IElement;
import com.til.particle_system.element.cell.ParticleCell;
import com.til.particle_system.element.cell.ParticleSystemCell;

/**
 * 用于储存粒子系统的主要元素
 *
 * @author til
 */
@BaseClass(sonClass = MainElement.class)
@SonClass()
public class MainElement implements IElement {
    /***
     * int 粒子系统的生命周期
     */
    @JsonField
    public int maxLife;

    /***
     * 粒子系统是否开始循环
     */
    @JsonField
    public boolean loop;

    /***
     * 粒子系统的启动延迟
     */
    @JsonField
    public IValue.IValueNumber delay;

    /***
     * 起始生命周期
     */
    @JsonField
    public ITime.ITimeNumber particleLife;

    /***
     * 起始速度
     */
    @JsonField
    public ITime.ITimeNumber particleSpeed;

    /***
     *  起始大小
     */
    @JsonField
    public ITime.ITimeV3 particleSize;

    /***
     * 起始旋转
     */
    @JsonField
    public ITime.ITimeV3 particleRotate;

    /***
     * 起始颜色
     */
    @JsonField
    public ITime.ITimeColour particleColour;

    /***
     * 粒子重力
     */
    @JsonField
    public ITime.ITimeNumber particleGravity;

    /***
     * 粒子坐标系
     */
    @JsonField
    public ParticleCoordinate worldCoordinate;

    /***
     * 获取最大粒子数（int）
     */
    @JsonField
    public int maxParticle;

    /***
     * 粒子的缓存模式
     */
    @JsonField
    public ParticleBufferMode bufferMode;

    /***
     * 粒子坐标系
     */
    @BaseClass(sonClass = ParticleCoordinate.class)
    @SonClass(transform = JsonTransform.EnumCurrencyJsonTransform.class)
    public enum ParticleCoordinate {
        /***
         * 世界坐标系
         */
        WORLD,
        /***
         * 局部坐标系
         */
        ENTITY;
    }

    @BaseClass(sonClass = ParticleBufferMode.class)
    @SonClass(transform = JsonTransform.EnumCurrencyJsonTransform.class)
    public enum ParticleBufferMode {
        /***
         * 杀死先生成的粒子
         */
        KILL,
        /***
         * 暂停生成
         */
        SUSPEND,
        /***
         * 忽略生成，直到有位置空出
         */
        IGNORE
    }


}
