package com.wirelesskings.data.repositories;

import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallReceiver;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallSender;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Constants;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;
import com.wirelesskings.wkreload.domain.repositories.ServerRepository;

import io.reactivex.Completable;
import io.reactivex.functions.BiConsumer;

/**
 * Created by Alberto on 24/10/2017.
 */

public class ServerRepositoryImpl implements ServerRepository {


    public ServerRepositoryImpl() {
    }

    @Override
    public Completable findUpdate(String nauta_mail, String nauta_password, String wk_username, String wk_password) {
        Setting senderSettings = new Setting(nauta_mail, nauta_password);
        senderSettings.setServerType(Constants.SMTP_PLAIN); //0 for plain , 1 for ssl
        senderSettings.setHost("smtp.nauta.cu");
        senderSettings.setPort(Constants.SMTP_PLAIN_PORT); //25 for smtp plain,465 for smtp ssl

        Setting receivedSetting = new Setting("amarturelo@nauta.cu", "adriana*2017");
        receivedSetting.setServerType(Constants.IMAP_PLAIN);
        receivedSetting.setHost("imap.nauta.cu");
        receivedSetting.setPort(Constants.IMAP_PLAIN_PORT);

        RxCallSender rxCallSender = new RxCallSender(senderSettings, 3, 5000);

        RxCallReceiver rxCallReceiver = new RxCallReceiver(3, 5000, receivedSetting);


        return rxCallSender.sender("", "", "reload.wirelesskingllc.com").concatWith(rxCallReceiver.receiver("")
                .doOnEvent(new BiConsumer() {
                    @Override
                    public void accept(Object o, Object o2) throws Exception {

                    }
                })

                .toCompletable());
    }
}
