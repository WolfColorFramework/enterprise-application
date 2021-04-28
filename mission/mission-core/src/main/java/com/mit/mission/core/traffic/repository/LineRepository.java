package com.mit.mission.core.traffic.repository;

import com.mit.mission.core.traffic.domain.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineRepository extends JpaRepository<Line, String>, JpaSpecificationExecutor<Line> {
    List<Line> findByCodeOrName(String code, String Name);

    @Query("from Line n order by n.updateTime desc")
    List<Line> findAllOrderByUpdateTimeDesc();

    @Query("from Line n order by n.name asc")
    List<Line> findAllOrderByNameAsc();

    List<Line> findAllByNumAndTypeIn(Integer num, List<Integer> types);

    List<Line> findAllByTypeIn(List<Integer> types);

    List<Line> findAllByParentId(String parentId);

    @Query(value = "select max(num)  from pcc_metro_line", nativeQuery = true)
    Integer findMaxNum();
}
