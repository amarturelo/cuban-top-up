package com.wirelesskings.wkreload.navigation;

import android.content.Context;
import android.content.Intent;

import com.wirelesskings.wkreload.activities.LoginActivity;
import com.wirelesskings.wkreload.activities.MainActivity;
import com.wirelesskings.wkreload.activities.ReloadActivity;
import com.wirelesskings.wkreload.activities.SearchActivity;
import com.wirelesskings.wkreload.domain.filter.Filter;

import java.util.ArrayList;

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

    public static void goToReload(Context context) {
        if (context != null) {
            Intent intentToLaunch = ReloadActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public static void goToSearchActivity(Context context, ArrayList<Filter> filters) {
        if (context != null) {
            Intent intentToLaunch = SearchActivity.getCallingIntent(context, filters);
            context.startActivity(intentToLaunch);
        }
    }
}
