package fr.giletvin.ornidroid.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.widget.ListAdapter;
import fr.giletvin.ornidroid.bo.Bird;
import fr.giletvin.ornidroid.bo.BirdFactoryImpl;
import fr.giletvin.ornidroid.bo.Taxon;
import fr.giletvin.ornidroid.data.OrnidroidDAOImpl;
import fr.giletvin.ornidroid.data.IOrnidroidDAO;

/**
 * The Class OrnidroidServiceImpl.
 */
public class OrnidroidServiceImpl implements IOrnidroidService {

	/** The activity. */
	private final Activity activity;

	/** The ornidroid dao. */
	private final IOrnidroidDAO ornidroidDAO;

	/** The service instance. */
	private static IOrnidroidService serviceInstance;

	/** The current bird. */
	private Bird currentBird;

	/**
	 * Instantiates a new ornidroid service impl.
	 * 
	 * @param pActivity
	 *            the activity
	 */
	private OrnidroidServiceImpl(Activity pActivity) {
		this.activity = pActivity;
		this.ornidroidDAO = OrnidroidDAOImpl.getInstance(pActivity);
	}

	/**
	 * Gets the single instance of OrnidroidServiceImpl.
	 * 
	 * @param pActivity
	 *            the activity
	 * @return single instance of OrnidroidServiceImpl
	 */
	protected static IOrnidroidService getInstance(Activity pActivity) {
		if (null == serviceInstance) {
			serviceInstance = new OrnidroidServiceImpl(pActivity);
		}
		return serviceInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidService#loadBirdDetails(android
	 * .net.Uri)
	 */
	public void loadBirdDetails(Uri uri) {
		Cursor cursor = activity.managedQuery(uri, null, null, null, null);

		loadBirdDetailsFromCursor(cursor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.giletvin.ornidroid.service.IOrnidroidService#getCurrentBird()
	 */
	public Bird getCurrentBird() {
		return currentBird;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.giletvin.ornidroid.service.IOrnidroidService#getNames(int)
	 */
	public List<Taxon> getNames(int id) {
		Cursor cursor = ornidroidDAO.getBirdNames(id);
		List<Taxon> result = new ArrayList<Taxon>();
		if (cursor != null) {
			int nbResults = cursor.getCount();
			for (int i = 0; i < nbResults; i++) {
				cursor.moveToPosition(i);
				int langIndex = cursor
						.getColumnIndexOrThrow(IOrnidroidDAO.LANG_COLUMN_NAME);
				int taxonIndex = cursor
						.getColumnIndexOrThrow(IOrnidroidDAO.TAXON);
				Taxon taxon = new Taxon(cursor.getString(langIndex),
						cursor.getString(taxonIndex));
				result.add(taxon);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidService#getBirdMatches(java.lang
	 * .String)
	 */
	public Cursor getBirdMatches(String query) {
		return ornidroidDAO.getBirdMatches(query);
	}

	/**
	 * Load bird details from cursor.
	 * 
	 * @param cursor
	 *            the cursor
	 */
	private void loadBirdDetailsFromCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.moveToFirst();
			int idIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);

			int taxonIndex = cursor
					.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
			int scientificNameIndex = cursor
					.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_2);
			int directoryNameIndex = cursor
					.getColumnIndexOrThrow(IOrnidroidDAO.DIRECTORY_NAME_COLUMN);
			int descriptionIndex = cursor
					.getColumnIndex(IOrnidroidDAO.DESCRIPTION_COLUMN);
			int scientificOrderIndex = cursor
					.getColumnIndex(IOrnidroidDAO.SCIENTIFIC_ORDER_NAME_COLUMN);
			int scientificFamilyIndex = cursor
					.getColumnIndex(IOrnidroidDAO.SCIENTIFIC_FAMILY_NAME_COLUMN);
			String description = (descriptionIndex == -1) ? "" : cursor
					.getString(descriptionIndex);
			String scientificOrder = (scientificOrderIndex == -1) ? "" : cursor
					.getString(scientificOrderIndex);
			String scientificFamily = (scientificFamilyIndex == -1) ? ""
					: cursor.getString(scientificFamilyIndex);
			BirdFactoryImpl birdFactory = new BirdFactoryImpl();
			currentBird = birdFactory.createBird(cursor.getInt(idIndex),
					cursor.getString(taxonIndex),
					cursor.getString(scientificNameIndex),
					cursor.getString(directoryNameIndex), description,
					scientificOrder, scientificFamily);

		}
		cursor.close();
	}

	/**
	 * Load bird details.
	 * 
	 * @param birdId
	 *            the bird id
	 */
	public void loadBirdDetails(Integer birdId) {
		Cursor cursor = ornidroidDAO.getBird(birdId.toString());

		loadBirdDetailsFromCursor(cursor);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidService#getHistoricResultsAdapter
	 * ()
	 */
	public ListAdapter getHistoricResultsAdapter() {

		return ornidroidDAO.getHistoricResultsAdapter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.giletvin.ornidroid.service.IOrnidroidService#hasHistory()
	 */
	public boolean hasHistory() {
		return ornidroidDAO.hasHistory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidService#getBirdIdInHistory(int)
	 */
	public Integer getBirdIdInHistory(int position) {
		return ornidroidDAO.getBirdIdInHistory(position);
	}

}
