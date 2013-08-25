package fr.ornidroid.helper;

/**
 * The Enum SupportedLanguages.
 */
public enum SupportedLanguage {

	/** The english. */
	ENGLISH("en"),
	/** The french. */
	FRENCH("fr"),
	/** The german. */
	GERMAN("de");
	/** The Constant ISO3_DEU. */
	public static final String ISO3_DEU = "deu";

	/** The Constant ISO3_FRA. */
	public static final String ISO3_FRA = "fra";

	/**
	 * Gets the supported language from iso3 code.
	 * 
	 * @param iso3Code
	 *            the iso3 code
	 * @return the supported language from iso3 code
	 */
	public static SupportedLanguage getSupportedLanguageFromIso3Code(
			final String iso3Code) {
		if (StringHelper.equalsIgnoreCase(ISO3_DEU, iso3Code)) {
			return SupportedLanguage.GERMAN;
		} else {
			if (StringHelper.equalsIgnoreCase(ISO3_FRA, iso3Code)) {
				return SupportedLanguage.FRENCH;
			} else {
				return SupportedLanguage.ENGLISH;
			}
		}
	}

	/** The code. */
	private final String code;

	/**
	 * Instantiates a new supported languages.
	 * 
	 * @param pCode
	 *            the code
	 */
	private SupportedLanguage(final String pCode) {
		this.code = pCode;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}
}
