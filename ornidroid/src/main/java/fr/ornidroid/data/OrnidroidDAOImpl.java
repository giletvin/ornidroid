package fr.ornidroid.data;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import fr.ornidroid.bo.BirdFactoryImpl;
import fr.ornidroid.bo.MultiCriteriaSearchFormBean;
import fr.ornidroid.bo.SimpleBird;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.I18nHelper;
import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.helper.SupportedLanguage;
import fr.ornidroid.ui.multicriteriasearch.MultiCriteriaSearchFieldType;

/**
 * Contains sql queries to search for birds in the database.
 */
public class OrnidroidDAOImpl implements IOrnidroidDAO {
	/** The bird factory. */
	private final BirdFactoryImpl birdFactory;

	/**
	 * The Class SqlDynamicFragments. Contains the sql code snippets where and
	 * from
	 */
	private class SqlDynamicFragments {

		/** The from clause. */
		private final String fromClause;

		/** The where clause. */
		private final String whereClause;

		/**
		 * Instantiates a new sql dynamic fragments.
		 * 
		 * @param pWhereClause
		 *            the where clause
		 * @param pFromClause
		 *            the from clause
		 */
		public SqlDynamicFragments(final String pWhereClause,
				final String pFromClause) {
			this.whereClause = pWhereClause;
			this.fromClause = pFromClause;
		}

		/**
		 * Gets the from clause.
		 * 
		 * @return the from clause
		 */
		public String getFromClause() {
			return this.fromClause;
		}

		/**
		 * Gets the where clause.
		 * 
		 * @return the where clause
		 */
		public String getWhereClause() {
			return this.whereClause;
		}

	}

	/** The Constant AS. */
	private static final String AS = " as ";

	/** The Constant CATEGORY_TABLE_NAME. */
	private static final String CATEGORY_TABLE_NAME = "category";

	/** The Constant COUNT_STAR. */
	private static final String COUNT_STAR = " count(*) ";

	/** The Constant DESCRIPTION_TABLE. */
	private static final String DESCRIPTION_TABLE = "bird_description";

	/** The Constant FROM. */
	private static final String FROM = " from ";

	/** The Constant FTS_VIRTUAL_TABLE_TAXONOMY. */
	private static final String FTS_VIRTUAL_TABLE_TAXONOMY = "taxonomy";

	/** The Constant HABITAT_TABLE_NAME. */
	private static final String HABITAT_TABLE_NAME = "habitat";

	/** The Constant INNER_JOIN. */
	private static final String INNER_JOIN = " inner join ";

	/** The Constant LEFT_OUTER_JOIN. */
	private static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";

	/** The Constant ORDER_BY. */
	private static final String ORDER_BY = " order by ";

	/** The Constant SELECT. */
	private static final String SELECT = "select ";

	/** The Constant AND. */
	private static final String AND = " and ";

	/** The singleton. */
	private static IOrnidroidDAO singleton;

	/** The Constant WHERE. */
	private static final String WHERE = " where ";

	private static final String GROUP_BY = " GROUP BY ";

	/**
	 * Gets the single instance of OrnidroidDAOImpl.
	 * 
	 * @return single instance of OrnidroidDAOImpl
	 */
	public static IOrnidroidDAO getInstance() {

		return singleton;
	}

	/**
	 * Gets the single instance of OrnidroidDAOImpl. If it doesn't exist, create
	 * it.
	 * 
	 * @param dataBaseOpenHelper
	 *            the data base open helper
	 * @return single instance of OrnidroidDAOImpl
	 */
	public static IOrnidroidDAO getInstance(
			final OrnidroidDatabaseOpenHelper dataBaseOpenHelper) {
		if (null == singleton) {
			singleton = new OrnidroidDAOImpl(dataBaseOpenHelper);
		}
		return singleton;
	}

	/** The data base open helper. */
	private final OrnidroidDatabaseOpenHelper dataBaseOpenHelper;

