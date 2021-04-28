package com.mit.mission.core.traffic.repository;

import com.mit.mission.core.traffic.domain.Configuration;
import com.mit.mission.core.traffic.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, String>, JpaSpecificationExecutor<Configuration> {

    List<Configuration> findByLineIdAndDirection(String lineId, String direction);

    List<Configuration> findByStationIdAndDirection(String stationId, String direction);

    List<Configuration> findByStationId(String stationId);

    List<Configuration> findByLineId(String lineId);

    List<Configuration> findAllByLineIdAndStationId(String lineId, String stationId);

    @Modifying
    @Query("DELETE from Configuration c WHERE c.lineId = :lineId")
    void deleteByLineId(@Param("lineId") String lineId);

    @Query(value = "SELECT s.uuid uuid, s.name name, s.nameEn nameEn, s.nameSpell nameSpell, s.capsules capsules, s.createTime createTime, s.updateTime updateTime, s.createUserId createUserId, s.updateUserId updateUserId FROM (select * from pcc_metro_configuration WHERE direction = 1 and lineId = :lineId ORDER BY sort DESC) c LEFT JOIN pcc_metro_station s ON s.uuid = c.stationId", nativeQuery = true)
    List<Object[]> findStationsByLineId(@Param("lineId") String lineId);

    @Query(value = "SELECT s.uuid uuid, s.name name, s.nameEn nameEn, s.nameSpell nameSpell, s.capsules capsules, s.createTime createTime, s.updateTime updateTime, s.createUserId createUserId, s.updateUserId updateUserId FROM (select * from pcc_metro_configuration WHERE direction = 1 and lineId = :lineId ORDER BY sort ASC) c LEFT JOIN pcc_metro_station s ON s.uuid = c.stationId", nativeQuery = true)
    List<Object[]> findStationsByLineASC(@Param("lineId") String lineId);

    @Query("select s from Station s, Configuration c where s.uuid = c.stationId and c.lineId = ?1 and c.direction = ?2")
    List<Station> findStationsByLineIdAndDirection(String lineId, String direction);

}
