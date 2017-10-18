package com.wirelesskings.wkreload.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created with IntelliJ IDEA.
 * User: Udini
 * Date: 19/03/13
 * Time: 19:10
 */
public class WKReloadAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        WKReloadAuthenticator authenticator = new WKReloadAuthenticator(this);
        return authenticator.getIBinder();
    }
}
