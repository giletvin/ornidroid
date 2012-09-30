package fr.ornidroid.helper;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import fr.ornidroid.R;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.OrnidroidFileType;

/**
 * The Class Constants. This file has imports from android sdk packages and thus
 * cannot be used in unit tests scope. The Constants that can be used in Junit
 * are placed in BasicConstants. <br>
 */
public class Constants extends BasicConstants {

	/** The CONTEXT. */
	private static Context CONTEXT;

	/** The Constant ORNIDROID_LANG_DEFAULT_VALUE. */
	private static final String ORNIDROID_LANG_DEFAULT_VALUE = I18nHelper.FRENCH;

	/** The Constant ORNIDROID_PREFERENCES_FILE_NAME. */
	private static final String ORNIDROID_PREFERENCES_FILE_NAME = "fr.ornidroid_preferences";

	/**
	 * Gets the cONTEXT.
	 * 
	 * @return the cONTEXT
	 */
	public static Context getCONTEXT() {
		return CONTEXT;
	}

	/**
	 * Gets the ornidroid home media of a given bird
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
			mediaHome = Constants.getOrnidroidHomeAudio();
			break;
		case PICTURE:
			mediaHome = Constants.getOrnidroidHomeImages();
			break;
		}
		return mediaHome + File.separator + bird.getBirdDirectoryName();
	}

	/**
	 * Gets the ornidroid db path.
	 * 
	 * @return the ornidroid db path
	 */
	public static final String getOrnidroidDbPath() {
		return Constants.getOrnidroidHome() + File.separator + DB_NAME;
	}

	/**
	 * Gets the ornidroid home.
	 * 
	 * @return the ornidroid home. Never null. If empty, returns a default value
	 */
	public static final String getOrnidroidHome() {
		return StringUtils
				.defaultIfBlank(
						Constants
								.getOrnidroidPreferences()
								.getString(
										getStringFromXmlResource(R.string.preferences_home_key),
										Constants
												.getOrnidroidHomeDefaultValue()),
						Constants.getOrnidroidHomeDefaultValue());
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
	 * Gets the ornidroid audio directory of a given bird.
	 * 
	 * @param bird
	 *            the bird
	 * @return the ornidroid audio directory
	 */
	public static final String getOrnidroidHomeAudio(final Bird bird) {
		return Constants.getOrnidroidHomeAudio() + File.separator
				+ bird.getBirdDirectoryName();
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
	 * Gets the ornidroid lang.
	 * 
	 * @return the ornidroid lang, never null. Default is French.
	 */
	public static final String getOrnidroidLang() {
		try {
			return Constants.getOrnidroidPreferences().getString(
					getStringFromXmlResource(R.string.preferences_lang_key),
					ORNIDROID_LANG_DEFAULT_VALUE);
		} catch (final NullPointerException e) {
			// should not occur. Only for test purposes
			return ORNIDROID_LANG_DEFAULT_VALUE;
		}
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
	 * not set by the user
	 * 
	 * @return the ornidroid home default value : directory "ornidroid" on the
	 *         external storage
	 */
	private static final String getOrnidroidHomeDefaultValue() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + "ornidroid/";
	}

	/**
	 * Gets the ornidroid preferences.
	 * 
	 * @return the ornidroid preferences
	 */
	private static final SharedPreferences getOrnidroidPreferences() {
		return CONTEXT.getSharedPreferences(ORNIDROID_PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
	}
}
