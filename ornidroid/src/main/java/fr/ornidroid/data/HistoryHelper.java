package fr.ornidroid.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.SearchManager;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import fr.ornidroid.R;
import fr.ornidroid.bo.BirdFactoryImpl;
import fr.ornidroid.bo.SimpleBird;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.ui.components.OrnidroidViewBinder;

/**
 * The Class HistoryHelper. This class allows to return to the search results
 * when the back button is clicked.
 * 
 * @deprecated
 */
public class HistoryHelper {

	/** The bird factory. */
	private final BirdFactoryImpl birdFactory;

	/** The current cursor. */
	private Cursor currentCursor;

	/**
	 * mapping in the adapter results between the from columns in SQL and the
	 * "to" fields in the displayed results.
	 * 
	 * @deprecated
	 */
	private final String[] from = new String[] {
			SearchManager.SUGGEST_COLUMN_TEXT_1,
			SearchManager.SUGGEST_COLUMN_TEXT_2 };

	/**
	 * List Adapter which contains the results of the search, to enable the
	 * return to the result list when the back button is pressed.
	 * 
	 * @deprecated
	 */
	private SimpleAdapter resultsAdapter;

	/**
	 * This list contains the ids of the birds which are referenced in the
	 * results_adapter.
	 * 
	 * @deprecated
	 */
	private List<Integer> resultsBirdIds;

	/**
	 * mapping in the adapter results between the from columns in SQL and the
	 * "to" fields in the displayed results.
	 * 
	 * @deprecated
	 */
	private final int[] to = new int[] { R.id.taxon, R.id.scientific_name };

	/**
	 * Instantiates a new history helper.
	 */
	protected HistoryHelper() {
		this.birdFactory = new BirdFactoryImpl();
	}

	/**
	 * Records the result list contained in the cursor in the history list.
	 * 
	 * @param cursor
	 *            the cursor
	 * @deprecated
	 */
	public void setHistory(final Cursor cursor) {
		if (null != this.currentCursor) {
			// issue #35 : close the previous cursor
			this.currentCursor.close();
		}
		if (null != cursor) {
			this.currentCursor = cursor;

			this.resultsBirdIds = new ArrayList<Integer>();
			final List<Map<String, SimpleBird>> data = new ArrayList<Map<String, SimpleBird>>();
			final List<String> scientificNamesList = new ArrayList<String>();
			final int nbRouws = cursor.getCount();
			for (int i = 0; i < nbRouws; i++) {
				cursor.moveToPosition(i);
				final int idIndex = cursor
						.getColumnIndexOrThrow(BaseColumns._ID);
				final String column_1 = cursor.getString(cursor
						.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
				final String directoryName = cursor.getString(cursor
						.getColumnIndex(IOrnidroidDAO.DIRECTORY_NAME_COLUMN));
				final String scientificName = cursor.getString(cursor
						.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2));
				if (!scientificNamesList.contains(scientificName)) {
					this.resultsBirdIds.add(cursor.getInt(cursor
							.getColumnIndex(BaseColumns._ID)));
					scientificNamesList.add(scientificName);
					final Map<String, SimpleBird> map = new HashMap<String, SimpleBird>();
					final SimpleBird birdLine1 = this.birdFactory
							.createSimpleBird(cursor.getInt(idIndex), column_1,
									directoryName);
					final SimpleBird birdLine2 = this.birdFactory
							.createSimpleBird(cursor.getInt(idIndex),
									scientificName, null);
					map.put(SearchManager.SUGGEST_COLUMN_TEXT_1, birdLine1);
					map.put(SearchManager.SUGGEST_COLUMN_TEXT_2, birdLine2);
					data.add(map);
				}
			}
			this.resultsAdapter = new SimpleAdapter(Constants.getCONTEXT(),
					data, R.layout.result, this.from, this.to);
			this.resultsAdapter.setViewBinder(new OrnidroidViewBinder());
		}

	}

	/**
	 * Gets the bird id in history.
	 * 
	 * @param position
	 *            the position in the list. Out of bound exception is handled in
	 *            the method.
	 * @return the bird id in history. If position is invalid, returns the first
	 *         id in the list
	 * @deprecated
	 */
	protected Integer getBirdIdInHistory(int position) {
		if ((position < 0) || (position >= this.resultsBirdIds.size())) {
			// Log.w(Constants.LOG_TAG, "invalid position" + position
			// + " size of list : " + resultsBirdIds.size());
			position = 0;
		}
		return this.resultsBirdIds.get(position);
	}

	/**
	 * Gets the results adapter.
	 * 
	 * @return the results adapter
	 */
	protected ListAdapter getResultsAdapter() {
		return this.resultsAdapter;
	}

}
