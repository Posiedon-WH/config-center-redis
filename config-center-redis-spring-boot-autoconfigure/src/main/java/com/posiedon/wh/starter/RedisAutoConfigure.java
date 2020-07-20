package com.posiedon.wh.starter;

import com.posiedon.wh.scope.RefreshConfig;
import com.posiedon.wh.scope.RefreshConfigScopeRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.ArrayList;
import java.util.List;
@ConditionalOnProperty(prefix = "redis.config",name = "enable",havingValue = "true")
@Configuration
public class RedisAutoConfigure {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

//    @Autowired
//    RedisMsgListener redisMsgListener;
    @Autowired
    RefreshConfig refreshConfig;


    @Autowired
    Environment environment;

    @Bean
    public RefreshConfigScopeRegistry refreshConfigScopeRegistry(){
       return new RefreshConfigScopeRegistry();
    }

    @Bean
    @ConditionalOnBean(RefreshConfigScopeRegistry.class)
    public RefreshConfig refreshConfig(){
        return new RefreshConfig();
    }

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
//    @Autowired
    public RedisConnectionFactory createRedisConnectionFactory(RedisConnectionFactory factory){
        return factory;
    }

    @Bean
    @ConditionalOnBean({RefreshConfig.class})
    RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        RedisConnectionFactory redisConnectionFactory = new RedisConnectionFactory();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(redisMsgListener(), topicList());
        return container;
    }


    RedisMsgListener redisMsgListener(){
       return new RedisMsgListener(refreshConfig);
    }

    List<PatternTopic> topicList() {
        List<PatternTopic> topics = new ArrayList<>();
        String appName = environment.getProperty("spring.application.name");
        String profile = environment.getProperty("spring.profiles.active");
        if (appName == null || "".equals(appName)) {
            topics.add(new PatternTopic("application*"));
            topics.add(new PatternTopic("bootstrap*"));
        } else {
            if (profile == null || "".equals(profile)) {
                topics.add(new PatternTopic(appName + "*"));
                topics.add(new PatternTopic(appName.toLowerCase() + "*"));
            } else {
                topics.add(new PatternTopic(appName + "-" + profile + "*"));
                topics.add(new PatternTopic(appName.toLowerCase() + "-" + profile + "*"));
            }
        }
        return topics;
    }
}
