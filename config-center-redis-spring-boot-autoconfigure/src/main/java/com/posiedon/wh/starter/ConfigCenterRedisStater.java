package com.posiedon.wh.starter;


import com.posiedon.wh.scope.RefreshConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class ConfigCenterRedisStater {

    @Autowired
    RefreshConfig refreshConfig;

    @Autowired
    Environment environment;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    RedisMessageListenerContainer messageListenerContainer;

    @PostConstruct
    public void init(){
        messageListenerContainer.setConnectionFactory(redisConnectionFactory);
        messageListenerContainer.addMessageListener(new RedisMsgListener(refreshConfig),topicList());
    }


    List<PatternTopic> topicList() {
        List<PatternTopic> topics = new ArrayList<>();
//        Environment environment = refreshConfig.getEnvironment();
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
