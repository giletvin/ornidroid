package fr.ornidroid.helper;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Display;

/**
 * The Class UIHelper.
 */
public class UIHelper {

	/**
	 * Gets the screen orientation.
	 * 
	 * @param pActivity
	 *            the activity
	 * @return the screen orientation
	 */
	private static int getScreenOrientation(Activity pActivity) {
		Display getOrient = pActivity.getWindowManager().getDefaultDisplay();
		int orientation;
		if (getOrient.getWidth() < getOrient.getHeight()) {
			orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		} else {
			orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}
		return orientation;
	}

	/**
	 * Lock screen orientation.
	 * 
	 * @param pActivity
	 *            the activity
	 */
	public static void lockScreenOrientation(Activity pActivity) {
		if (pActivity != null) {
			pActivity.setRequestedOrientation(getScreenOrientation(pActivity));
		}
	}

	/**
	 * Unlock screen orientation.
	 * 
	 * @param pActivity
	 *            the activity
	 */
	public static void unlockScreenOrientation(Activity pActivity) {
		if (pActivity != null) {
			pActivity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}
	}
}
