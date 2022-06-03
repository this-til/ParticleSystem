package com.til.particle_system.element.system;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.json_read_write.util.math.ITime;
import com.til.json_read_write.util.math.V2;
import com.til.particle_system.element.IElement;

/***
 * 基于粒子发射速度决定生命周期
 * @author til
 */
@BaseClass(sonClass = StartSpeedLifeElement.class)
@SonClass()
public class StartSpeedLifeElement implements IElement {

    /***
     * 生命乘积
     */
    @JsonField
    public ITime.ITimeNumber multiplyLife;

    /***
     * 速度范围
     */
    @JsonField
    public V2 speedRange;


}
