package com.example.clund.myredis.lock.generator;

import com.example.clund.myredis.lock.annotation.CacheLock;
import com.example.clund.myredis.lock.annotation.CacheParam;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 通过接口注入的方式去写不同的生成规则
 *
 * @author dashuai
 */
public class LockKeyGenerator implements CacheKeyGenerator {

    @Override
    public String getLockKey(ProceedingJoinPoint pjp) {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock cacheLock = method.getAnnotation(CacheLock.class);
        // 获取参数
        final Object[] args = pjp.getArgs();
        final Parameter[] parameters = method.getParameters();

        StringBuilder sb = new StringBuilder();
        //默认解析方法里面带 CacheParam注解的属性 如果没有尝试解析实体对象

        for (int i = 0; i < parameters.length; i++) {
            final CacheParam cacheParam = parameters[i].getAnnotation(CacheParam.class);
            if (cacheParam == null) {
                continue;
            }
            sb.append(cacheLock.delimiter()).append(args[i]);
        }

        if (StringUtils.isEmpty(sb.toString())) {
            final Annotation[][] parameter = method.getParameterAnnotations();

            for (int i = 0; i < parameter.length; i++) {
                final Object object = args[i];
                final Field[] fileds = object.getClass().getDeclaredFields();
                for (Field field : fileds) {
                    final CacheParam cacheParam = field.getAnnotation(CacheParam.class);

                    if (cacheParam == null) {
                        continue;
                    }
                    field.setAccessible(true);
                    sb.append(cacheLock.delimiter()).append(ReflectionUtils.getField(field, object));
                }

            }
        }

        return cacheLock.prefix() + sb.toString();
    }
}
