package org.kata.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.convert.RedisCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableCaching
@PropertySource(value = "classpath:application.properties")
public class CacheConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;


//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofHours(3)) // Установите время жизни для кэша
//                .disableCachingNullValues() // Не кэшируйте null значения
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cacheConfiguration)
//                .build();
//    }

    @Bean
    public RedisCustomConversions redisCustomConversions() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new AuthenticationSessionConnverter(objectMapper).getConverters);

        return new RedisCustomConversions(converters);
    }


//        new AuthenticationSessionConnverter(objectMapper).getConverters

    @Bean
    public CacheResolver setCacheResolver(CacheManager cacheManager) {
        return new SimpleCacheResolver(cacheManager);
    }

}