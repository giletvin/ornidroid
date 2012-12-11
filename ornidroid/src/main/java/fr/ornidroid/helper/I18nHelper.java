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

	/** The Constant ISO3_DEU. */
	public static final String ISO3_DEU = "deu";

	/** The Constant ISO3_FRA. */
	public static final String ISO3_FRA = "fra";

	/** The singleton. */
	private static I18nHelper SINGLETON;

	/**
	 * Gets the lang.
	 * 
	 * 
	 * @return the lang
	 */
	public static String getLang() {
		if (SINGLETON == null) {
			SINGLETON = new I18nHelper();
		}
		return SINGLETON.getAndroidLang();
	}

	/** The android lang. */
	private final String androidLang;

	/**
	 * Instantiates a new i18n helper.
	 */
	private I18nHelper() {
		final String langCode = Locale.getDefault().getISO3Language();

		if (StringUtils.equalsIgnoreCase(ISO3_DEU, langCode)) {
			this.androidLang = DEUTSCH;
		} else {
			if (StringUtils.equalsIgnoreCase(ISO3_FRA, langCode)) {
				this.androidLang = FRENCH;
			} else {
				this.androidLang = ENGLISH;
			}
		}

	}

	/**
	 * Gets the android lang.
	 * 
	 * @return the android lang
	 */
	private String getAndroidLang() {
		return this.androidLang;
	}
}
