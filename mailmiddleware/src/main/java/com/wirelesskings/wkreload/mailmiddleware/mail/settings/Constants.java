package com.wirelesskings.wkreload.mailmiddleware.mail.settings;

public class Constants {
    public static final int PLAIN               = 0;
    public static final int SSL                 = 1;

    public static final int SMTP_PLAIN          = 0;
    public static final int SMTP_SSL            = 1;
    public static final int SMTP_PLAIN_PORT     = 25;
    public static final int SMTP_SSL_PORT       = 465;

    public static final int POP_PLAIN           = 0;
    public static final int POP_SSL             = 1;
    public static final int POP_PLAIN_PORT      = 110;
    public static final int POP_SSL_PORT        = 995;

    public static final int IMAP_PLAIN          = 2;
    public static final int IMAP_SSL            = 3;
    public static final int IMAP_PLAIN_PORT     = 143;
    public static final int IMAP_SSL_PORT       = 993;

    public static String POP_STORE_TYPE         = "pop3";
    public static String IMAP_STORE_TYPE        = "imap";
}
