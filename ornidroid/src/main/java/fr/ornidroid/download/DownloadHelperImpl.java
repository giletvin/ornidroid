package fr.ornidroid.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.FileHelper;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;

/**
 * The Class DownloadHelperImpl.
 */
public class DownloadHelperImpl implements DownloadHelperInterface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.download.DownloadHelperInterface#downloadFile(java
	 * .lang.String, java.lang.String, java.lang.String,
	 * fr.ornidroid.download.MimeType)
	 */
	public File downloadFile(final String baseUrl, final String fileName,
			final String destinationPath) throws OrnidroidException {
		URL url;
		File downloadedFile = null;
		try {
			url = new URL(baseUrl + File.separator + fileName);
			final DefaultDownloadable mediaFileDownloadable = new DefaultDownloadable(
					url, destinationPath);
			mediaFileDownloadable.download();
			checkDownloadErrors(mediaFileDownloadable);
			// it is important to download the properties file AFTER the media
			// since its presence is used to control the validity of the
			// downloading
			url = new URL(baseUrl + File.separator + fileName
					+ OrnidroidFile.PROPERTIES_SUFFIX);
			final DefaultDownloadable propertiesFileDownloadable = new DefaultDownloadable(
					url, destinationPath);
			propertiesFileDownloadable.download();
			checkDownloadErrors(mediaFileDownloadable);
			// if the mediaFile exists, return it. Otherwise, we return null
			if ((mediaFileDownloadable.getFile() != null)
					&& mediaFileDownloadable.getFile().exists()) {
				downloadedFile = mediaFileDownloadable.getFile();
			}
		} catch (final MalformedURLException e) {
			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_DOWNLOAD_ERROR, e);
		}
		return downloadedFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.download.DownloadHelperInterface#downloadFiles(
	 * java.lang.String, java.lang.String, fr.ornidroid.bo.OrnidroidFileType)
	 */
	public List<File> downloadFiles(final String ornidroidMediaHome,
			final String directoryName, final OrnidroidFileType fileType)
			throws OrnidroidException {
		final String destinationPath = ornidroidMediaHome + File.separator
				+ directoryName;
		final String baseUrl = getBaseUrl(directoryName, fileType);

		final String[] filesToDownload = readContentFile(baseUrl,
				destinationPath);
		final List<File> downloadedFiles = new ArrayList<File>();
		if (null != filesToDownload) {
			for (final String fileName : filesToDownload) {
				final File downloadedFile = downloadFile(baseUrl, fileName,
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
	 * fr.ornidroid.download.DownloadHelperInterface#getBaseUrl(java.lang.String
	 * , fr.ornidroid.bo.OrnidroidFileType)
	 */
	public String getBaseUrl(final String directoryName,
			final OrnidroidFileType fileType) {
		String baseUrl = null;

		switch (fileType) {
		case PICTURE:
			baseUrl = DownloadConstants.getOrnidroidWebSiteImages()
					+ File.separator + directoryName;

			break;
		case AUDIO:
			baseUrl = DownloadConstants.getOrnidroidWebSiteAudio()
					+ File.separator + directoryName;
			break;
		case WIKIPEDIA_PAGE:
			baseUrl = DownloadConstants.getOrnidroidWebSiteWikipedia()
					+ File.separator + directoryName;
		default:
			break;
		}
		return baseUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.download.DownloadHelperInterface#readContentFile
	 * (java.lang.String, java.lang.String)
	 */
	public String[] readContentFile(final String baseUrl,
			final String destinationPath) throws OrnidroidException {
		URL url;
		String[] filesToDownload = null;
		FileInputStream fis = null;
		try {
			url = new URL(baseUrl + File.separator
					+ BasicConstants.CONTENTS_PROPERTIES);
			final DefaultDownloadable contentFileDownloadable = new DefaultDownloadable(
					url, destinationPath);
			contentFileDownloadable.refresh();
			checkDownloadErrors(contentFileDownloadable);
			if (contentFileDownloadable.getFile().exists()) {
				filesToDownload = FileHelper
						.parseContentFile(contentFileDownloadable.getFile());
			}
		} catch (final MalformedURLException e) {
			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_DOWNLOAD_ERROR, e);
		} catch (final FileNotFoundException e) {
			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_DOWNLOAD_ERROR, e);
		} catch (final IOException e) {
			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_DOWNLOAD_ERROR, e);
		} finally {
			if (null != fis) {
				try {
					fis.close();
					fis = null;
				} catch (final IOException e) {
					throw new OrnidroidException(
							OrnidroidError.ORNIDROID_DOWNLOAD_ERROR, e);
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
	private void checkDownloadErrors(final Downloadable downloadableFile)
			throws OrnidroidException {
		switch (downloadableFile.getStatus()) {
		case CONNECTION_PROBLEM:
			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_CONNECTION_PROBLEM, null);
		case BROKEN:
			throw new OrnidroidException(
					OrnidroidError.ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST,
					null);

		}

	}
}
