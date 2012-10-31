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

/**
 * Contains sql queries to search for birds in the database.
 */
public class OrnidroidDAOImpl implements IOrnidroidDAO {

	/** The Constant DESCRIPTION_TABLE. */
	private static final String DESCRIPTION_TABLE = "bird_description";

	/** The Constant FTS_VIRTUAL_TABLE_TAXONOMY. */
	private static final String FTS_VIRTUAL_TABLE_TAXONOMY = "taxonomy";

	/** The singleton. */
	private static IOrnidroidDAO singleton;

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
	 * @see fr.ornidroid.data.IOrnidroidDAO#getBird(java.lang.String)
	 */
	public Cursor getBird(final String rowId) {
		final String selection = "bird.id = ?";
		final String[] selectionArgs = new String[] { rowId };

		return query(selection, selectionArgs, true);

		/*
		 * This builds a query that looks like: SELECT <columns> FROM <table>
		 * WHERE rowid = <rowId>
		 */
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
		final String selection = SEARCHED_TAXON + " MATCH ?";
		final String[] selectionArgs = new String[] { query + "*" };
		final Cursor cursor = query(selection, selectionArgs, false);
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
		String selection = "1=1";
		if (formBean.getCategoryId() != 0) {
			selection += " AND bird.category_fk = " + formBean.getCategoryId();
		}
		if (formBean.getHabitatId() != 0) {
			selection += " AND (bird.habitat1_fk = " + formBean.getHabitatId()
					+ " OR bird.habitat2_fk = " + formBean.getHabitatId() + ")";
		}
		// final String[] selectionArgs = new String[] {
		// formBean.getCategoryId()
		// .toString() };

		final Cursor cursor = query(selection, null, false);
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
			query.append("select ");
			query.append(LANG_COLUMN_NAME);
			query.append(Constants.COMMA_STRING);
			query.append(TAXON);
			query.append(" from taxonomy where bird_fk=");
			query.append(id);
			query.append(" order by ");
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
		Cursor cursor = null;
		try {
			final SQLiteDatabase db = this.dataBaseOpenHelper
					.getReadableDatabase();
			final StringBuilder query = new StringBuilder();
			query.append("select ");
			query.append("id");
			query.append(Constants.COMMA_STRING);
			query.append("name");
			query.append(" from category where ");
			query.append("lang=\"");
			query.append(Constants.getOrnidroidLang());
			query.append("\"");
			query.append(" order by ");
			query.append("name");
			final String[] selectionArgs = null;
			cursor = db.rawQuery(query.toString(), selectionArgs);
			if (cursor == null) {
				return null;
			} else if (!cursor.moveToFirst()) {
				cursor.close();
				return null;
			}
		} catch (final SQLException e) {
			Log.e(Constants.LOG_TAG, "Exception sql " + e);
		} finally {
			this.dataBaseOpenHelper.close();
		}
		return cursor;
	}

	public Cursor getHabitats() {
		Cursor cursor = null;
		try {
			final SQLiteDatabase db = this.dataBaseOpenHelper
					.getReadableDatabase();
			final StringBuilder query = new StringBuilder();
			query.append("select ");
			query.append("id");
			query.append(Constants.COMMA_STRING);
			query.append("name");
			query.append(" from habitat where ");
			query.append("lang=\"");
			query.append(Constants.getOrnidroidLang());
			query.append("\"");
			query.append(" order by ");
			query.append("name");
			final String[] selectionArgs = null;
			cursor = db.rawQuery(query.toString(), selectionArgs);
			if (cursor == null) {
				return null;
			} else if (!cursor.moveToFirst()) {
				cursor.close();
				return null;
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
		// TODO : à améliorer et refactorer avec la méthode query déjà utilisée
		String countQuery;
		countQuery = "select count(*) from " + BIRD_TABLE + " where 1=1";
		if (formBean.getCategoryId() != 0) {
			countQuery += " AND category_fk=" + formBean.getCategoryId();
		}
		if (formBean.getHabitatId() != 0) {
			countQuery += " AND (bird.habitat1_fk = " + formBean.getHabitatId()
					+ " OR bird.habitat2_fk = " + formBean.getHabitatId() + ")";
		}
		final SQLiteDatabase db = this.dataBaseOpenHelper.getReadableDatabase();
		final int countResults = (int) DatabaseUtils.longForQuery(db,
				countQuery, null);

		return countResults;
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
	 * Performs a database query.
	 * 
	 * @param selection
	 *            The selection where clause of the sql query
	 * @param selectionArgs
	 *            Selection arguments for "?" components in the selection
	 * @param fullBirdInfo
	 *            if true, all info about the birds are requested and will be
	 *            stored in the cursor
	 * @return A Cursor over all rows matching the query
	 */
	private Cursor query(final String selection, final String[] selectionArgs,
			final boolean fullBirdInfo) {

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
			}
			query.append(" from ");
			query.append(FTS_VIRTUAL_TABLE_TAXONOMY);
			query.append(" ,");
			query.append(BIRD_TABLE);
			if (fullBirdInfo) {
				query.append(" LEFT OUTER JOIN ");
				query.append(DESCRIPTION_TABLE);
				query.append(" on bird.id=");
				query.append(DESCRIPTION_TABLE);
				query.append(".bird_fk and ");
				query.append(DESCRIPTION_TABLE);
				query.append(".lang=\"");
				query.append(Constants.getOrnidroidLang());
				query.append("\"");
				// join on scientific order table
				query.append(" LEFT OUTER JOIN ");
				query.append(SCIENTIFIC_ORDER_TABLE);
				query.append(" on ");
				query.append(BIRD_TABLE);
				query.append(".scientific_order_fk=");
				query.append(SCIENTIFIC_ORDER_TABLE);
				query.append(".id and ");
				query.append(SCIENTIFIC_ORDER_TABLE);
				query.append(".lang=\"");
				query.append(Constants.getOrnidroidLang());
				query.append("\"");
				// join on scientific family table
				query.append(" LEFT OUTER JOIN ");
				query.append(SCIENTIFIC_FAMILY_TABLE);
				query.append(" on ");
				query.append(BIRD_TABLE);
				query.append(".scientific_family_fk=");
				query.append(SCIENTIFIC_FAMILY_TABLE);
				query.append(".id and ");
				query.append(SCIENTIFIC_FAMILY_TABLE);
				query.append(".lang=\"");
				query.append(Constants.getOrnidroidLang());
				query.append("\"");

			}
			query.append(" where ");
			query.append(selection);
			query.append(" and bird.id=taxonomy.bird_fk");

			query.append(" and taxonomy.lang=\"");
			query.append(Constants.getOrnidroidLang());
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
