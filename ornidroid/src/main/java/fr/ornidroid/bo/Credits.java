package fr.ornidroid.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ornidroid.helper.StringHelper;

/**
 * The Class Credits.
 */
public class Credits {

	/** The Constant FIELD_SEPARATOR in the credit array */
	private static final String FIELD_SEPARATOR = "|";

	/** The Constant CREDIT_LICENSE. */
	public static final String CREDIT_LICENSE = "credit_license";

	/** The Constant CREDIT_URL. */
	public static final String CREDIT_URL = "credit_url";

	/** The Constant CREDIT_AUTHOR. */
	public static final String CREDIT_AUTHOR = "credit_author";

	/** The Constant CREDIT_TITLE. */
	public static final String CREDIT_TITLE = "credit_title";

	/** The credits. */
	private final String[] credits;

	/** The credit map. */
	private Map<String, String> creditMap;

	/**
	 * Instantiates a new credits.
	 * 
	 * @param pCredits
	 *            the credits
	 */
	public Credits(String[] pCredits) {
		credits = pCredits;

	}

	/**
	 * Gets the list credits.
	 * 
	 * @return the list credits
	 */
	public List<Map<String, String>> getListCredits() {
		List<Map<String, String>> creditList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < credits.length; i++) {
			creditMap = new HashMap<String, String>();
			String[] creditArray = StringHelper.split(credits[i],
					FIELD_SEPARATOR);
			loadCredit(CREDIT_TITLE, 0, creditArray);
			loadCredit(CREDIT_AUTHOR, 1, creditArray);
			loadCredit(CREDIT_URL, 2, creditArray);
			loadCredit(CREDIT_LICENSE, 3, creditArray);
			creditList.add(creditMap);
		}
		return creditList;
	}

	/**
	 * Load credit.
	 * 
	 * @param key
	 *            the key
	 * @param i
	 *            the i
	 * @param creditArray
	 *            the credit array
	 */
	private void loadCredit(String key, int i, String[] creditArray) {
		try {
			creditMap.put(key, creditArray[i]);
		} catch (IndexOutOfBoundsException e) {
		}
	}
}
