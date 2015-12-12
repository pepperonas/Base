package com.pepperonas.jbasx.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class jUtils {

    public static List<String> _fields(Class<?> clazz) {
        List<String> list = new ArrayList<>();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            list.add(field.getName());
        }
        return list;
    }


    public static List<String> _fields(Class<?> clazz, int modifier) {
        List<String> list = new ArrayList<>();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.getModifiers() == modifier) {
                list.add(field.getName());
            }
        }
        return list;
    }


    public static String _stringValue(Field f) {
        Object o = f;
        try {
            return (String) f.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Integer _intValue(Field f) {
        Object o = f;
        try {
            return (Integer) f.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Float _floatValue(Field f) {
        Object o = f;
        try {
            return (Float) f.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Double _doubleValue(Field f) {
        Object o = f;
        try {
            return (Double) f.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Long _longValue(Field f) {
        Object o = f;
        try {
            return (Long) f.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<String> _stringValues(Class<?> clazz) {
        List<String> list = new ArrayList<>();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            try {
                list.add((String) field.get(field));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        return list;
    }

}
