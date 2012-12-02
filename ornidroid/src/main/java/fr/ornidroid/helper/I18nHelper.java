package fr.ornidroid.helper;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * The Class I18nHelper.
 */
public class I18nHelper {

	/** The Constant DEUTSCH. */
	public static final String DEUTSCH = "de";

	/** The Constant ENGLISH. */
	public static final String ENGLISH = "en";

	/** The Constant FRENCH. */
	public static final String FRENCH = "fr";

	/**
	 * Gets the lang.
	 * 
	 * 
	 * @return the lang
	 */
	public static String getLang() {
		final String langCode = Locale.getDefault().getDisplayLanguage();
		if (StringUtils.equalsIgnoreCase(DEUTSCH, langCode)) {
			return DEUTSCH;
		}
		if (StringUtils.equalsIgnoreCase(FRENCH, langCode)) {
			return FRENCH;
		}
		return ENGLISH;
	}
}
