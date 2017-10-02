package com.wirelesskings.wkreload.mail.async;

import android.os.AsyncTask;


import com.wirelesskings.wkreload.mail.MailFetcher;
import com.wirelesskings.wkreload.mail.model.Email;
import com.wirelesskings.wkreload.mail.model.ImapResponse;
import com.wirelesskings.wkreload.mail.settings.Setting;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;


public class CallReceiver {
    private Setting setting;
    private OnStateChangedListener changedListener;
    private AsyncCall asyncCall;

    public CallReceiver(Setting setting) {
        this.setting = setting;

    }

    public void cancel(){
        if (asyncCall!=null){
            asyncCall.onCancelled();
            asyncCall.cancel(true);
            asyncCall=null;
        }
    }

    public void execute(String header, OnStateChangedListener changedListener) {
        this.changedListener = changedListener;
        if (asyncCall != null) {
            asyncCall.cancel(true);
            asyncCall=null;
        }
        asyncCall= new AsyncCall(header);
        asyncCall.execute();
    }

    public class AsyncCall extends AsyncTask<Void, Integer, ImapResponse> {
       private  String criteriaHeader;
       private MailFetcher fetcher;

        public AsyncCall(String criteriaHeader) {
            this.criteriaHeader = criteriaHeader;
        }

        @Override
        protected ImapResponse doInBackground(Void... voids) {
            ImapResponse response           = new ImapResponse();

            try {
                fetcher         = new MailFetcher(setting);
                ArrayList<Email> emails     = fetcher.fetchMail(criteriaHeader);
                response.setEmails(emails);
            } catch (AddressException e) {
                response.setErrorCode(-1);
                response.setErrorDescription(e.getMessage());
                e.printStackTrace();
            } catch (MessagingException e) {
                response.setErrorCode(-2);
                response.setErrorDescription(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                response.setErrorCode(-3);
                response.setErrorDescription(e.getMessage());
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(ImapResponse response) {
           if (response.getErrorCode() != 0) {
               changedListener.onError(response.getErrorCode(), response.getErrorDescription());
           } else {
               changedListener.onSuccess(response.getEmails());
           }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            changedListener.onExecuting();
        }


        @Override
        protected void onCancelled() {
            if (fetcher!=null){
                fetcher.closeConecction();
            }
            changedListener.onCanceled();
        }
    }
}
