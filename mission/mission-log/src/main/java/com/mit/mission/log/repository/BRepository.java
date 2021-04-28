package com.mit.mission.log.repository;

import com.mit.mission.log.domain.B;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BRepository extends JpaRepository<B, String>, JpaSpecificationExecutor<B> {
}
