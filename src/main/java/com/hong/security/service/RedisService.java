package com.hong.security.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hong.security.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @Author: wanghong
 * @Description:
 * @Date: 2020/2/13 17:42
 **/
@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    
    public Long ttl(String key) {
        return stringRedisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    
    public void del(String... key) {
        if (null != key && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
                stringRedisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
                stringRedisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    
    public Integer getInt(String key) {
        String value = stringRedisTemplate.boundValueOps(key).get();
        if (StringUtils.isNotBlank(value)) {
            return Integer.valueOf(value);
        }
        return null;
    }

    
    public String getStr(String key) {
        return stringRedisTemplate.boundValueOps(key).get();
    }

    
    public String getAndSetStr(String key, String value) {
        return stringRedisTemplate.boundValueOps(key).getAndSet(value);
    }


    
    public Boolean setStrNX(String key, String value) {
        return stringRedisTemplate.execute(new SessionCallback<Boolean>() {
            
            public Boolean execute(RedisOperations redisOperations) {
                return redisOperations.boundValueOps(key).setIfAbsent(value);
            }
        });
    }

    
    public String getStr(String key, boolean retain) {
        String value = stringRedisTemplate.boundValueOps(key).get();
        if (!retain) {
            redisTemplate.delete(key);
        }
        return value;
    }

    
    public Object getObj(String key) {
        return redisTemplate.boundValueOps(key).get();
    }

    
    public Object getObj(String key, boolean retain) {
        Object obj = redisTemplate.boundValueOps(key).get();
        if (!retain) {
            redisTemplate.delete(key);
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    
    public <T> T get(String key, Class<T> clazz) {
        return (T) redisTemplate.boundValueOps(key).get();
    }

    
    public <T> T getJson(String key, Class<T> clazz) {
        return JSONObject.parseObject(stringRedisTemplate.boundValueOps(key).get(), clazz);
    }

    
    public void set(String key, Object value, long time) {
        boolean flag = true;
        if (value.getClass().equals(String.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Integer.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Double.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Float.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Short.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Long.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Boolean.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else {
            flag = false;
            redisTemplate.opsForValue().set(key, value);
        }
        if (time > 0) {
            if (flag) {
                stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        }
    }

    
    public void set(String key, Object value) {
        if (value.getClass().equals(String.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Integer.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Double.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Float.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Short.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Long.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else if (value.getClass().equals(Boolean.class)) {
            stringRedisTemplate.opsForValue().set(key, value.toString());
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    
    public void setJson(String key, Object value, int time) {
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));
        if (time > 0) {
            stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    
    public void setJson(String key, Object value) {
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));
    }

    
    public void setJsonField(String key, String field, String value) {
        JSONObject obj = JSON.parseObject(stringRedisTemplate.boundValueOps(key).get());
        obj.put(field, value);
        stringRedisTemplate.opsForValue().set(key, obj.toJSONString());
    }

    
    public Double decr(String key, double by) {
        return redisTemplate.opsForValue().increment(key, -by);
    }

    
    public Double incr(String key, double by) {
        return redisTemplate.opsForValue().increment(key, by);
    }

    
    public Double getDouble(String key) {
        String value = stringRedisTemplate.boundValueOps(key).get();
        if (StringUtils.isNoneBlank(value)) {
            return Double.valueOf(value);
        }
        return 0d;
    }

    
    public void setDouble(String key, double value, int time) {
        stringRedisTemplate.boundValueOps(key).get();
        if (time > 0) {
            stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    
    public void setDouble(String key, double value) {
        stringRedisTemplate.boundValueOps(key).get();
    }

    
    public void setInt(String key, int value, int time) {
        stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
        if (time > 0) {
            stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    
    public void setInt(String key, int value) {
        stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
    }

    
    public <T> void setMap(String key, Map<String, T> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    
    public void setMap(String key, Map<String, Object> map, int time) {
        redisTemplate.opsForHash().putAll(key, map);
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    
    public <T> void setMap(String key, T obj) {
        Map<String, String> map = (Map<String, String>) JSON.parseObject(JSON.toJSONString(obj), Map.class);
        redisTemplate.opsForHash().putAll(key, map);
    }

    
    public <T> void addMap(String key, Map<String, T> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    
    public void addMap(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    
    public <T> void addMap(String key, String field, T obj) {
        redisTemplate.opsForHash().put(key, field, obj);
    }

    
    public <T> Map<String, T> mget(String key, Class<T> clazz) {
        BoundHashOperations<String, String, T> boundHashOps = redisTemplate.boundHashOps(key);
        return boundHashOps.entries();
    }

    
    public Map<String, Object> getMap(String key) {
        BoundHashOperations<String, String, Object> boundHashOps = redisTemplate.boundHashOps(key);
        return boundHashOps.entries();
    }

    @SuppressWarnings("unchecked")
    
    public <T> T getMapField(String key, String field, Class<T> clazz) {
        return (T) redisTemplate.boundHashOps(key).get(field);
    }

    
    public <T> List<T> getMapLon(String key, String field, Class clazz) {

        return redisTemplate.execute(new SessionCallback<List<T>>() {
            
            public List<T> execute(RedisOperations redisOperations) {
                return redisOperations.boundHashOps(key).multiGet(Arrays.asList(new String[]{field}));
            }
        });
    }

    
    public void delMapField(String key, String... field) {
        BoundHashOperations<String, String, ?> boundHashOps = redisTemplate.boundHashOps(key);
        boundHashOps.delete(field);
    }

    
    public void expire(String key, int time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    
    public void expireSecond(String key, long time) {
        if (time >= 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    
    public void expireMsec(String key, long time) {
        if (time >= 0) {
            redisTemplate.expire(key, time, TimeUnit.MILLISECONDS);
        }
    }

    
    public void strSadd(String key, String... value) {
        this.stringRedisTemplate.boundSetOps(key).add(value);
    }

    
    public Set<String> smembers(String key) {
        return this.stringRedisTemplate.boundSetOps(key).members();
    }

    
    public void sadd(String key, String... value) {
        redisTemplate.boundSetOps(key).add(value);
    }

    
    public void srem(String key, String... value) {
        redisTemplate.boundSetOps(key).remove(value);
    }

    
    public void srename(String oldkey, String newkey) {
        redisTemplate.boundSetOps(oldkey).rename(newkey);
    }

    
    public Boolean sisMember(String key, String value) {
        return this.stringRedisTemplate.opsForSet().isMember(key, value);
    }

    
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    
    public Boolean zAdd(String key, double score, String value) {

        Boolean isSuccess = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            
            public Boolean doInRedis(RedisConnection connection) {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
                Boolean set = stringRedisConnection.zAdd(key, score, value);
                return set;
            }
        });
        return isSuccess == null ? false : isSuccess;
    }

    
    public Set<String> zRange(String key, long start, long end) {

        Set<String> setStr = stringRedisTemplate.execute(new RedisCallback<Set<String>>() {
            
            public Set<String> doInRedis(RedisConnection connection) {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
                Set<String> set = stringRedisConnection.zRange(key, start, end);
                return set;
            }
        });
        return setStr;
    }

    
    public Long zCard(String key) {

        Long count = stringRedisTemplate.execute(new RedisCallback<Long>() {
            
            public Long doInRedis(RedisConnection connection) {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
                Long count = stringRedisConnection.zCard(key);
                return count;
            }
        });
        return count;
    }

    
    public Set<String> zRevRange(String key, long start, long end) {
        Set<String> setStr = stringRedisTemplate.execute(new RedisCallback<Set<String>>() {
            
            public Set<String> doInRedis(RedisConnection connection) {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
                Set<String> set = stringRedisConnection.zRevRange(key, start, end);
                return set;
            }
        });
        return setStr;
    }

    
    public Set<String> zRangeByScore(String key, double min, double max) {
        Set<String> setStr = stringRedisTemplate.execute(new RedisCallback<Set<String>>() {
            
            public Set<String> doInRedis(RedisConnection connection) {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
                Set<String> set = stringRedisConnection.zRangeByScore(key, min, max);
                return set;
            }
        });
        return setStr;
    }

    
    public Set<String> zRevRangeByScore(String key, double min, double max) {
        Set<String> setStr = stringRedisTemplate.execute(new RedisCallback<Set<String>>() {
            
            public Set<String> doInRedis(RedisConnection connection) {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
                Set<String> set = stringRedisConnection.zRevRangeByScore(key, min, max);
                return set;
            }
        });
        return setStr;
    }

    
    public List<Long> hMGetLon(String key, List<String> fields) {

        return redisTemplate.execute(new SessionCallback<List<Long>>() {
            
            public List<Long> execute(RedisOperations redisOperations) {
                return redisOperations.boundHashOps(key).multiGet(fields);
            }
        });
    }

    
    public List hMGet(String key, List fields) {
        return redisTemplate.execute(new SessionCallback<List<Object>>() {
            
            public List<Object> execute(RedisOperations redisOperations) {
                return redisOperations.boundHashOps(key).multiGet(fields);
            }
        });
    }

    
    public void hMSet(String key, Map map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    
    public List<String> keysByScan(String pattern) {
        List<String> keys = new ArrayList<>();
        scan(pattern, item -> {
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);
        });
        return keys;
    }

    /**
     * 使用scan命令替代keys命令，推荐使用
     *
     * @param pattern
     * @param consumer
     */
    private void scan(String pattern, Consumer<byte[]> consumer) {
        redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(
                    ScanOptions.scanOptions().count(Long.MAX_VALUE)
                            .match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                throw new BusinessException("Redis scan command error:" + e.getMessage(), e);
            }
        });
    }

}
