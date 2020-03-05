package com.org.house.sfpbackend.service.impl;

import com.org.house.sfpbackend.message.MessageSender;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
public class MessageService {
    private final String TEMPLATE_PATH = "src/main/java/com/org/house/sfpbackend/message/templates/";
    private VelocityEngine velocityEngine;
    private MessageSender messageSender;

    @Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void sendActivateMessage(final HashMap<String, String> parameters) {
        VelocityContext model = new VelocityContext();
        parameters.forEach((k, v) -> {
            model.put(k, v);
        });
        String content = getMessageContent("user-register", model);
        messageSender.send(new Object());
    }

    private String getMessageContent(final String templateName, final VelocityContext model) {
        try (StringWriter stringWriter = new StringWriter()) {
            Template template = velocityEngine.getTemplate(TEMPLATE_PATH + templateName + ".vm", String.valueOf(StandardCharsets.UTF_8));
            template.merge(model, stringWriter);
            return stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
