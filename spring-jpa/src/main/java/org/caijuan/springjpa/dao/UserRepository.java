package org.caijuan.springjpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<UserDO, Integer>, JpaSpecificationExecutor<UserDO> {

}