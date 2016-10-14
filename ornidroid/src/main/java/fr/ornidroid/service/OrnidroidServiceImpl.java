package fr.ornidroid.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.SearchManager;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;
import fr.ornidroid.R;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.BirdFactoryImpl;
import fr.ornidroid.bo.MultiCriteriaSearchFormBean;
import fr.ornidroid.bo.SimpleBird;
import fr.ornidroid.bo.Taxon;
import fr.ornidroid.data.IOrnidroidDAO;
import fr.ornidroid.data.OrnidroidDAOImpl;
import fr.ornidroid.data.OrnidroidDatabaseOpenHelper;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.I18nHelper;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.helper.SupportedLanguage;
import fr.ornidroid.ui.multicriteriasearch.MultiCriteriaSeachMenuItem;
import fr.ornidroid.ui.multicriteriasearch.MultiCriteriaSearchFieldType;
import fr.ornidroid.ui.picture.PictureHelper;

/**
 * The Class OrnidroidServiceImpl.
 */
public class OrnidroidServiceImpl implements IOrnidroidService {

	/**
	 * The Class SelectFieldsValue : dto object to embed the map and the list of
	 * the select fields. The SQL queries handles the order by clause.
	 */
	private class SelectFieldsValue {

		/** The fields values. */
		private final List<String> fieldsValues;

		/** The map name code. */
		private final Map<String, String> mapNameCode;
		/** The map name id. */
		private final Map<String, Integer> mapNameId;

		/**
		 * Instantiates a new select fields value.
		 * 
		 * @param pMapNameId
		 *            the map name id
		 * @param pMapNameCode
		 *            the map name code
		 * @param pFieldValues
		 *            the field values
		 */
		public SelectFieldsValue(final Map<String, Integer> pMapNameId,
				final Map<String, String> pMapNameCode,
				final List<String> pFieldValues) {
			this.mapNameId = pMapNameId;
			this.fieldsValues = pFieldValues;
			this.mapNameCode = pMapNameCode;
		}

		/**
		 * Gets the map name code.
		 * 
		 * @return the map name code
		 */
		public Map<String, String> getMapNameCode() {
			return this.mapNameCode;
		}

		/**
		 * Gets the fields values.
		 * 
		 * @return the fields values
		 */
		protected List<String> getFieldsValues() {
			return this.fieldsValues;
		}

		/**
		 * Gets the map name id.
		 * 
		 * @return the map name id
		 */
		protected Map<String, Integer> getMapNameId() {
			return this.mapNameId;
		}
	}

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

	/** The beak forms list. */
	private List<MultiCriteriaSeachMenuItem> beakFormsList;
	/** The beak forms maps. */
	private Map<String, Integer> beakFormsMaps;

	/** The categories list. */
	private List<MultiCriteriaSeachMenuItem> categoriesList;

	/** The categories map. */
	private Map<String, Integer> categoriesMap;

	/** The colours list. */
	private List<MultiCriteriaSeachMenuItem> coloursList;

	/** The colours map. */
	private Map<String, Integer> coloursMap;

	/** The countries list. */
	private List<MultiCriteriaSeachMenuItem> countriesList;

	/** The countries map. France --> FRA */
	private Map<String, String> countriesMap;

	/** The current bird. */
	private Bird currentBird;

	/** The data base open helper. */
	private final OrnidroidDatabaseOpenHelper dataBaseOpenHelper;

	/** The habitats list. */
	private List<MultiCriteriaSeachMenuItem> habitatsList;

	/** The habitats map. */
	private Map<String, Integer> habitatsMap;

	/** The ornidroid dao. */
	private final IOrnidroidDAO ornidroidDAO;

	/** The remarkable signs list. */
	private List<MultiCriteriaSeachMenuItem> remarkableSignsList;

	/** The remarkable signs map. */
	private Map<String, Integer> remarkableSignsMap;

	/** The sizes list. */
	private List<MultiCriteriaSeachMenuItem> sizesList;

	/** The sizes map. */
	private Map<String, Integer> sizesMap;

