package org.kata.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableCaching
@PropertySource(value = "classpath:application.properties")
public class CacheConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;


    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(3)) // Установите время жизни для кэша
                .disableCachingNullValues() // Не кэшируйте null значения
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    @Bean
    public Jedis jedis() {
        return new Jedis(host, port);
    }

    @Bean
    public CacheResolver cacheResolver(CacheManager cacheManager) {
        return new SimpleCacheResolver(cacheManager);
    }

//    @Override
//    public KeyGenerator keyGenerator() {
//        return new SimpleKeyGenerator() {
//            @Override
//            public Object generate(Object target, Method method, Object... params) {
//                StringBuilder keyBuilder = new StringBuilder();
//                keyBuilder.append(method.getName());
//                for (Object param : params) {
//                    keyBuilder.append("_").append(param.toString());
//                }
//                return keyBuilder.toString();
//            }
//        };
//    }

    private static class SetRedisSerializer implements RedisSerializer<Set<?>> {

        private final RedisSerializer<String> stringSerializer = RedisSerializer.string();

        @Override
        public byte[] serialize(Set<?> set) throws SerializationException {
            // Преобразуйте Set в массив байтов
            // Например, можно использовать JSON сериализацию
            return stringSerializer.serialize(set.toString());
        }

        @Override
        public Set<?> deserialize(byte[] bytes) throws SerializationException {
            // Преобразуйте массив байтов обратно в Set
            // Например, можно использовать JSON десериализацию
            String setString = stringSerializer.deserialize(bytes);
            // В данном примере, предполагается, что Set был сериализован в виде строки
            // и будет десериализован обратно в Set
            return new HashSet<>(Arrays.asList(setString.split(",")));
        }
    }


//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
//        return redisTemplate;
//    }


//    MyObject myObject = new MyObject();
//        myObject.setName("Test");
//        myObject.setValue(123);
//
//    // Сериализация объекта в JSON
//    Gson gson = new Gson();
//    String json = gson.toJson(myObject);
//
//    // Сохранение сериализованного объекта в Redis
//        try (Jedis jedis = new Jedis("localhost")) {
//        jedis.set("myObjectKey", json);
//    }
//
//    // Десериализация объекта из Redis
//    String retrievedJson = jedis.get("myObjectKey");
//    MyObject retrievedObject = gson.fromJson(retrievedJson, MyObject.class);


//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(){
//        return  new RedisTemplate<Object, Object>();
//    }
}