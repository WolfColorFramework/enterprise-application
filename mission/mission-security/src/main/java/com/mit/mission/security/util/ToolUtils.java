package com.mit.mission.security.util;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 *  @autor: gaoy
 *  @date: 2021/4/27 17:26
 *  @description: 工具类
 */
public class ToolUtils {
    /**
     * 根据id,parent_id,parentIds获取树型结构数据
     *
     * @param tree
     * @return
     * @throws Exception
     */
    public static List factorTree(List tree) throws Exception {
        if (tree != null) {
            List t_list = new ArrayList();
            Map map = new HashMap();
            for (Object o : tree) {
                Class clazz = o.getClass();
                Field uuid = clazz.getDeclaredField("id");
                Field parentIds = clazz.getDeclaredField("parentIds");
                Field parent_ids = clazz.getDeclaredField("parentIdList");
                if (parentIds != null && parent_ids != null) {
                    if (!parentIds.isAccessible()) {
                        parentIds.setAccessible(true);
                    }
                    String ids = ((String) parentIds.get(o)).replace("[", "").replace("]", "");
                    parentIds.set(o, ids);
                    if (!parent_ids.isAccessible()) {
                        parent_ids.setAccessible(true);
                    }
                    parent_ids.set(o, ids.split(","));
                }
                if (!uuid.isAccessible()) {
                    uuid.setAccessible(true);
                }
                String lId = (String) uuid.get(o);
                map.put(lId, o);
            }

            for (Object o : map.keySet()) {
                String cId = (String) o;
                Object obj = map.get(cId);
                Class clazz = obj.getClass();
                Field pId = clazz.getDeclaredField("parentId");
                if (!pId.isAccessible()) {
                    pId.setAccessible(true);
                }
                String id = (String) pId.get(obj);
                if ("0".equals(id) || map.get(id) == null) {
                    t_list.add(obj);
                } else {
                    Object object = map.get(id);
                    Class clazz1 = object.getClass();
                    Field children = clazz1.getDeclaredField("children");
                    if (!children.isAccessible()) {
                        children.setAccessible(true);
                    }
                    List list = (List) children.get(object);
                    if (CollectionUtils.isEmpty(list)) {
                        list = new ArrayList();
                    }
                    list.add(obj);
                    Collections.sort(list);
                    children.set(object, list);
                }
            }
            Collections.sort(t_list);
            return t_list;
        }
        return null;
    }
}
