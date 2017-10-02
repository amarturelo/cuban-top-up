package com.wirelesskings.wkreload.mail;



import com.wirelesskings.wkreload.mail.settings.Constants;
import com.wirelesskings.wkreload.mail.settings.Setting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender extends Authenticator {

    private InternetAddress mailHost;
    private String user;
    private String password;
    private Session session; //The JavaMail session object
    public Boolean sentStatus;


    public MailSender(Setting setting) throws AddressException {
        this.mailHost       = new InternetAddress(setting.getHost());
        this.user           = setting.getUsername();
        this.password       = setting.getPassword();
        this.sentStatus     = true;
        int serverType      = setting.getServerType();
        int port            = setting.getPort();
        Properties props    = new Properties();

        switch (serverType) {
            case Constants.SMTP_PLAIN: // plain authentication
                // Create properties field
                props.setProperty("mail.host", mailHost.getAddress());
                props.setProperty("mail.transport.protocol", "smtp");
                props.setProperty("mail.smtp.auth", "true");
                props.setProperty("mail.smtp.port", String.valueOf(port));
                // Get session object
                session = Session.getInstance(props, this);
                break;
            case Constants.SMTP_SSL: // ssl Auth
                // Create properties field
                props.setProperty("mail.host", mailHost.getAddress());
                props.setProperty("mail.transport.protocol", "smtp");
                props.setProperty("mail.smtp.auth", "true");
                props.setProperty("mail.smtp.port", String.valueOf(port));
                // SSL setting
                props.setProperty("mail.pop3.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                props.setProperty("mail.pop3.socketFactory.fallback", "false");
                props.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));
                session = Session.getInstance(props, this);
                break;
        }

        session = Session.getInstance(props, this);
    }
    public void closeConecction() {
        try {
            session.getStore().close();
        }
        catch (MessagingException e){

        }
    }
    static {
        Security.addProvider(new JSSEProvider());
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }


    public synchronized void sendMail(String subject, String body, String sender, String recipients)
            throws AddressException,
            MessagingException {
        MimeMessage message = new MimeMessage(session);//The JavaMail message object from the session object
        DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
        message.setFrom(new InternetAddress(sender));
        message.setSender(new InternetAddress(sender));
        message.setSubject(subject);

        GregorianCalendar m = new GregorianCalendar();
        m.setTime(new Date());
        message.setSentDate(m.getTime());

        //message.setDataHandler(handler);
        message.setText(body,"UTF-8");
        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));

        Transport.send(message);
    }

    private class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }

        @Override
        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        @Override
        public String getName() {
            return "ByteArrayDataSource";
        }
    }
}

