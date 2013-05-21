package fr.ornidroid.ui.multicriteriasearch;

import java.util.HashMap;
import java.util.Map;

import fr.ornidroid.R;

/**
 * The Class SpinnerIconSelector. This is a helper that allows to display icons
 * in the spinners.
 */
public class SpinnerIconSelector {

	/** The Constant AUSTRIA. */
	private static final String AUSTRIA = "AUT";
	/** The Constant BELGIUM. */
	private static final String BELGIUM = "BEL";

	/** The Constant BOSNIA. */
	private static final String BOSNIA = "BIH";

	/** The Constant BULGARIA. */
	private static final String BULGARIA = "BGR";

	private static Map<String, Integer> COUNTRIES_FLAGS_RES_ID = new HashMap<String, Integer>();

	/** The Constant CROATIA. */
	private static final String CROATIA = "HRV";

	/** The Constant CZECH. */
	private static final String CZECH = "CZE";

	/** The Constant DENMARK. */
	private static final String DENMARK = "DNK";

	/** The Constant ESTONIA. */
	private static final String ESTONIA = "EST";

	/** The Constant FINLAND. */
	private static final String FINLAND = "FIN";

	/** The Constant FRANCE. */
	private static final String FRANCE = "FRA";

	/** The Constant GBR. */
	private static final String GBR = "GBR";

	/** The Constant GERMANY. */
	private static final String GERMANY = "DEU";

	/** The Constant GREECE. */
	private static final String GREECE = "GRC";

	/** The Constant HUNGARY. */
	private static final String HUNGARY = "HUN";

	/** The Constant ICELAND. */
	private static final String ICELAND = "ISL";

	/** The Constant IRELAND. */
	private static final String IRELAND = "IRL";

	/** The Constant ITALY. */
	private static final String ITALY = "ITA";

	/** The Constant LATVIA. */
	private static final String LATVIA = "LVA";

	/** The Constant LITHUANIA. */
	private static final String LITHUANIA = "LTU";

	/** The Constant LUXEMBOURG. */
	private static final String LUXEMBOURG = "LUX";

	/** The Constant MACEDONIA. */
	private static final String MACEDONIA = "MKD";

	/** The Constant NETHERLANDS. */
	private static final String NETHERLANDS = "NLD";

	/** The Constant NORWAY. */
	private static final String NORWAY = "NOR";

	/** The Constant POLAND. */
	private static final String POLAND = "POL";

	/** The Constant PORTUGAL. */
	private static final String PORTUGAL = "PRT";

	/** The Constant ROMANIA. */
	private static final String ROMANIA = "ROM";

	/** The Constant SERBIA. */
	private static final String SERBIA = "YUG";

	/** The Constant SLOVAKIA. */
	private static final String SLOVAKIA = "SVK";

	/** The Constant SLOVENIA. */
	private static final String SLOVENIA = "SVN";

	/** The Constant SPAIN. */
	private static final String SPAIN = "ESP";

	/** The Constant SWEDEN. */
	private static final String SWEDEN = "SWE";

	/** The Constant SWITZERLAND. */
	private static final String SWITZERLAND = "CHE";

	/**
	 * Gets the icon resource id from beak form id. This is very dependant to
	 * the sql data. Maps the beak form id to an icon.
	 * 
	 * @param beakFormId
	 *            the beak form id
	 * @return the icon resource name from beak form id
	 */
	public static int getIconResourceIdFromBeakFormId(final int beakFormId) {

		// INSERT INTO beak_form(id,name,lang)
		// VALUES(1,"autres becs droits",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(2,"épais et court",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(3,"autre",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(4,"courbé",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(5,"droit et long",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(6,"crochu",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(7,"fin et court",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(8,"canard",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(9,"mouette",'fr');

		int resourceId = 0;

		switch (beakFormId) {
		case 1:
			resourceId = R.drawable.bec_autres_becs_droits;
			break;
		case 2:
			resourceId = R.drawable.bec_epais;
			break;
		case 3:
			resourceId = R.drawable.bec_autre;
			break;
		case 4:
			resourceId = R.drawable.bec_courbe;
			break;
		case 5:
			resourceId = R.drawable.bec_droit_long;
			break;
		case 6:
			resourceId = R.drawable.bec_crochu;
			break;
		case 7:
			resourceId = R.drawable.bec_fin_court;
			break;
		case 8:
			resourceId = R.drawable.bec_canard;
			break;
		case 9:
			resourceId = R.drawable.bec_mouette;
			break;
		default:
			resourceId = R.drawable.bec_tous;
			break;
		}
		return resourceId;
	}

