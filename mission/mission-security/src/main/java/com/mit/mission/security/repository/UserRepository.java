package com.mit.mission.security.repository;

import com.mit.mission.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    User findOneByAccount(String account);

    User findOneByName(String name);

    User findOneByEmail(String email);

    List<User> findByUuidInAndNameLike(List<String> ids, String name);

    List<User> findByUuidIn(List<String> ids);

    List<User> findAllByDepartId(String departId);

}
