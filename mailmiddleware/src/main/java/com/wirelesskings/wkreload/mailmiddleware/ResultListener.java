package com.wirelesskings.wkreload.mailmiddleware;

public interface ResultListener extends Listener {

	void onSuccess(String result);

	void onError(String error, String reason, String details);

}
