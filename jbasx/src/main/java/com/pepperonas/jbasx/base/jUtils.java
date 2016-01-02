/*
 * Copyright (c) 2016 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pepperonas.jbasx.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class jUtils {

    /**
     * Fields list.
     *
     * @param clazz the clazz
     * @return the list
     */
    public static List<String> _fields(Class<?> clazz) {
        List<String> list = new ArrayList<>();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            list.add(field.getName());
        }
        return list;
    }


    /**
     * Fields list.
     *
     * @param clazz    the clazz
     * @param modifier the modifier
     * @return the list
     */
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


    /**
     * String value string.
     *
     * @param field the field
     * @return the string
     */
    public static String _stringValue(Field field) {
        Object o = field;
        try {
            return (String) field.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Int value integer.
     *
     * @param field the field
     * @return the integer
     */
    public static Integer _intValue(Field field) {
        Object o = field;
        try {
            return (Integer) field.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Float value float.
     *
     * @param field the field
     * @return the float
     */
    public static Float _floatValue(Field field) {
        Object o = field;
        try {
            return (Float) field.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Double value double.
     *
     * @param field the field
     * @return the double
     */
    public static Double _doubleValue(Field field) {
        Object o = field;
        try {
            return (Double) field.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Long value long.
     *
     * @param field the field
     * @return the long
     */
    public static Long _longValue(Field field) {
        Object o = field;
        try {
            return (Long) field.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * String values list.
     *
     * @param clazz the clazz
     * @return the list
     */
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
