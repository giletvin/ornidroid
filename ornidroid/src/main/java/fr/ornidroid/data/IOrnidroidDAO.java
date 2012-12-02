package fr.ornidroid.data;

import android.database.Cursor;
import android.widget.ListAdapter;
import fr.ornidroid.bo.MultiCriteriaSearchFormBean;

/**
 * The Interface IOrnidroidDAO.
 */
public interface IOrnidroidDAO {

	/** The Constant BEAK_FORM_TABLE. */
	public static final String BEAK_FORM_TABLE = "beak_form";
	/** The Constant BIRD_TABLE. */
	public static final String BIRD_TABLE = "bird";
	/** The Constant DESCRIPTION_COLUMN. */
	public static final String DESCRIPTION_COLUMN = "description";
	/** The Constant DIRECTORY_NAME_COLUMN. */
	public static final String DIRECTORY_NAME_COLUMN = "directory_name";
	/** The Constant DISTRIBUTION_COLUMN. */
	public static final String DISTRIBUTION_COLUMN = "distribution";
	/** The Constant ID. */
	public static final String ID = "id";
	/** The Constant LANG_COLUMN_NAME. */
	public static final String LANG_COLUMN_NAME = "lang";
	/** The Constant NAME_FIELD_NAME. */
	public static final String NAME_COLUMN_NAME = "name";
	/** The Constant SCIENTIFIC_FAMILY_NAME_COLUMN. */
	public static final String SCIENTIFIC_FAMILY_NAME_COLUMN = "scientific_family";
	/** The Constant SCIENTIFIC_FAMILY_TABLE. */
	public static final String SCIENTIFIC_FAMILY_TABLE = "scientific_family";
	/** The Constant SCIENTIFIC_NAME. */
	public static final String SCIENTIFIC_NAME = "scientific_name";
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
	 * Gets the bird id in history.
	 * 
	 * @param position
	 *            the position
	 * @return the bird id in history
	 */
	Integer getBirdIdInHistory(int position);

	/**
	 * Returns a Cursor over all birds that match the given query.
	 * 
	 * @param query
	 *            The string to search for
	 * @return Cursor over all birds that match, or null if none found.
	 */
	Cursor getBirdMatches(String query);

	/**
	 * Gets the bird matches from multi search criteria and stores the results
	 * in the history stack.
	 * 
	 * @param formBean
	 *            the form bean
	 * @return the bird matches from multi search criteria
	 */
	void getBirdMatchesFromMultiSearchCriteria(
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
	 * Gets the habitats.
	 * 
	 * @return the habitats
	 */
	Cursor getHabitats();

	/**
	 * Gets the historic results adapter.
	 * 
	 * @return the historic results adapter
	 */
	ListAdapter getHistoricResultsAdapter();

	/**
	 * Gets the multi search criteria count results.
	 * 
	 * @param formBean
	 *            the form bean
	 * @return the multi search criteria count results
	 */
	int getMultiSearchCriteriaCountResults(MultiCriteriaSearchFormBean formBean);

	/**
	 * Gets the sizes.
	 * 
	 * @return the sizes
	 */
	Cursor getSizes();

	/**
	 * Checks for history.
	 * 
	 * @return true, if successful
	 */
	boolean hasHistory();

}