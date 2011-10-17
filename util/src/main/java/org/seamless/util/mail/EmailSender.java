/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.seamless.util.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * @author Christian Bauer
 */
public class EmailSender {

    final protected Properties properties = new Properties();
    final protected String host;
    final protected String user;
    final protected String password;

    public EmailSender(String host, String user, String password) {
        if (host == null || host.length() == 0)
            throw new IllegalArgumentException("Host is required");

        this.host = host;
        this.user = user;
        this.password = password;

        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.quitwait", "false");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.starttls.enable", "true");
        if (user != null && password != null) {
            properties.put("mail.smtp.auth", "true");
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void send(Email email) throws MessagingException {
        Session session = createSession();

        MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(email.getSender()));

        InternetAddress[] receipients = {new InternetAddress(email.getRecipient())};
        msg.setRecipients(Message.RecipientType.TO, receipients);

        msg.setSubject(email.getSubject());

        msg.setSentDate(new Date());

        msg.setContent(createContent(email));

        Transport transport = createConnectedTransport(session);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
    }

    protected Multipart createContent(Email email) throws MessagingException {
        MimeBodyPart partOne = new MimeBodyPart();
        partOne.setText(email.getPlaintext());

        Multipart mp = new MimeMultipart("alternative");
        mp.addBodyPart(partOne);

        if (email.getHtml() != null) {
            MimeBodyPart partTwo = new MimeBodyPart();
            partTwo.setContent(email.getHtml(), "text/html");
            mp.addBodyPart(partTwo);
        }
        return mp;
    }

    protected Session createSession() {
        return Session.getInstance(properties, null);
    }

    protected Transport createConnectedTransport(Session session) throws MessagingException {
        Transport transport = session.getTransport("smtp");
        if (user != null && password != null)
            transport.connect(user, password);
        else
            transport.connect();
        return transport;
    }
}
