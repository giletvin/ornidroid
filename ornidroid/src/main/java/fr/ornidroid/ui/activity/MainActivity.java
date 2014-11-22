package fr.ornidroid.ui.activity;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.app.ListActivity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import fr.ornidroid.R;
import fr.ornidroid.bo.SimpleBird;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.NewBirdActivity;
import fr.ornidroid.ui.adapter.SearchResultsAdapter;
import fr.ornidroid.ui.components.OrnidroidAutoCompleteAdapter;

/**
 * The main activity for the dictionary. Displays search results triggered by
 * the search dialog and handles actions from search suggestions.
 */
@EActivity(R.layout.main)
public class MainActivity extends ListActivity {
	SearchResultsAdapter adapter;
	/**
	 * The Constant SHOW_SEARCH_FIELD_INTENT_PRM to hide or show the search
	 * field at the top of this screen.
	 */
	public static final String SHOW_SEARCH_FIELD_INTENT_PRM = "show_search_field";

	/** The Constant BIRD_ID_ITENT_PRM. */
	public static final String BIRD_ID_ITENT_PRM = "bird_id_intent_prm";

	/** The clicked position in the list. */
	private int clickedPositionInTheList = 0;

	@ViewById(android.R.id.list)
	ListView mListView;
	@Extra(SHOW_SEARCH_FIELD_INTENT_PRM)
	boolean showTextField = true;
	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The search field. */
	@ViewById(R.id.home_search_field)
	AutoCompleteTextView searchField;

	/** The adapter autocomplete text view. */
	private ArrayAdapter<SimpleBird> adapterAutocompleteTextView;

	/**
	 * Instantiates a new main activity.
	 */
	public MainActivity() {
		super();
		this.ornidroidService = OrnidroidServiceFactory.getService(this);
	}

	/**
	 * After views.
	 */
	@AfterViews
	void afterViews() {
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
				NewBirdActivity.class);
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
	@AfterViews
	void initAutoCompleteField() {
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
				adapterAutocompleteTextView = new OrnidroidAutoCompleteAdapter(
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
	 * Bird clicked.
	 * 
	 * @param bird
	 *            the bird
	 */
	@ItemClick(android.R.id.list)
	void birdClicked(SimpleBird bird) {
		startActivity(buildIntentBirdInfoActivity(bird.getId()));
	}

	/**
	 * Print query results in the list view.
	 */
	private final void printQueryResults() {

		setListAdapter(new SearchResultsAdapter(this,
				ornidroidService.getQueryResult()));

		MainActivity.this.searchField.dismissDropDown();
	}

}
