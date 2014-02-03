package fr.ornidroid.ui.downloads;

import fr.ornidroid.ui.downloads.GenericTaskHandler.GenericTaskCallback;

/**
 * The Class CheckForUpdateFilesLoaderInfo.
 */
public class CheckForUpdateFilesLoaderInfo extends LoaderInfo {
	/** The updates available. */
	private boolean updatesAvailable = false;

	/** The manual check. */
	private final boolean manualCheck;

	/**
	 * Instantiates a new check for update files loader info.
	 * 
	 * @param callback
	 *            the callback
	 * @param manualCheck
	 *            the manual check
	 */
	public CheckForUpdateFilesLoaderInfo(GenericTaskCallback callback,
			final boolean manualCheck) {
		super(callback);
		this.manualCheck = manualCheck;
	}

	/**
	 * Checks if is manual check.
	 * 
	 * @return true, if is manual check
	 */
	public boolean isManualCheck() {
		return manualCheck;
	}

	/**
	 * Checks if is updates available.
	 * 
	 * @return true, if is updates available
	 */
	public boolean isUpdatesAvailable() {
		return updatesAvailable;
	}

	/**
	 * Sets the updates available.
	 * 
	 * @param updatesAvailable
	 *            the new updates available
	 */
	public void setUpdatesAvailable(boolean updatesAvailable) {
		this.updatesAvailable = updatesAvailable;
	}
}
