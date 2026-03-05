package com.tihuz.indentity_service.repository;

import com.tihuz.indentity_service.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;


@Repository
public interface PermissionRepository extends JpaRepository <Permission,String> {



}
