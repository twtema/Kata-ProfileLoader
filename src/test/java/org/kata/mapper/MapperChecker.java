package org.kata.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MapperChecker {

    public void checkFieldsEquivalence(Object from, Object to) throws IllegalAccessException {
        Field[] fromFields = from.getClass().getDeclaredFields();
        Field[] toFields = to.getClass().getDeclaredFields();

        Set<String> fromFieldsSet = new HashSet<>(Arrays.asList(Arrays.stream(fromFields)
                .map(Field::getName).toArray(String[]::new)));
        Set<String> toFieldsSet = new HashSet<>(Arrays.asList(Arrays.stream(toFields)
                .map(Field::getName).toArray(String[]::new)));

        toFieldsSet.retainAll(fromFieldsSet);


        for (String field: toFieldsSet) {
            assertThat(to).
                    hasFieldOrPropertyWithValue(field,
                            Arrays.stream(fromFields)
                                    .filter(f -> f.getName().equals(field))
                                    .peek(f -> f.setAccessible(true))
                                    .findFirst().get().get(from));
        }

    }
}
