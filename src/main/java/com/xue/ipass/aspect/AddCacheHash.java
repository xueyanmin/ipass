package com.xue.ipass.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.Set;

@Configuration
@Aspect

/**基于sptingaop+redis做通用缓存*/
public class AddCacheHash {

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;


    /**添加缓存 切入点(通知+切面)*/
    //@Around("@annotation(com.xue.ipass.annotation.AddCache)")
    public Object addCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("环绕通知");

        //序列化解决乱码
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        StringBuilder sb = new StringBuilder();

        //key：方法名:全局唯一 value 类型
        //key 类的全线命名+方法名+参数名(实参)
        //value 放的是缓存的数据 String
        //key:String value:String 类
        /*大Key          Hash(小key,value)_
        * 类的全线命名    方法名+参数(实参)=数据
        *                1方法 数据
        *                2方法 数据
        *                ........
        * */

        //获取类的权限命名
        String className = proceedingJoinPoint.getTarget().getClass().getName();

        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        sb.append(methodName);
        //获取参数
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            sb.append(arg);
        }
        //拼接 小key
        String key = sb.toString();

        //取出key
        Boolean aBoolean = redisTemplate.opsForHash().hasKey(className, key);

        HashOperations hashOperations = redisTemplate.opsForHash();


        Object result = null;

        //去redis中判断kry是否存在
        if (aBoolean){
            //存在说明缓存中有数据 直接取出数据
          result = hashOperations.get(className,key);
        }else {
            //如果不存在 缓存中没有 放行方法 得到结果
            //通过切面拿到返回结果
           result = proceedingJoinPoint.proceed();

           //拿到结果 加入缓存
            hashOperations.put(className,key,result);
        }
         return result;
    }

    /**清空缓存：通过切面,不包含所有类下查询方法*/
    //@After("@annotation(com.xue.ipass.annotation.DelCache)")
    public void delCache(JoinPoint joinPoint){

        //清空缓存

        //获取类的全限命名 清空该类下的所有缓存
        String className = joinPoint.getTarget().getClass().getName();

        //删除该类下所有的数据
        redisTemplate.delete(className);
    }
}
