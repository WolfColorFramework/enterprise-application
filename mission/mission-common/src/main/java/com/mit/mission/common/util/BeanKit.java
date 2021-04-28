package com.mit.mission.common.util;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.mit.mission.common.exception.CustomException;
import com.mit.mission.common.exception.CustomExceptionType;
import org.springframework.beans.BeanUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanKit {

    /**
     * 对象转Map
     *
     * @param bean bean对象
     * @return Map
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        return beanToMap(bean, false);
    }

    /**
     * 对象转Map
     *
     * @param bean              bean对象
     * @param isToUnderlineCase 是否转换为下划线模式
     * @return Map
     */
    public static <T> Map<String, Object> beanToMap(T bean, boolean isToUnderlineCase) {

        if (bean == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            final PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(bean.getClass());
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(bean);
                    if (null != value) {
                        map.put(isToUnderlineCase ? StrKit.toUnderlineCase(key) : key, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR);
        }
        return map;
    }

}
