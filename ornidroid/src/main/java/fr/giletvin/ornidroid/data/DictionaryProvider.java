package fr.giletvin.ornidroid.data;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Provides access to the dictionary database.
 */
public class DictionaryProvider extends ContentProvider {

	/** The AUTHORITY. */
	public static String AUTHORITY = "fr.giletvin.ornidroid.data.DictionaryProvider";

	/** The Constant CONTENT_URI. */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/dictionary");

	/** The Constant DEFINITION_MIME_TYPE. */
	public static final String DEFINITION_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/vnd.example.android.searchabledict";

	// MIME types used for searching words or looking up a single definition
	/** The Constant WORDS_MIME_TYPE. */
	public static final String WORDS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/vnd.example.android.searchabledict";

	/** The Constant GET_WORD. */
	private static final int GET_WORD = 1;

	/** The Constant REFRESH_SHORTCUT. */
	private static final int REFRESH_SHORTCUT = 3;

	/** The Constant SEARCH_SUGGEST. */
	private static final int SEARCH_SUGGEST = 2;

	// UriMatcher stuff
	/** The Constant SEARCH_WORDS. */
	private static final int SEARCH_WORDS = 0;

	/** The Constant sURIMatcher. */
	private static final UriMatcher sURIMatcher = buildUriMatcher();

	/**
	 * Builds up a UriMatcher for search suggestion and shortcut refresh
	 * queries.
	 * 
	 * @return the uri matcher
	 */
	private static UriMatcher buildUriMatcher() {
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		// to get definitions...
		matcher.addURI(AUTHORITY, "dictionary", SEARCH_WORDS);
		matcher.addURI(AUTHORITY, "dictionary/#", GET_WORD);
		// to get suggestions...
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY,
				SEARCH_SUGGEST);
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*",
				SEARCH_SUGGEST);

		/*
		 * The following are unused in this implementation, but if we include
		 * {@link SearchManager#SUGGEST_COLUMN_SHORTCUT_ID} as a column in our
		 * suggestions table, we could expect to receive refresh queries when a
		 * shortcutted suggestion is displayed in Quick Search Box, in which
		 * case, the following Uris would be provided and we would return a
		 * cursor with a single item representing the refreshed suggestion data.
		 */
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT,
				REFRESH_SHORTCUT);
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT
				+ "/*", REFRESH_SHORTCUT);
		return matcher;
	}

	/** The TAG. */
	String TAG = "DictionaryProvider";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is required in order to query the supported types. It's also
	 * useful in our own query() method to determine the type of Uri received.
	 * 
	 * @param uri
	 *            the uri
	 * @return the type
	 */
	@Override
	public String getType(Uri uri) {
		switch (sURIMatcher.match(uri)) {
		case SEARCH_WORDS:
			return WORDS_MIME_TYPE;
		case GET_WORD:
			return DEFINITION_MIME_TYPE;
		case SEARCH_SUGGEST:
			return SearchManager.SUGGEST_MIME_TYPE;
		case REFRESH_SHORTCUT:
			return SearchManager.SHORTCUT_MIME_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	/**
	 * Handles all the dictionary searches and suggestion queries from the
	 * Search Manager. When requesting a specific word, the uri alone is
	 * required. When searching all of the dictionary for matches, the
	 * selectionArgs argument must carry the search query as the first element.
	 * All other arguments are ignored.
	 * 
	 * @param uri
	 *            the uri
	 * @param projection
	 *            the projection
	 * @param selection
	 *            the selection
	 * @param selectionArgs
	 *            the selection args
	 * @param sortOrder
	 *            the sort order
	 * @return the cursor
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Use the UriMatcher to see what kind of query we have and format the
		// db query accordingly
		switch (sURIMatcher.match(uri)) {
		case SEARCH_SUGGEST:
			if (selectionArgs == null) {
				throw new IllegalArgumentException(
						"selectionArgs must be provided for the Uri: " + uri);
			}
			return getSuggestions(selectionArgs[0]);
		case SEARCH_WORDS:
			if (selectionArgs == null) {
				throw new IllegalArgumentException(
						"selectionArgs must be provided for the Uri: " + uri);
			}
			return search(selectionArgs[0]);

		case GET_WORD:
			return getWord(uri);
		case REFRESH_SHORTCUT:
			return refreshShortcut(uri);
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	private IOrnidroidDAO getDao() {
		return OrnidroidDAOImpl.getInstance();
	}

	// Other required implementations...

	/**
	 * Gets the suggestions.
	 * 
	 * @param query
	 *            the query
	 * @return the suggestions
	 */
	private Cursor getSuggestions(String query) {
		query = query.toLowerCase();
		Cursor cursor = getDao().getBirdMatches(query);

		return cursor;
	}

	/**
	 * Gets the word.
	 * 
	 * @param uri
	 *            the uri
	 * @return the word
	 */
	private Cursor getWord(Uri uri) {
		String rowId = uri.getLastPathSegment();
		return this.getDao().getBird(rowId);
	}

	/**
	 * Refresh shortcut.
	 * 
	 * @param uri
	 *            the uri
	 * @return the cursor
	 */
	private Cursor refreshShortcut(Uri uri) {
		/*
		 * This won't be called with the current implementation, but if we
		 * include {@link SearchManager#SUGGEST_COLUMN_SHORTCUT_ID} as a column
		 * in our suggestions table, we could expect to receive refresh queries
		 * when a shortcutted suggestion is displayed in Quick Search Box. In
		 * which case, this method will query the table for the specific word,
		 * using the given item Uri and provide all the columns originally
		 * provided with the suggestion query.
		 */
		String rowId = uri.getLastPathSegment();

		return this.getDao().getBird(rowId);
	}

	/**
	 * Search.
	 * 
	 * @param query
	 *            the query
	 * @return the cursor
	 */
	private Cursor search(String query) {
		query = query.toLowerCase();
		Cursor cursor = this.getDao().getBirdMatches(query);
		// Constants.setRESULTS_ADAPTER(cursor);
		return cursor;
	}

}
