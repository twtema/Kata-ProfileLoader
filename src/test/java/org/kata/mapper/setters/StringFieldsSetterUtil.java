package org.kata.mapper.setters;

import org.kata.mapper.util.Util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class StringFieldsSetterUtil {
    public static void set(Object o) {
        List<Field> fields = Arrays.stream(o.getClass().getDeclaredFields()).toList();
        fields.stream().
                filter(field -> String.class.isAssignableFrom(field.getType())).
                peek(field -> field.setAccessible(true)).forEach(field -> {
                    try {
                        field.set(o, Util.generateRandomString());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

}
