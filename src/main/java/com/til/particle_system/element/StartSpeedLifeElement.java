package com.til.particle_system.element;

import com.til.particle_system.util.ITime;
import com.til.particle_system.util.V2;

/***
 * 基于粒子发射速度决定生命周期
 * @author til
 */
public class StartSpeedLifeElement implements IElement {

    /***
     * 生命乘积
     */
    public ITime.ITimeNumber multiplyLife;

    /***
     * 速度范围
     */
    public V2 speedRange;


}
