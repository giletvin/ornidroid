package fr.ornidroid.ui.preferences;

import fr.ornidroid.ui.preferences.MoveDirectoryHandler.MoveDirectoryCallback;

/**
 * Wrapper to pass information between threads
 * 
 * 
 */
public class LoaderInfo {

	/** The callback. */
	public MoveDirectoryCallback callback;

	/** The url. */
	public final String fromDirectory;

	/** The to directory. */
	public final String toDirectory;

	/** The exception. */
	private Exception exception;

	/**
	 * Instantiates a new loader info.
	 * 
	 * @param fromDirectory
	 *            the sroe directory
	 * @param toDirectory
	 *            the destination directory
	 * @param callback
	 *            the callback
	 */
	public LoaderInfo(final String fromDirectory, final String toDirectory,
			final MoveDirectoryCallback callback) {
		this.fromDirectory = fromDirectory;
		this.toDirectory = toDirectory;
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
