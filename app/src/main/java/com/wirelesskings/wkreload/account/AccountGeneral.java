package com.wirelesskings.wkreload.account;

/**
 * Created with IntelliJ IDEA.
 * User: Udini
 * Date: 20/03/13
 * Time: 18:11
 */
public class AccountGeneral {

    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "com.wirelesskings.wkreload";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "WKReload";

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an WKReload account";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an WKReload account";

    public static final ServerAuthenticate sServerAuthenticate = new ParseComServerAuthenticate();
}
