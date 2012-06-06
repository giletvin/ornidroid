package fr.giletvin.ornidroid.download;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import fr.giletvin.ornidroid.R;
import fr.giletvin.ornidroid.helper.BasicConstants;
import fr.giletvin.ornidroid.helper.Constants;

/**
 * The Class DownloadConstants.
 */
public class DownloadConstants {

	/** The Constant DOWNLOAD_SITE_PROPERTY_KEY. */
	private static final String DOWNLOAD_SITE_PROPERTY_KEY = "ornidroid_download_site";

	/** The Constant DEFAULT_DOWNLOAD_SITE. */
	private static final String DEFAULT_DOWNLOAD_SITE = "http://ornidroid.free.fr/ornidroid";

	/** The Constant JUNIT_DOWNLOAD_SITE. */
	private static final String JUNIT_DOWNLOAD_SITE = "http://ornidroid.free.fr/tests";
	/** The Constant ORNIDROID_WEB_SITE_ROOT. */
	private static String ORNIDROID_WEB_SITE_ROOT;

	/**
	 * Gets the ornidroid web site images.
	 * 
	 * @return the ornidroid web site images
	 */
	public static String getOrnidroidWebSiteImages() {
		loadWebSiteRoot();
		return ORNIDROID_WEB_SITE_ROOT + File.separator
				+ BasicConstants.IMAGES_DIRECTORY;
	}

	/**
	 * Gets the ornidroid web site audio.
	 * 
	 * @return the ornidroid web site audio
	 */
	public static String getOrnidroidWebSiteAudio() {
		loadWebSiteRoot();
		return ORNIDROID_WEB_SITE_ROOT + File.separator
				+ BasicConstants.AUDIO_DIRECTORY;
	}

	/**
	 * Gets the ornidroid web site.
	 * 
	 * @return the ornidroid web site
	 */
	public static String getOrnidroidWebSite() {
		loadWebSiteRoot();
		return ORNIDROID_WEB_SITE_ROOT;
	}

	/**
	 * Load web site root.
	 */
	private static void loadWebSiteRoot() {
		if (StringUtils.isBlank(ORNIDROID_WEB_SITE_ROOT)) {
			try {
				InputStream rawResource = Constants.getCONTEXT().getResources()
						.openRawResource(R.raw.ornidroid);
				Properties properties = new Properties();
				properties.load(rawResource);
				ORNIDROID_WEB_SITE_ROOT = properties.getProperty(
						DOWNLOAD_SITE_PROPERTY_KEY, DEFAULT_DOWNLOAD_SITE);
			} catch (Exception e) {
				// only for junit : exception android stub
				ORNIDROID_WEB_SITE_ROOT = JUNIT_DOWNLOAD_SITE;
			} finally {
				if (StringUtils.isBlank(ORNIDROID_WEB_SITE_ROOT)) {
					ORNIDROID_WEB_SITE_ROOT = DEFAULT_DOWNLOAD_SITE;
				}
			}
		}
	}
}
