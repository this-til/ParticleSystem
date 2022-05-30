package com.til.particle_system.event;

import com.til.particle_system.MainParticleSystem;
import com.til.particle_system.register.IFromJsonElement;
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

        /***
         * 注册项目
         */
        public static class Register extends Init {

            /***
             * 注册类型
             */
            public static class Type extends Register {
                public <E> void put(Class<E> type) {
                    main.registerManage.put(type);
                }
            }

            /***
             * 注册解析元素
             */
            public static class Element extends Register {
                @SuppressWarnings("unchecked")
                public <E, EE extends E> void put(Class<E> basic, IFromJsonElement<EE> iFromJsonElement) {
                    main.registerManage.get(basic).put((IFromJsonElement<E>) iFromJsonElement);
                }
            }

            /***
             * 反编译字段
             */
            public static class Field extends Register {

            }

        }

    }
}
