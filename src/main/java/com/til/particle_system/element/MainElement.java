package com.til.particle_system.element;

import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.util.ITime;
import com.til.particle_system.util.IValue;

/**
 * 用于储存粒子系统的主要元素
 *
 * @author til
 */
public class MainElement extends Element {
    /***
     * int 粒子系统的生命周期
     */
    @UseField
    public Number time;

    /***
     * 粒子系统是否开始循环
     */
    @UseField
    public Boolean loop;

    /***
     * 粒子系统的启动延迟
     */
    @UseField
    public IValue.IValueNumber delay;

    /***
     * 起始生命周期
     */
    @UseField
    public ITime.ITimeNumber particleLife;

    /***
     * 起始速度
     */
    @UseField
    public ITime.ITimeV3 particleSpeed;

    /***
     *  起始大小
     */
    @UseField
    public ITime.ITimeV3 particleSize;

    /***
     * 起始旋转
     */
    @UseField
    public ITime.ITimeV3 particleRotate;

    /***
     * 起始颜色
     */
    @UseField
    public ITime.ITimeColour particleColour;

    /***
     * 粒子重力
     */
    @UseField
    public ITime.ITimeNumber particleGravity;

    /***
     * 粒子坐标系
     */
    @UseField
    public ParticleCoordinate worldCoordinate;

    /***
     * 获取最大粒子数（int）
     */
    @UseField
    public Number maxParticle;

    /***
     * 粒子的缓存模式
     */
    @UseField
    public ParticleBufferMode bufferMode;

    /***
     * 粒子坐标系
     */
    public enum ParticleCoordinate {
        /***
         * 世界坐标系
         */
        WORLD,
        /***
         * 实体坐标系
         * 为粒子使用实体坐标*本身坐标
         */
        ENTITY;
    }

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
         * 忽略生成，知道有位置空出
         */
        IGNORE;
    }


}
