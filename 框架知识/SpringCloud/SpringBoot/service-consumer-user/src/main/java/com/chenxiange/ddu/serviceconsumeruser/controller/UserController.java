package com.chenxiange.ddu.serviceconsumeruser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.chenxiange.ddu.serviceconsumeruser.entity.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/{id}")
	public User findById(@PathVariable Long id) {
		User user = restTemplate.getForObject("http://localhost:8001/user/" + id, User.class);
		return user;
	}
	
	@GetMapping("/version")
	public String getVersion() {
		return restTemplate.getForObject("http://localhost:8001/user/version", String.class);
	}

}
