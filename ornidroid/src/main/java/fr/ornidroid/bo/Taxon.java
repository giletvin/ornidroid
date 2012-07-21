package fr.ornidroid.bo;

/**
 * The Class Taxon.
 */
public class Taxon {

	/** The name. */
	private final String name;

	/** The lang. */
	private final String lang;

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the lang.
	 * 
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * Instantiates a new taxon.
	 * 
	 * @param pLang
	 *            the lang
	 * @param pName
	 *            the name
	 */
	public Taxon(String pLang, String pName) {
		this.lang = pLang;
		this.name = pName;
	}
}
