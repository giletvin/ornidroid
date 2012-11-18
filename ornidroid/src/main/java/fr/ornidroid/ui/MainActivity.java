package fr.ornidroid.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import fr.ornidroid.R;
import fr.ornidroid.data.DictionaryProvider;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;

/**
 * The main activity for the dictionary. Displays search results triggered by
 * the search dialog and handles actions from search suggestions.
 */
public class MainActivity extends AbstractOrnidroidActivity {

	/**
	 * The Constant SHOW_SEARCH_FIELD_INTENT_PRM to hide or show the search
	 * field at the top of this screen
	 */
	public static final String SHOW_SEARCH_FIELD_INTENT_PRM = "show_search_field";

	/** The Constant USER_QUERY. */
	private static final String USER_QUERY = "user_query";

	/** The clicked position in the list. */
	private int clickedPositionInTheList = 0;

	/** The m list view. */
	private ListView mListView;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The search field. */
	private AutoCompleteTextView searchField;

	/**
	 * Instantiates a new main activity.
	 */
	public MainActivity() {
		super();
		Constants.initializeConstants(this);
		this.ornidroidService = OrnidroidServiceFactory.getService(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.mListView = (ListView) findViewById(R.id.list);

		setTitle(R.string.app_name);
		final Intent intent = getIntent();
		initAutoCompleteField(intent);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if (this.ornidroidService.hasHistory()) {
			this.mListView.setAdapter(this.ornidroidService
					.getHistoricResultsAdapter());
			this.mListView.setSelection(this.clickedPositionInTheList);
			this.mListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(final AdapterView<?> parent,
						final View view, final int position, final long id) {
					MainActivity.this.clickedPositionInTheList = position;
					startActivity(buildIntentBirdInfoActivity(String
							.valueOf(MainActivity.this.ornidroidService
									.getBirdIdInHistory(position))));
				}
			});
		}
	}

	/**
	 * Builds the intent to open bird info activity.
	 * 
	 * @param birdId
	 *            the bird id
	 * @return the intent
	 */
	private Intent buildIntentBirdInfoActivity(final String birdId) {
		final Intent birdIntent = new Intent(getApplicationContext(),
				BirdActivity.class);
		final Uri data = Uri.withAppendedPath(DictionaryProvider.CONTENT_URI,
				birdId);
		birdIntent.setData(data);
		return birdIntent;
	}

	/**
	 * Inits the auto complete field. Set up the suggest methods and the
	 * behaviour to open the bird info activity
	 * 
	 * @param intent
	 */
	private void initAutoCompleteField(final Intent intent) {
		this.searchField = (AutoCompleteTextView) findViewById(R.id.home_search_field);
		final boolean showTextField = intent.getBooleanExtra(
				SHOW_SEARCH_FIELD_INTENT_PRM, true);
		if (!showTextField) {
			this.searchField.setVisibility(View.GONE);
		}

		// if coming with a query previously typed by the user
		final CharSequence userQuery = intent.getCharSequenceExtra(USER_QUERY);
		this.searchField.setText(userQuery);

		final int[] to = new int[] { android.R.id.text1 };
		final String[] from = new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1 };

		final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_dropdown_item_1line, null, from, to);
		this.searchField.setAdapter(adapter);

		// Set an OnItemClickListener, to open the BirdInfoActivity
		this.searchField.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> listView,
					final View view, final int position, final long id) {
				// Get the cursor, positioned to the corresponding row in the
				// result set
				final Cursor cursor = (Cursor) listView
						.getItemAtPosition(position);
				final String birdId = cursor.getString(cursor
						.getColumnIndexOrThrow(BaseColumns._ID));
				startActivity(buildIntentBirdInfoActivity(birdId));
			}
		});

		// Set the CursorToStringConverter, to provide the labels for the
		// choices to be displayed in the AutoCompleteTextView.
		adapter.setCursorToStringConverter(new CursorToStringConverter() {
			public String convertToString(final android.database.Cursor cursor) {
				// Get the label for this row out of the bird's name column
				final int columnIndex = cursor
						.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
				final String str = cursor.getString(columnIndex);
				return str;
			}
		});

		// Set the FilterQueryProvider, to run queries for choices
		// that match the specified input.
		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(final CharSequence constraint) {
				// Search for birds whose names begin with the specified
				// letters.
				final Cursor cursor = MainActivity.this.ornidroidService
						.getBirdMatches((constraint != null ? constraint
								.toString() : null));
				return cursor;
			}
		});

		// action when the user tapes Enter : reopen the same activity with the
		// search results displayed in the list
		this.searchField
				.setOnEditorActionListener(new OnEditorActionListener() {
					public boolean onEditorAction(final TextView v,
							final int actionId, final KeyEvent event) {
						final Intent intent = new Intent(
								getApplicationContext(), MainActivity.class);
						intent.putExtra(USER_QUERY, v.getText());
						startActivity(intent);
						return true;
					}

				});
	}

}
