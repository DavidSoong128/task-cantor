package com.horizon.cantor.redis;

import com.horizon.cantor.config.CantorConfig;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * <pre>
 *    redis功能操作类
 * </pre>
 * @author : David.Song/Java Engineer
 * @date : 2016/1/28 12:01
 * @see
 * @since : 1.0.0
 */
public class RedisOperator {

    private CantorConfig config = CantorConfig.configHolder();

    private SingleRedisHolder redisPool = SingleRedisHolder.redisHolder();

    public void setValue(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = redisPool.getJedis();
            jedis.select(config.getRedisDb());
            jedis.set(key, value);
        }catch(Exception e){
            throw new RuntimeException("redis error !!",e);
        } finally {
            if(jedis != null)
                redisPool.returnJedis(jedis);
        }
    }

    public void setValues(final byte[]... values){
        Jedis jedis = null;
        try {
            jedis = redisPool.getJedis();
            jedis.select(config.getRedisDb());
            jedis.mset(values);
        } catch(Exception e){
            throw new RuntimeException("redis error !!",e);
        }finally {
            if(jedis != null)
                redisPool.returnJedis(jedis);
        }
    }

    public void delValue(byte[] key) {
        Jedis jedis = null ;
        try {
            jedis = redisPool.getJedis();
            jedis.select(config.getRedisDb());
            jedis.del(key);
        } catch(Exception e){
            throw new RuntimeException("redis error !!",e);
        }finally {
            if(jedis != null)
                redisPool.returnJedis(jedis);
        }
    }


    public byte[] getValue(byte[] key) {
        Jedis jedis = null;
        byte[] bytes;
        try {
            jedis = redisPool.getJedis();
            jedis.select(config.getRedisDb());
            bytes = jedis.get(key);
        }catch(Exception e){
            throw new RuntimeException("redis error !!",e);
        }finally {
            if(jedis != null)
                redisPool.returnJedis(jedis);
        }
        return bytes;
    }

    public Set<byte[]> getKeys() {
        Jedis jedis = null;
        Set<byte[]> list;
        try {
            jedis = redisPool.getJedis();
            jedis.select(config.getRedisDb());
            list = jedis.keys((config.getMonitorTopic()+"*").getBytes());
        }catch(Exception e){
            throw new RuntimeException("redis error !!",e);
        }finally {
            if(jedis != null)
                redisPool.returnJedis(jedis);
        }
        return list;
    }
}
