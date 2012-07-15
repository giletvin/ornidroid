package fr.giletvin.ornidroid.service;

import java.util.List;

import android.database.Cursor;
import android.net.Uri;
import android.widget.ListAdapter;
import fr.giletvin.ornidroid.bo.Bird;
import fr.giletvin.ornidroid.bo.Taxon;
import fr.giletvin.ornidroid.helper.OrnidroidException;

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
