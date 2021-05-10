package com.navercorp.micro;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	
	@Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Order.class));
        return redisTemplate;
    }
	
//    @Bean
//    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
//        RedisSerializer<String> serializer = new StringRedisSerializer();
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Order.class);
//        RedisSerializationContext serializationContext = RedisSerializationContext
//                .<String, String>newSerializationContext()
//                .key(serializer)
//                .value(jackson2JsonRedisSerializer)
//                .hashKey(serializer)
//                .hashValue(jackson2JsonRedisSerializer)
//                .build();
//
//        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
//    }


//	@Bean
//	ReactiveRedisOperations<String, String> redisOperations(ReactiveRedisConnectionFactory factory) {
//		RedisSerializer<String> serializer = new StringRedisSerializer();
//		RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
//				.<String, String>newSerializationContext()
//				.key(serializer)
//				.value(serializer)
//				.hashKey(serializer)
//				.hashValue(serializer)
//				.build();
//		return new ReactiveRedisTemplate<>(factory, serializationContext);
//	}
//
//	@Bean
//	ReactiveRedisOperations<String, String> redisOperations(ReactiveRedisConnectionFactory factory) {
////		Jackson2JsonRedisSerializer<String> serializer = new Jackson2JsonRedisSerializer<>(StringLoader.class);
//		RedisSerializer<String> serializer = new StringRedisSerializer();
//		RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder = RedisSerializationContext
//				.newSerializationContext(new StringRedisSerializer());
//
//		RedisSerializationContext<String, String> context = builder.value(serializer).build();
//
//		return new ReactiveRedisTemplate<>(factory, context);
//	}
//	

}
