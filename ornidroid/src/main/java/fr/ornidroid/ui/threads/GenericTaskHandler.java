package fr.ornidroid.ui.threads;

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
		 * @param taskHandler
		 *            the task handler
		 * @param info
		 *            the info
		 */
		void onTaskEnded(GenericTaskHandler taskHandler, LoaderInfo info);

	}

	/**
	 * Generic task.
	 * 
	 * @param callback
	 *            the callback
	 */
	public void genericTask(GenericTaskCallback callback);

	/**
	 * Gets the thread type.
	 * 
	 * @return the thread type
	 */
	public ThreadEnum getThreadType();
}
