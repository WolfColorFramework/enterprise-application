package com.mit.mission.security.util;

import com.mit.mission.security.enums.MenuStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
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
                PropertyDescriptor uuid = BeanUtils.getPropertyDescriptor(clazz, "uuid");
                PropertyDescriptor parentIds = BeanUtils.getPropertyDescriptor(clazz, "parentIds");
                PropertyDescriptor parent_ids = BeanUtils.getPropertyDescriptor(clazz, "parentIdList");

                if (parentIds != null && parent_ids != null) {
                    String ids = ((String) parentIds.getReadMethod().invoke(o)).replace("[", "").replace("]", "");
                    parentIds.getWriteMethod().invoke(o, ids);
                    parent_ids.getWriteMethod().invoke(o, ids.split(","));
                }
                String lId = uuid.getReadMethod().invoke(o).toString();
                map.put(lId, o);
            }

            for (Object o : map.keySet()) {
                String cId = (String) o;
                Object obj = map.get(cId);
                Class clazz = obj.getClass();
                PropertyDescriptor pId = BeanUtils.getPropertyDescriptor(clazz, "parentId");
                String id = pId.getReadMethod().invoke(obj).toString();
                if ("0".equals(id) || map.get(id) == null) {
                    t_list.add(obj);
                } else {
                    Object object = map.get(id);
                    Class clazz1 = object.getClass();
                    PropertyDescriptor children = BeanUtils.getPropertyDescriptor(clazz, "children");
                    List list = (List) children.getReadMethod().invoke(object);
                    if (CollectionUtils.isEmpty(list)) {
                        list = new ArrayList();
                    }
                    list.add(obj);
                    Collections.sort(list);
                    children.getWriteMethod().invoke(object, list);
                }
            }
            Collections.sort(t_list);
            return t_list;
        }
        return null;
    }

    /**
     * 根据id,parentId获取树型结构数据
     *
     * @param tree
     * @return
     * @throws Exception
     */
    public static List factorTree2(List tree, Boolean isStatus) throws Exception {
        if (!CollectionUtils.isEmpty(tree)) {
            List t_list = new ArrayList();
            Map map = new HashMap();
            for (Object o : tree) {
                Class clazz = o.getClass();
                PropertyDescriptor uuid = BeanUtils.getPropertyDescriptor(clazz, "uuid");
                String lId = uuid.getReadMethod().invoke(o).toString();
                map.put(lId, o);
            }

            for (Object o : map.keySet()) {
                String cId = (String) o;
                Object obj = map.get(cId);
                Class clazz = obj.getClass();
                PropertyDescriptor pId = BeanUtils.getPropertyDescriptor(clazz, "parentId");
                boolean state = true;
                if (isStatus != null && isStatus) {
                    PropertyDescriptor status = BeanUtils.getPropertyDescriptor(clazz, "status");
                    state = ((Integer) status.getReadMethod().invoke(obj) == MenuStatus.ENABLE.getCode());
                }
                String id = pId.getReadMethod().invoke(obj).toString();
                if ("0".equals(id) || map.get(id) == null) {
                    if (state) {
                        t_list.add(obj);
                    }
                } else {
                    Object object = map.get(id);
                    Class clazz1 = object.getClass();
                    PropertyDescriptor children = BeanUtils.getPropertyDescriptor(clazz1, "children");
                    List list = (List) children.getReadMethod().invoke(object);
                    if (CollectionUtils.isEmpty(list)) {
                        list = new ArrayList();
                    }
                    if (state) {
                        list.add(obj);
                    }
                    Collections.sort(list);
                    children.getWriteMethod().invoke(object, list);
                }
            }
            Collections.sort(t_list);
            return t_list;
        }
        return null;
    }

}
