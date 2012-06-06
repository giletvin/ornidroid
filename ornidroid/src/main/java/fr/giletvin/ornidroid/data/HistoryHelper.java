package fr.giletvin.ornidroid.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.SearchManager;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import fr.giletvin.ornidroid.R;
import fr.giletvin.ornidroid.bo.BirdFactoryImpl;
import fr.giletvin.ornidroid.bo.SimpleBird;
import fr.giletvin.ornidroid.helper.Constants;
import fr.giletvin.ornidroid.ui.components.OrnidroidViewBinder;

/**
 * The Class HistoryHelper. This class allows to return to the search results
 * when the back button is clicked.
 */
public class HistoryHelper {
	private final BirdFactoryImpl birdFactory;
	/**
	 * mapping in the adapter results between the from columns in SQL and the
	 * "to" fields in the displayed results.
	 */
	private final String[] from = new String[] {
			SearchManager.SUGGEST_COLUMN_TEXT_1,
			SearchManager.SUGGEST_COLUMN_TEXT_2 };

	/**
	 * mapping in the adapter results between the from columns in SQL and the
	 * "to" fields in the displayed results.
	 */
	private final int[] to = new int[] { R.id.taxon, R.id.scientific_name };

	/**
	 * Instantiates a new history helper.
	 */
	protected HistoryHelper() {
		birdFactory = new BirdFactoryImpl();
	}

	/**
	 * List Adapter which contains the results of the search, to enable the
	 * return to the result list when the back button is pressed.
	 */
	private SimpleAdapter resultsAdapter;

	/**
	 * This list contains the ids of the birds which are referenced in the
	 * results_adapter.
	 */
	private List<Integer> resultsBirdIds;

	/**
	 * Gets the bird id in history.
	 * 
	 * @param position
	 *            the position in the list. Out of bound exception is handled in
	 *            the method.
	 * @return the bird id in history. If position is invalid, returns the first
	 *         id in the list
	 */
	protected Integer getBirdIdInHistory(int position) {
		if (position < 0 || position >= resultsBirdIds.size()) {
			Log.w(Constants.LOG_TAG, "invalid position" + position
					+ " size of list : " + resultsBirdIds.size());
			position = 0;
		}
		return resultsBirdIds.get(position);
	}

	/**
	 * Records the result list contained in the cursor in the history list.
	 * 
	 * @param cursor
	 *            the cursor
	 */
	public void setHistory(final Cursor cursor) {
		if (null != cursor) {

			resultsBirdIds = new ArrayList<Integer>();
			List<Map<String, SimpleBird>> data = new ArrayList<Map<String, SimpleBird>>();
			int nbRouws = cursor.getCount();
			for (int i = 0; i < nbRouws; i++) {
				cursor.moveToPosition(i);
				resultsBirdIds.add(cursor.getInt(cursor
						.getColumnIndex(BaseColumns._ID)));
				String column_1 = cursor.getString(cursor
						.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
				String directoryName = cursor.getString(cursor
						.getColumnIndex(IOrnidroidDAO.DIRECTORY_NAME_COLUMN));
				String column_2 = cursor.getString(cursor
						.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2));

				Map<String, SimpleBird> map = new HashMap<String, SimpleBird>();
				SimpleBird birdLine1 = birdFactory.createSimpleBird(column_1,
						directoryName);
				SimpleBird birdLine2 = birdFactory.createSimpleBird(column_2,
						null);
				map.put(SearchManager.SUGGEST_COLUMN_TEXT_1, birdLine1);
				map.put(SearchManager.SUGGEST_COLUMN_TEXT_2, birdLine2);
				data.add(map);
			}
			resultsAdapter = new SimpleAdapter(Constants.getCONTEXT(), data,
					R.layout.result, from, to);
			resultsAdapter.setViewBinder(new OrnidroidViewBinder());
		}

	}

	/**
	 * Checks for history.
	 * 
	 * @return true, if successful
	 */
	protected boolean hasHistory() {
		return null != resultsBirdIds && resultsBirdIds.size() > 0;
	}

	/**
	 * Gets the results adapter.
	 * 
	 * @return the results adapter
	 */
	protected ListAdapter getResultsAdapter() {
		return resultsAdapter;
	}

}
