package fr.ornidroid.service;

import java.io.File;

import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.OrnidroidException;

/**
 * The Interface IOrnidroidIOService.
 */
public interface IOrnidroidIOService {

	/**
	 * Check and create directory if necessary.
	 * 
	 * @param fileDirectory
	 *            the file directory
	 * @throws OrnidroidException
	 */
	public void checkAndCreateDirectory(File fileDirectory)
			throws OrnidroidException;

	/**
	 * Download media files.
	 * 
	 * @param mediaHomeDirectory
	 *            the media home directory
	 * @param bird
	 *            the bird
	 * @param fileType
	 *            the file type
	 */
	public void downloadMediaFiles(String mediaHomeDirectory, Bird bird,
			OrnidroidFileType fileType) throws OrnidroidException;

	/**
	 * Checks if is directory empty.
	 * 
	 * @param fileDirectory
	 *            the file directory
	 * @return true, if is directory empty
	 */
	public boolean isDirectoryEmpty(File fileDirectory);

	/**
	 * Load media files from the local directory.
	 * 
	 * @param mediaHomeDirectory
	 *            directory of images or sounds
	 * @param bird
	 *            the bird
	 * @param fileType
	 *            the file type
	 */
	public void loadMediaFiles(String mediaHomeDirectory, Bird bird,
			OrnidroidFileType fileType) throws OrnidroidException;

	/**
	 * Check ornidroid home. If the directory doesn't exist, try to create it.
	 * Check if the subdirectories images and audio exist too. If not, try to
	 * create it.
	 * 
	 * @param ornidroidHome
	 *            the ornidroid home : the local path where ornidroid files
	 *            should be
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	void checkOrnidroidHome(String ornidroidHome) throws OrnidroidException;

}
