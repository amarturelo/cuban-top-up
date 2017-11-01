package com.wirelesskings.wkreload.mailmiddleware.mail;

import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;
import com.wirelesskings.wkreload.mailmiddleware.mail.model.Email;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Constants;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.SearchTerm;


public class MailFetcher {

    InternetAddress host;
    String storeType;
    String user;
    String password;
    Session session;
    int serverType;
    private Store store;


    public MailFetcher(Setting setting) throws AddressException {
        this.host = new InternetAddress(setting.getHost());
        this.user = setting.getUsername();
        this.password = setting.getPassword();
        this.serverType = setting.getServerType();
        int port = setting.getPort();

        Properties properties = new Properties();

        try {
            switch (serverType) {
                case Constants.POP_PLAIN: // plain authentication
                    // Create properties field
                    properties.put("mail.store.protocol", Constants.POP_STORE_TYPE);
                    properties.put("mail.pop3.host", this.host.getAddress());
                    properties.put("mail.pop3.port", port);

                    // Get session object
                    session = Session.getInstance(properties);
                    storeType = "pop3";
                    break;
                case Constants.POP_SSL: // ssl Auth

                    properties.put("mail.store.protocol", Constants.POP_STORE_TYPE);
                    properties.put("mail.pop3.host", this.host.getAddress());
                    properties.put("mail.pop3.port", port);
                    properties.put("mail.pop3.starttls.enable", "true");

                    session = Session.getInstance(properties);
                    storeType = "pop3";
                    break;
                case Constants.IMAP_PLAIN:

                    properties.put("mail.store.protocol", Constants.IMAP_STORE_TYPE);
                    properties.put("mail.imap.host", this.host.getAddress());
                    properties.put("mail.imap.port", port);
                    properties.put("mail.imap.partialfetch", "false");

                    session = Session.getDefaultInstance(properties);
                    storeType = "imap";
                    break;
                case Constants.IMAP_SSL:

                    properties.put("mail.store.protocol", Constants.IMAP_STORE_TYPE);
                    properties.put("mail.imaps.host", this.host.getAddress());
                    properties.put("mail.imaps.port", port);
                    properties.put("mail.imap.partialfetch", "false");

                    session = Session.getDefaultInstance(properties);
                    storeType = "imaps"; // this set enabled ssl auth
                    break;
            }

            store = session.getStore(storeType);

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void closeConecction() {
        try {
            store.close();
        } catch (MessagingException e) {

        }
    }

    public ArrayList<Email> fetchMail(String criteria) throws IOException, MessagingException {

        ArrayList<Email> Emails = new ArrayList<Email>();

        store.connect(this.host.getAddress(), this.user, this.password);

        // create the folder object and open it
        Folder inboxFolder;
        if (serverType == Constants.IMAP_PLAIN || serverType == Constants.IMAP_SSL) {
            IMAPStore imapStore = (IMAPStore) store;
            inboxFolder = imapStore.getFolder("INBOX");
        } else {
            inboxFolder = store.getFolder("INBOX");
        }
        inboxFolder.open(Folder.READ_WRITE);


//        String android_id = criteria1;
//        String app_id = criteria2;

        // creates a search criterion
        final String keyword = String.format("%s", criteria);
        SearchTerm searchCondition = new SearchTerm() {
            @Override
            public boolean match(Message message) {
                try {
                    if (message.getSubject() != null && ((IMAPMessage) message).getSender().toString().equals(keyword)) {
                        return true;
                    }
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        };

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // performs search through the folder
        Message[] messages = inboxFolder.search(searchCondition);

        /*
        FetchProfile fetchProfile = new FetchProfile();
        fetchProfile.add(FetchProfile.Item.ENVELOPE);
        inboxFolder.fetch(messages, fetchProfile);*/

        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];
            Address[] a;
            String asunto = message.getSubject();

            // FROM
            String from = "";
            if ((a = message.getFrom()) != null) {
                for (int j = 0; j < a.length; j++)
                    from += a[j].toString();
            }

            // TO
            String to = "";
            if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
                for (int j = 0; j < a.length; j++)
                    to += a[j].toString();
            }

            String contentType = message.getContentType().toLowerCase();
            String messageContent = "";
            if (contentType.contains("multipart")) {
                Multipart multiPart = (Multipart) message.getContent();
                for (int partCount = 0; partCount < multiPart.getCount(); partCount++) {
                    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                    if (part.getContentType().contains("multipart/alternative")) {
                        Multipart multiPart1 = (Multipart) part.getContent();
                        for (int parte = 0; i < multiPart1.getCount(); parte++) {
                            MimeBodyPart part1 = (MimeBodyPart) multiPart1.getBodyPart(parte);
                            if (part1.getContentType().contains("text/plain")) {
                                // this part may be the message content
                                messageContent = part1.getContent().toString();
                                break;
                            }
                        }
                    } else if (part.getContentType().contains("text/plain")) {
                        // this part may be the message content
                        messageContent = part.getContent().toString();
                    }
                }

            } else if (contentType.contains("text/plain")) {
                Object content = message.getContent();
                if (content != null) {
                    messageContent = content.toString();
                }
            }

            message.setFlag(Flags.Flag.DELETED, true);

            Email mEmail = new Email(from, to, asunto, messageContent);
            Emails.add(mEmail);

            // close the store and folder objects
            if (Emails.size() > 0)
                inboxFolder.expunge();

            String line = reader.readLine();
            if ("QUIT".equals(line)) {
                break;
            }
        }
        inboxFolder.close(false);
        store.close();

        return Emails;

    }
}