package fr.ornidroid.ui;

import fr.ornidroid.helper.FileHelper;
import fr.ornidroid.ui.HandlerThreadOrnidroidHomeMvDirectory.LoaderInfo;

/**
 * The Interface MoveDirectoryHandler. source code found :
 * http://cyrilmottier.com
 * /2011/07/18/android-et-la-programmation-concurrente-partie-2/
 */
public interface MoveDirectoryHandler {
	/**
	 * Callback to notify state changes.
	 * 
	 */
	public static interface MoveDirectoryCallback {

		/**
		 * Gets the file helper.
		 * 
		 * @return the file helper
		 */
		FileHelper getFileHelper();

		/**
		 * On move ended.
		 * 
		 * @param loader
		 *            the loader
		 */
		void onMoveEnded(MoveDirectoryHandler loader, LoaderInfo info);

	}

	/**
	 * Move directory.
	 * 
	 * @param fromDirectory
	 *            the from directory
	 * @param toDirectory
	 *            the to directory
	 * @param callback
	 *            the callback
	 */
	public void moveDirectory(String fromDirectory, String toDirectory,
			MoveDirectoryCallback callback);
}
