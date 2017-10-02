package com.wirelesskings.wkreload.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.mail.async.CallSender;
import com.wirelesskings.wkreload.mail.async.OnStateChangedListener;
import com.wirelesskings.wkreload.mail.settings.Constants;
import com.wirelesskings.wkreload.mail.settings.Setting;

import java.util.ArrayList;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sendMail();
            }
        });
    }

    private void sendMail() {
        Setting mSetting = new Setting("amarturelo@nauta.cu", "adriana*2017");
        mSetting.setServerType(Constants.SMTP_PLAIN); //0 for plain , 1 for ssl
        mSetting.setHost("smtp.nauta.cu");
        mSetting.setPort(Constants.SMTP_PLAIN_PORT); //25 for smtp plain,465 for smtp ssl

        CallSender mSender=new CallSender(mSetting);
        mSender.execute("Test", "texto", "amarturelo@nauta.cu", new OnStateChangedListener() {
            @Override
            public void onExecuting() {

            }

            @Override
            public void onSuccess(ArrayList<?> list) {
                Log.i("REPORT", "Correo enviado");

            }

            @Override
            public void onError(int code, String msg) {
                Log.i("REPORT", "Correo en envio");

            }

            @Override
            public void onCanceled() {

            }
        });
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
