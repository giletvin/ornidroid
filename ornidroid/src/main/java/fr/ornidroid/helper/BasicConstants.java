package fr.ornidroid.helper;

/**
 * The Class BasicConstants.
 */
public class BasicConstants {
	/** The Constant AUDIO Directory. */
	public static final String AUDIO_DIRECTORY = "audio";
	/** The Constant BIRD_PARAMETER_NAME. */
	public static final String BIRD_DIRECTORY_PARAMETER_NAME = "BIRD_DIRECTORY";
	/** The Constant BIRD_ICONS_DIRECTORY. */
	public static final String BIRD_ICONS_DIRECTORY = "bird_icons";
	/** The Constant BIRD_ID_PARAMETER_NAME. */
	public static final String BIRD_ID_PARAMETER_NAME = "BIRD_ID";

	/** The Constant BLANK_STRING. */
	public static final String BLANK_STRING = " ";
	/** The Constant CARRIAGE_RETURN. */
	public static final String CARRIAGE_RETURN = "\n";

	/** The Constant COLUMN_STRING. */
	public static final String COLUMN_STRING = ": ";

	/** The Constant UNDERSCORE_STRING. */
	public static final String UNDERSCORE_STRING = "_";

	/** The Constant COMMA. */
	public static final String COMMA_STRING = ",";
	/** The Constant CONTENTS_PROPERTIES. */
	public static final String CONTENTS_PROPERTIES = "contents.properties";

	/** The Constant CUSTOM_MEDIA_FILE_PREFIX. */
	public static final String CUSTOM_MEDIA_FILE_PREFIX = "custom_";
	/**
	 * The Constant DB_CHECKSIZE_NAME. This file contains the size ot the
	 * database file. It is used to detect a new version of the database. If a
	 * new version of the apk file comes with a new version of the db, its
	 * checksize file will have a different value than the size of the locally
	 * installed db.
	 */
	public final static String DB_CHECKSIZE_NAME = "ornidroid.jpg.size";

	/**
	 * The D b_ name. It ends with a .jpg extension although it is a sqlite file
	 */
	public final static String DB_NAME = "ornidroid.jpg";

	/** The Constant EMPTY_STRING. */
	public static final String EMPTY_STRING = "";

	/** The Constant EQUALS_STRING. */
	public static final String EQUALS_STRING = "=";

	/** The Constant IMAGES_DIRECTORY. */
	public static final String IMAGES_DIRECTORY = "images";
	/** The Constant LOG_TAG. */
	public static final String LOG_TAG = "Ornidroid";

	/** The Constant MP3_PATH. */
	public static final String MP3_PATH = "MP3_PATH";
	/** The Constant NO_MEDIA_FILENAME. */
	public static final String NO_MEDIA_FILENAME = ".nomedia";
	/** The Constant SLASH_STRING. */
	public static final String SLASH_STRING = "/";

	/**
	 * The junit context. Always false, except when running junit tests. This is
	 * to avoid the "Stub" Runtime Error when calling android SDK methods
	 */
	private static boolean JUNIT_CONTEXT = false;

	/**
	 * Checks if is junit context.
	 * 
	 * @return true, if is junit context
	 */
	public static boolean isJunitContext() {
		return JUNIT_CONTEXT;
	}

	/**
	 * Sets the junit context. This method is only to be used from the Junit
	 * tests
	 * 
	 * @param junitContext
	 *            the new junit context
	 */
	public static void setJunitContext(final boolean junitContext) {
		JUNIT_CONTEXT = junitContext;
	}
}
