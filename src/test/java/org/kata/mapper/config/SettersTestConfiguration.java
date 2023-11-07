package org.kata.mapper.config;

import org.kata.mapper.setters.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SettersTestConfiguration {
    @Bean
    public AddressSetter addressSetter() {
        return new AddressSetter();
    }
    @Bean
    public AvatarSetter avatarSetter() {
        return new AvatarSetter();
    }
    @Bean
    public ContactSetter contactSetter() {
        return new ContactSetter();
    }
    @Bean
    public DocumentSetter documentSetter() {
        return new DocumentSetter();
    }
    @Bean
    public IndividualSetter individualSetter() {
        return new IndividualSetter();
    }
}
