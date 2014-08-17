package fr.ornidroid.ui.downloads;

import java.util.List;

import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.service.IOrnidroidIOService;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.AbstractDownloadableMediaActivity;
import fr.ornidroid.ui.threads.HandlerGenericThread;
import fr.ornidroid.ui.threads.LoaderInfo;

/**
 * The Class HandlerForCheckUpdateFilesThread.
 */
public class HandlerForCheckUpdateFilesThread extends HandlerGenericThread {

	/** The ornidroid io service. */
	private IOrnidroidIOService ornidroidIOService;

	/** The ornidroid service. */
	private IOrnidroidService ornidroidService;

	/** The media home directory. */
	private String mediaHomeDirectory;

	/** The file type. */
	private OrnidroidFileType fileType;

	/** The manual check. */
	private final boolean manualCheck;

	/**
	 * Instantiates a new handler for check update files thread.
	 * 
	 * @param activity
	 *            the activity
	 * @param manualCheck
	 *            the manual check
	 * @deprecated
	 */
	public HandlerForCheckUpdateFilesThread(
			AbstractDownloadableMediaActivity activity, boolean manualCheck) {
		super();

		this.ornidroidIOService = activity.getOrnidroidIOService();
		this.ornidroidService = OrnidroidServiceFactory.getService(activity);
		this.mediaHomeDirectory = activity.getMediaHomeDirectory();
		this.fileType = activity.getFileType();
		this.manualCheck = manualCheck;
	}

	/**
	 * Instantiates a new handler for check update files thread.
	 * 
	 * @param ornidroidIOService
	 *            the ornidroid io service
	 * @param ornidroidService
	 *            the ornidroid service
	 * @param mediaHomeDirectory
	 *            the media home directory
	 * @param fileType
	 *            the file type
	 * @param manualCheck
	 *            the manual check
	 */
	public HandlerForCheckUpdateFilesThread(
			IOrnidroidIOService ornidroidIOService,
			IOrnidroidService ornidroidService, String mediaHomeDirectory,
			OrnidroidFileType fileType, boolean manualCheck) {
		super();

		this.ornidroidIOService = ornidroidIOService;
		this.ornidroidService = ornidroidService;
		this.mediaHomeDirectory = mediaHomeDirectory;
		this.fileType = fileType;
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
			List<String> filesToDownload = this.ornidroidIOService
					.filesToUpdate(this.mediaHomeDirectory,
							ornidroidService.getCurrentBird(), this.fileType);
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
	 * fr.ornidroid.ui.threads.HandlerGenericThread#getLoaderInfo(fr.ornidroid
	 * .ui.threads.GenericTaskHandler.GenericTaskCallback)
	 */
	@Override
	protected LoaderInfo getLoaderInfo(GenericTaskCallback callback) {
		if (loaderInfo == null) {
			loaderInfo = new CheckForUpdateFilesLoaderInfo(callback,
					manualCheck);
		}
		return loaderInfo;
	}

}
