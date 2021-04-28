package com.mit.mission.core.base.service;

import com.mit.mission.core.base.domain.Argument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ArgumentService {
    <S extends Argument> S save(S entity);

    Argument findOneByUuid(String uuid);

    Page<Argument> findAll(Pageable pageable, Map<String, Object> condition, int showAdminArguments);

    void delete(String uuids);

    Argument findOneByName(String name);

    Argument findOneByNameAndUuidNot(String name, String uuid);

    List<Argument> findAll(Map<String, String> condition);

    void update(String id, String value, Integer type);

    void update(String id, String value);
}
