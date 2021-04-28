package com.mit.mission.core.base.repository;

import com.mit.mission.core.base.domain.Argument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArgumentRepository extends JpaRepository<Argument, String>, JpaSpecificationExecutor<Argument> {
    Argument findOneByName(String name);

    Argument findOneByNameAndUuidNot(String name, String uuid);
}
