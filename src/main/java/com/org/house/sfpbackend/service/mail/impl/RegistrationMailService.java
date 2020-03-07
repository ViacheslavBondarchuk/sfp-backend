package com.org.house.sfpbackend.service.mail.impl;

import com.org.house.sfpbackend.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Component
public class RegistrationMailService implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mail.settings.from}")
    private String from;


    @Override
    public boolean send(String fullname, String link, String... to) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setContent(String.format(
                "Dear %s,please follow by this link that <a href=\"%s\">activate</a> your account ", fullname, link), "text/html; charset=utf8");
        mimeMessage.setSentDate(new Date());
        mimeMessage.setRecipients(Message.RecipientType.TO, to[0]);
        mimeMessage.setFrom(from);
        try {
            new Thread(() -> {
                javaMailSender.send(mimeMessage);
            }).start();
            return true;
        } catch (MailException e) {
            e.printStackTrace();
        }
        return false;
    }
}
