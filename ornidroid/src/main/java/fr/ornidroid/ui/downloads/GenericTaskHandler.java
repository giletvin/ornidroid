package fr.ornidroid.ui.downloads;

/**
 * source code found : http://cyrilmottier.com
 * /2011/07/18/android-et-la-programmation-concurrente-partie-2/
 */
public interface GenericTaskHandler {
	/**
	 * Callback to notify state changes.
	 * 
	 */
	public static interface GenericTaskCallback {

		/**
		 * On task ended.
		 * 
		 * @param loader
		 *            the loader
		 * @param info
		 *            the info
		 */
		void onTaskEnded(GenericTaskHandler loader, LoaderInfo info);

	}

	/**
	 * Generic task.
	 * 
	 * @param callback
	 *            the callback
	 */
	public void genericTask(GenericTaskCallback callback);
}
