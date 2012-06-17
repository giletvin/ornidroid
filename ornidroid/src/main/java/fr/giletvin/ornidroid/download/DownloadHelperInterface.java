package fr.giletvin.ornidroid.download;

import java.io.File;
import java.util.List;

import fr.giletvin.ornidroid.bo.OrnidroidFileType;
import fr.giletvin.ornidroid.helper.OrnidroidException;

/**
 * The Interface DownloadHelperInterface.
 */
public interface DownloadHelperInterface {

	/**
	 * Download files from ornidroid web site for a given bird.
	 * 
	 * @param ornidroidMediaHome
	 *            local root path of the media (picture or audio) :
	 *            ornidroidHome + images or audio
	 * @param directoryName
	 *            the directory name : directory corresponding to the name of
	 *            the bird (both local and distant)
	 * @param fileType
	 *            the file type picture or sound
	 * @return the list of downloaded files, never null but can be empty if the
	 *         bird doesnt have files.
	 * @throws OrnidroidException
	 *             if an exception has occurred, it is wrapped in a
	 *             OrnidroidException
	 */
	List<File> downloadFiles(String ornidroidMediaHome, String directoryName,
			OrnidroidFileType fileType) throws OrnidroidException;

	/**
	 * Read content file in the directory of the bird. The contents.txt file
	 * lists the names of the files to download.
	 * 
	 * @param baseUrl
	 *            the base url where the contents.txt file is to be found
	 * @param destinationPath
	 *            : directory where contents.txt will be copied
	 * @return the list of files read from the contents.txt file. Can be null if
	 *         an exception occurs
	 * @throws OrnidroidException
	 *             if an exception occurs
	 */
	String[] readContentFile(String baseUrl, String destinationPath)
			throws OrnidroidException;

	/**
	 * Download a file (whatever : sound, picture, database).
	 * 
	 * @param baseUrl
	 *            the base url containing the file
	 * @param fileName
	 *            the file name
	 * @param destinationPath
	 *            : local directory where the file is to be put.
	 * @return the file, can be null if the file doesnt not exist
	 * @throws OrnidroidException
	 *             if an exception occurs
	 */
	File downloadFile(String baseUrl, String fileName, String destinationPath)
			throws OrnidroidException;
}