package fr.giletvin.ornidroid.helper;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import fr.giletvin.ornidroid.R;
import fr.giletvin.ornidroid.bo.Bird;
import fr.giletvin.ornidroid.bo.OrnidroidFileType;

/**
 * The Class Constants. This file has imports from android sdk packages and thus
 * cannot be used in unit tests scope. The Constants that can be used in Junit
 * are placed in BasicConstants. <br>
 */
public class Constants extends BasicConstants {

	/** The Constant ORNIDROID_HOME_DEFAULT_VALUE. */
	private static final String ORNIDROID_HOME_DEFAULT_VALUE = "/mnt/storage/ornidroid/";

	/** The Constant ORNIDROID_LANG_DEFAULT_VALUE. */
	private static final String ORNIDROID_LANG_DEFAULT_VALUE = I18nHelper.FRENCH;

	/** The Constant ORNIDROID_PREFERENCES_FILE_NAME. */
	private static final String ORNIDROID_PREFERENCES_FILE_NAME = "fr.giletvin.ornidroid_preferences";

	/** The Constant LOG_TAG. */
	public static final String LOG_TAG = "Ornidroid";

	/** The Constant MP3_PATH. */
	public static final String MP3_PATH = "MP3_PATH";

	/** The Constant BIRD_ID_PARAMETER_NAME. */
	public static final String BIRD_ID_PARAMETER_NAME = "BIRD_ID";

	/** The Constant CARRIAGE_RETURN. */
	public static final String CARRIAGE_RETURN = "\n";

	/** The CONTEXT. */
	private static Context CONTEXT;

	/**
	 * Gets the cONTEXT.
	 * 
	 * @return the cONTEXT
	 */
	public static Context getCONTEXT() {
		return CONTEXT;
	}

	/**
	 * Initialize constants. Called from the constructor of the main activity.
	 * The context is mandatory for other methods
	 * 
	 * @param context
	 *            the context
	 */
	public static void initializeConstants(Context context) {
		CONTEXT = context;
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

	/**
	 * Gets the ornidroid home.
	 * 
	 * @return the ornidroid home. Never null. If empty, returns a default value
	 */
	public static final String getOrnidroidHome() {
		return Constants.getOrnidroidPreferences().getString(
				getStringFromXmlResource(R.string.preferences_home_key),
				ORNIDROID_HOME_DEFAULT_VALUE);
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
	 * Gets the ornidroid home media of a given bird
	 * 
	 * @param bird
	 *            the bird
	 * @param fileType
	 *            the file type
	 * @return the ornidroid home media
	 */
	public static final String getOrnidroidBirdHomeMedia(Bird bird,
			OrnidroidFileType fileType) {
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
	public static final String getOrnidroidHomeAudio(Bird bird) {
		return Constants.getOrnidroidHomeAudio() + File.separator
				+ bird.getBirdDirectoryName();
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
	 * Gets the ornidroid lang.
	 * 
	 * @return the ornidroid lang, never null. Default is French.
	 */
	public static final String getOrnidroidLang() {
		try {
			return Constants.getOrnidroidPreferences().getString(
					getStringFromXmlResource(R.string.preferences_lang_key),
					ORNIDROID_LANG_DEFAULT_VALUE);
		} catch (NullPointerException e) {
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
	public static final String getStringFromXmlResource(int resId) {
		return CONTEXT.getString(resId);
	}
}