	/**
	 * Gets the icon resource id from country code.
	 * 
	 * @param countrycode
	 *            the countrycode
	 * @return the icon resource id from country code
	 */
	public static int getIconResourceIdFromCountryCode(final String countrycode) {
		if (COUNTRIES_FLAGS_RES_ID.size() == 0) {
			COUNTRIES_FLAGS_RES_ID.put(AUSTRIA, R.drawable.flag_aut);
			COUNTRIES_FLAGS_RES_ID.put(BELGIUM, R.drawable.flag_bel);
			COUNTRIES_FLAGS_RES_ID.put(BULGARIA, R.drawable.flag_bgr);
			COUNTRIES_FLAGS_RES_ID.put(BOSNIA, R.drawable.flag_bih);
			COUNTRIES_FLAGS_RES_ID.put(SWITZERLAND, R.drawable.flag_che);
			COUNTRIES_FLAGS_RES_ID.put(CZECH, R.drawable.flag_cze);
			COUNTRIES_FLAGS_RES_ID.put(GERMANY, R.drawable.flag_deu);
			COUNTRIES_FLAGS_RES_ID.put(DENMARK, R.drawable.flag_dnk);
			COUNTRIES_FLAGS_RES_ID.put(SPAIN, R.drawable.flag_esp);
			COUNTRIES_FLAGS_RES_ID.put(ESTONIA, R.drawable.flag_est);
			COUNTRIES_FLAGS_RES_ID.put(FINLAND, R.drawable.flag_fin);
			COUNTRIES_FLAGS_RES_ID.put(FRANCE, R.drawable.flag_fra);
			COUNTRIES_FLAGS_RES_ID.put(GBR, R.drawable.flag_gbr);
			COUNTRIES_FLAGS_RES_ID.put(GREECE, R.drawable.flag_grc);
			COUNTRIES_FLAGS_RES_ID.put(CROATIA, R.drawable.flag_hrv);
			COUNTRIES_FLAGS_RES_ID.put(HUNGARY, R.drawable.flag_hun);
			COUNTRIES_FLAGS_RES_ID.put(IRELAND, R.drawable.flag_irl);
			COUNTRIES_FLAGS_RES_ID.put(ICELAND, R.drawable.flag_isl);
			COUNTRIES_FLAGS_RES_ID.put(ITALY, R.drawable.flag_ita);
			COUNTRIES_FLAGS_RES_ID.put(LITHUANIA, R.drawable.flag_ltu);
			COUNTRIES_FLAGS_RES_ID.put(LATVIA, R.drawable.flag_lva);
			COUNTRIES_FLAGS_RES_ID.put(LUXEMBOURG, R.drawable.flag_lux);
			COUNTRIES_FLAGS_RES_ID.put(MACEDONIA, R.drawable.flag_mkd);
			COUNTRIES_FLAGS_RES_ID.put(NETHERLANDS, R.drawable.flag_nld);
			COUNTRIES_FLAGS_RES_ID.put(NORWAY, R.drawable.flag_nor);
			COUNTRIES_FLAGS_RES_ID.put(POLAND, R.drawable.flag_pol);
			COUNTRIES_FLAGS_RES_ID.put(PORTUGAL, R.drawable.flag_prt);
			COUNTRIES_FLAGS_RES_ID.put(ROMANIA, R.drawable.flag_rom);
			COUNTRIES_FLAGS_RES_ID.put(SLOVAKIA, R.drawable.flag_svk);
			COUNTRIES_FLAGS_RES_ID.put(SLOVENIA, R.drawable.flag_svn);
			COUNTRIES_FLAGS_RES_ID.put(SWEDEN, R.drawable.flag_swe);
			COUNTRIES_FLAGS_RES_ID.put(SERBIA, R.drawable.flag_yug);

		}
		final Integer resId = COUNTRIES_FLAGS_RES_ID.get(countrycode);
		return resId == null ? R.drawable.bec_tous : resId;
	}
}
