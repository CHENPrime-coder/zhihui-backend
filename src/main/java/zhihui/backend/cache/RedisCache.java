package zhihui.backend.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.springframework.data.redis.core.RedisTemplate;
import zhihui.backend.exception.BeanNotFoundException;
import zhihui.backend.util.SpringContextHolder;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Mybatis二级缓存Redis实现
 * @author CHENPrime-Coder
 */
@Slf4j
public class RedisCache implements Cache {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private String id;
    private RedisTemplate redisTemplate;

    public RedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    private RedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            try {
                return SpringContextHolder.getBean(RedisTemplate.class);
            } catch (BeanNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return redisTemplate;
    }

    @Override
    public String getId() {
        log.info("获取当前的缓存id: [{}]",id);
        return id;
    }

    /**
     * 加入缓存
     * @param key
     *          Can be any object but usually it is a {@link CacheKey}
     * @param value
     *          The result of a select.
     */
    @Override
    public void putObject(Object key, Object value) {
        log.info("放入缓存key:[{}] 放入缓存的value:[{}]",key,value);
        getRedisTemplate().opsForHash().put(id,key.toString(),value);
    }

    /**
     * 获取缓存
     * @param key
     *          The key
     * @return 缓存
     */
    @Override
    public Object getObject(Object key) {
        log.info("获取缓存的key:[{}]",key.toString());
        return getRedisTemplate().opsForHash().get(id,key.toString());
    }

    /**
     * 移除缓存
     * @param key
     *          The key
     * @return
     */
    @Override
    public Object removeObject(Object key) {
        return getRedisTemplate().opsForHash().delete(key);
    }

    @Override
    public void clear() {
        log.warn("清除所有缓存信息...");
        getRedisTemplate().delete(id);
    }

    @Override
    public int getSize() {
        return getRedisTemplate().opsForHash().size(id).intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
