package com.mit.mission.common.util;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 *  @autor: gaoy
 *  @date: 2021/4/19 15:46
 *  @description: 自动转换类
 */
public class AutoMapper {
    /**
     * 类型转换
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @param <V>
     * @return
     * @throws Exception
     */
    public static <T, V> V mapper(T source, Class<V> targetClass) {
        return mapper(source, targetClass, null);
    }

    /**
     * 默认null不能操作
     *
     * @param source
     * @param targetClass
     * @param handler
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> V mapper(T source, Class<V> targetClass, BiConsumer<T, V> handler) {
        Objects.requireNonNull(source, "source is null");
        Objects.requireNonNull(targetClass, "targetClass is null");
        V target = null;
        try {
            target = targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(source, target);
        if (handler != null) {
            handler.accept(source, target);
        }
        return target;
    }

    /**
     * 转换List VO 同一属性修改
     *
     * @param source
     * @param targetVOClass
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> List<V> mapperList(List<T> source, Class<V> targetVOClass) {
        return mapperList(source, targetVOClass, null);
    }

    /**
     * 转换List VO 不同属性修改
     *
     * @param source
     * @param targetVOClass
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> List<V> mapperList(List<T> source, Class<V> targetVOClass, BiConsumer<T, V> handler) {

        return source.stream().map(item -> mapper(item, targetVOClass, handler)).collect(Collectors.toList());
    }
}
