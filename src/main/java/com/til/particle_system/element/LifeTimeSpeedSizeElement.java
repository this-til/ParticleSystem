package com.til.particle_system.element;

import com.til.particle_system.util.ITime;
import net.minecraft.world.phys.Vec2;

/***
 * 速度决定大小
 * @author til
 */
public class LifeTimeSpeedSizeElement {

    /***
     * 大小变换
     * -根据速度范围计算time
     * -粒子大小*改值
     */
    public ITime.ITimeVec3 size;

    /***
     * 速度范围
     * -根据移动速度的模计算time
     */
    public Vec2 speedRange;

}
