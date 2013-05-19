package fr.ornidroid.ui.components.progressbar;

import fr.ornidroid.ui.components.progressbar.ProgressActionHandler.ProgressActionCallback;

/**
 * Wrapper to pass information between threads
 * 
 * 
 */
public class ProgressActionLoaderInfo {

	/** The callback. */
	public ProgressActionCallback callback;

	/** The exception. */
	private Exception exception;

	/**
	 * Instantiates a new loader info.
	 * 
	 * 
	 * @param callback
	 *            the callback
	 */
	public ProgressActionLoaderInfo(final ProgressActionCallback callback) {

		this.callback = callback;
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
}
