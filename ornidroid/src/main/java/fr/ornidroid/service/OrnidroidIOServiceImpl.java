package fr.ornidroid.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.OrnidroidFileFactoryImpl;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.download.DownloadHelperImpl;
import fr.ornidroid.download.DownloadHelperInterface;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;

/**
 * The Class OrnidroidIOServiceImpl.
 */
public class OrnidroidIOServiceImpl implements IOrnidroidIOService {
	/**
	 * The Class OrnidroidFileFilter.
	 */
	private class OrnidroidFileFilter implements FileFilter {

		/** The file type. */
		private final OrnidroidFileType fileType;

		/**
		 * Instantiates a new ornidroid file filter.
		 * 
		 * @param fileType
		 *            the file type
		 */
		OrnidroidFileFilter(final OrnidroidFileType fileType) {
			this.fileType = fileType;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.FileFilter#accept(java.io.File)
		 */
		public boolean accept(final File pathname) {

			if (pathname.getAbsolutePath().endsWith(
					OrnidroidFileType.getExtension(this.fileType))) {
				return true;
			}
			return false;
		}
	}

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
	 * @see fr.ornidroid.service.IOrnidroidIOService#checkAndCreateDirectory
	 * (java.io.File)
	 */
	public void checkAndCreateDirectory(final File fileDirectory)
			throws OrnidroidException {
		if (!fileDirectory.exists()) {
			try {
				FileUtils.forceMkdir(fileDirectory);
			} catch (final IOException e) {
				throw new OrnidroidException(
						OrnidroidError.ORNIDROID_HOME_NOT_FOUND, e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidIOService#checkOrnidroidHome(
	 * java.lang.String)
	 */
	public void checkOrnidroidHome(final String ornidroidHome)
			throws OrnidroidException {
		final File fileOrnidroidHome = new File(ornidroidHome);
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
	 * @see fr.ornidroid.service.IOrnidroidIOService#downloadMediaFiles(
	 * java.lang.String, fr.ornidroid.bo.Bird,
	 * fr.ornidroid.bo.OrnidroidFileType)
	 */
	public void downloadMediaFiles(final String mediaHomeDirectory,
			final Bird bird, final OrnidroidFileType fileType)
			throws OrnidroidException {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidIOService#isDirectoryEmpty(java
	 * .io.File)
	 */
	public boolean isDirectoryEmpty(final File fileDirectory) {
		return FileUtils.listFiles(fileDirectory, null, false).isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidIOService#loadMediaFiles(java
	 * .io.File, fr.ornidroid.bo.Bird)
	 */
	public void loadMediaFiles(final String fileDirectory, final Bird bird,
			final OrnidroidFileType fileType) throws OrnidroidException {
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
			final String ornidroidMediaHome, final String directoryName,
			final OrnidroidFileType fileType, final boolean downloadFromInternet)
			throws OrnidroidException {
		final List<AbstractOrnidroidFile> files = new ArrayList<AbstractOrnidroidFile>();
		if (StringUtils.isNotBlank(directoryName)) {
			final File filesDirectory = new File(ornidroidMediaHome
					+ File.separator + directoryName);
			if (!filesDirectory.exists()) {
				try {
					FileUtils.forceMkdir(filesDirectory);
				} catch (final IOException e) {
					throw new OrnidroidException(
							OrnidroidError.ORNIDROID_HOME_NOT_FOUND, e);
				}
			}
			if (filesDirectory.isDirectory()) {
				List<File> filesList = Arrays.asList(filesDirectory
						.listFiles(new OrnidroidFileFilter(fileType)));
				// no files in the local directory. Try to download from
				// internet
				if ((filesList.size() == 0) && downloadFromInternet) {
					filesList = this.downloadHelper.downloadFiles(
							ornidroidMediaHome, directoryName, fileType);
				}
				try {
					// TODO : getOrnidroidSearchLang ??
					for (final File file : filesList) {
						final AbstractOrnidroidFile ornidroidFile = OrnidroidFileFactoryImpl
								.getFactory().createOrnidroidFile(
										file.getAbsolutePath(), fileType,
										Constants.getOrnidroidSearchLang());
						files.add(ornidroidFile);
					}
				} catch (final FileNotFoundException e) {
					// one of the media files doesnt have its properties.
					// this reveals a pb in the downloading : maybe the picture
					// or the sound file is corrupted
					// we remove the entire bird media directory and don't load
					// anything.
					files.clear();
					// Log.w(Constants.LOG_TAG, e.getMessage());
					// Log.w(Constants.LOG_TAG,
					// "The directory " + filesDirectory.getPath()
					// + " is going to be deleted");
					try {
						FileUtils.forceDelete(filesDirectory);
					} catch (final IOException e1) {
						// Log.e(Constants.LOG_TAG,
						// "Unable to delete the directory "
						// + filesDirectory.getPath());
					}

				}
			}
		}
		return files;
	}

}
