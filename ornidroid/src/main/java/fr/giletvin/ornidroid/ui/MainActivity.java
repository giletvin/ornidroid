package fr.giletvin.ornidroid.ui;

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
import fr.giletvin.ornidroid.R;
import fr.giletvin.ornidroid.data.DictionaryProvider;
import fr.giletvin.ornidroid.helper.Constants;
import fr.giletvin.ornidroid.service.IOrnidroidService;
import fr.giletvin.ornidroid.service.OrnidroidServiceFactory;

/**
 * The main activity for the dictionary. Displays search results triggered by
 * the search dialog and handles actions from search suggestions.
 */
public class MainActivity extends AbstractOrnidroidActivity {

	private static final String USER_QUERY = "user_query";

	/** The m list view. */
	private ListView mListView;

	/** The search field. */
	private AutoCompleteTextView searchField;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/**
	 * Instantiates a new main activity.
	 */
	public MainActivity() {
		super();
		Constants.initializeConstants(this);
		ornidroidService = OrnidroidServiceFactory.getService(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mListView = (ListView) findViewById(R.id.list);
		setTitle(R.string.app_name);
		Intent intent = getIntent();
		initAutoCompleteField(intent);

	}

	/**
	 * Inits the auto complete field. Set up the suggest methods and the
	 * behaviour to open the bird info activity
	 * 
	 * @param intent
	 */
	private void initAutoCompleteField(Intent intent) {
		searchField = (AutoCompleteTextView) findViewById(R.id.home_search_field);

		// if coming with a query previously typed by the user
		CharSequence userQuery = intent.getCharSequenceExtra(USER_QUERY);
		searchField.setText(userQuery);

		final int[] to = new int[] { android.R.id.text1 };
		final String[] from = new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1 };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_dropdown_item_1line, null, from, to);
		searchField.setAdapter(adapter);

		// Set an OnItemClickListener, to open the BirdInfoActivity
		searchField.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				// Get the cursor, positioned to the corresponding row in the
				// result set
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);
				String birdId = cursor.getString(cursor
						.getColumnIndexOrThrow(BaseColumns._ID));
				startActivity(buildIntentBirdInfoActivity(birdId));
			}
		});

		// Set the CursorToStringConverter, to provide the labels for the
		// choices to be displayed in the AutoCompleteTextView.
		adapter.setCursorToStringConverter(new CursorToStringConverter() {
			public String convertToString(android.database.Cursor cursor) {
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
			public Cursor runQuery(CharSequence constraint) {
				// Search for birds whose names begin with the specified
				// letters.
				Cursor cursor = ornidroidService
						.getBirdMatches((constraint != null ? constraint
								.toString() : null));
				return cursor;
			}
		});

		// action when the user tapes Enter : reopen the same activity with the
		// search results displayed in the list
		searchField.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra(USER_QUERY, v.getText());
				startActivity(intent);
				return true;
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if (ornidroidService.hasHistory()) {
			mListView.setAdapter(ornidroidService.getHistoricResultsAdapter());

			mListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					startActivity(buildIntentBirdInfoActivity(String
							.valueOf(ornidroidService
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
	private Intent buildIntentBirdInfoActivity(String birdId) {
		Intent birdIntent = new Intent(getApplicationContext(),
				BirdInfoActivity.class);
		Uri data = Uri.withAppendedPath(DictionaryProvider.CONTENT_URI, birdId);
		birdIntent.setData(data);
		return birdIntent;
	}

}
