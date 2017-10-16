package com.wirelesskings.wkreload.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.wirelesskings.data.mail.async.CallSender;
import com.wirelesskings.data.mail.async.OnStateChangedListener;
import com.wirelesskings.data.mail.rx.RxCallReceiver;
import com.wirelesskings.data.mail.rx.RxCallSender;
import com.wirelesskings.data.mail.settings.Constants;
import com.wirelesskings.data.mail.settings.Setting;
import com.wirelesskings.wkreload.R;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                /*MaterialDialog mMaterialDialog = new MaterialDialog(MainActivity.this);
                mMaterialDialog
                        .setTitle("MaterialDialog")
                        .setMessage("Hello world!")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        })
                        .setNegativeButton("CANCEL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });

                mMaterialDialog.show();*/

                MaterialDialog mMaterialDialog = new MaterialDialog(MainActivity.this)
                        .setTitle("Nueva recarga")
                        .setContentView(R.layout.layout_recharge)
                        .setPositiveButton("ACEPTAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .setNegativeButton("CANCELAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });

                mMaterialDialog.show();

                //receivedMail();
            }
        });
    }

    private void sendMail() {
        Setting mSetting = new Setting("amarturelo@nauta.cu", "adriana*2017");
        mSetting.setServerType(Constants.SMTP_PLAIN); //0 for plain , 1 for ssl
        mSetting.setHost("smtp.nauta.cu");
        mSetting.setPort(Constants.SMTP_PLAIN_PORT); //25 for smtp plain,465 for smtp ssl

        /*RxCallSender rxCallSender = new RxCallSender(mSetting, 2, 1000);

        rxCallSender.sender("Test", "texto", "amarturelo@nauta.cu")
                .subscribe(() -> System.out.println("subscribe " + "complete")
                        , throwable -> System.out.println("error " + throwable.toString()));*/

    }

    private void receivedMail() {
        Setting mSetting = new Setting("amarturelo@nauta.cu", "adriana*2017");
        mSetting.setServerType(Constants.IMAP_PLAIN);
        mSetting.setHost("imap.nauta.cu");
        mSetting.setPort(Constants.IMAP_PLAIN_PORT);

        RxCallReceiver rxCallReceiver = new RxCallReceiver(2, 1000, mSetting);

        rxCallReceiver.receiver("Toma").subscribe(o -> Log.d("Main", o.toString()), throwable -> Log.d("Main", throwable.toString()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
