package com.chenxiange.ddu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chenxiange.ddu.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
