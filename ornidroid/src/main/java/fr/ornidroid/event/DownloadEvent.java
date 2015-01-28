package fr.ornidroid.event;

import fr.ornidroid.bo.OrnidroidFileType;

public class DownloadEvent extends GenericEvent {

	public final OrnidroidFileType fileType;

	public DownloadEvent(EventType eventType, Exception pException,
			OrnidroidFileType pFileType) {
		super(eventType, pException);
		fileType = pFileType;
	}
}
