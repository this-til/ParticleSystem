package com.til.particle_system.event;

import com.til.particle_system.MainParticleSystem;
import net.minecraftforge.eventbus.api.Event;

/***
 * mod事件
 * @author til
 */
public class EventMod extends Event {

    public final MainParticleSystem main = MainParticleSystem.main;

    /***
     * 初始化程序
     */
    public static class Init extends EventMod {


    }
}
