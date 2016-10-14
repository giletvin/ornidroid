package fr.ornidroid.data;

import java.util.List;

import android.database.Cursor;
import fr.ornidroid.bo.MultiCriteriaSearchFormBean;
import fr.ornidroid.bo.SimpleBird;
import fr.ornidroid.ui.multicriteriasearch.MultiCriteriaSearchFieldType;

/**
 * The Interface IOrnidroidDAO.
 */
public interface IOrnidroidDAO {

	/** The Constant BEAK_FORM_TABLE. */
	public static final String BEAK_FORM_TABLE = "beak_form";

	/** The Constant BIRD_COUNTRY_TABLE. */
	public static final String BIRD_COUNTRY_TABLE = "bird_country";
	/** The Constant BIRD_TABLE. */
	public static final String BIRD_TABLE = "bird";

	/** The Constant CATEGORY_COLUMN. */
	public static final String CATEGORY_COLUMN = "category";

	/** The Constant COLOUR_TABLE. */
	public static final String COLOUR_TABLE = "colour";

	/** The Constant COUNTRY_TABLE. */
	public static final String COUNTRY_TABLE = "country";
	/** The Constant DESCRIPTION_COLUMN. */
	public static final String DESCRIPTION_COLUMN = "description";
	/** The Constant DIRECTORY_NAME_COLUMN. */
	public static final String DIRECTORY_NAME_COLUMN = "directory_name";
	/** The Constant DISTRIBUTION_COLUMN. */
	public static final String DISTRIBUTION_COLUMN = "distribution";
	/** The Constant HABITAT_1_NAME_COLUMN. */
	public static final String HABITAT_1_NAME_COLUMN = "habitat1";
	/** The Constant HABITAT_2_NAME_COLUMN. */
	public static final String HABITAT_2_NAME_COLUMN = "habitat2";
	/** The Constant ID. */
	public static final String ID = "id";
	/** The Constant LANG_COLUMN_NAME. */
	public static final String LANG_COLUMN_NAME = "lang";
	/** The Constant NAME_FIELD_NAME. */
	public static final String NAME_COLUMN_NAME = "name";

	/** The Constant OISEAUX_NET_COLUMN. */
	public static final String OISEAUX_NET_COLUMN = "oiseaux_net_link";
	/** The Constant REMARKABLE_SIGN_TABLE. */
	public static final String REMARKABLE_SIGN_TABLE = "remarkable_sign";
	/** The Constant SCIENTIFIC_FAMILY_NAME_COLUMN. */
	public static final String SCIENTIFIC_FAMILY_NAME_COLUMN = "scientific_family";
	/** The Constant SCIENTIFIC_FAMILY_TABLE. */
	public static final String SCIENTIFIC_FAMILY_TABLE = "scientific_family";

	/** The Constant SCIENTIFIC_NAME. */
	public static final String SCIENTIFIC_NAME = "scientific_name";

	/** The Constant SCIENTIFIC_NAME_2_COLUMN. */
	public static final String SCIENTIFIC_NAME_2_COLUMN = "scientific_name2";

	/** The Constant SCIENTIFIC_ORDER_NAME_COLUMN. */
	public static final String SCIENTIFIC_ORDER_NAME_COLUMN = "scientific_order";

	/** The Constant SCIENTIFIC_ORDER_TABLE. */
	public static final String SCIENTIFIC_ORDER_TABLE = "scientific_order";

	/**
	 * The Constant SEARCHED_TAXON. The taxon where diacritics are removed. Ex
	 * taxon "Bécassine" is "Becassine" in this column
	 */
	public static final String SEARCHED_TAXON = "searched_taxon";

	/** The Constant SIZE_TABLE. */
	public static final String SIZE_TABLE = "size_table";

	/** The Constant SIZE_VALUE_COLUMN. */
	public static final String SIZE_VALUE_COLUMN = "size_value";

	/** The Constant TAXON. */
	public static final String TAXON = "taxon";

	/**
	 * Gets the beak forms.
	 * 
	 * @return the beak forms
	 */
	Cursor getBeakForms();

	/**
	 * Returns a Cursor positioned at the bird specified by rowId.
	 * 
	 * @param rowId
	 *            id of bird to retrieve
	 * @return Cursor positioned to matching bird, or null if not found.
	 */
	Cursor getBird(String rowId);

	/**
	 * Gets the bird matches from multi search criteria and stores the results
	 * in the history stack.
	 * 
	 * @param formBean
	 *            the form bean
	 * @return the bird matches from multi search criteria
	 */
	List<SimpleBird> getBirdMatchesFromMultiSearchCriteria(
			MultiCriteriaSearchFormBean formBean);

	/**
	 * Gets the bird names in different languages.
	 * 
	 * @param id
	 *            the id of the bird
	 * @return cursor on the results of the query. Can be null if something goes
	 *         wrong
	 */
	Cursor getBirdNames(Integer id);

	/**
	 * Gets the categories.
	 * 
	 * @return the categories
	 */
	Cursor getCategories();

	/**
	 * Gets the colours.
	 * 
	 * @return the colours
	 */
	Cursor getColours();

	/**
	 * Gets the countries.
	 * 
	 * @return the countries
	 */
	Cursor getCountries();

	/**
	 * Gets the habitats.
	 * 
	 * @return the habitats
	 */
	Cursor getHabitats();

	/**
	 * Gets the multi search criteria count results.
	 * 
	 * @param formBean
	 *            the form bean
	 * @return the multi search criteria count results
	 */
	int getMultiSearchCriteriaCountResults(MultiCriteriaSearchFormBean formBean);

	/**
	 * Gets the remarkable signs.
	 * 
	 * @return the remarkable signs
	 */
	Cursor getRemarkableSigns();

	/**
	 * Gets the sizes.
	 * 
	 * @return the sizes
	 */
	Cursor getSizes();

	/**
	 * gets the countries where the bird can be seen
	 * 
	 * @param id
	 *            the id of the bird
	 * @return cursor on the results of the query. Can be null if something goes
	 *         wrong
	 */
	Cursor getGeographicDistribution(int id);

	/**
	 * Gets the matching birds.
	 * 
	 * @param query
	 *            the query
	 * @return the matching birds
	 */
	List<SimpleBird> getMatchingBirds(String query);

	/**
	 * Gets the release notes.
	 * 
	 * @return the release notes
	 */
	String getReleaseNotes();

	/**
	 * update the spinner items counts for the given field type
	 * 
	 * @param multiCriteriaSearchFormBean
	 * @param fieldType
	 * @return Cursor of the list of items with their counts for the field type
	 */
	Cursor updateSpinnerItemsCounts(MultiCriteriaSearchFormBean formBean,
			MultiCriteriaSearchFieldType fieldType);

}