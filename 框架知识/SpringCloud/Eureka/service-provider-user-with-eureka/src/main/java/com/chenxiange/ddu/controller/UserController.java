package com.chenxiange.ddu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chenxiange.ddu.dao.UserRepository;
import com.chenxiange.ddu.entity.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Value("${service.version}")
	private String serviceVersion;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DiscoveryClient client;
	
	@GetMapping("/{id}")
	public User findById(@PathVariable Long id) {
		User user = userRepository.getOne(id);
		return user;
	}
	
	@GetMapping("/version")
	public String getVersion() {
		return serviceVersion;
	}
	
	@GetMapping("/service-instance-info")
	public String getServiceInstanceInfo() {
		List<String> services = client.getServices();
		
		String servicesInfo = null;
		if(services != null && services.size() > 0) {
			for(String service: services) {
				servicesInfo = servicesInfo == null ? "[" + service : "," + service;
			}
			servicesInfo = servicesInfo == null ? "" : servicesInfo + "]";
		}
		return servicesInfo;
	}
}
