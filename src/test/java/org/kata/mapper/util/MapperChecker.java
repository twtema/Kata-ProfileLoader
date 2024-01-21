package org.kata.mapper.util;

import java.lang.reflect.Field;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class MapperChecker {

    public void checkFieldsEquivalence(Object from, Object to) throws IllegalAccessException {

        List<Field> fromFields = getFields(from);
        List<Field> toFields = getFields(to);


        Optional<List<Field>> fromCollectionFields = Optional.of(ejectCollectionFields(fromFields, from));
        Optional<List<Field>> toCollectionFields = Optional.of(ejectCollectionFields(toFields, to));

        fromFields = removeCollectionFields(fromFields);
        toFields = removeCollectionFields(toFields);


        fromCollectionFields.ifPresent(check -> {
            try {
                checkCollectionFieldsEquivalence(
                        fromCollectionFields.get(),
                        toCollectionFields.get(),
                        from,
                        to);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        Set<String> fromFieldsSet = new HashSet<>(fromFields.stream().map(Field::getName).toList());
        Set<String> toFieldsSet = new HashSet<>(toFields.stream().map(Field::getName).toList());

        toFieldsSet.retainAll(fromFieldsSet);


        for (String field : toFieldsSet) {
            assertThat(to).hasFieldOrPropertyWithValue(field,
                    fromFields.stream().
                            filter(f -> f.getName().equals(field)).
                            peek(f -> f.setAccessible(true)).
                            findFirst().get().get(from));
        }

    }

    private List<Field> removeCollectionFields(List<Field> fields) {
        return fields.stream().filter(field -> !Collection.class.isAssignableFrom(field.getType())).toList();
    }

    private List<Field> ejectCollectionFields(List<Field> fields, Object o) {
        List<Field> fromCollectionFields = new ArrayList<>();
        fields.forEach(field -> {
            if (Collection.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    if (field.get(o) != null) {
                        if (!((List<Object>) field.get(o)).isEmpty()) {
                            fromCollectionFields.add(field);
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return fromCollectionFields;
    }

    private List<Field> getFields(Object o) {
        return Arrays.stream(o.getClass().getDeclaredFields()).toList();
    }

    private void checkCollectionFieldsEquivalence(List<Field> fromCollectionFields, List<Field> toCollectionFields, Object from, Object to) throws IllegalAccessException {


        Set<String> fromFieldsSet = new HashSet<>(fromCollectionFields.stream().map(Field::getName).toList());
        Set<String> toFieldsSet = new HashSet<>(toCollectionFields.stream().map(Field::getName).toList());

        toFieldsSet.retainAll(fromFieldsSet);


        for (String name : toFieldsSet) {
            Field nextFromField = fromCollectionFields.stream().filter(field -> field.getName().equals(name)).findFirst().get();
            Field nextToField = toCollectionFields.stream().filter(field -> field.getName().equals(name)).findFirst().get();

            Iterator<Object> fromIterator = ((List<Object>)nextFromField.get(from)).listIterator();
            Iterator<Object> toIterator = ((List<Object>)nextToField.get(to)).listIterator();
            checkFieldsEquivalence(fromIterator.next(), toIterator.next());
        }


    }
}
