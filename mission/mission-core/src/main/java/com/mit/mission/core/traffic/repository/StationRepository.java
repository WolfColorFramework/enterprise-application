package com.mit.mission.core.traffic.repository;

import com.mit.mission.core.traffic.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, String>, JpaSpecificationExecutor<Station> {
    Station findOneByName(String name);

    List<Station> findAllByNameLike(String name);

    @Query("from Station s order by s.updateTime desc")
    List<Station> findAllOrderByUpdateTimeDesc();

    List<Station> findAllByUuidIsIn(List<String> ids);
}
