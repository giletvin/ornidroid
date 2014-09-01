package fr.ornidroid.ui.preferences;

import java.io.File;

import fr.ornidroid.helper.FileHelper;
import fr.ornidroid.ui.threads.HandlerGenericThread;
import fr.ornidroid.ui.threads.LoaderInfo;
import fr.ornidroid.ui.threads.ThreadEnum;

/**
 * The Class HandlerThreadOrnidroidHomeMvDirectory.
 */
public class HandlerThreadOrnidroidHomeMvDirectory extends HandlerGenericThread {

	/** The old home. */
	private final String oldHome;

	/** The new home. */
	private final String newHome;

	/**
	 * Instantiates a new handler thread ornidroid home mv directory.
	 * 
	 * @param oldOrnidroidHome
	 *            the old ornidroid home
	 * @param newOrnidroidHome
	 *            the new ornidroid home
	 */
	public HandlerThreadOrnidroidHomeMvDirectory(String oldOrnidroidHome,
			String newOrnidroidHome) {
		super();
		oldHome = oldOrnidroidHome;
		newHome = newOrnidroidHome;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.threads.HandlerGenericThread#doTask(fr.ornidroid.ui.threads
	 * .LoaderInfo)
	 */
	@Override
	protected void doTask(LoaderInfo loaderInfo) {
		try {
			final File srcDir = new File(oldHome);
			final File destDir = new File(newHome);
			FileHelper.moveDirectory(srcDir, destDir);
		} catch (final Exception e) {
			// Error occured : keep the exception in the loader info
			loaderInfo.setException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.ui.threads.GenericTaskHandler#getThreadType()
	 */
	public ThreadEnum getThreadType() {
		return ThreadEnum.MOVE_ORNIDROID_HOME;
	}

}
