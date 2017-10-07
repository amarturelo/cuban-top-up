package com.wirelesskings.data.mail.async;

import android.os.AsyncTask;


import com.wirelesskings.data.mail.MailSender;
import com.wirelesskings.data.mail.model.SmtpResponse;
import com.wirelesskings.data.mail.settings.Setting;

import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;


public class CallSender {
    private Setting setting;
    private OnStateChangedListener changedListener;
    private AsyncCall asyncCall;
    /**
     * @param setting
     */
    public CallSender(Setting setting) {
        this.setting = setting;
    }
    public void cancel(){
        if (asyncCall!=null){
            asyncCall.onCancelled();
            asyncCall.cancel(true);
            asyncCall=null;
        }
    }

    /**
     * @param header
     * @param request
     * @param recipient
     * @param listener
     */
    public void execute(String header, String request, String recipient, OnStateChangedListener listener) {
        this.changedListener = listener;
        new AsyncCall(header, recipient).execute(request);
    }

    private class AsyncCall extends AsyncTask<String, Integer, SmtpResponse> {
        private String header;
        private String recipient;
        private MailSender sender;

        /**
         * @param header
         * @param recipient
         */
        public AsyncCall(String header, String recipient) {
            this.header = header;
            this.recipient = recipient;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            changedListener.onExecuting();
        }

        @Override
        protected SmtpResponse doInBackground(String... strings) {
            SmtpResponse response = new SmtpResponse();
            String body = strings[0];

            try {
                sender = new MailSender(setting);
                publishProgress(1);
                sender.sendMail(header, body, setting.getUsername(), recipient);
            } catch (AddressException e) {
                response.setErrorCode(-1);
                response.setErrorDescription(e.getMessage());
                return response;
            } catch (MessagingException e) {
                response.setErrorCode(-2);
                response.setErrorDescription(e.getMessage());
                return response;
            }
            return response;
        }

        @Override
        protected void onPostExecute(SmtpResponse smtpResponse) {

            if (smtpResponse.getErrorCode() != 0) {
                changedListener.onError(smtpResponse.getErrorCode(), smtpResponse.getErrorDescription());
            } else {
                ArrayList<SmtpResponse> list = new ArrayList<>();
                list.add(smtpResponse);
                changedListener.onSuccess(list);
            }
        }
        @Override
        protected void onCancelled() {
            if (sender!=null){
                sender.closeConecction();
            }
            changedListener.onCanceled();
        }
    }
}