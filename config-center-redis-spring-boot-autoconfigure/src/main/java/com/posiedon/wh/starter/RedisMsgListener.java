package com.posiedon.wh.starter;

import com.posiedon.wh.scope.RefreshConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;


public class RedisMsgListener implements MessageListener {
    private final RefreshConfig refreshConfig;

    public RedisMsgListener(RefreshConfig refreshConfig) {
        this.refreshConfig = refreshConfig;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("===refresh config from redis===");
        refreshConfig.refresh();
    }
}
