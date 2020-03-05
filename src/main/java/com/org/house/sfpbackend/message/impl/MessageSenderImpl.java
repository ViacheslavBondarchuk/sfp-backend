package com.org.house.sfpbackend.message.impl;

import com.org.house.sfpbackend.message.MessageSender;
import org.springframework.stereotype.Component;

@Component
public class MessageSenderImpl implements MessageSender {


    @Override
    public boolean send(Object o) {
        return false;
    }
}
