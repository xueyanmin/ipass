package com.xue.ipass.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.Set;

@Configuration
@Aspect

/**基于sptingaop做通用缓存*/
public class AddCache {

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;


    /**添加缓存 切入点(通知+切面)*/
    //@Around("execution(* com.xue.ipass.serviceImpl.*.query*(..))")
    public Object addCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("环绕通知");

        //序列化解决乱码
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        StringBuilder sb = new StringBuilder();
        //key：方法名:全局唯一 value 类型

        //key 类的全线命名+方法名+参数名(实参)
        //小key 放的是缓存的数据 String


        //获取类的权限命名
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        sb.append(className);

        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        sb.append(methodName);

        //获取参数
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            sb.append(arg);
        }
        //拼接
       String key = sb.toString();

        //取出key
        Boolean aBoolean = stringRedisTemplate.hasKey(key);


        ValueOperations valueOperations = redisTemplate.opsForValue();

        Object result = null;

        //去redis中判断kry是否存在
        if (aBoolean){

            //存在说明缓存中有数据 直接取出数据
          result = valueOperations.get(key);
        }else {

            //如果不存在 缓存中没有 放行方法 得到结果
            //通过切面拿到返回结果
           result = proceedingJoinPoint.proceed();

           //拿到结果 加入缓存
            valueOperations.set(key,result);
        }
         return result;
    }

    /**清空缓存：通过切面,不包含所有类下查询方法*/
    //@After("execution(* com.xue.ipass.serviceImpl.*.*(..))&& !execution(* com.xue.ipass.serviceImpl.*.query*(..))")
    public void delCache(JoinPoint joinPoint){

        /**在对数据进行操作之后-->清空缓存-->从新查询,-->添加缓存*/

        //获取类的全限命名 清空该类下的所有方法
        String className = joinPoint.getTarget().getClass().getName();

        //获取到所有的 key
        Set<String> keys = stringRedisTemplate.keys("*");

        for (String key : keys) {

            //判断key是以className开头，全部删掉
            if (key.contains(className)){

                //删除key
                stringRedisTemplate.delete(key);
            }

        }
    }
}
