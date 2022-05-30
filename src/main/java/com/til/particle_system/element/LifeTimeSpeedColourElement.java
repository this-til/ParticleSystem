package com.til.particle_system.element;

import com.til.particle_system.util.ITime;
import net.minecraft.world.phys.Vec2;

/***
 * 速度颜色
 * @author til
 */
public class LifeTimeSpeedColourElement {

    /***
     * 眼色变换
     * -根据速度范围计算time
     * -粒子颜色*该颜色
     */
    public ITime.ITimeColour colour;

    /***
     * 速度范围
     * -根据移动速度的模计算time
     */
    public Vec2 speedRange;

}
