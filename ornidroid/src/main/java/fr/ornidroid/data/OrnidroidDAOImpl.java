package fr.ornidroid.data;


import android.app.SearchManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.ListAdapter;
import fr.ornidroid.bo.MultiCriteriaSearchFormBean;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.I18nHelper;
import fr.ornidroid.helper.StringHelper;

/**
 * Contains sql queries to search for birds in the database.
 */
public class OrnidroidDAOImpl implements IOrnidroidDAO {

	/** The Constant CATEGORY_TABLE_NAME. */
	private static final String CATEGORY_TABLE_NAME = "category";

	/** The Constant COUNT. */
	private static final String COUNT = " count(*) ";

	/** The Constant DESCRIPTION_TABLE. */
	private static final String DESCRIPTION_TABLE = "bird_description";

	/** The Constant FROM. */
	private static final String FROM = " from ";

	/** The Constant FTS_VIRTUAL_TABLE_TAXONOMY. */
	private static final String FTS_VIRTUAL_TABLE_TAXONOMY = "taxonomy";

	/** The Constant HABITAT_TABLE_NAME. */
	private static final String HABITAT_TABLE_NAME = "habitat";

	/** The Constant LEFT_OUTER_JOIN. */
	private static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";

	/** The Constant ORDER_BY. */
	private static final String ORDER_BY = " order by ";

	/** The Constant SELECT. */
	private static final String SELECT = "select ";

	/** The singleton. */
	private static IOrnidroidDAO singleton;

	/** The Constant WHERE. */
	private static final String WHERE = " where ";

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

	/** The history helper. */
	private final HistoryHelper historyHelper;

	/**
	 * Constructor.
	 * 
	 * @param pDataBaseOpenHelper
	 *            the data base open helper
	 */
	private OrnidroidDAOImpl(
			final OrnidroidDatabaseOpenHelper pDataBaseOpenHelper) {
		this.dataBaseOpenHelper = pDataBaseOpenHelper;
		this.historyHelper = new HistoryHelper();
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

		return query(whereClause, selectionArgs, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getBirdIdInHistory(int)
	 */
	public Integer getBirdIdInHistory(final int position) {
		return this.historyHelper.getBirdIdInHistory(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getBirdMatches(java.lang.String)
	 */
	public Cursor getBirdMatches(final String query) {
		final StringBuffer whereClause = new StringBuffer().append(WHERE)
				.append(SEARCHED_TAXON).append(" MATCH ?");
		final String[] selectionArgs = new String[] { query + "*" };
		final Cursor cursor = query(whereClause.toString(), selectionArgs,
				false);
		this.historyHelper.setHistory(cursor);
		return cursor;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.data.IOrnidroidDAO#getBirdMatchesFromMultiSearchCriteria
	 * (fr.ornidroid.bo.MultiCriteriaSearchFormBean)
	 */
	public void getBirdMatchesFromMultiSearchCriteria(
			final MultiCriteriaSearchFormBean formBean) {
		final Cursor cursor = query(getWhereSqlClauses(formBean), null, false);
		this.historyHelper.setHistory(cursor);
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
	 * @see fr.ornidroid.data.IOrnidroidDAO#getHabitats()
	 */
	public Cursor getHabitats() {
		return getCursorFromListTable(HABITAT_TABLE_NAME, NAME_COLUMN_NAME,
				I18nHelper.getLang());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#getHistoricResultsAdapter()
	 */
	public ListAdapter getHistoricResultsAdapter() {
		return this.historyHelper.getResultsAdapter();
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
		countQuery.append(SELECT).append(COUNT).append(FROM).append(BIRD_TABLE)
				.append(getWhereSqlClauses(formBean));
		final SQLiteDatabase db = this.dataBaseOpenHelper.getReadableDatabase();
		final int countResults = (int) DatabaseUtils.longForQuery(db,
				countQuery.toString(), null);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.data.IOrnidroidDAO#hasHistory()
	 */
	public boolean hasHistory() {
		return this.historyHelper.hasHistory();
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
			final String orderBy, final String lang) {
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
			query.append(lang);
			query.append("\"");
			query.append(ORDER_BY);
			query.append(orderBy);
			final String[] selectionArgs = null;
			cursor = db.rawQuery(query.toString(), selectionArgs);
			if (cursor == null) {
				return null;
			} else if (!cursor.moveToFirst()) {
				cursor.close();
				if (StringHelper.equals(lang, I18nHelper.FRENCH)) {
					return null;
				} else {
					// if not found in the locale of the user, try the same
					// search in FRENCH
					return getCursorFromListTable(tableName, orderBy,
							I18nHelper.FRENCH);
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
	 * @return the where sql clauses
	 */
	private String getWhereSqlClauses(final MultiCriteriaSearchFormBean formBean) {
		final StringBuffer whereClauses = new StringBuffer();
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
					.append(formBean.getBeakColourId()).append(")");
		}
		if (formBean.getPawColourId() != 0) {
			whereClauses.append(" AND (bird.paw_colour_fk = ")
					.append(formBean.getPawColourId())
					.append(" OR bird.paw_colour_2_fk = ")
					.append(formBean.getPawColourId()).append(")");
		}
		if (formBean.getBeakFormId() != 0) {
			whereClauses.append(" AND bird.beak_form_fk = ").append(
					formBean.getBeakFormId());
		}
		if (formBean.getSizeId() != 0) {
			whereClauses.append(" AND bird.size_fk = ").append(
					formBean.getSizeId());
		}
		return whereClauses.toString();
	}

	/**
	 * Performs a database query.
	 * 
	 * @param whereClause
	 *            The selection where clause of the sql query, containing the
	 *            WHERE keyword
	 * @param selectionArgs
	 *            Selection arguments for "?" components in the selection
	 * @param fullBirdInfo
	 *            if true, all info about the birds are requested and will be
	 *            stored in the cursor
	 * @return A Cursor over all rows matching the query
	 */
	private Cursor query(final String whereClause,
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
			}
			query.append(FROM);
			query.append(FTS_VIRTUAL_TABLE_TAXONOMY);
			query.append(" ,");
			query.append(BIRD_TABLE);
			if (fullBirdInfo) {
				query.append(LEFT_OUTER_JOIN);
				query.append(DESCRIPTION_TABLE);
				query.append(" on bird.id=");
				query.append(DESCRIPTION_TABLE);
				query.append(".bird_fk and ");
				query.append(DESCRIPTION_TABLE);
				query.append(".lang=\"");
				query.append(I18nHelper.getLang());
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
				query.append(I18nHelper.getLang());
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
				query.append(I18nHelper.getLang());
				query.append("\"");
				// join on habitat table
				query.append(LEFT_OUTER_JOIN);
				query.append(HABITAT_TABLE_NAME);
				query.append(" h1 on ");
				query.append(BIRD_TABLE);
				query.append(".habitat1_fk=h1.id and h1.lang=\"");
				query.append(I18nHelper.getLang());
				query.append("\"");
				// habitat 2
				query.append(LEFT_OUTER_JOIN);
				query.append(HABITAT_TABLE_NAME);
				query.append(" h2 on ");
				query.append(BIRD_TABLE);
				query.append(".habitat2_fk=h2.id and h2.lang=\"");
				query.append(I18nHelper.getLang());
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
				query.append(I18nHelper.getLang());
				query.append("\"");
			}
			query.append(whereClause);
			query.append(" and bird.id=taxonomy.bird_fk");
			query.append(" and taxonomy.lang=\"");
			query.append(Constants.getOrnidroidSearchLang());
			query.append("\"");
			query.append(" order by taxon");
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
}
