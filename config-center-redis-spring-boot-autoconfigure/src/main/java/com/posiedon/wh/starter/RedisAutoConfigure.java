package com.posiedon.wh.starter;

import com.posiedon.wh.scope.RefreshConfig;
import com.posiedon.wh.scope.RefreshConfigScopeRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;



@ConditionalOnProperty(prefix = "redis.config", name = "enable", havingValue = "true")
@Configuration
public class RedisAutoConfigure {


    @Bean("lettuceConnectionFactory")
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    @ConfigurationProperties(prefix = "spring.redis.host")
    public LettuceConnectionFactory createLettuceConnectionFactory(RedisProperties properties) {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setPort(properties.getPort());
        standaloneConfiguration.setHostName(properties.getHost());
        standaloneConfiguration.setPassword(properties.getPassword());
        standaloneConfiguration.setDatabase(properties.getDatabase());
        return new LettuceConnectionFactory(standaloneConfiguration);
    }

    @Primary
    @Bean("redisConnectionFactory")
    @Autowired
    public RedisConnectionFactory createRedisConnectionFactory(@Qualifier("lettuceConnectionFactory") LettuceConnectionFactory factory) {
        return factory;
    }

    @Bean
    public RefreshConfigScopeRegistry refreshConfigScopeRegistry() {
        return new RefreshConfigScopeRegistry();
    }

    @Bean("refreshConfig")
    @ConditionalOnBean(RefreshConfigScopeRegistry.class)
    public RefreshConfig refreshConfig() {
        return new RefreshConfig();
    }


    @Bean
    @ConditionalOnBean({RefreshConfig.class})
    RedisMessageListenerContainer redisMessageListenerContainer() {
        return new RedisMessageListenerContainer();
    }

}
