package fr.ornidroid.helper;

/**
 * The Class BasicConstants.
 */
public class BasicConstants {
	/** The Constant AUDIO Directory. */
	public static final String AUDIO_DIRECTORY = "audio";

	/** The Constant BIRD_ICONS_DIRECTORY. */
	public static final String BIRD_ICONS_DIRECTORY = "bird_icons";

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

	/** The Constant IMAGES_DIRECTORY. */
	public static final String IMAGES_DIRECTORY = "images";
}
