package com.til.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;

/***
 * @author til
 */
public class Util {


    /***
     * 强行转换
     * @return 转换后
     */
    public static <E> E forcedConversion(Object obj) {
        //noinspection unchecked
        return (E) obj;
    }

    public static String toString(Object o) {
        try {
            Field[] fields = o.getClass().getDeclaredFields();
            List<Field> fieldList = new List<>();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    fieldList.add(field);
                }
            } StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("_classType=[");
            sb.append(o.getClass().toString());
            sb.append("]");
            if (fieldList.size() == 0) {
              sb.append("}");
              return sb.toString();
            }
            Iterator<Field> iterator = fieldList.iterator();
            while (iterator.hasNext()) {
                Field field = iterator.next();
                sb.append(field.getName());
                sb.append('=');
                sb.append('[');
                try {
                    sb.append(field.get(o).toString());
                } catch (Exception e) {
                    sb.append(e);
                }
                sb.append("]");
                if (!iterator.hasNext()) {
                    sb.append('}');
                   break;
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    public static float section(float e, float a, float b) {
        float max = Math.max(a, b);
        float min = Math.min(a, b);
        return e > max ? max : Math.max(e, min);
    }

    public static double section(double e, double a, double b) {
        double max = Math.max(a, b);
        double min = Math.min(a, b);
        return e > max ? max : Math.max(e, min);
    }

}
