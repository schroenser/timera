package de.schroenser.timera.cache;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
class CacheConfiguration
{
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(ObjectMapper objectMapper)
    {
        objectMapper = objectMapper.copy();

        objectMapper = objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY);

        WrapperGenericJackson2JsonRedisSerializer serializer = new WrapperGenericJackson2JsonRedisSerializer(
            objectMapper);

        RedisSerializationContext.SerializationPair<Object>
            valueSerializationPair
            = RedisSerializationContext.SerializationPair.fromSerializer(serializer);

        return redisCacheManagerBuilder -> {
            redisCacheManagerBuilder.cacheDefaults(redisCacheManagerBuilder.cacheDefaults()
                .serializeValuesWith(valueSerializationPair));
        };
    }
}
