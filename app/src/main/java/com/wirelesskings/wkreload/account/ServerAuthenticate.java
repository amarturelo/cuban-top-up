package com.wirelesskings.wkreload.account;

/**
 * User: WKReload
 * Date: 3/27/13
 * Time: 2:35 AM
 */
public interface ServerAuthenticate {
    public String userSignUp(final String name, final String email, final String pass, String authType) throws Exception;
    public String userSignIn(final String user, final String pass, String authType) throws Exception;
}
