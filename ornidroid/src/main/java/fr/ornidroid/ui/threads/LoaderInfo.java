package fr.ornidroid.ui.threads;

import fr.ornidroid.ui.threads.GenericTaskHandler.GenericTaskCallback;

/**
 * Wrapper to pass information between threads.
 */
public abstract class LoaderInfo {

	/** The callback. */
	private final GenericTaskCallback callback;
	/** The exception. */
	private Exception exception;

	/**
	 * Gets the callback.
	 * 
	 * @return the callback
	 */
	public GenericTaskCallback getCallback() {
		return callback;
	}

	/**
	 * Instantiates a new loader info.
	 * 
	 * @param callback
	 *            the callback
	 */
	public LoaderInfo(final GenericTaskCallback callback) {
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
