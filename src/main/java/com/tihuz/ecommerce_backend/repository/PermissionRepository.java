package com.tihuz.ecommerce_backend.repository;

import com.tihuz.ecommerce_backend.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository <Permission,String> {

}
