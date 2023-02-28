package zhihui.backend.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

/**
 * Redis配置文件
 * @author CHENPrime-Coder
 */
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private Integer db = 0;
    private String hostname = "localhost";
    private String password = "";
    private Integer port = 6379;

    /**
     * 获取redis数据库配置
     * @return RedisStandaloneConfiguration配置
     */
    public RedisStandaloneConfiguration getRedisStandaloneConfiguration() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        configuration.setDatabase(db);
        if (StringUtils.hasText(password)) {
            configuration.setHostName(hostname);
        }

        if (StringUtils.hasText(password)) {
            configuration.setPassword(password);
        }

        configuration.setPort(port);
        return configuration;
    }

    /**
     * RedisTemplate
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(getRedisStandaloneConfiguration());

        factory.afterPropertiesSet();

        template.setConnectionFactory(factory);

        // 序列化
        RedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // String
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(redisSerializer);
        // Hashmap
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(redisSerializer);

        return template;
    }

    public void setDb(Integer db) {
        this.db = db;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}
