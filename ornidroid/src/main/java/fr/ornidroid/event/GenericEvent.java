package fr.ornidroid.event;

public class GenericEvent {
	/** The exception. */
	public final Exception exception;
	public final EventType eventType;

	public GenericEvent(EventType eventType, Exception pException) {
		this.exception = pException;
		this.eventType = eventType;
	}

	public GenericEvent(EventType eventType) {
		this.exception = null;
		this.eventType = eventType;
	}

	public GenericEvent(Exception pException) {
		this.exception = pException;
		this.eventType = null;
	}

	public GenericEvent() {
		this.exception = null;
		this.eventType = null;
	}
}
