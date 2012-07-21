package fr.ornidroid.service;

import android.app.Activity;

/**
 * A factory for creating OrnidroidService objects.
 */
public class OrnidroidServiceFactory {

	/**
	 * Gets the service.
	 * 
	 * @param activity
	 *            the activity
	 * @return the service
	 */
	public static IOrnidroidService getService(Activity activity) {
		return OrnidroidServiceImpl.getInstance(activity);
	}
}
