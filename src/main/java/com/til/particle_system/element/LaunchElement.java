package com.til.particle_system.element;


import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.util.*;

/**
 * 粒子系统的发射元素
 *
 * @author til
 */
public class LaunchElement implements IElement {

    /***
     * 随时间生成粒子数
     * 单位1t
     */
    @UseField
    public ITime.ITimeNumber timeGenerate;

    /***
     * 获取移动生成数量
     * 单位1移动向量模
     */
    @UseField
    public ITime.ITimeNumber moveGenerate;

    /***
     * 获取粒子爆发数组
     */
    @UseField
    public LaunchBurst.LaunchBurstList launchBursts;

    /***
     * 生成突发
     */
    public static class LaunchBurst {

        /***
         * 获取突发时间（从粒子系统生成开始计算）（int）
         */
        @UseField
        public Number needTime;

        /***
         * 总共爆发多少周期（int）
         */
        @UseField
        public Number cycle;

        /***
         * 爆发时数量
         */
        @UseField
        public ITime.ITimeNumber amount;

        /***
         * 获取概率
         */
        @UseField
        public IValue.IValueNumber probability;

        public static class LaunchBurstList extends List<LaunchBurst> {
        }

    }



}
