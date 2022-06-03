package com.til.particle_system.element.main;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.json_read_write.api.JsonTransform;
import com.til.json_read_write.util.List;
import com.til.json_read_write.util.math.ITime;
import com.til.json_read_write.util.math.IValue;
import com.til.particle_system.element.IElement;

/**
 * 粒子系统的发射元素
 *
 * @author til
 */
@BaseClass(sonClass = LaunchElement.class)
@SonClass()
public class LaunchElement implements IElement {

    /***
     * 随时间生成粒子数
     * 单位1t
     */
    @JsonField
    public ITime.ITimeNumber timeGenerate;

    /***
     * 获取移动生成数量
     * 单位1移动向量模
     */
    @JsonField
    public ITime.ITimeNumber moveGenerate;

    /***
     * 获取粒子爆发数组
     */
    @JsonField
    public LaunchBurst.LaunchBurstList launchBursts;

    /***
     * 生成突发
     */
    @BaseClass(sonClass = LaunchBurst.class)
    @SonClass()
    public static class LaunchBurst {

        /***
         * 获取突发时间（从粒子系统生成开始计算）（int）
         */
        @JsonField
        public Number needTime;

        /***
         * 总共爆发多少周期（int）
         */
        @JsonField
        public Number cycle;

        /***
         * 爆发时数量
         */
        @JsonField
        public ITime.ITimeNumber amount;

        /***
         * 获取概率
         */
        @JsonField
        public IValue.IValueNumber probability;

        @BaseClass(sonClass = LaunchBurstList.class)
        @SonClass(transform = JsonTransform.ListCurrencyJsonTransform.class)
        public static class LaunchBurstList extends List<LaunchBurst> {
        }

    }


}
