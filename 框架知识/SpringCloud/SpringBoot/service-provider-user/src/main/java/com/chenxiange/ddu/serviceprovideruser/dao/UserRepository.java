package com.chenxiange.ddu.serviceprovideruser.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chenxiange.ddu.serviceprovideruser.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
