package com.org.house.sfpbackend.service.mail;

import javax.mail.MessagingException;

public interface MailService {

    boolean send(final String fullname, final String link, final String... to) throws MessagingException;
}
