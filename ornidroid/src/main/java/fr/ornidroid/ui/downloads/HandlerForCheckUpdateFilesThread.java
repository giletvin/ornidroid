package fr.ornidroid.ui.downloads;

import java.util.List;

import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.ui.AbstractDownloadableMediaActivity;

/**
 * The Class HandlerForCheckUpdateFilesThread.
 */
public class HandlerForCheckUpdateFilesThread extends HandlerGenericThread {

	/** The activity. */
	private final AbstractDownloadableMediaActivity activity;

	/** The manual check. */
	private final boolean manualCheck;

	/**
	 * Instantiates a new handler for check update files thread.
	 * 
	 * @param activity
	 *            the activity
	 * @param manualCheck
	 *            the manual check
	 */
	public HandlerForCheckUpdateFilesThread(
			AbstractDownloadableMediaActivity activity, boolean manualCheck) {
		super();
		this.activity = activity;
		this.manualCheck = manualCheck;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.downloads.HandlerGenericThread#doTask(fr.ornidroid.ui
	 * .downloads.UpdateFilesLoaderInfo)
	 */
	@Override
	protected void doTask(LoaderInfo loaderInfo) {
		CheckForUpdateFilesLoaderInfo loader = (CheckForUpdateFilesLoaderInfo) loaderInfo;
		boolean updatesToDo = false;
		try {
			List<String> filesToDownload = activity.getOrnidroidIOService()
					.filesToUpdate(activity.getMediaHomeDirectory(),
							activity.getBird(), activity.getFileType());
			updatesToDo = (filesToDownload.size() > 0);
		} catch (final OrnidroidException e) {
			loader.setException(e);
		}

		loader.setUpdatesAvailable(updatesToDo);

	}

	/** The loader info. */
	private CheckForUpdateFilesLoaderInfo loaderInfo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.downloads.HandlerGenericThread#getLoaderInfo(fr.ornidroid
	 * .ui.downloads.GenericTaskHandler.GenericTaskCallback)
	 */
	@Override
	LoaderInfo getLoaderInfo(GenericTaskCallback callback) {
		if (loaderInfo == null) {
			loaderInfo = new CheckForUpdateFilesLoaderInfo(callback,
					manualCheck);
		}
		return loaderInfo;
	}

}