	/**
	 * Constructor.
	 * 
	 * @param pDataBaseOpenHelper
	 *            the data base open helper
	 */
	private OrnidroidDAOImpl(
			final OrnidroidDatabaseOpenHelper pDataBaseOpenHelper) {
		this.dataBaseOpenHelper = pDataBaseOpenHelper;

		this.birdFactory = new BirdFactoryImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getBeakForms()
	 */
	public Cursor getBeakForms() {
		return getCursorFromListTable(BEAK_FORM_TABLE, NAME_COLUMN_NAME,
				I18nHelper.getLang());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getBird(java.lang.String)
	 */
	public Cursor getBird(final String rowId) {
		final String whereClause = WHERE + "bird.id = ?";
		final String[] selectionArgs = new String[] { rowId };

		return query(new SqlDynamicFragments(whereClause,
				Constants.EMPTY_STRING), selectionArgs, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.data.IOrnidroidDAO#getBirdMatchesFromMultiSearchCriteria
	 * (fr.ornidroid.bo.MultiCriteriaSearchFormBean)
	 */
	public List<SimpleBird> getBirdMatchesFromMultiSearchCriteria(
			final MultiCriteriaSearchFormBean formBean) {

		final Cursor cursor = query(getSqlDynamicFragments(formBean, true),
				null, false);
		return getBirdListFromCursor(cursor);

	}

	/**
	 * Gets the bird list from cursor and close the cursor once the birds are
	 * instanciated
	 * 
	 * @param cursor
	 *            the cursor
	 * @return the bird list from cursor
	 */
	private List<SimpleBird> getBirdListFromCursor(final Cursor cursor) {
		List<SimpleBird> results = new ArrayList<SimpleBird>();
		if (null == cursor) {
			return results;
		}

		final int nbRouws = cursor.getCount();
		for (int i = 0; i < nbRouws; i++) {
			cursor.moveToPosition(i);
			final int idIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);

			final String taxon = cursor.getString(cursor
					.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
			final String directoryName = cursor.getString(cursor
					.getColumnIndex(IOrnidroidDAO.DIRECTORY_NAME_COLUMN));
			final String scientificName = cursor.getString(cursor
					.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2));

			final SimpleBird bird = this.birdFactory.createSimpleBird(
					cursor.getInt(idIndex), taxon, directoryName,
					scientificName);
			results.add(bird);

		}
		cursor.close();
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getBirdNames(java.lang.Integer)
	 */
	public Cursor getBirdNames(final Integer id) {
		Cursor cursor = null;
		try {
			final SQLiteDatabase db = this.dataBaseOpenHelper
					.getReadableDatabase();
			final StringBuilder query = new StringBuilder();
			query.append(SELECT);
			query.append(LANG_COLUMN_NAME);
			query.append(Constants.COMMA_STRING);
			query.append(TAXON);
			query.append(" from taxonomy where bird_fk=");
			query.append(id);
			query.append(ORDER_BY);
			query.append(LANG_COLUMN_NAME);
			// Log.d(Constants.LOG_TAG, "Perform SQL query " +
			// query.toString());
			final String[] selectionArgs = null;
			cursor = db.rawQuery(query.toString(), selectionArgs);
			if (cursor == null) {
				return null;
			} else if (!cursor.moveToFirst()) {
				cursor.close();
				return null;
			}
		} catch (final SQLException e) {
			// Log.e(Constants.LOG_TAG, "Exception sql " + e);
		} finally {
			this.dataBaseOpenHelper.close();
		}
		return cursor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getCategories()
	 */
	public Cursor getCategories() {
		return getCursorFromListTable(CATEGORY_TABLE_NAME, NAME_COLUMN_NAME,
				I18nHelper.getLang());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getColours()
	 */
	public Cursor getColours() {
		return getCursorFromListTable(COLOUR_TABLE, NAME_COLUMN_NAME,
				I18nHelper.getLang());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getCountries()
	 */
	public Cursor getCountries() {
		Cursor cursor = null;
		try {
			final String nameColumnWithLangSuffix = NAME_COLUMN_NAME + "_"
					+ I18nHelper.getLang().getCode();
			final SQLiteDatabase db = this.dataBaseOpenHelper
					.getReadableDatabase();
			final StringBuilder query = new StringBuilder();
			query.append(SELECT);
			query.append("code as id");
			query.append(Constants.COMMA_STRING);
			query.append(nameColumnWithLangSuffix);
			query.append(AS);
			query.append(NAME_COLUMN_NAME);
			query.append(FROM);
			query.append(COUNTRY_TABLE);
			query.append(ORDER_BY);
			query.append(NAME_COLUMN_NAME);
			final String[] selectionArgs = null;
			cursor = db.rawQuery(query.toString(), selectionArgs);
			if (cursor == null) {
				return null;
			} else if (!cursor.moveToFirst()) {
				cursor.close();
			}
		} catch (final SQLException e) {
			Log.e(Constants.LOG_TAG, "Exception sql " + e);
		} finally {
			this.dataBaseOpenHelper.close();
		}
		return cursor;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getHabitats()
	 */
	public Cursor getHabitats() {
		return getCursorFromListTable(HABITAT_TABLE_NAME, NAME_COLUMN_NAME,
				I18nHelper.getLang());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.data.IOrnidroidDAO#getMultiSearchCriteriaCountResults(fr
	 * .ornidroid.bo.MultiCriteriaSearchFormBean)
	 */
	public int getMultiSearchCriteriaCountResults(
			final MultiCriteriaSearchFormBean formBean) {
		final StringBuilder countQuery = new StringBuilder();
		final SqlDynamicFragments sqlFragments = getSqlDynamicFragments(
				formBean, false);
		countQuery.append(SELECT).append(COUNT_STAR).append(FROM)
				.append(BIRD_TABLE).append(sqlFragments.getFromClause())
				.append(sqlFragments.getWhereClause());
		final SQLiteDatabase db = this.dataBaseOpenHelper.getReadableDatabase();
		final int countResults = (int) DatabaseUtils.longForQuery(db,
				countQuery.toString(), null);
		db.close();
		return countResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getRemarkableSigns()
	 */
	public Cursor getRemarkableSigns() {
		return getCursorFromListTable(REMARKABLE_SIGN_TABLE, NAME_COLUMN_NAME,
				I18nHelper.getLang());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getSizes()
	 */
	public Cursor getSizes() {
		return getCursorFromListTable(SIZE_TABLE, ID, I18nHelper.getLang());
	}

	/**
	 * Gets the cursor from list table with ID AND NAME column NAMEs (habitat,
	 * category).
	 * 
	 * @param tableName
	 *            the table name (habitat, category, ..)
	 * @param orderBy
	 *            the column used in the order by
	 * @param lang
	 *            the lang
	 * @return the cursor from list table
	 */
	private Cursor getCursorFromListTable(final String tableName,
			final String orderBy, final SupportedLanguage lang) {
		Cursor cursor = null;
		try {
			final SQLiteDatabase db = this.dataBaseOpenHelper
					.getReadableDatabase();
			final StringBuilder query = new StringBuilder();
			query.append(SELECT);
			query.append(ID);
			query.append(Constants.COMMA_STRING);
			query.append(NAME_COLUMN_NAME);
			query.append(FROM);
			query.append(tableName);
			query.append(WHERE).append("lang=\"");
			query.append(lang.getCode());
			query.append("\"");
			query.append(ORDER_BY);
			query.append(orderBy);
			final String[] selectionArgs = null;
			cursor = db.rawQuery(query.toString(), selectionArgs);
			if (cursor == null) {
				return null;
			} else if (!cursor.moveToFirst()) {
				// no results
				cursor.close();
				if (lang.equals(SupportedLanguage.FRENCH)) {
					return null;
				} else {
					// if not found in the locale of the user, try the same
					// search in FRENCH
					return getCursorFromListTable(tableName, orderBy,
							SupportedLanguage.FRENCH);
				}
			}
		} catch (final SQLException e) {
			Log.e(Constants.LOG_TAG, "Exception sql " + e);
		} finally {
			this.dataBaseOpenHelper.close();
		}
		return cursor;
	}

	/**
	 * Gets the where sql clauses according to the fields completed by the user
	 * in the Form bean.
	 * 
	 * @param formBean
	 *            the form bean
	 * @param resultQuery
	 *            context of the query : results (true) or count(*) query
	 *            (false)
	 * @return the where sql clauses
	 */
	private SqlDynamicFragments getSqlDynamicFragments(
			final MultiCriteriaSearchFormBean formBean,
			final boolean resultQuery) {
		final StringBuffer whereClauses = new StringBuffer();
		final StringBuffer fromClauses = new StringBuffer();
		whereClauses.append(WHERE).append("1=1");
		if (formBean.getCategoryId() != 0) {
			whereClauses.append(" AND bird.category_fk = ").append(
					formBean.getCategoryId());
		}
		if (formBean.getRemarkableSignId() != 0) {
			whereClauses.append(" AND bird.remarkable_sign_fk = ").append(
					formBean.getRemarkableSignId());
		}
		if (formBean.getHabitatId() != 0) {
			whereClauses.append(" AND (bird.habitat1_fk = ")
					.append(formBean.getHabitatId())
					.append(" OR bird.habitat2_fk = ")
					.append(formBean.getHabitatId()).append(")");
		}
		if (formBean.getFeatherColourId() != 0) {
			whereClauses.append(" AND (bird.feather_colour_fk = ")
					.append(formBean.getFeatherColourId())
					.append(" OR bird.feather_colour_2_fk = ")
					.append(formBean.getFeatherColourId()).append(")");
		}
		if (formBean.getBeakColourId() != 0) {
			whereClauses.append(" AND (bird.beak_colour_fk = ")
					.append(formBean.getBeakColourId())
					.append(" OR bird.beak_colour_2_fk = ")
					.append(formBean.getBeakColourId())
					.append(" OR bird.beak_colour_fk= -1 ").append(")");
		}
		if (formBean.getPawColourId() != 0) {
			whereClauses.append(" AND (bird.paw_colour_fk = ")
					.append(formBean.getPawColourId())
					.append(" OR bird.paw_colour_2_fk = ")
					.append(formBean.getPawColourId())
					.append(" OR bird.paw_colour_fk= -1 ").append(")");
		}
		if (formBean.getBeakFormId() != 0) {
			whereClauses.append(" AND bird.beak_form_fk = ").append(
					formBean.getBeakFormId());
		}
		if (formBean.getSizeId() != 0) {
			whereClauses.append(" AND bird.size_fk = ").append(
					formBean.getSizeId());
		}
		if (StringHelper.isNotBlank(formBean.getCountryCode())) {
			if (resultQuery) {
				// result query faster with this sql code with a subquery
				// and exists (select 1 from bird_country bc where
				// bc.bird_fk=bird.id and bc.country_code MATCH 'DEU')
				whereClauses
						.append(" and exists (select 1 from ")
						.append(BIRD_COUNTRY_TABLE)
						.append(" bc where bc.bird_fk=bird.id and bc.country_code MATCH '")
						.append(formBean.getCountryCode()).append("')");
			} else {
				// count query faster with this sql code
				fromClauses.append(INNER_JOIN).append(BIRD_COUNTRY_TABLE)
						.append(" on bird_country.bird_fk=bird.id");
				whereClauses.append(" AND bird_country.country_code MATCH '")
						.append(formBean.getCountryCode()).append("'")
						.append(" AND bird_country.bird_fk=bird.id");

			}
		}

		return new SqlDynamicFragments(whereClauses.toString(),
				fromClauses.toString());
	}

	/**
	 * Performs a database query.
	 * 
	 * @param sqlDynamicFragments
	 *            the sql dynamic fragments
	 * @param selectionArgs
	 *            Selection arguments for "?" components in the selection
	 * @param fullBirdInfo
	 *            if true, all info about the birds are requested and will be
	 *            stored in the cursor
	 * @return A Cursor over all rows matching the query
	 */
	private Cursor query(final SqlDynamicFragments sqlDynamicFragments,
			final String[] selectionArgs, final boolean fullBirdInfo) {
		Cursor cursor = null;
		try {
			final SQLiteDatabase db = this.dataBaseOpenHelper
					.getReadableDatabase();
			final StringBuilder query = new StringBuilder();
			query.append("select bird.id as ");
			query.append(BaseColumns._ID);
			query.append(",scientific_name as ");
			query.append(SearchManager.SUGGEST_COLUMN_TEXT_2);
			query.append(",taxon as ");
			query.append(SearchManager.SUGGEST_COLUMN_TEXT_1);
			query.append(", bird.id as ");
			query.append(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
			query.append(Constants.COMMA_STRING);
			query.append(DIRECTORY_NAME_COLUMN);
			if (fullBirdInfo) {
				query.append(Constants.COMMA_STRING);
				query.append(DESCRIPTION_COLUMN);
				query.append(Constants.COMMA_STRING);
				query.append(DISTRIBUTION_COLUMN);
				query.append(Constants.COMMA_STRING);
				query.append("scientific_order.name as ");
				query.append(SCIENTIFIC_ORDER_NAME_COLUMN);
				query.append(Constants.COMMA_STRING);
				query.append("scientific_family.name as ");
				query.append(SCIENTIFIC_FAMILY_NAME_COLUMN);
				query.append(Constants.COMMA_STRING);
				query.append("h1.name as ");
				query.append(HABITAT_1_NAME_COLUMN);
				query.append(Constants.COMMA_STRING);
				query.append("h2.name as ");
				query.append(HABITAT_2_NAME_COLUMN);
				query.append(Constants.COMMA_STRING);
				query.append(SIZE_VALUE_COLUMN);
				query.append(Constants.COMMA_STRING);
				query.append("category.name as ");
				query.append(CATEGORY_COLUMN);
				query.append(Constants.COMMA_STRING);
				query.append(IOrnidroidDAO.SCIENTIFIC_NAME_2_COLUMN);
				query.append(Constants.COMMA_STRING);
				query.append(IOrnidroidDAO.OISEAUX_NET_COLUMN);
			}
			query.append(FROM);
			query.append(FTS_VIRTUAL_TABLE_TAXONOMY);
			query.append(" ,");
			query.append(BIRD_TABLE);
			query.append(sqlDynamicFragments.getFromClause());
			if (fullBirdInfo) {
				query.append(LEFT_OUTER_JOIN);
				query.append(DESCRIPTION_TABLE);
				query.append(" on bird.id=");
				query.append(DESCRIPTION_TABLE);
				query.append(".bird_fk and ");
				query.append(DESCRIPTION_TABLE);
				query.append(".lang=\"");
				query.append(I18nHelper.getLang().getCode());
				query.append("\"");
				// join on scientific order table
				query.append(LEFT_OUTER_JOIN);
				query.append(SCIENTIFIC_ORDER_TABLE);
				query.append(" on ");
				query.append(BIRD_TABLE);
				query.append(".scientific_order_fk=");
				query.append(SCIENTIFIC_ORDER_TABLE);
				query.append(".id and ");
				query.append(SCIENTIFIC_ORDER_TABLE);
				query.append(".lang=\"");
				query.append(I18nHelper.getLang().getCode());
				query.append("\"");
				// join on scientific family table
				query.append(LEFT_OUTER_JOIN);
				query.append(SCIENTIFIC_FAMILY_TABLE);
				query.append(" on ");
				query.append(BIRD_TABLE);
				query.append(".scientific_family_fk=");
				query.append(SCIENTIFIC_FAMILY_TABLE);
				query.append(".id and ");
				query.append(SCIENTIFIC_FAMILY_TABLE);
				query.append(".lang=\"");
				query.append(I18nHelper.getLang().getCode());
				query.append("\"");
				// join on habitat table
				query.append(LEFT_OUTER_JOIN);
				query.append(HABITAT_TABLE_NAME);
				query.append(" h1 on ");
				query.append(BIRD_TABLE);
				query.append(".habitat1_fk=h1.id and h1.lang=\"");
				query.append(I18nHelper.getLang().getCode());
				query.append("\"");
				// habitat 2
				query.append(LEFT_OUTER_JOIN);
				query.append(HABITAT_TABLE_NAME);
				query.append(" h2 on ");
				query.append(BIRD_TABLE);
				query.append(".habitat2_fk=h2.id and h2.lang=\"");
				query.append(I18nHelper.getLang().getCode());
				query.append("\"");
				// join on category table
				query.append(LEFT_OUTER_JOIN);
				query.append(CATEGORY_TABLE_NAME);
				query.append(" on ");
				query.append(BIRD_TABLE);
				query.append(".category_fk=");
				query.append(CATEGORY_TABLE_NAME);
				query.append(".id and ");
				query.append(CATEGORY_TABLE_NAME);
				query.append(".lang=\"");
				query.append(I18nHelper.getLang().getCode());
				query.append("\"");
			}
			query.append(sqlDynamicFragments.getWhereClause());
			query.append(" and bird.id=taxonomy.bird_fk");
			query.append(handleSetOfLanguagesinSqlQuery(Constants
					.getOrnidroidSearchLanguages()));
			// query.append(" and taxonomy.lang=\"");
			// query.append(Constants.getOrnidroidSearchLanguages());
			// query.append("\"");
			query.append(" order by searched_taxon");
			// Log.d(Constants.LOG_TAG, "Perform SQL query " +
			// query.toString());
			cursor = db.rawQuery(query.toString(), selectionArgs);
			if (cursor == null) {
				return null;
			} else if (!cursor.moveToFirst()) {
				cursor.close();
				return null;
			}
		} catch (final SQLException e) {

			// Log.e(Constants.LOG_TAG, "Exception sql " + e);
		} finally {
			this.dataBaseOpenHelper.close();
		}
		return cursor;

	}

	/**
	 * Handle set of languagesin sql query.
	 * 
	 * @param ornidroidSearchLanguages
	 *            the ornidroid search languages
	 * @return the string
	 */
	private String handleSetOfLanguagesinSqlQuery(
			List<String> ornidroidSearchLanguages) {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(" and taxonomy.lang in (\"\"");
		for (String lang : ornidroidSearchLanguages) {
			sbuf.append(",\"");
			sbuf.append(lang);
			sbuf.append("\"");
		}
		sbuf.append(")");
		return sbuf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getGeographicDistribution(int)
	 */
	public Cursor getGeographicDistribution(int id) {
		Cursor cursor = null;
		try {
			final SQLiteDatabase db = this.dataBaseOpenHelper
					.getReadableDatabase();
			final StringBuilder query = new StringBuilder();
			String countryNameColumn = NAME_COLUMN_NAME
					+ Constants.UNDERSCORE_STRING
					+ I18nHelper.getLang().getCode();
			query.append(SELECT);
			query.append(countryNameColumn);
			query.append(AS);
			query.append(NAME_COLUMN_NAME);
			query.append(FROM);
			query.append(COUNTRY_TABLE);
			query.append(INNER_JOIN);
			query.append(BIRD_COUNTRY_TABLE);
			query.append(" on country_code=code");
			query.append(WHERE);
			query.append("bird_fk=");
			query.append(id);
			query.append(ORDER_BY);
			query.append(NAME_COLUMN_NAME);

			final String[] selectionArgs = null;
			cursor = db.rawQuery(query.toString(), selectionArgs);
			if (cursor == null) {
				return null;
			} else if (!cursor.moveToFirst()) {
				cursor.close();
				return null;
			}
		} catch (final SQLException e) {
			// Log.e(Constants.LOG_TAG, "Exception sql " + e);
		} finally {
			this.dataBaseOpenHelper.close();
		}
		return cursor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getMatchingBirds(java.lang.String)
	 */
	public List<SimpleBird> getMatchingBirds(String pQuery) {
		List<SimpleBird> results = new ArrayList<SimpleBird>();
		final StringBuffer whereClause = new StringBuffer().append(WHERE)
				.append(SEARCHED_TAXON).append(" MATCH ?");
		final String[] selectionArgs = new String[] { prepareSearchedString(pQuery) };
		final Cursor cursor = query(
				new SqlDynamicFragments(whereClause.toString(),
						Constants.EMPTY_STRING), selectionArgs, false);
		if (StringHelper.isBlank(pQuery) || null == cursor) {
			return results;
		}

		return getBirdListFromCursor(cursor);

	}

	/**
	 * Prepare searched string to be used in the 'match' sqlite clause.
	 * <ul>
	 * <li>adds "*" at the end of the string
	 * <li>remove diacritics
	 * <li>escape the '-' character
	 * </ul>
	 * 
	 * @param searchedString
	 *            the searched string
	 * @return the prepared string
	 */
	private String prepareSearchedString(String searchedString) {
		return StringHelper.replace(StringHelper.stripAccents(searchedString),
				BasicConstants.DASH_STRING, "'-'", -1)
				+ BasicConstants.STAR_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getReleaseNotes()
	 */
	public String getReleaseNotes() {
		Cursor cursor = null;
		String releaseNotes = null;
		try {
			final SQLiteDatabase db = this.dataBaseOpenHelper
					.getReadableDatabase();

			String commentsColumn = "comments_"
					+ I18nHelper.getLang().getCode();
			final StringBuilder query = new StringBuilder();
			query.append(SELECT);
			query.append(commentsColumn);
			query.append(FROM);
			query.append("release_notes");
			query.append(WHERE);
			query.append("version_code=");
			query.append(Constants.getVersionCode());
			query.append(AND);
			query.append("read=0");

			final String[] selectionArgs = null;
			cursor = db.rawQuery(query.toString(), selectionArgs);

			if (cursor.moveToFirst()) {
				int commentsColumnIndex = cursor.getColumnIndex(commentsColumn);
				releaseNotes = cursor.getString(commentsColumnIndex);
			}
		} catch (final SQLException e) {
			// Log.e(Constants.LOG_TAG, "Exception sql " + e);
		} finally {
			this.dataBaseOpenHelper.close();
			if (cursor != null) {
				cursor.close();
			}
		}
		if (StringHelper.isNotBlank(releaseNotes)) {
			updateReadReleaseNoteFlag(Constants.getVersionCode());

		}
		return releaseNotes;
	}

	/**
	 * Update read release note flag.
	 * 
	 * @param versionCode
	 *            the version code
	 */
	private void updateReadReleaseNoteFlag(int versionCode) {
		try {
			final SQLiteDatabase db = this.dataBaseOpenHelper
					.getWritableDatabase();

			String strSQL = "UPDATE release_notes SET read = 1 WHERE version_code="
					+ Constants.getVersionCode();
			db.execSQL(strSQL);
			// db.setTransactionSuccessful();
			// db.endTransaction();
		} catch (final SQLException e) {

		} finally {
			this.dataBaseOpenHelper.close();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.data.IOrnidroidDAO#updateSpinnerItemsCounts(fr.ornidroid
	 * .bo.MultiCriteriaSearchFormBean,
	 * fr.ornidroid.ui.multicriteriasearch.MultiCriteriaSearchFieldType)
	 */
	@Override
	public Cursor updateSpinnerItemsCounts(
			MultiCriteriaSearchFormBean formBean,
			MultiCriteriaSearchFieldType fieldType) {
		Cursor cursor = null;

		final StringBuilder countQuery = new StringBuilder();
		MultiCriteriaSearchFormBean clonedFormBean = formBean.clone();
		SqlDynamicFragments sqlFragments;
		switch (fieldType) {
		// TODO #150
		case CATEGORY:
			clonedFormBean.setCategoryId(null);
			sqlFragments = getSqlDynamicFragments(clonedFormBean, false);
			// select bird.category_fk, count(*) from bird group by category_fk;
			// select category.name, count(*) from bird inner join category on
			// category.id = bird.category_fk where category.lang='fr' group by
			// category.name order by category.name;
			countQuery.append(SELECT).append(NAME_COLUMN_NAME)
					.append(BasicConstants.COMMA_STRING).append(COUNT_STAR)
					.append(FROM).append(BIRD_TABLE)

					.append(sqlFragments.getFromClause())
					.append(BasicConstants.COMMA_STRING)
					.append(CATEGORY_TABLE_NAME)
					.append(" on category.id=bird.category_fk ")
					.append(sqlFragments.getWhereClause())
					.append(" and category.lang=\"")
					.append(I18nHelper.getLang().getCode()).append("\"")
					.append(GROUP_BY).append(CATEGORY_COLUMN).append("_fk");

			break;
		case COUNTRY:
			// select country.name_fr,count(*) from bird inner join bird_country
			// on bird_country.bird_fk = bird.id inner join country on
			// country.code=bird_country.country_code
			// group by country.name_fr order by country.name_fr asc;
			clonedFormBean.setCountryCode(null);
			sqlFragments = getSqlDynamicFragments(clonedFormBean, false);
			countQuery
					.append(SELECT)
					.append(NAME_COLUMN_NAME)
					.append(BasicConstants.UNDERSCORE_STRING)
					.append(I18nHelper.getLang().getCode())
					.append(BasicConstants.COMMA_STRING)
					.append(COUNT_STAR)
					.append(FROM)
					.append(BIRD_TABLE)
					.append(sqlFragments.getFromClause())
					// inner join country on
					// country.code=bird_country.country_code
					.append(INNER_JOIN)
					.append(COUNTRY_TABLE)
					.append(" on country.code=bird_country.country_code")
					// inner join bird_country on bird_country.bird_fk = bird.id
					.append(INNER_JOIN)
					.append(" bird_country on bird_country.bird_fk = bird.id")
					.append(sqlFragments.getWhereClause()).append(GROUP_BY)
					.append(NAME_COLUMN_NAME)
					.append(BasicConstants.UNDERSCORE_STRING)
					.append(I18nHelper.getLang().getCode());
			break;
		case REMARKABLE_SIGN:
			clonedFormBean.setRemarkableSignId(null);
			sqlFragments = getSqlDynamicFragments(clonedFormBean, false);
			// select remarkable_sign.name, count(*) from bird inner join
			// remarkable_sign on
			// remarkable_sign.id = bird.remarkable_sign_fk where
			// remarkable_sign.lang='fr' group by
			// remarkable_sign.name order by remarkable_sign.name;
			countQuery
					.append(SELECT)
					.append(NAME_COLUMN_NAME)
					.append(BasicConstants.COMMA_STRING)
					.append(COUNT_STAR)
					.append(FROM)
					.append(BIRD_TABLE)
					.append(sqlFragments.getFromClause())
					.append(BasicConstants.COMMA_STRING)
					.append(REMARKABLE_SIGN_TABLE)
					.append(" on remarkable_sign.id = bird.remarkable_sign_fk ")
					.append(sqlFragments.getWhereClause())
					.append(" and remarkable_sign.lang=\"")
					.append(I18nHelper.getLang().getCode()).append("\"")
					.append(GROUP_BY).append(REMARKABLE_SIGN_TABLE)
					.append("_fk");
			break;
		case BEAK_FORM:
			clonedFormBean.setBeakColourId(null);
			sqlFragments = getSqlDynamicFragments(clonedFormBean, false);
			countQuery.append(SELECT).append(NAME_COLUMN_NAME)
					.append(BasicConstants.COMMA_STRING).append(COUNT_STAR)
					.append(FROM).append(BIRD_TABLE)
					.append(sqlFragments.getFromClause())
					.append(BasicConstants.COMMA_STRING)
					.append(BEAK_FORM_TABLE)
					.append(" on beak_form.id = bird.beak_form_fk ")
					.append(sqlFragments.getWhereClause())
					.append(" and beak_form.lang=\"")
					.append(I18nHelper.getLang().getCode()).append("\"")
					.append(GROUP_BY).append(BEAK_FORM_TABLE).append("_fk");
			break;
		case SIZE:
			clonedFormBean.setSizeId(null);
			sqlFragments = getSqlDynamicFragments(clonedFormBean, false);
			countQuery.append(SELECT).append(NAME_COLUMN_NAME)
					.append(BasicConstants.COMMA_STRING).append(COUNT_STAR)
					.append(FROM).append(BIRD_TABLE)
					.append(sqlFragments.getFromClause())
					.append(BasicConstants.COMMA_STRING).append(SIZE_TABLE)
					.append(" on size_table.id = bird.size_fk ")
					.append(sqlFragments.getWhereClause())
					.append(" and size_table.lang=\"")
					.append(I18nHelper.getLang().getCode()).append("\"")
					.append(GROUP_BY).append("size_fk");
			break;
		default:
			break;
		}
		final SQLiteDatabase db = this.dataBaseOpenHelper.getReadableDatabase();
		try {
			cursor = db.rawQuery(countQuery.toString(), null);
			if (cursor == null) {
				return null;
			} else if (!cursor.moveToFirst()) {
				cursor.close();
				return null;
			}
		} catch (final SQLException e) {
			Log.e(Constants.LOG_TAG, "Exception sql " + e);
			if (cursor != null) {
				cursor.close();
			}
			cursor = null;

		} finally {
			this.dataBaseOpenHelper.close();
		}

		return cursor;

	}
}
