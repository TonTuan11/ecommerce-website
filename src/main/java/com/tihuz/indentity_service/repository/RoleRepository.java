package com.tihuz.indentity_service.repository;

import com.tihuz.indentity_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role,String> {


}
