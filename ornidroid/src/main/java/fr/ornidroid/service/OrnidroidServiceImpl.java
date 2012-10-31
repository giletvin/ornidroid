package fr.ornidroid.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.SearchManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.widget.ListAdapter;
import fr.ornidroid.R;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.BirdFactoryImpl;
import fr.ornidroid.bo.MultiCriteriaSearchFormBean;
import fr.ornidroid.bo.Taxon;
import fr.ornidroid.data.IOrnidroidDAO;
import fr.ornidroid.data.OrnidroidDAOImpl;
import fr.ornidroid.data.OrnidroidDatabaseOpenHelper;
import fr.ornidroid.helper.OrnidroidException;

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

	/** The categories list. */
	private List<String> categoriesList;

	/** The categories map. */
	private Map<String, Integer> categoriesMap;
	/** The current bird. */
	private Bird currentBird;

	/** The data base open helper. */
	private final OrnidroidDatabaseOpenHelper dataBaseOpenHelper;

	/** The habitats list. */
	private List<String> habitatsList;

	/** The habitats map. */
	private Map<String, Integer> habitatsMap;

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
	 * @see fr.ornidroid.service.IOrnidroidService#createDbIfNecessary()
	 */
	public void createDbIfNecessary() throws OrnidroidException {
		this.dataBaseOpenHelper.createDbIfNecessary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getBirdIdInHistory(int)
	 */
	public Integer getBirdIdInHistory(final int position) {
		return this.ornidroidDAO.getBirdIdInHistory(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getBirdMatches(java.lang
	 * .String)
	 */
	public Cursor getBirdMatches(final String query) {
		return this.ornidroidDAO.getBirdMatches(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getBirdMatchesFromMultiSearchCriteria
	 * (fr.ornidroid.bo.MultiCriteriaSearchFormBean)
	 */
	public void getBirdMatchesFromMultiSearchCriteria(
			final MultiCriteriaSearchFormBean formBean) {
		this.ornidroidDAO.getBirdMatchesFromMultiSearchCriteria(formBean);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getCategories()
	 */
	public List<String> getCategories() {
		// TODO : quand il faudra éventuellement afficher la catégorie de
		// l'oiseau dans un onglet, ensuite, creer ici une map de <Category> et
		// faire evoluer Bird
		// pour y ajouter une categorie. En reecuperant l'instance de Bird, je
		// veux qu'il y ait une instance de Categorie qu'on ira piocher dans la
		// map qui est ici, à partir du category_fk qui est recuperé par sql
		// quand on a l'oiseau
		if (this.categoriesMap == null) {
			this.categoriesMap = new HashMap<String, Integer>();
			this.categoriesList = new ArrayList<String>();
			// init the map and the list with "ALL" with id = 0
			this.categoriesMap.put(
					this.activity.getString(R.string.search_all), 0);
			this.categoriesList.add(this.activity
					.getString(R.string.search_all));
			final Cursor cursorQueryCategories = this.ornidroidDAO
					.getCategories();
			if (cursorQueryCategories != null) {
				final int nbResults = cursorQueryCategories.getCount();
				for (int i = 0; i < nbResults; i++) {
					cursorQueryCategories.moveToPosition(i);
					final int idIndex = cursorQueryCategories
							.getColumnIndexOrThrow("id");
					final int nameIndex = cursorQueryCategories
							.getColumnIndexOrThrow("name");
					this.categoriesMap.put(
							cursorQueryCategories.getString(nameIndex),
							cursorQueryCategories.getInt(idIndex));
					this.categoriesList.add(cursorQueryCategories
							.getString(nameIndex));
				}
				cursorQueryCategories.close();
			}

		}
		return this.categoriesList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getCategoryId(java.lang.String)
	 */
	public Integer getCategoryId(final String categoryName) {
		return this.categoriesMap.get(categoryName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getCurrentBird()
	 */
	public Bird getCurrentBird() {
		return this.currentBird;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getHabitatId(java.lang.String)
	 */
	public Integer getHabitatId(final String habitatName) {
		return this.habitatsMap.get(habitatName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getHabitats()
	 */
	public List<String> getHabitats() {
		if (this.habitatsMap == null) {
			this.habitatsMap = new HashMap<String, Integer>();
			this.habitatsList = new ArrayList<String>();
			// init the map and the list with "ALL" with id = 0
			this.habitatsMap.put(this.activity.getString(R.string.search_all),
					0);
			this.habitatsList.add(this.activity.getString(R.string.search_all));
			final Cursor cursorQueryHabitats = this.ornidroidDAO.getHabitats();
			if (cursorQueryHabitats != null) {
				final int nbResults = cursorQueryHabitats.getCount();
				for (int i = 0; i < nbResults; i++) {
					cursorQueryHabitats.moveToPosition(i);
					final int idIndex = cursorQueryHabitats
							.getColumnIndexOrThrow("id");
					final int nameIndex = cursorQueryHabitats
							.getColumnIndexOrThrow("name");
					this.habitatsMap.put(
							cursorQueryHabitats.getString(nameIndex),
							cursorQueryHabitats.getInt(idIndex));
					this.habitatsList.add(cursorQueryHabitats
							.getString(nameIndex));
				}
				cursorQueryHabitats.close();
			}

		}
		return this.habitatsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getHistoricResultsAdapter ()
	 */
	public ListAdapter getHistoricResultsAdapter() {

		return this.ornidroidDAO.getHistoricResultsAdapter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getMultiSearchCriteriaCountResults
	 * (fr.ornidroid.bo.MultiCriteriaSearchFormBean)
	 */
	public int getMultiSearchCriteriaCountResults(
			final MultiCriteriaSearchFormBean formBean) {
		return this.ornidroidDAO.getMultiSearchCriteriaCountResults(formBean);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getNames(int)
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
	 * @see fr.ornidroid.service.IOrnidroidService#hasHistory()
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
	 * @see fr.ornidroid.service.IOrnidroidService#loadBirdDetails(android
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
			final int distributionIndex = cursor
					.getColumnIndex(IOrnidroidDAO.DISTRIBUTION_COLUMN);
			final int scientificOrderIndex = cursor
					.getColumnIndex(IOrnidroidDAO.SCIENTIFIC_ORDER_NAME_COLUMN);
			final int scientificFamilyIndex = cursor
					.getColumnIndex(IOrnidroidDAO.SCIENTIFIC_FAMILY_NAME_COLUMN);
			final String description = (descriptionIndex == -1) ? "" : cursor
					.getString(descriptionIndex);
			final String distribution = (distributionIndex == -1) ? "" : cursor
					.getString(distributionIndex);
			final String scientificOrder = (scientificOrderIndex == -1) ? ""
					: cursor.getString(scientificOrderIndex);
			final String scientificFamily = (scientificFamilyIndex == -1) ? ""
					: cursor.getString(scientificFamilyIndex);
			final BirdFactoryImpl birdFactory = new BirdFactoryImpl();
			this.currentBird = birdFactory.createBird(cursor.getInt(idIndex),
					cursor.getString(taxonIndex),
					cursor.getString(scientificNameIndex),
					cursor.getString(directoryNameIndex), description,
					distribution, scientificOrder, scientificFamily);

		}
		cursor.close();
	}
}
