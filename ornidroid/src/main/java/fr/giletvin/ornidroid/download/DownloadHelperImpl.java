package fr.giletvin.ornidroid.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import fr.giletvin.ornidroid.bo.AbstractOrnidroidFile;
import fr.giletvin.ornidroid.bo.OrnidroidFileType;
import fr.giletvin.ornidroid.helper.OrnidroidError;
import fr.giletvin.ornidroid.helper.OrnidroidException;

/**
 * The Class DownloadHelperImpl.
 */
public class DownloadHelperImpl implements DownloadHelperInterface {

	/** The Constant CONTENTS_PROPERTIES. */
	private static final String CONTENTS_PROPERTIES = "contents.properties";

	/** The Constant FILES_PROPERTY_KEY. */
	private static final String FILES_PROPERTY_KEY = "files";

	/** The Constant FILES_SEPARATOR_PROPERTY_VALUE. */
	private static final String FILES_SEPARATOR_PROPERTY_VALUE = ",";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.download.DownloadHelperInterface#downloadFile(java
	 * .lang.String, java.lang.String, java.lang.String,
	 * fr.giletvin.ornidroid.download.MimeType)
	 */
	public File downloadFile(String baseUrl, String fileName,
			String destinationPath) throws OrnidroidException {
		URL url;
		File downloadedFile = null;
		try {
			url = new URL(baseUrl + File.separator + fileName);
			DefaultDownloadable mediaFileDownloadable = new DefaultDownloadable(
					url, destinationPath);
			mediaFileDownloadable.download();
			checkDownloadErrors(mediaFileDownloadable);
			// it is important to download the properties file AFTER the media
			// since its presence is used to control the validity of the
			// downloading
			url = new URL(baseUrl + File.separator + fileName
					+ AbstractOrnidroidFile.PROPERTIES_SUFFIX);
			DefaultDownloadable propertiesFileDownloadable = new DefaultDownloadable(
					url, destinationPath);
			propertiesFileDownloadable.download();
			checkDownloadErrors(mediaFileDownloadable);
			// if the mediaFile exists, return it. Otherwise, we return null
			if ((mediaFileDownloadable.getFile() != null)
					&& mediaFileDownloadable.getFile().exists()) {
				downloadedFile = mediaFileDownloadable.getFile();
			}
		} catch (MalformedURLException e) {
			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_DOWNLOAD_ERROR);
		}
		return downloadedFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.download.DownloadHelperInterface#downloadFiles(
	 * java.lang.String, java.lang.String,
	 * fr.giletvin.ornidroid.bo.OrnidroidFileType)
	 */
	public List<File> downloadFiles(String ornidroidMediaHome,
			String directoryName, OrnidroidFileType fileType)
			throws OrnidroidException {
		String baseUrl = null;
		String destinationPath = null;

		switch (fileType) {
		case PICTURE:
			baseUrl = DownloadConstants.getOrnidroidWebSiteImages()
					+ File.separator + directoryName;
			destinationPath = ornidroidMediaHome + File.separator
					+ directoryName;
			break;
		case AUDIO:
			baseUrl = DownloadConstants.getOrnidroidWebSiteAudio()
					+ File.separator + directoryName;
			destinationPath = ornidroidMediaHome + File.separator
					+ directoryName;
			break;
		default:
			break;
		}

		String[] filesToDownload = readContentFile(baseUrl, destinationPath);
		List<File> downloadedFiles = new ArrayList<File>();
		if (null != filesToDownload) {
			for (String fileName : filesToDownload) {
				File downloadedFile = downloadFile(baseUrl, fileName,
						destinationPath);
				if ((null != downloadedFile) && downloadedFile.exists()) {
					downloadedFiles.add(downloadedFile);
				}
			}
		}
		return downloadedFiles;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.download.DownloadHelperInterface#readContentFile
	 * (java.lang.String, java.lang.String)
	 */
	public String[] readContentFile(String baseUrl, String destinationPath)
			throws OrnidroidException {
		URL url;
		String[] filesToDownload = null;
		FileInputStream fis = null;
		try {
			url = new URL(baseUrl + File.separator + CONTENTS_PROPERTIES);
			DefaultDownloadable contentFileDownloadable = new DefaultDownloadable(
					url, destinationPath);
			contentFileDownloadable.download();
			checkDownloadErrors(contentFileDownloadable);
			if (contentFileDownloadable.getFile().exists()) {
				// contentFileDownloadable.getFile();
				fis = new FileInputStream(contentFileDownloadable.getFile());
				Properties properties = new Properties();
				properties.load(fis);
				String files = StringUtils.defaultString(properties
						.getProperty(FILES_PROPERTY_KEY));
				filesToDownload = StringUtils.split(files,
						FILES_SEPARATOR_PROPERTY_VALUE);
			}

		} catch (MalformedURLException e) {

			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_DOWNLOAD_ERROR);
		} catch (FileNotFoundException e) {

			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_DOWNLOAD_ERROR);
		} catch (IOException e) {

			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_DOWNLOAD_ERROR);
		} finally {
			if (null != fis) {
				try {
					fis.close();
					fis = null;
				} catch (IOException e) {
					throw new OrnidroidException(
							OrnidroidError.ORNIDROID_DOWNLOAD_ERROR);
				}
			}

		}

		return filesToDownload;

	}

	/**
	 * Check download errors. Check the status of the downloadable.
	 * 
	 * @param downloadableFile
	 *            the downloadable file
	 * @throws OrnidroidException
	 *             the ornidroid exception if the status is an error.
	 */
	private void checkDownloadErrors(Downloadable downloadableFile)
			throws OrnidroidException {
		switch (downloadableFile.getStatus()) {
		case CONNECTION_PROBLEM:
			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_CONNECTION_PROBLEM);
		case BROKEN:
			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST);

		}

	}
}
