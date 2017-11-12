package com.wirelesskings.wkreload.navigation;

import android.content.Context;
import android.content.Intent;

import com.wirelesskings.wkreload.activities.LoginActivity;
import com.wirelesskings.wkreload.activities.MainActivity;

/**
 * Created by Alberto on 18/10/2017.
 */

public class Navigator {

    public static void goToMain(Context context) {
        if (context != null) {
            Intent intentToLaunch = MainActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public static void goToLogin(Context context) {
        if (context != null) {
            Intent intentToLaunch = LoginActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }
}
