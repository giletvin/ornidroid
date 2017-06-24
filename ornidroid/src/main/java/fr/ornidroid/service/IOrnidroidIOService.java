package fr.ornidroid.service;

import java.io.File;
import java.util.List;

import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.bo.ZipPackage;
import fr.ornidroid.helper.OrnidroidException;

/**
 * The Interface IOrnidroidIOService.
 */
public interface IOrnidroidIOService {

	/**
	 * Adds the custom media file.
	 * 
	 * @param birdDirectory
	 *            the bird directory
	 * @param fileType
	 *            the file type
	 * @param selectedFileName
	 *            the selected file name
	 * @param selectedFile
	 *            the selected file
	 * @param comment
	 *            the comment
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	public void addCustomMediaFile(String birdDirectory,
			OrnidroidFileType fileType, String selectedFileName,
			File selectedFile, String comment) throws OrnidroidException;

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

	/**
	 * Checks if there are updates for the given bird and file type. This
	 * methods only checks for new files - it doesn't detect any file deletion.
	 * 
	 * @param mediaHomeDirectory
	 *            the media home directory
	 * @param bird
	 *            the bird
	 * @param fileType
	 *            the file type
	 * @return the list of files to download, never null
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	List<String> filesToUpdate(String mediaHomeDirectory, Bird bird,
			OrnidroidFileType fileType) throws OrnidroidException;

	/**
	 * Removes the custom media file.
	 * 
	 * @param ornidroidFile
	 *            the file name
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	void removeCustomMediaFile(OrnidroidFile ornidroidFile)
			throws OrnidroidException;

	/**
	 * Download zip package.
	 * 
	 * @param zipname
	 *            zipname
	 * @param mediaHomeDirectory
	 *            the media home directory
	 * @param fileType
	 *            the file type
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	public void downloadZipPackage(String zipname, String mediaHomeDirectory)
			throws OrnidroidException;

	/**
	 * Checks if is enough free space.
	 * 
	 * @param fileType
	 *            the file type
	 * @return true, if is enough free space
	 */
	public boolean isEnoughFreeSpace(OrnidroidFileType fileType);

	/**
	 * Gets the zipname according to the filetype
	 * 
	 * @param fileType
	 *            the file type
	 * @return the zipname
	 */
	public ZipPackage getZipname(OrnidroidFileType fileType);

	/**
	 * Gets the download progress in percent of the zip package downloading.
	 * 
	 * @param fileType
	 * @param folderSizeBeforeDownload
	 * @return the zip download progress percent
	 */
	public int getZipDownloadProgressPercent(OrnidroidFileType fileType,
			int folderSizeBeforeDownload);

	/**
	 * Gets the install progress percent.
	 * 
	 * @param fileType
	 *            the file type
	 * @return the install progress percent
	 */
	public int getInstallProgressPercent(OrnidroidFileType fileType);

	/**
	 * reset the content of a directory, except the custom files added by the
	 * user
	 * 
	 * @param directoryToReset
	 * @throws OrnidroidException
	 */
	void resetExistingDirectory(File directoryToReset)
			throws OrnidroidException;

}
