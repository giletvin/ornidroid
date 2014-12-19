package fr.ornidroid.event;

public class DownloadZipEvent {

	/** The exception. */
	public final Exception exception;

	public DownloadZipEvent(Exception pException) {
		this.exception = pException;
	}
}
