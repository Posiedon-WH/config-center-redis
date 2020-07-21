# config-center-redis

**基于redis分布式配置中心**

常见的配置中心有spring-cloud、dubbo、zk、nacos等，都需要引入第三方中间件，如果项目比较小，只是为了实现配置中心而引入中间件，有点得不偿失，增加维护成本；redis在大多数引用中都有使用，基于这一背景产生了此项目。

通过redis hash,PubSub实现动态更新@Vaule,Environment等配置属性值功能。

## 项目介绍

采用spring-boot-stater方式接入，实现了低耦合，便于开发管理。

- **config-center-redis-spring-boot-starter**
- **config-center-redis-spring-boot-autoconfigure**

### 使用指南

1. 将项目打包安装到本地仓库

   install **config-center-redis-spring-boot-autoconfigure**

   install **config-center-redis-spring-boot-starter**

2. 加入依赖pom.xml 

   ```pom.xml
   <dependency>
      <groupId>com.posiedon.wh</groupId>
      <artifactId>config-center-redis-spring-boot-starter</artifactId>
      <version>0.1.0</version>
   </dependency>
   <dependency>
   	<groupId>org.springframework.boot</groupId>
   	<artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```

3. 属性注入@Value方式

   需要加入注解**@Scope("RefreshConfig")**

   ```java
   @Scope("RefreshConfig")
   @RestController
   public class ConfigController {
   
       @Value("${xx.name}")
       private String name;
   
       @Autowired
       Environment environment;
   
       @RequestMapping("config")
       public String config(){
           String re="====redis.name.@Value=="+name+" @environment==="+environment.getProperty("xx.name");
           return re;
       }
   }
   ```

4. 开启redis配置中心

   ```application.yml
   redis:
     config:
       enable: true
   ```

5. redis中配置主键

   默认为应用名称+profile.active>>>helloclient-dev

   ```application.yml
   spring:
     application:
       name: HelloClient
     profiles:
       active: dev
   ```

6. 动态更新命名

   ```redis
   publish helloclient xx
   ```

   