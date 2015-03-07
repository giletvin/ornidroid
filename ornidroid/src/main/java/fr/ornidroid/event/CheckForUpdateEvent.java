package fr.ornidroid.event;

import fr.ornidroid.bo.OrnidroidFileType;

public class CheckForUpdateEvent extends GenericEvent {

	public final boolean updatesAvailable;
	public final boolean manualCheck;
	public final OrnidroidFileType fileType;

	public CheckForUpdateEvent(Exception pException, boolean pUpdatesAvailable,
			boolean pManualCheck, OrnidroidFileType pFileType) {
		super(EventType.CHECK_FOR_UPDATE, pException);
		updatesAvailable = pUpdatesAvailable;
		manualCheck = pManualCheck;
		fileType = pFileType;
	}
}
