package com.wirelesskings.wkreload.mailmiddleware;

public interface ResultListener extends Listener {

	void onSuccess(String result);

	void onError(Exception e);

}
