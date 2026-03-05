package com.tihuz.indentity_service.repository;

import com.tihuz.indentity_service.entity.Role;
import com.tihuz.indentity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
  //  User findByUsername(String username);



}
