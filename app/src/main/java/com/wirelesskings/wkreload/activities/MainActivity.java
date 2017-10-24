package com.wirelesskings.wkreload.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.wirelesskings.wkreload.R;

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

                /*MaterialDialog mMaterialDialog = new MaterialDialog(MainActivity.this)
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

                mMaterialDialog.show();*/

                /*new BottomDialog.Builder(MainActivity.this)
                        .setTitle("Awesome!")
                        .setContent("What can we improve? Your feedback is always welcome.")
                        .show();*/
                View v = getLayoutInflater().inflate(R.layout.layout_recharge, null);
                new BottomDialog.Builder(MainActivity.this)
                        .setTitle("Nueva recarga")
                        .setContent("What can we improve? Your feedback is always welcome.")
                        .setCustomView(v)
                        .onPositive(new BottomDialog.ButtonCallback() {
                            @Override
                            public void onClick(BottomDialog dialog) {
                                Log.d("BottomDialogs", "Do something!");
                            }
                        })
                        .show();

                //receivedMail();
            }
        });
    }

    /*private void sendMail() {
        Setting mSetting = new Setting("amarturelo@nauta.cu", "adriana*2017");
        mSetting.setServerType(Constants.SMTP_PLAIN); //0 for plain , 1 for ssl
        mSetting.setHost("smtp.nauta.cu");
        mSetting.setPort(Constants.SMTP_PLAIN_PORT); //25 for smtp plain,465 for smtp ssl

        *//*RxCallSender rxCallSender = new RxCallSender(mSetting, 2, 1000);

        rxCallSender.sender("Test", "texto", "amarturelo@nauta.cu")
                .subscribe(() -> System.out.println("subscribe " + "complete")
                        , throwable -> System.out.println("error " + throwable.toString()));*//*

    }

    private void receivedMail() {
        Setting mSetting = new Setting("amarturelo@nauta.cu", "adriana*2017");
        mSetting.setServerType(Constants.IMAP_PLAIN);
        mSetting.setHost("imap.nauta.cu");
        mSetting.setPort(Constants.IMAP_PLAIN_PORT);

        RxCallReceiver rxCallReceiver = new RxCallReceiver(2, 1000, mSetting);

        rxCallReceiver.receiver("Toma").subscribe(o -> Log.d("Main", o.toString()), throwable -> Log.d("Main", throwable.toString()));
    }*/

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

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, MainActivity.class);
        return callingIntent;
    }
}
