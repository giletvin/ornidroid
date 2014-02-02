package fr.ornidroid.ui.downloads;

/**
 * source code found : http://cyrilmottier.com
 * /2011/07/18/android-et-la-programmation-concurrente-partie-2/
 */
public interface CheckUpdateFilesHandler {
	/**
	 * Callback to notify state changes.
	 * 
	 */
	public static interface UpdateFilesCallback {

		/**
		 * On update files ended.
		 * 
		 * @param loader
		 *            the loader
		 * @param info
		 *            the info
		 */
		void onUpdateFilesEnded(CheckUpdateFilesHandler loader,
				UpdateFilesLoaderInfo info);

	}

	/**
	 * Check for updates.
	 * 
	 * @param manualCheck
	 *            the manual check
	 * @param callback
	 *            the callback
	 */
	public void checkForUpdates(boolean manualCheck,
			UpdateFilesCallback callback);
}
