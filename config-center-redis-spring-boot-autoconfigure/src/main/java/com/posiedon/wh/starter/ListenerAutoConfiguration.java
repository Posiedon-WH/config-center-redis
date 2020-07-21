package com.posiedon.wh.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(name = {"redisConnectionFactory","refreshConfig"})
@AutoConfigureAfter(RedisAutoConfigure.class)
public class ListenerAutoConfiguration {

    @Bean
    ConfigCenterRedisStater configCenterRedisStater(){
        return new ConfigCenterRedisStater();
    }
}
