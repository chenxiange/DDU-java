package com.chenxiange.ddu.serviceprovideruser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chenxiange.ddu.serviceprovideruser.dao.UserRepository;
import com.chenxiange.ddu.serviceprovideruser.entity.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Value("${service.version}")
	private String serviceVersion;

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/{id}")
	public User findById(@PathVariable Long id) {
		User user = userRepository.getOne(id);
		return user;
	}
	
	@GetMapping("/version")
	public String getVersion() {
		return serviceVersion;
	}
}
