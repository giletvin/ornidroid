package fr.giletvin.ornidroid.service;

import java.util.List;

import android.database.Cursor;
import android.net.Uri;
import android.widget.ListAdapter;
import fr.giletvin.ornidroid.bo.Bird;
import fr.giletvin.ornidroid.bo.Taxon;

/**
 * The Interface IOrnidroidService.
 */
public interface IOrnidroidService {

	/**
	 * Load bird details. Load info from the database. The created bird doesn't
	 * have its media files yet.
	 * 
	 * @param uri
	 *            the uri
	 */
	void loadBirdDetails(Uri uri);

	/**
	 * Load bird details.
	 * 
	 * @param birdId
	 *            the bird id
	 */
	void loadBirdDetails(Integer birdId);

	/**
	 * Gets the current bird. If a previous call to show bird detail was already
	 * done, get the bird without querying the db
	 * 
	 * @return the current bird
	 */
	Bird getCurrentBird();

	/**
	 * Gets the names of the bird in different languages.
	 * 
	 * @param id
	 *            the id
	 * @return List of taxon
	 */
	List<Taxon> getNames(int id);

	/**
	 * Gets the bird matches.
	 * 
	 * @param query
	 *            the query
	 * @return the bird matches
	 */
	Cursor getBirdMatches(String query);

	/**
	 * Gets the historic results adapter.
	 * 
	 * @return the historic results adapter
	 */
	ListAdapter getHistoricResultsAdapter();

	/**
	 * Checks for history.
	 * 
	 * @return true, if successful
	 */
	boolean hasHistory();

	/**
	 * Gets the bird id in history.
	 * 
	 * @param position
	 *            the position
	 * @return the bird id in history
	 */
	Integer getBirdIdInHistory(int position);

}
