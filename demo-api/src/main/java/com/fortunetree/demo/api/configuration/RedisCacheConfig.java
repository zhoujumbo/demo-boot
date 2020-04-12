
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ObjectUtils;

import java.time.Duration;

/**
 *  缓存配置类
 */
@Configuration
@EnableCaching
public class RedisCacheConfig  extends CachingConfigurerSupport {
    private final static Logger logger = LoggerFactory.getLogger(RedisCacheConfig.class);

    /**
     * 功能描述: redis模型
     */
    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 功能描述: string类型的template
     * 键和值都是String,主要功能就是方便String类型的存储
     *
     * @param
     * @return:org.springframework.data.redis.core.StringRedisTemplate
     * @since: v1.0
     * @Author:wangcanfeng
     * @Date: 2019/4/1 16:45
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return new StringRedisTemplate(lettuceConnectionFactory);
    }


    /**
     * 选择redis作为默认缓存工具
     * @param
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 初始化缓存管理器，在这里我们可以缓存的整体过期时间什么的，我这里默认没有配置
        logger.info("初始化 -> [{}]", "CacheManager RedisCacheManager Start");
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(2)); // 设置缓存有效期一小时
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
        //springboot2.X以上版本引入的缓存管理器和以前版本的缓存管理器初始化方式不一样，
        // 新版本的缓存管理器通过连接工程进行初始化
//        return RedisCacheManager.create(lettuceConnectionFactory());
    }

    /**
     * 功能描述: 缓存键的生成器
     */
//    @Bean
//    @Override
//    public KeyGenerator keyGenerator() {
//        return (target, method, form) -> {
//            StringBuilder sb = new StringBuilder();
//            // 目标类的类名
//            sb.append(target.getClass().getName());
//            // 目标方法名
//            sb.append('#').append(method.getName());
//            if (ObjectUtils.isEmpty(form)) {
//                sb.append("()");
//            } else {
//                sb.append("(");
//                for (Object param : form) {
//                    if (param != null) {
//                        sb.append(param).append(",");
//                    } else {
//                        // 参数为空的时候拼接上null
//                        sb.append("null").append(",");
//                    }
//                }
//                //移除最后一个逗号
//                sb.deleteCharAt(sb.length() - 1);
//                sb.append(")");
//            }
//            return sb.toString();
//        };
//    }


    /**
     * 功能描述: 缓存解析器，可以通过定时的方式，对缓存中的内容进行处理
     */
//    @Override
//    public CacheResolver cacheResolver() {
//        return new SimpleCacheResolver(cacheManager());
//    }


    @Override
    @Bean
    public CacheErrorHandler errorHandler() {
        // 异常处理，当Redis发生异常时，打印日志，但是程序正常走
        logger.info("初始化 -> [{}]", "Redis CacheErrorHandler");
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                logger.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                logger.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key)    {
                logger.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                logger.error("Redis occur handleCacheClearError：", e);
            }
        };
        return cacheErrorHandler;
    }

}
