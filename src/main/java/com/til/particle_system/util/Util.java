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

    public boolean isSuperClass(Class<?> basics, Class<?> target) {
        for (Class<?> anInterface : target.getInterfaces()) {
            if (anInterface.equals(basics)) {
                return true;
            }
        }
        Class<?> bClass = target.getSuperclass();

        if (basics != null) {
            if (basics.equals(bClass)) {
                return true;
            } else {
                return isSuperClass(basics, bClass);
            }
        }
        return false;

    }

    public static String titleCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }


}
