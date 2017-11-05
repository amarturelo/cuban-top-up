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
import android.widget.TextView;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.wirelesskings.wkreload.R;
import com.wirelesskings.wkreload.dialogs.ReloadBottomDialog;
import com.wirelesskings.wkreload.fragments.ReloadsFragment;
import com.wirelesskings.wkreload.fragments.ReloadsPresenter;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallReceiver;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallSender;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Constants;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;

public class MainActivity extends AppCompatActivity implements ReloadsFragment.OnReloadsFragmentListened {

    private ReloadBottomDialog reloadBottomDialog;

    private TextView tvDebit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reloadBottomDialog = new ReloadBottomDialog(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        tvDebit = (TextView) findViewById(R.id.tv_debit);

        fab.setOnClickListener(view -> showReload());

    }

    private void showReload() {
        reloadBottomDialog.show();
    }

    /*  private void sendMail() {
          Setting mSetting = new Setting("amarturelo@nauta.cu", "adriana*2017");
          mSetting.setServerType(Constants.SMTP_PLAIN); //0 for plain , 1 for ssl
          mSetting.setHost("smtp.nauta.cu");
          mSetting.setPort(Constants.SMTP_PLAIN_PORT); //25 for smtp plain,465 for smtp ssl

          RxCallSender rxCallSender = new RxCallSender(mSetting, 2, 1000);


          String bodyReload = "{\"action\":\"reload\",\"id\":\"ididididid234ddd235\",\"params\":{\"user\":\"amarturelo@nauta.cu\",\"pass\":\"MZAtfDUMcYT12HikEYHuiI2JAgBB4PLp4egp\\/ZqRpQsPzMOleaFdhCT759I0cTHR1okwBQ9Q6djhMaxe+D5w0A==\",\"user_nauta\":\"amarturelo@nauta.cu\",\"client\":{\"number\":53192289,\"name\":\"albertini\"},\"reload\":{\"count\":1,\"amount\":20}}}";
          String bodyUpdate = "{\"action\":\"update\",\"id\":\"ididididid234235\",\"params\":{\"user\":\"amarturelo@nauta.cu\",\"pass\":\"MZAtfDUMcYT12HikEYHuiI2JAgBB4PLp4egp\\/ZqRpQsPzMOleaFdhCT759I0cTHR1okwBQ9Q6djhMaxe+D5w0A==\",\"user_nauta\":\"amarturelo@nauta.cu\"}}";


          String sender = "reload@wirelesskingsllc.com";
          String me = "amarturelo@nauta.cu";

          String updateSubject = "2bafc6c3270b0ee8f002e48f5f57773b";
          String reloadSubject = "c5cf025ef4457b0b61d527584ee689ed";

          rxCallSender.sender(updateSubject, bodyUpdate.trim(), sender)
                  .subscribe(() -> System.out.println("subscribe " + "complete")
                          , throwable -> System.out.println("error " + throwable.toString()));

      }

      private void receivedMail() {
          Setting mSetting = new Setting("amarturelo@nauta.cu", "adriana*2017");
          mSetting.setServerType(Constants.IMAP_PLAIN);
          mSetting.setHost("imap.nauta.cu");
          mSetting.setPort(Constants.IMAP_PLAIN_PORT);

          RxCallReceiver rxCallReceiver = new RxCallReceiver(mSetting, 0, 6000);

          rxCallReceiver.receiver("Toma").subscribe(o -> Log.d("Main", o.toString()), throwable -> Log.d("Main", throwable.toString()));
      }
  */
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
        if(id==R.id.action_update){

        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, MainActivity.class);
        return callingIntent;
    }

    @Override
    public void onDebit(long debit) {
        tvDebit.setText("$" + String.valueOf(debit));
    }
}
