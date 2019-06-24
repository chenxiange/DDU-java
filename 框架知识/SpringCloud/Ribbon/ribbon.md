Ribbon
======
Spring Cloud Ribbion是一个基于http和tcp的客户端负载均衡工具.


### 单独使用ribbon

1. 复制SpringBoot中基础的消费者服务service-consumer-user, 修改为service-consumer-user-without-eureka.
2. 在pom.xml添加ribbon依赖:
```
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
  </dependency>
```
3. 在application.yml添加以下配置:
```
spring:
  application:
    name: serivce-consumer-user-without-eureka
service-provider-user:
  ribbon:
    listOfServers: localhost:8001,localhost:8002
```
4. 负载均衡注解
```
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```
5. 修改UserController中的http请求路径:
```
@GetMapping("/version")
public String getVersion() {
    return restTemplate.getForObject("http://service-provider-user/user/version", String.class);
}
```
6. 启动服务:
```
java -jar .\service-provider-user-0.0.1-SNAPSHOT.jar --spring.profiles.active=peer1

java -jar .\service-provider-user-0.0.1-SNAPSHOT.jar --spring.profiles.active=peer2

java -jar .\serivce-consumer-user-without-eureka-0.0.1-SNAPSHOT.jar
```
7. 访问http://localhost:8000/user/version, 可以看到返回结果在1.0.0和2.0.0中切换, 请求均匀的分布到了两个结点, ribbon负载均衡生效.

