package org.kata.mapper.setters;

public interface Setter<Entity, Dto> {

    void setEntityFields(Entity entity);
    void setDtoFields(Dto dto);
}
