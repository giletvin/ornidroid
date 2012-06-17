package fr.giletvin.ornidroid.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import android.util.Log;
import fr.giletvin.ornidroid.bo.AbstractOrnidroidFile;
import fr.giletvin.ornidroid.bo.Bird;
import fr.giletvin.ornidroid.bo.OrnidroidFileFactoryImpl;
import fr.giletvin.ornidroid.bo.OrnidroidFileType;
import fr.giletvin.ornidroid.download.DownloadConstants;
import fr.giletvin.ornidroid.download.DownloadHelperImpl;
import fr.giletvin.ornidroid.download.DownloadHelperInterface;
import fr.giletvin.ornidroid.helper.BasicConstants;
import fr.giletvin.ornidroid.helper.Constants;
import fr.giletvin.ornidroid.helper.OrnidroidError;
import fr.giletvin.ornidroid.helper.OrnidroidException;

/**
 * The Class OrnidroidIOServiceImpl.
 */
public class OrnidroidIOServiceImpl implements IOrnidroidIOService {
	/** The download helper. */
	private final DownloadHelperInterface downloadHelper;

	/**
	 * Instantiates a new ornidroid io service impl.
	 */
	public OrnidroidIOServiceImpl() {

		this.downloadHelper = new DownloadHelperImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidService#checkOrnidroidDatabase
	 * (java.lang.String)
	 */
	public void checkOrnidroidDatabase(String localDatabaseDirectory,
			String dbName) throws OrnidroidException {
		File ornidroidDb = new File(localDatabaseDirectory + File.separator
				+ dbName);
		String topFileName = dbName + ".top";
		File topFile = new File(localDatabaseDirectory + File.separator
				+ topFileName);
		if (!ornidroidDb.exists() || !topFile.exists()) {
			// try to download it if it doesnt not exist
			File dbFile = downloadHelper.downloadFile(
					DownloadConstants.getOrnidroidWebSite(), dbName,
					localDatabaseDirectory);
			// try to download the top file
			topFile = downloadHelper.downloadFile(
					DownloadConstants.getOrnidroidWebSite(), topFileName,
					localDatabaseDirectory);
			if (null == topFile || null == dbFile) {
				if (null != dbFile) {
					dbFile.delete();
				}
				throw new OrnidroidException(OrnidroidError.DATABASE_NOT_FOUND);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidIOService#checkOrnidroidHome(
	 * java.lang.String)
	 */
	public void checkOrnidroidHome(String ornidroidHome)
			throws OrnidroidException {
		File fileOrnidroidHome = new File(ornidroidHome);
		if (!fileOrnidroidHome.exists()) {
			checkAndCreateDirectory(fileOrnidroidHome);
		}
		checkAndCreateDirectory(new File(ornidroidHome + File.separator
				+ BasicConstants.IMAGES_DIRECTORY));
		checkAndCreateDirectory(new File(ornidroidHome + File.separator
				+ BasicConstants.AUDIO_DIRECTORY));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidIOService#checkAndCreateDirectory
	 * (java.io.File)
	 */
	public void checkAndCreateDirectory(File fileDirectory)
			throws OrnidroidException {
		if (!fileDirectory.exists()) {
			try {
				FileUtils.forceMkdir(fileDirectory);
			} catch (IOException e) {
				throw new OrnidroidException(
						OrnidroidError.ORNIDROID_HOME_NOT_FOUND);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidIOService#isDirectoryEmpty(java
	 * .io.File)
	 */
	public boolean isDirectoryEmpty(File fileDirectory) {
		return FileUtils.listFiles(fileDirectory, null, false).isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidIOService#loadMediaFiles(java
	 * .io.File, fr.giletvin.ornidroid.bo.Bird)
	 */
	public void loadMediaFiles(String fileDirectory, Bird bird,
			OrnidroidFileType fileType) throws OrnidroidException {
		switch (fileType) {
		case PICTURE:
			bird.setPictures(lookForOrnidroidFiles(fileDirectory,
					bird.getBirdDirectoryName(), OrnidroidFileType.PICTURE,
					false));
			break;

		case AUDIO:
			bird.setSounds(lookForOrnidroidFiles(fileDirectory,
					bird.getBirdDirectoryName(), OrnidroidFileType.AUDIO, false));
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidIOService#downloadMediaFiles(
	 * java.lang.String, fr.giletvin.ornidroid.bo.Bird,
	 * fr.giletvin.ornidroid.bo.OrnidroidFileType)
	 */
	public void downloadMediaFiles(String mediaHomeDirectory, Bird bird,
			OrnidroidFileType fileType) throws OrnidroidException {
		switch (fileType) {
		case PICTURE:
			bird.setPictures(lookForOrnidroidFiles(mediaHomeDirectory,
					bird.getBirdDirectoryName(), OrnidroidFileType.PICTURE,
					true));
			break;

		case AUDIO:
			bird.setSounds(lookForOrnidroidFiles(mediaHomeDirectory,
					bird.getBirdDirectoryName(), OrnidroidFileType.AUDIO, true));
			break;
		}

	}

	/**
	 * Look for OrnidroidFiles.
	 * 
	 * @param ornidroidMediaHome
	 *            root path of the media (picture or audio) : ornidroidHome +
	 *            images or audio
	 * @param directoryName
	 *            the directory name of the bird
	 * @param fileType
	 *            the file type
	 * @param downloadFromInternet
	 *            allow download from internet
	 * @return the list containing instances of OrnidroidFile, never null. If
	 *         there are no files, the list is empty. If one of the file misses
	 *         its properties file, the entire bird media directory is deleted
	 *         and the returned list is empty : the user will be given the
	 *         choice to try a download from the web site
	 */
	private List<AbstractOrnidroidFile> lookForOrnidroidFiles(
			String ornidroidMediaHome, String directoryName,
			OrnidroidFileType fileType, boolean downloadFromInternet)
			throws OrnidroidException {
		List<AbstractOrnidroidFile> files = new ArrayList<AbstractOrnidroidFile>();
		if (StringUtils.isNotBlank(directoryName)) {
			File filesDirectory = new File(ornidroidMediaHome + File.separator
					+ directoryName);
			if (!filesDirectory.exists()) {
				try {
					FileUtils.forceMkdir(filesDirectory);
				} catch (IOException e) {
					throw new OrnidroidException(
							OrnidroidError.ORNIDROID_HOME_NOT_FOUND);
				}
			}
			if (filesDirectory.isDirectory()) {
				List<File> filesList = Arrays.asList(filesDirectory
						.listFiles(new OrnidroidFileFilter(fileType)));
				// no files in the local directory. Try to download from
				// internet
				if (filesList.size() == 0 && downloadFromInternet) {
					filesList = downloadHelper.downloadFiles(
							ornidroidMediaHome, directoryName, fileType);
				}
				try {
					for (File file : filesList) {
						AbstractOrnidroidFile ornidroidFile = OrnidroidFileFactoryImpl
								.getFactory().createOrnidroidFile(
										file.getAbsolutePath(), fileType,
										Constants.getOrnidroidLang());
						files.add(ornidroidFile);
					}
				} catch (FileNotFoundException e) {
					// one of the media files doesnt have its properties.
					// this reveals a pb in the downloading : maybe the picture
					// or the sound file is corrupted
					// we remove the entire bird media directory and don't load
					// anything.
					files.clear();
					Log.w(Constants.LOG_TAG, e.getMessage());
					Log.w(Constants.LOG_TAG,
							"The directory " + filesDirectory.getPath()
									+ " is going to be deleted");
					try {
						FileUtils.forceDelete(filesDirectory);
					} catch (IOException e1) {
						Log.e(Constants.LOG_TAG,
								"Unable to delete the directory "
										+ filesDirectory.getPath());
					}

				}
			}
		}
		return files;
	}

	/**
	 * The Class OrnidroidFileFilter.
	 */
	private class OrnidroidFileFilter implements FileFilter {

		/** The file type. */
		private OrnidroidFileType fileType;

		/**
		 * Instantiates a new ornidroid file filter.
		 * 
		 * @param fileType
		 *            the file type
		 */
		OrnidroidFileFilter(OrnidroidFileType fileType) {
			this.fileType = fileType;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.FileFilter#accept(java.io.File)
		 */
		public boolean accept(File pathname) {

			if (pathname.getAbsolutePath().endsWith(
					OrnidroidFileType.getExtension(fileType))) {
				return true;
			}
			return false;
		}
	}

}
