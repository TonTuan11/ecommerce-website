package com.tihuz.ecommerce_backend.repository;

import com.tihuz.ecommerce_backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {


}
