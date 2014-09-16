package fr.ornidroid.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import fr.ornidroid.R;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.ui.preferences.ListPreferenceMultiSelect;

/**
 * The Class Constants. This file has imports from android sdk packages and thus
 * cannot be used in unit tests scope. The Constants that can be used in Junit
 * are placed in BasicConstants. <br>
 */
public class Constants extends BasicConstants {

	/** The Constant ORNIDROID_DIRECTORY_NAME. */
	public static final String ORNIDROID_DIRECTORY_NAME = "ornidroid";

	/** The CONTEXT. */
	private static Context CONTEXT;

	/** The ornidroid home default value. */
	private static String ORNIDROID_HOME_DEFAULT_VALUE;

	/** The Constant ORNIDROID_PREFERENCES_FILE_NAME. */
	private static final String ORNIDROID_PREFERENCES_FILE_NAME = "fr.ornidroid_preferences";

	/** The Constant ORNIDROID_SEARCH_LANG_DEFAULT_VALUE. */
	private static final String ORNIDROID_SEARCH_LANG_DEFAULT_VALUE = SupportedLanguage.FRENCH
			.getCode();

	/**
	 * Gets the automatic update check preference.
	 * 
	 * @return the automatic update check preference
	 */
	public static final boolean getAutomaticUpdateCheckPreference() {
		return Constants
				.getOrnidroidPreferences()
				.getBoolean(
						getStringFromXmlResource(R.string.preferences_automatic_update_check_key),
						false);
	}

	/**
	 * Gets the cONTEXT.
	 * 
	 * @return the cONTEXT
	 */
	public static Context getCONTEXT() {
		return CONTEXT;
	}

	/**
	 * Gets the ornidroid home media of a given bird.
	 * 
	 * @param bird
	 *            the bird
	 * @param fileType
	 *            the file type
	 * @return the ornidroid home media
	 */
	public static final String getOrnidroidBirdHomeMedia(final Bird bird,
			final OrnidroidFileType fileType) {
		String mediaHome = null;
		switch (fileType) {
		case AUDIO:
			mediaHome = Constants.getOrnidroidHomeAudio() + File.separator
					+ bird.getBirdDirectoryName();
			break;
		case PICTURE:
			mediaHome = Constants.getOrnidroidHomeImages() + File.separator
					+ bird.getBirdDirectoryName();
			break;
		case WIKIPEDIA_PAGE:
			mediaHome = Constants.getOrnidroidHomeWikipedia() + File.separator
					+ I18nHelper.getLang().getCode();
		}
		return mediaHome;
	}

	/**
	 * Gets the ornidroid home.
	 * 
	 * @return the ornidroid home
	 */
	public static final String getOrnidroidHome() {
		return Constants
				.getOrnidroidPreferences()
				.getString(
						getStringFromXmlResource(R.string.preferences_ornidroid_home_key),
						Constants.getOrnidroidHomeDefaultValue());
	}

	/**
	 * Gets the ornidroid home.
	 * 
	 * 
	 * @return the ornidroid home default value : directory "ornidroid" on the
	 *         external storage
	 */
	public static final String getOrnidroidHome__() {
		if (StringHelper.isBlank(ORNIDROID_HOME_DEFAULT_VALUE)) {
			if (isExternalStorageWritable()) {
				ORNIDROID_HOME_DEFAULT_VALUE = Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ File.separator + ORNIDROID_DIRECTORY_NAME;
			} else {
				// external storage not available. Try on the data directory of
				// the app
				// something like /data/data/fr.ornidroid/files/
				ORNIDROID_HOME_DEFAULT_VALUE = CONTEXT.getFilesDir()
						.getAbsolutePath()
						+ File.separator
						+ ORNIDROID_DIRECTORY_NAME;
			}
		}
		return ORNIDROID_HOME_DEFAULT_VALUE;
	}

	/**
	 * Gets the ornidroid home audio.
	 * 
	 * @return the ornidroid home audio
	 */
	public static final String getOrnidroidHomeAudio() {
		return Constants.getOrnidroidHome() + File.separator + AUDIO_DIRECTORY;
	}

	/**
	 * Gets the ornidroid images directory.
	 * 
	 * @return the ornidroid images directory
	 */
	public static final String getOrnidroidHomeImages() {
		return Constants.getOrnidroidHome() + File.separator + IMAGES_DIRECTORY;
	}

	/**
	 * Gets the ornidroid home media.
	 * 
	 * @param fileType
	 *            the file type
	 * @return the ornidroid home media
	 */
	public static final String getOrnidroidHomeMedia(OrnidroidFileType fileType) {
		String path = null;
		switch (fileType) {
		case AUDIO:
			path = getOrnidroidHomeAudio();
			break;
		case PICTURE:
			path = getOrnidroidHomeImages();
			break;
		case WIKIPEDIA_PAGE:
			path = getOrnidroidHomeWikipedia();
			break;
		}
		return path;

	}

