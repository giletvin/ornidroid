package fr.ornidroid.ui.downloads;

import fr.ornidroid.ui.downloads.CheckUpdateFilesHandler.UpdateFilesCallback;

/**
 * Wrapper to pass information between threads.
 */
public class UpdateFilesLoaderInfo {

	/** The callback. */
	private final UpdateFilesCallback callback;

	/**
	 * Gets the callback.
	 * 
	 * @return the callback
	 */
	public UpdateFilesCallback getCallback() {
		return callback;
	}

	/** The updates available. */
	private boolean updatesAvailable = false;

	/** The manual check. */
	private final boolean manualCheck;

	/** The exception. */
	private Exception exception;

	/**
	 * Instantiates a new update files loader info.
	 * 
	 * @param manualCheck
	 *            the manual check
	 * @param callback
	 *            the callback
	 */
	public UpdateFilesLoaderInfo(final boolean manualCheck,
			final UpdateFilesCallback callback) {
		this.manualCheck = manualCheck;
		this.callback = callback;
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
	 * Gets the exception.
	 * 
	 * @return the exception
	 */
	public Exception getException() {
		return this.exception;
	}

	/**
	 * Sets the exception.
	 * 
	 * @param exception
	 *            the new exception
	 */
	public void setException(final Exception exception) {
		this.exception = exception;
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