	/** The query result. */
	private List<SimpleBird> queryResult;

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
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getBeakFormId(java.lang.String)
	 */
	public Integer getBeakFormId(final MultiCriteriaSeachMenuItem beakFormName) {
		return this.beakFormsMaps != null ? this.beakFormsMaps.get(beakFormName
				.getLabel()) : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getBeakForms()
	 */
	public List<MultiCriteriaSeachMenuItem> getBeakForms() {
		if (this.beakFormsMaps == null) {
			// Find the names of the beak forms in the selected language
			final Cursor cursorQueryHabitats = this.ornidroidDAO.getBeakForms();
			final SelectFieldsValue sfv = loadSelectFieldsFromCursor(
					cursorQueryHabitats, true);
			this.beakFormsMaps = sfv.getMapNameId();
			this.beakFormsList = MultiCriteriaSeachMenuItem.initList(sfv
					.getFieldsValues());

		}
		return this.beakFormsList;
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
		queryResult = this.ornidroidDAO
				.getBirdMatchesFromMultiSearchCriteria(formBean);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getCategories()
	 */
	public List<MultiCriteriaSeachMenuItem> getCategories() {
		if (this.categoriesMap == null) {
			final Cursor cursorQueryHabitats = this.ornidroidDAO
					.getCategories();
			final SelectFieldsValue sfv = loadSelectFieldsFromCursor(
					cursorQueryHabitats, true);
			this.categoriesMap = sfv.getMapNameId();
			this.categoriesList = MultiCriteriaSeachMenuItem.initList(sfv
					.getFieldsValues());

		}
		return this.categoriesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getCategoryId(java.lang.String)
	 */
	public Integer getCategoryId(final MultiCriteriaSeachMenuItem categoryName) {

		return this.categoriesMap != null ? this.categoriesMap.get(categoryName
				.getLabel()) : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getColourId(java.lang.String)
	 */
	public Integer getColourId(final MultiCriteriaSeachMenuItem colourName) {
		return this.coloursMap != null ? this.coloursMap.get(colourName
				.getLabel()) : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getColours()
	 */
	public List<MultiCriteriaSeachMenuItem> getColours() {
		if (this.coloursMap == null) {
			final Cursor cursorQueryColours = this.ornidroidDAO.getColours();
			final SelectFieldsValue sfv = loadSelectFieldsFromCursor(
					cursorQueryColours, true);
			this.coloursMap = sfv.getMapNameId();
			this.coloursList = MultiCriteriaSeachMenuItem.initList(sfv
					.getFieldsValues());

		}
		return this.coloursList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getCountries()
	 */
	public List<MultiCriteriaSeachMenuItem> getCountries() {
		if (this.countriesMap == null) {
			final Cursor cursorCountries = this.ornidroidDAO.getCountries();
			final SelectFieldsValue sfv = loadSelectFieldsFromCursor(
					cursorCountries, false);
			this.countriesMap = sfv.getMapNameCode();
			this.countriesList = MultiCriteriaSeachMenuItem.initList(sfv
					.getFieldsValues());

		}
		return this.countriesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getCountryCode(java.lang.String)
	 */
	public String getCountryCode(final MultiCriteriaSeachMenuItem countryName) {
		return this.countriesMap != null ? this.countriesMap.get(countryName
				.getLabel()) : BasicConstants.EMPTY_STRING;
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
	public Integer getHabitatId(final MultiCriteriaSeachMenuItem habitatName) {
		return this.habitatsMap != null ? this.habitatsMap.get(habitatName
				.getLabel()) : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getHabitats()
	 */
	public List<MultiCriteriaSeachMenuItem> getHabitats() {
		if (this.habitatsMap == null) {
			final Cursor cursorQueryHabitats = this.ornidroidDAO.getHabitats();
			final SelectFieldsValue sfv = loadSelectFieldsFromCursor(
					cursorQueryHabitats, true);
			this.habitatsMap = sfv.getMapNameId();
			this.habitatsList = MultiCriteriaSeachMenuItem.initList(sfv
					.getFieldsValues());

		}
		return this.habitatsList;
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
			cursor.close();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getOiseauxNetLink(fr.ornidroid
	 * .bo.Bird)
	 */
	public String getOiseauxNetLink(final Bird currentBird) {
		if (StringHelper.isNotBlank(currentBird.getOiseauxNetUrl())) {
			final StringBuffer sbuf = new StringBuffer();
			sbuf.append("<a href=\"");
			sbuf.append(currentBird.getOiseauxNetUrl());
			sbuf.append("\">");
			sbuf.append(Constants.getCONTEXT().getResources()
					.getString(R.string.oiseaux_net));
			sbuf.append(currentBird.getTaxon());
			sbuf.append("</a>");
			return sbuf.toString();
		} else {
			return BasicConstants.EMPTY_STRING;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getRemarkableSignId(java.lang.
	 * String)
	 */
	public Integer getRemarkableSignId(
			final MultiCriteriaSeachMenuItem remarkableSignName) {
		return this.remarkableSignsMap != null ? this.remarkableSignsMap
				.get(remarkableSignName.getLabel()) : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getRemarkableSigns()
	 */
	public List<MultiCriteriaSeachMenuItem> getRemarkableSigns() {
		if (this.remarkableSignsMap == null) {
			final Cursor cursorQueryHabitats = this.ornidroidDAO
					.getRemarkableSigns();
			final SelectFieldsValue sfv = loadSelectFieldsFromCursor(
					cursorQueryHabitats, true);
			this.remarkableSignsMap = sfv.getMapNameId();
			this.remarkableSignsList = MultiCriteriaSeachMenuItem.initList(sfv
					.getFieldsValues());

		}
		return this.remarkableSignsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getSizeId(java.lang.String)
	 */
	public Integer getSizeId(final MultiCriteriaSeachMenuItem sizeName) {
		return this.sizesMap != null ? this.sizesMap.get(sizeName.getLabel())
				: 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getSizes()
	 */
	public List<MultiCriteriaSeachMenuItem> getSizes() {
		if (this.sizesMap == null) {
			final Cursor cursorQuerySizes = this.ornidroidDAO.getSizes();
			final SelectFieldsValue sfv = loadSelectFieldsFromCursor(
					cursorQuerySizes, true);
			this.sizesMap = sfv.getMapNameId();
			this.sizesList = MultiCriteriaSeachMenuItem.initList(sfv
					.getFieldsValues());

		}
		return this.sizesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getWikipediaLink(fr.ornidroid.
	 * bo.Bird)
	 */
	public String getWikipediaLink(final Bird currentBird) {
		final SupportedLanguage lang = I18nHelper.getLang();
		final StringBuffer sbuf = new StringBuffer();
		sbuf.append("<a href=\"http://");
		sbuf.append(lang.getCode());
		sbuf.append(".wikipedia.org/wiki/");
		sbuf.append(currentBird.getScientificName().replaceAll(" ", "%20"));
		sbuf.append("\">");
		sbuf.append(Constants.getCONTEXT().getResources()
				.getString(R.string.wikipedia));
		sbuf.append(currentBird.getTaxon());
		sbuf.append("</a>");
		return sbuf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#hasHistory()
	 */
	public boolean hasHistory() {
		return (queryResult != null && queryResult.size() > 0);
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

	/**
	 * Gets the habitat from cursor. Concatenates habitat 1 and habitat 2
	 * 
	 * @param cursor
	 *            the cursor
	 * @return the habitat from cursor
	 */
	private String getHabitatFromCursor(final Cursor cursor) {
		final int habitat1Index = cursor
				.getColumnIndex(IOrnidroidDAO.HABITAT_1_NAME_COLUMN);
		final int habitat2Index = cursor
				.getColumnIndex(IOrnidroidDAO.HABITAT_2_NAME_COLUMN);
		final StringBuilder habitat = new StringBuilder(
				(habitat1Index == -1)
						|| (cursor.getString(habitat1Index) == null) ? BasicConstants.EMPTY_STRING
						: cursor.getString(habitat1Index));
		final String habitat2 = (habitat2Index == -1) ? BasicConstants.EMPTY_STRING
				: cursor.getString(habitat2Index);
		if (StringHelper.isNotBlank(habitat2)) {
			habitat.append(BasicConstants.SLASH_STRING);
			habitat.append(habitat2);
		}
		return habitat.toString();
	}

	/**
	 * Load bird details from cursor and closes it
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
			final int scientificName2Index = cursor
					.getColumnIndexOrThrow(IOrnidroidDAO.SCIENTIFIC_NAME_2_COLUMN);
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
			final int categoryIndex = cursor
					.getColumnIndex(IOrnidroidDAO.CATEGORY_COLUMN);
			final int sizeIndex = cursor
					.getColumnIndex(IOrnidroidDAO.SIZE_VALUE_COLUMN);
			final int oiseauxNetIndex = cursor
					.getColumnIndex(IOrnidroidDAO.OISEAUX_NET_COLUMN);

			final String description = (descriptionIndex == -1) ? BasicConstants.EMPTY_STRING
					: cursor.getString(descriptionIndex);
			final String distribution = (distributionIndex == -1) ? BasicConstants.EMPTY_STRING
					: cursor.getString(distributionIndex);
			final String scientificOrder = (scientificOrderIndex == -1) ? BasicConstants.EMPTY_STRING
					: cursor.getString(scientificOrderIndex);
			final String scientificFamily = (scientificFamilyIndex == -1) ? BasicConstants.EMPTY_STRING
					: cursor.getString(scientificFamilyIndex);
			final String size = (sizeIndex == -1) ? BasicConstants.EMPTY_STRING
					: cursor.getString(sizeIndex);
			final String category = (categoryIndex == -1) ? BasicConstants.EMPTY_STRING
					: cursor.getString(categoryIndex);
			final String oiseauxNetUrl = (oiseauxNetIndex == -1) ? BasicConstants.EMPTY_STRING
					: cursor.getString(oiseauxNetIndex);

			final BirdFactoryImpl birdFactory = new BirdFactoryImpl();
			this.currentBird = birdFactory
					.createBird(cursor.getInt(idIndex),
							cursor.getString(taxonIndex),
							cursor.getString(scientificNameIndex),
							cursor.getString(scientificName2Index),
							cursor.getString(directoryNameIndex), description,
							distribution, scientificOrder, scientificFamily,
							getHabitatFromCursor(cursor), size, category,
							oiseauxNetUrl);
			// when a new bird arrives, clear the hashmap of stored bitmaps
			PictureHelper.resetLoadedBitmaps();
			cursor.close();
		}

	}

	/**
	 * Load select fields from cursor, used to populate the Spinners and closes
	 * the cursor
	 * 
	 * @param cursor
	 *            the cursor from the DAO, from a select query on the fields ID
	 *            and NAME
	 * @param intId
	 *            true if the id to map from the database is an integer. If
	 *            false, the id is a String (like in the country table)
	 * @return the map, NAME (String)-> ID (Integer)
	 */
	private SelectFieldsValue loadSelectFieldsFromCursor(final Cursor cursor,
			final boolean intId) {
		final Map<String, Integer> mapNameId = new HashMap<String, Integer>();
		final Map<String, String> mapNameCode = new HashMap<String, String>();
		// init the map and the list with "ALL" with id = 0
		mapNameId.put(this.activity.getString(R.string.search_all), 0);
		mapNameCode.put(this.activity.getString(R.string.search_all),
				BasicConstants.EMPTY_STRING);

		final List<String> fieldsValues = new ArrayList<String>();

		fieldsValues.add(this.activity.getString(R.string.search_all));
		if (cursor != null) {
			final int nbResults = cursor.getCount();
			for (int i = 0; i < nbResults; i++) {
				cursor.moveToPosition(i);
				final int idIndex = cursor
						.getColumnIndexOrThrow(IOrnidroidDAO.ID);
				final int nameIndex = cursor
						.getColumnIndexOrThrow(IOrnidroidDAO.NAME_COLUMN_NAME);

				if (intId) {
					mapNameId.put(cursor.getString(nameIndex),
							cursor.getInt(idIndex));
				} else {
					// in this case, if the id is a string (country code)
					mapNameCode.put(cursor.getString(nameIndex),
							cursor.getString(idIndex));
				}
				fieldsValues.add(cursor.getString(nameIndex));
			}
			cursor.close();
		}
		final SelectFieldsValue sfv = new SelectFieldsValue(mapNameId,
				mapNameCode, fieldsValues);
		return sfv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getGeographicDistribution(int)
	 */
	public List<String> getGeographicDistribution(int id) {
		final Cursor cursor = this.ornidroidDAO.getGeographicDistribution(id);
		final List<String> result = new ArrayList<String>();
		if (cursor != null) {
			final int nbResults = cursor.getCount();
			for (int i = 0; i < nbResults; i++) {
				cursor.moveToPosition(i);
				final int countryNameIndex = cursor
						.getColumnIndexOrThrow(IOrnidroidDAO.NAME_COLUMN_NAME);
				final String country = cursor.getString(countryNameIndex);
				result.add(country);
			}
			cursor.close();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getXenoCantoMapUrl(fr.ornidroid
	 * .bo.Bird)
	 */
	public String getXenoCantoMapUrl(Bird currentBird) {

		final StringBuffer sbuf = new StringBuffer();
		sbuf.append("<a href=\"http://www.xeno-canto.org/species/");

		sbuf.append(currentBird.getScientificName().replaceAll(
				BasicConstants.BLANK_STRING, BasicConstants.DASH_STRING));
		sbuf.append("\">");
		sbuf.append(Constants.getCONTEXT().getResources()
				.getString(R.string.xeno_canto_map));
		sbuf.append("</a>");
		return sbuf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#getMatchingBirds(java.lang.String)
	 */
	public List<SimpleBird> getMatchingBirds(String query) {
		queryResult = this.ornidroidDAO.getMatchingBirds(query);
		return queryResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getQueryResult()
	 */
	public List<SimpleBird> getQueryResult() {
		return queryResult == null ? new ArrayList<SimpleBird>() : queryResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.service.IOrnidroidService#getReleaseNotes()
	 */
	public String getReleaseNotes() {
		return ornidroidDAO.getReleaseNotes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.service.IOrnidroidService#updateSpinnerItemsCounts(fr.ornidroid
	 * .bo.MultiCriteriaSearchFormBean,
	 * fr.ornidroid.ui.multicriteriasearch.MultiCriteriaSearchFieldType)
	 */
	@Override
	public void updateSpinnerItemsCounts(
			final MultiCriteriaSearchFormBean formBean,
			MultiCriteriaSearchFieldType fieldType) {
		Cursor cursor = this.ornidroidDAO.updateSpinnerItemsCounts(formBean,
				fieldType);
		List<MultiCriteriaSeachMenuItem> itemsWithCounts = new ArrayList<MultiCriteriaSeachMenuItem>();
		// first item of the list : ALL
		MultiCriteriaSeachMenuItem itemAll = new MultiCriteriaSeachMenuItem();
		itemAll.setLabel(this.activity.getString(R.string.search_all));
		itemAll.setCount(this.ornidroidDAO
				.getMultiSearchCriteriaCountResults(formBean));
		itemsWithCounts.add(itemAll);

		if (cursor != null) {
			final int nbResults = cursor.getCount();

			for (int i = 0; i < nbResults; i++) {
				cursor.moveToPosition(i);
				MultiCriteriaSeachMenuItem item = new MultiCriteriaSeachMenuItem();
				String label = cursor.getString(0);
				item.setLabel(label);
				item.setCount(cursor.getInt(1));
				Log.i(BasicConstants.LOG_TAG, label
						+ BasicConstants.BLANK_STRING + cursor.getInt(1));
				itemsWithCounts.add(item);
			}
			cursor.close();
		}
		List<MultiCriteriaSeachMenuItem> listToUpdate = null;

		// TODO #150
		switch (fieldType) {
		case CATEGORY:
			listToUpdate = this.categoriesList;
			break;
		case COUNTRY:
			listToUpdate = this.countriesList;
			break;
		case REMARKABLE_SIGN:
			listToUpdate = this.remarkableSignsList;
		default:
			break;
		}
		listToUpdate.clear();
		listToUpdate.addAll(itemsWithCounts);

	}
}