	/**
	 * Gets the ornidroid preferences.
	 * 
	 * @return the ornidroid preferences
	 */
	public static final SharedPreferences getOrnidroidPreferences() {
		return CONTEXT.getSharedPreferences(ORNIDROID_PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * Gets the ornidroid search lang used in the search engine. It is not the
	 * same as the UI lang!
	 * 
	 * @return the ornidroid search languages, never null. Default is a set with
	 *         only French.
	 */
	public static final List<String> getOrnidroidSearchLanguages() {
		List<String> ornidroidSearchLanguages;
		try {
			String languagesFromPreferences = Constants
					.getOrnidroidPreferences()
					.getString(
							getStringFromXmlResource(R.string.preferences_lang_key),
							ORNIDROID_SEARCH_LANG_DEFAULT_VALUE);
			ornidroidSearchLanguages = StringHelper.parseListPreferenceValue(
					languagesFromPreferences,
					ListPreferenceMultiSelect.DEFAULT_SEPARATOR);
			if (ornidroidSearchLanguages.size() == 0) {
				ornidroidSearchLanguages
						.add(ORNIDROID_SEARCH_LANG_DEFAULT_VALUE);
			}

		} catch (final NullPointerException e) {
			// this should not occur
			ornidroidSearchLanguages = new ArrayList<String>();
			ornidroidSearchLanguages.add(ORNIDROID_SEARCH_LANG_DEFAULT_VALUE);
		}
		return ornidroidSearchLanguages;
	}

	/**
	 * Gets the string from xml resource.
	 * 
	 * @param resId
	 *            the res id
	 * @return the string from xml resource
	 */
	public static final String getStringFromXmlResource(final int resId) {
		return CONTEXT.getString(resId);
	}

	/**
	 * Initialize constants. Called from the constructor of the main activity.
	 * The context is mandatory for other methods
	 * 
	 * @param context
	 *            the context
	 */
	public static void initializeConstants(final Context context) {
		CONTEXT = context;
	}

	/**
	 * Gets the ornidroid home default value if the OrnidroidHomePreference is
	 * not set by the user.
	 * 
	 * @return the ornidroid home default value : directory "ornidroid" on the
	 *         external storage
	 */
	private static final String getOrnidroidHomeDefaultValue() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + ORNIDROID_DIRECTORY_NAME;
	}

	/**
	 * Checks if is external storage writable.
	 * 
	 * @return true, if is external storage writable
	 */
	private static final boolean isExternalStorageWritable() {
		boolean mExternalStorageWriteable;
		final String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageWriteable = false;
		}
		return mExternalStorageWriteable;
	}

	/**
	 * Gets the ornidroid home wikipedia.
	 * 
	 * @return the ornidroid home wikipedia
	 */
	public static final String getOrnidroidHomeWikipedia() {
		return Constants.getOrnidroidHome() + File.separator
				+ WIKIPEDIA_DIRECTORY;
	}

	/**
	 * Gets the application name.
	 * 
	 * @return the application name
	 */
	public static final String getApplicationName() {
		final PackageManager pm = CONTEXT.getPackageManager();
		ApplicationInfo ai;
		try {
			ai = pm.getApplicationInfo(CONTEXT.getPackageName(), 0);
		} catch (final NameNotFoundException e) {
			ai = null;
		}
		final String applicationName = (String) (ai != null ? pm
				.getApplicationLabel(ai) : "(unknown)");
		return applicationName;
	}

	/**
	 * Gets the version name.
	 * 
	 * @return the version name
	 */
	public static final String getVersionName() {
		final PackageManager pm = CONTEXT.getPackageManager();
		PackageInfo pinfo;
		try {
			pinfo = pm.getPackageInfo(CONTEXT.getPackageName(), 0);
		} catch (final NameNotFoundException e) {
			pinfo = null;
		}
		final String versionName = pinfo != null ? pinfo.versionName
				: "(unknown)";
		return versionName;
	}

	/**
	 * Gets the version name.
	 * 
	 * @return the version name
	 */
	public static final int getVersionCode() {
		final PackageManager pm = CONTEXT.getPackageManager();
		PackageInfo pinfo;
		try {
			pinfo = pm.getPackageInfo(CONTEXT.getPackageName(), 0);
		} catch (final NameNotFoundException e) {
			pinfo = null;
		}
		final int versionName = pinfo != null ? pinfo.versionCode : 0;
		return versionName;
	}
}
