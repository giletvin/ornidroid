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
import fr.giletvin.ornidroid.data.IOrnidroidDAO;
import fr.giletvin.ornidroid.data.OrnidroidDAOImpl;
import fr.giletvin.ornidroid.data.OrnidroidDatabaseOpenHelper;
import fr.giletvin.ornidroid.helper.OrnidroidException;

/**
 * The Class OrnidroidServiceImpl.
 */
public class OrnidroidServiceImpl implements IOrnidroidService {

	/** The service instance. */
	private static IOrnidroidService serviceInstance;

	/**
	 * Gets the single instance of OrnidroidServiceImpl.
	 * 
	 * @param pActivity
	 *            the activity
	 * @return single instance of OrnidroidServiceImpl
	 */
	protected static IOrnidroidService getInstance(final Activity pActivity) {
		if (null == serviceInstance) {
			serviceInstance = new OrnidroidServiceImpl(pActivity);
		}
		return serviceInstance;
	}

	/** The activity. */
	private final Activity activity;
	/** The current bird. */
	private Bird currentBird;

	/** The data base open helper. */
	private final OrnidroidDatabaseOpenHelper dataBaseOpenHelper;

	/** The ornidroid dao. */
	private final IOrnidroidDAO ornidroidDAO;

	/**
	 * Instantiates a new ornidroid service impl.
	 * 
	 * @param pActivity
	 *            the activity
	 */
	private OrnidroidServiceImpl(final Activity pActivity) {
		this.activity = pActivity;
		this.dataBaseOpenHelper = new OrnidroidDatabaseOpenHelper(pActivity);
		this.ornidroidDAO = OrnidroidDAOImpl
				.getInstance(this.dataBaseOpenHelper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidService#createDbIfNecessary()
	 */
	public void createDbIfNecessary() throws OrnidroidException {
		this.dataBaseOpenHelper.createDbIfNecessary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidService#getBirdIdInHistory(int)
	 */
	public Integer getBirdIdInHistory(final int position) {
		return this.ornidroidDAO.getBirdIdInHistory(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidService#getBirdMatches(java.lang
	 * .String)
	 */
	public Cursor getBirdMatches(final String query) {
		return this.ornidroidDAO.getBirdMatches(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.giletvin.ornidroid.service.IOrnidroidService#getCurrentBird()
	 */
	public Bird getCurrentBird() {
		return this.currentBird;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidService#getHistoricResultsAdapter
	 * ()
	 */
	public ListAdapter getHistoricResultsAdapter() {

		return this.ornidroidDAO.getHistoricResultsAdapter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.giletvin.ornidroid.service.IOrnidroidService#getNames(int)
	 */
	public List<Taxon> getNames(final int id) {
		final Cursor cursor = this.ornidroidDAO.getBirdNames(id);
		final List<Taxon> result = new ArrayList<Taxon>();
		if (cursor != null) {
			final int nbResults = cursor.getCount();
			for (int i = 0; i < nbResults; i++) {
				cursor.moveToPosition(i);
				final int langIndex = cursor
						.getColumnIndexOrThrow(IOrnidroidDAO.LANG_COLUMN_NAME);
				final int taxonIndex = cursor
						.getColumnIndexOrThrow(IOrnidroidDAO.TAXON);
				final Taxon taxon = new Taxon(cursor.getString(langIndex),
						cursor.getString(taxonIndex));
				result.add(taxon);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.giletvin.ornidroid.service.IOrnidroidService#hasHistory()
	 */
	public boolean hasHistory() {
		return this.ornidroidDAO.hasHistory();
	}

	/**
	 * Load bird details.
	 * 
	 * @param birdId
	 *            the bird id
	 */
	public void loadBirdDetails(final Integer birdId) {
		final Cursor cursor = this.ornidroidDAO.getBird(birdId.toString());

		loadBirdDetailsFromCursor(cursor);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.service.IOrnidroidService#loadBirdDetails(android
	 * .net.Uri)
	 */
	public void loadBirdDetails(final Uri uri) {
		final Cursor cursor = this.activity.managedQuery(uri, null, null, null,
				null);

		loadBirdDetailsFromCursor(cursor);
	}

	/**
	 * Load bird details from cursor.
	 * 
	 * @param cursor
	 *            the cursor
	 */
	private void loadBirdDetailsFromCursor(final Cursor cursor) {
		if (cursor != null) {
			cursor.moveToFirst();
			final int idIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);

			final int taxonIndex = cursor
					.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
			final int scientificNameIndex = cursor
					.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_2);
			final int directoryNameIndex = cursor
					.getColumnIndexOrThrow(IOrnidroidDAO.DIRECTORY_NAME_COLUMN);
			final int descriptionIndex = cursor
					.getColumnIndex(IOrnidroidDAO.DESCRIPTION_COLUMN);
			final int scientificOrderIndex = cursor
					.getColumnIndex(IOrnidroidDAO.SCIENTIFIC_ORDER_NAME_COLUMN);
			final int scientificFamilyIndex = cursor
					.getColumnIndex(IOrnidroidDAO.SCIENTIFIC_FAMILY_NAME_COLUMN);
			final String description = (descriptionIndex == -1) ? "" : cursor
					.getString(descriptionIndex);
			final String scientificOrder = (scientificOrderIndex == -1) ? ""
					: cursor.getString(scientificOrderIndex);
			final String scientificFamily = (scientificFamilyIndex == -1) ? ""
					: cursor.getString(scientificFamilyIndex);
			final BirdFactoryImpl birdFactory = new BirdFactoryImpl();
			this.currentBird = birdFactory.createBird(cursor.getInt(idIndex),
					cursor.getString(taxonIndex),
					cursor.getString(scientificNameIndex),
					cursor.getString(directoryNameIndex), description,
					scientificOrder, scientificFamily);

		}
		cursor.close();
	}

}
