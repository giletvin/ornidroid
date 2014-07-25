package fr.ornidroid.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import fr.ornidroid.R;
import fr.ornidroid.bo.BirdFactoryImpl;
import fr.ornidroid.bo.SimpleBird;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.components.OrnidroidViewBinder;

/**
 * The main activity for the dictionary. Displays search results triggered by
 * the search dialog and handles actions from search suggestions.
 */
public class MainActivity extends AbstractOrnidroidActivity {

	/**
	 * The Constant SHOW_SEARCH_FIELD_INTENT_PRM to hide or show the search
	 * field at the top of this screen.
	 */
	public static final String SHOW_SEARCH_FIELD_INTENT_PRM = "show_search_field";

	/** The Constant BIRD_ID_ITENT_PRM. */
	public static final String BIRD_ID_ITENT_PRM = "bird_id_intent_prm";

	/** The bird factory. */
	private final BirdFactoryImpl birdFactory;
	/**
	 * mapping in the adapter results between the from columns in SQL and the
	 * "to" fields in the displayed results.
	 */
	private final String[] from = new String[] {
			SearchManager.SUGGEST_COLUMN_TEXT_1,
			SearchManager.SUGGEST_COLUMN_TEXT_2 };
	/**
	 * mapping in the adapter results between the from columns in SQL and the
	 * "to" fields in the displayed results.
	 * 
	 * 
	 */
	private final int[] to = new int[] { R.id.taxon, R.id.scientific_name };
	/** The clicked position in the list. */
	private int clickedPositionInTheList = 0;

	/** The m list view. */
	private ListView mListView;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The search field. */
	private AutoCompleteTextView searchField;

	/** The adapter autocomplete text view. */
	private ArrayAdapter<SimpleBird> adapterAutocompleteTextView;

	/** The adapter results. */
	private SimpleAdapter adapterResults;
	/**
	 * This list contains the ids of the birds which are referenced in the
	 * results_adapter.
	 * 
	 */
	private List<Integer> resultsBirdIds;

	/**
	 * Instantiates a new main activity.
	 */
	public MainActivity() {
		super();
		this.ornidroidService = OrnidroidServiceFactory.getService(this);
		this.birdFactory = new BirdFactoryImpl();
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
		this.mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				MainActivity.this.clickedPositionInTheList = position;
				Integer clickedBirdId = resultsBirdIds.get(position);
				startActivity(buildIntentBirdInfoActivity(clickedBirdId));

			}
		});
		if (this.ornidroidService.hasHistory()) {
			printQueryResults();
			this.mListView.setSelection(this.clickedPositionInTheList);

		}
	}

	/**
	 * Builds the intent to open bird info activity.
	 * 
	 * @param birdId
	 *            the bird id
	 * @return the intent
	 */
	private Intent buildIntentBirdInfoActivity(final Integer birdId) {
		final Intent birdIntent = new Intent(getApplicationContext(),
				BirdActivity.class);
		birdIntent.putExtra(BIRD_ID_ITENT_PRM, birdId);
		return birdIntent;
	}

	/**
	 * Inits the auto complete field. Set up the suggest methods and the
	 * behaviour to open the bird info activity
	 * 
	 * @param intent
	 *            the intent
	 */
	private void initAutoCompleteField(final Intent intent) {
		this.searchField = (AutoCompleteTextView) findViewById(R.id.home_search_field);
		final boolean showTextField = intent.getBooleanExtra(
				SHOW_SEARCH_FIELD_INTENT_PRM, true);
		if (!showTextField) {
			this.searchField.setVisibility(View.GONE);
		}

		// add the listener so it will tries to suggest while the user types
		searchField.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence userInput, int start,
					int before, int count) {
				// query the database based on the user input
				List<SimpleBird> queryResult = ornidroidService
						.getMatchingBirds(userInput.toString());

				// update the adapater
				adapterAutocompleteTextView.notifyDataSetChanged();
				adapterAutocompleteTextView = new ArrayAdapter<SimpleBird>(
						MainActivity.this,
						android.R.layout.simple_dropdown_item_1line,
						queryResult);
				searchField.setAdapter(adapterAutocompleteTextView);

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});

		// set our adapter
		adapterAutocompleteTextView = new ArrayAdapter<SimpleBird>(this,
				android.R.layout.simple_dropdown_item_1line,
				ornidroidService.getQueryResult());
		searchField.setAdapter(adapterAutocompleteTextView);

		// Set an OnItemClickListener, to open the BirdInfoActivity
		this.searchField.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> listView,
					final View view, final int position, final long id) {
				if (ornidroidService.getQueryResult().size() == 1) {
					SimpleBird clickedBird = ornidroidService.getQueryResult()
							.get(0);
					printQueryResults();
					startActivity(buildIntentBirdInfoActivity(clickedBird
							.getId()));
				}
			}
		});

		// action when the user tapes Enter : reopen the same activity with the
		// search results displayed in the list
		this.searchField
				.setOnEditorActionListener(new OnEditorActionListener() {
					public boolean onEditorAction(final TextView v,
							final int actionId, final KeyEvent event) {
						printQueryResults();
						// #39 : just refresh the list instead of starting the
						// activity
						return true;
					}

				});
	}

	/**
	 * Print query results in the list view.
	 */
	private final void printQueryResults() {
		final List<Map<String, SimpleBird>> data = new ArrayList<Map<String, SimpleBird>>();
		resultsBirdIds = new ArrayList<Integer>();
		for (SimpleBird sBird : ornidroidService.getQueryResult()) {
			final Map<String, SimpleBird> map = new HashMap<String, SimpleBird>();
			map.put(SearchManager.SUGGEST_COLUMN_TEXT_1, sBird);
			SimpleBird sBirdScientificName = birdFactory.createSimpleBird(
					sBird.getId(), sBird.getScientificName(),
					sBird.getBirdDirectoryName(), sBird.getScientificName());
			map.put(SearchManager.SUGGEST_COLUMN_TEXT_2, sBirdScientificName);
			data.add(map);
			resultsBirdIds.add(sBird.getId());

		}

		// #39 : just refresh the list instead of starting the
		// activity
		MainActivity.this.adapterResults = new SimpleAdapter(
				Constants.getCONTEXT(), data, R.layout.result,
				MainActivity.this.from, MainActivity.this.to);
		MainActivity.this.adapterResults
				.setViewBinder(new OrnidroidViewBinder());
		MainActivity.this.mListView
				.setAdapter(MainActivity.this.adapterResults);
		MainActivity.this.searchField.dismissDropDown();
	}

}
