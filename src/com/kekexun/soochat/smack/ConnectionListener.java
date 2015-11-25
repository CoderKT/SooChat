package com.kekexun.soochat.smack;


/**
 * 
 * @author Ke.Wang
 * @date 2015.11.25
 *
 */
public interface ConnectionListener {

	public void onSuccess();
	
	public void onFailure(String errorMessage);
}
