package com.mit.mission.core.base.service.impl;

import com.mit.mission.core.base.domain.Argument;
import com.mit.mission.core.base.repository.ArgumentRepository;
import com.mit.mission.core.base.service.ArgumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ArgmentServiceImpl implements ArgumentService {

    @Autowired
    private ArgumentRepository argumentRepository;

    @Override
    public <S extends Argument> S save(S entity) {
        return argumentRepository.save(entity);
    }

    @Override
    public Argument findOneByUuid(String uuid) {
        return argumentRepository.getOne(uuid);
    }

    @Override
    public List<Argument> findAll(Map<String, String> condition) {
        return argumentRepository.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();

            if (null != condition) {
                if (!StringUtils.isEmpty(condition.get("name"))) {
                    list.add(cb.like(root.get("name").as(String.class), "%" + condition.get("name").replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                if (!StringUtils.isEmpty(condition.get("value"))) {
                    list.add(cb.like(root.get("value").as(String.class), "%" + condition.get("value").replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                Predicate or = cb.equal(root.get("type").as(String.class), Integer.valueOf(0));
                if (!StringUtils.isEmpty(condition.get("type")) && (Integer.valueOf(condition.get("type")) != -1)) {
                    or = cb.or(or, cb.equal(root.get("type").as(Integer.class), Integer.valueOf(condition.get("type"))));
                    list.add(or);
                } else {
                    list.add(or);
                }
                if (!StringUtils.isEmpty(condition.get("remark"))) {
                    list.add(cb.like(root.get("remark").as(String.class), "%" + condition.get("remark").replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                if (!StringUtils.isEmpty(condition.get("beginTime"))) {
                    list.add(cb.greaterThanOrEqualTo(root.get("updateTime").as(String.class), condition.get("beginTime") + " 00:00:00"));
                }
                if (!StringUtils.isEmpty(condition.get("endTime"))) {
                    list.add(cb.lessThanOrEqualTo(root.get("updateTime").as(String.class), condition.get("endTime") + " 23:59:59"));
                }
            }
            Predicate[] pre = new Predicate[list.size()];
            return query.where(list.toArray(pre)).getRestriction();
        });
    }

    @Override
    public Page<Argument> findAll(Pageable pageable, Map<String, Object> condition, int type) {
        return argumentRepository.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();

            if (null != condition) {
                if (!StringUtils.isEmpty(condition.get("name"))) {
                    list.add(cb.like(root.get("name").as(String.class), "%" + ((String) condition.get("name")).replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                if (!StringUtils.isEmpty(condition.get("value"))) {
                    list.add(cb.like(root.get("value").as(String.class), "%" + ((String) condition.get("value")).replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                if (!StringUtils.isEmpty(condition.get("remark"))) {
                    list.add(cb.like(root.get("remark").as(String.class), "%" + ((String) condition.get("remark")).replace("_", "\\_").replace("%", "\\%") + "%"));
                }
                if (!StringUtils.isEmpty(condition.get("beginTime"))) {
                    list.add(cb.greaterThanOrEqualTo(root.get("updateTime").as(String.class), condition.get("beginTime") + " 00:00:00"));
                }
                if (!StringUtils.isEmpty(condition.get("endTime"))) {
                    list.add(cb.lessThanOrEqualTo(root.get("updateTime").as(String.class), condition.get("endTime") + " 23:59:59"));
                }
            }
            if (type != -1) {
                list.add(cb.equal(root.get("type").as(Integer.class), type));
            }
            Predicate[] pre = new Predicate[list.size()];
            return query.where(list.toArray(pre)).getRestriction();
        }, pageable);
    }

    @Override
    public void delete(String uuids) {
        String[] ids = uuids.split(",");
        for (String uuid : ids) {
            argumentRepository.deleteById(uuid);
        }
    }

    @Override
    public Argument findOneByName(String name) {
        return argumentRepository.findOneByName(name);
    }

    @Override
    public Argument findOneByNameAndUuidNot(String name, String uuid) {
        return argumentRepository.findOneByNameAndUuidNot(name, uuid);
    }

    @Override
    public void update(String id, String value, Integer type) {
        Argument argument = argumentRepository.getOne(id);
        if (argument != null) {
            argument.setValue(value);
            argument.setType(type);
            argumentRepository.save(argument);
        }
    }

    @Override
    public void update(String id, String value) {
        Argument argument = argumentRepository.getOne(id);
        if (argument != null) {
            argument.setValue(value);
            argumentRepository.save(argument);
        }
    }

}
