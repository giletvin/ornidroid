package fr.ornidroid.service;

import java.util.List;

import android.database.Cursor;
import android.net.Uri;
import android.widget.ListAdapter;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.MultiCriteriaSearchFormBean;
import fr.ornidroid.bo.Taxon;
import fr.ornidroid.helper.OrnidroidException;

/**
 * The Interface IOrnidroidService.
 */
public interface IOrnidroidService {

	/**
	 * Creates the db if necessary.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	void createDbIfNecessary() throws OrnidroidException;

	/**
	 * Gets the bird id in history.
	 * 
	 * @param position
	 *            the position
	 * @return the bird id in history
	 */
	Integer getBirdIdInHistory(int position);

	/**
	 * Gets the bird matches.
	 * 
	 * @param query
	 *            the query
	 * @return the bird matches
	 */
	Cursor getBirdMatches(String query);

	/**
	 * Gets the bird matches from the multi criteria search and load the results
	 * in the history stack.
	 * 
	 * @param formBean
	 *            the form bean
	 * @return the bird matches from multi search criteria
	 */
	void getBirdMatchesFromMultiSearchCriteria(
			MultiCriteriaSearchFormBean formBean);

	/**
	 * Gets the categories to load the select menu of the bird categories.
	 * 
	 * @return the categories
	 */
	List<String> getCategories();

	/**
	 * Gets the category id.
	 * 
	 * @param categoryName
	 *            the category name
	 * @return the category id
	 */
	Integer getCategoryId(String categoryName);

	/**
	 * Gets the current bird. If a previous call to show bird detail was already
	 * done, get the bird without querying the db
	 * 
	 * @return the current bird
	 */
	Bird getCurrentBird();

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
	 * Gets the names of the bird in different languages.
	 * 
	 * @param id
	 *            the id
	 * @return List of taxon
	 */
	List<Taxon> getNames(int id);

	/**
	 * Checks for history.
	 * 
	 * @return true, if successful
	 */
	boolean hasHistory();

	/**
	 * Load bird details.
	 * 
	 * @param birdId
	 *            the bird id
	 */
	void loadBirdDetails(Integer birdId);

	/**
	 * Load bird details. Load info from the database. The created bird doesn't
	 * have its media files yet.
	 * 
	 * @param uri
	 *            the uri
	 */
	void loadBirdDetails(Uri uri);

}
