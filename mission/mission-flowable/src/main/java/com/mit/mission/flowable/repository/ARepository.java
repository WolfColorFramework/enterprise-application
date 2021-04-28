package com.mit.mission.flowable.repository;

import com.mit.mission.flowable.domain.A;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ARepository extends JpaRepository<A, String>, JpaSpecificationExecutor<A> {
}
