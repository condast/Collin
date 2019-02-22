package org.collin.moodle.core;

import org.collin.moodle.advice.IAdvice;
import org.condast.commons.messaging.push.ISubscription;

import nl.martijndwars.webpush.core.PushManager;


public class Push {

	public static final String S_PUBLIC_KEY = "BDvq04Lz9f7WBugyNHW2kdgFI7cjd65fzfFRpNdRpa9zWvi4yAD8nAvgb8c8PpRXdtgUqqZDG7KbamEgxotOcaA";
	public static final String S_PRIVATE_KEY = "CxbJjjbVMABqzv72ZL4GH_0gNStbZV0TSBaNOIzLwbE";
	
	public static final String S_CODED = "BMfyyFPnyR8MRrzPJ6jloLC26FyXMcrL8v46d7QEUccbQVArghc9YHC6USyp4TggrFleNzAUq8df0RiSS13xwtM";

	
	private static PushManager pushManager = new PushManager();
	
	private Push() {
		super();
	}

	public static ISubscription subscribe( long id, String token, String subscription ) {
		ISubscription sub = pushManager.subscribe(id, token, subscription);
		return sub;
	}
	
	/**
	 * Push the given advice to the user
	 * @param userId
	 * @param advice
	 * @return
	 */
	public static boolean sendPushMessage( long userId, IAdvice advice ) {
		if(( advice ==  null ) ||( userId < 0 ))
			return false;
		PushManager pm = pushManager;
		ISubscription subscription = pm.getSubscription( userId );
		PushOptionsAdviceBuilder builder = new PushOptionsAdviceBuilder();
		try {
			PushManager.sendPushMessage( Push.S_PUBLIC_KEY, Push.S_PRIVATE_KEY, subscription, builder.createPayLoad( advice, true ));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}				
		return true;
	}
}
