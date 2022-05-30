package com.til.particle_system.util;

/***
 * @author til
 */
public class Util {

    /***
     * 强行转换
     * @return 转换后
     */
    public static <E> E forcedVonversion(Object obj) {
        //noinspection unchecked
        return (E) obj;
    }

}
