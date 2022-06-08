package com.til.particle_system.client.cell;

import com.til.math.Quaternion;
import com.til.math.V3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/***
 * 对粒子系统的支持
 * @author til
 */

@OnlyIn(Dist.CLIENT)
public interface IParticleSystemSupport {

    /***
     * 返回粒子系统所在位置
     */
    V3 getPos();

    /***
     * 返回上一t粒子系统所在位置
     */
    V3 getOldPos();

    /***
     * 返回粒子系统的旋转
     */
    Quaternion getRotate();

    /***
     * 获取粒子系统上t旋转
     */
    Quaternion getOldRotate();

    /***
     * 获取粒子系统的大小
     */
    V3 getSize();

    /***
     * 获取粒子系统上一t大小
     */
    V3 getOldSize();

    ClientLevel getClient();


}
