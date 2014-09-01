package fr.ornidroid.ui.downloads;

import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.service.IOrnidroidIOService;
import fr.ornidroid.ui.threads.HandlerGenericThread;
import fr.ornidroid.ui.threads.LoaderInfo;
import fr.ornidroid.ui.threads.ThreadEnum;

/**
 * The Class HandlerForCheckUpdateFilesThread.
 */
public class HandlerForDownloadZipPackageThread extends HandlerGenericThread {

	/** The ornidroid io service. */
	private IOrnidroidIOService ornidroidIOService;

	/** The media home directory. */
	private String mediaHomeDirectory;

	/** The file type. */
	private OrnidroidFileType fileType;

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
	 */
	public HandlerForDownloadZipPackageThread(
			IOrnidroidIOService ornidroidIOService, String mediaHomeDirectory,
			OrnidroidFileType fileType) {
		super();

		this.ornidroidIOService = ornidroidIOService;
		this.mediaHomeDirectory = mediaHomeDirectory;
		this.fileType = fileType;

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
		try {
			String zipname = this.ornidroidIOService.getZipname(fileType);
			this.ornidroidIOService.downloadZipPackage(zipname,
					this.mediaHomeDirectory);
		} catch (final OrnidroidException e) {
			loaderInfo.setException(e);
		}
	}

	/** The loader info. */
	private LoaderInfo loaderInfo;

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
			loaderInfo = new LoaderInfo(callback);
		}
		return loaderInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.ui.threads.GenericTaskHandler#getThreadType()
	 */
	public ThreadEnum getThreadType() {
		return ThreadEnum.DOWNLOAD_ZIP;
	}

}
