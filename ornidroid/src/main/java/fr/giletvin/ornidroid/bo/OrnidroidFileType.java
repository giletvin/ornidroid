package fr.giletvin.ornidroid.bo;

/**
 * The Enum OrnidroidFileType.
 */
public enum OrnidroidFileType {

	/** The PICTURE. */
	PICTURE,
	/** The SOUND. */
	AUDIO;

	/** The Constant PICTURE_EXTENSION. */
	public final static String PICTURE_EXTENSION = ".jpg";

	/** The Constant AUDIO_EXTENSION. */
	public final static String AUDIO_EXTENSION = ".mp3";

	/**
	 * Gets the extension.
	 * 
	 * @param type
	 *            the type
	 * @return the extension
	 */
	public static String getExtension(OrnidroidFileType type) {
		String extension = null;
		switch (type) {
		case PICTURE:
			extension = PICTURE_EXTENSION;
			break;
		case AUDIO:
			extension = AUDIO_EXTENSION;
			break;

		}
		return extension;
	}

	/**
	 * Gets the code.
	 * 
	 * @param type
	 *            the type
	 * @return the code
	 */
	public static int getCode(OrnidroidFileType type) {
		int code = 0;
		switch (type) {
		case PICTURE:
			code = 0;
			break;
		case AUDIO:
			code = 1;
			break;

		}
		return code;
	}
}
