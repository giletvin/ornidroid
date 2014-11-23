package fr.ornidroid.ui.activity;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import fr.ornidroid.R;
import fr.ornidroid.bo.SimpleBird;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.NewBirdActivity;
import fr.ornidroid.ui.adapter.SearchResultsAdapter;
import fr.ornidroid.ui.components.OrnidroidAutoCompleteAdapter;
import fr.ornidroid.ui.preferences.OrnidroidPreferenceActivity;

/**
 * The main activity for the dictionary. Displays search results triggered by
 * the search dialog and handles actions from search suggestions.
 */
@EActivity(R.layout.main)
@OptionsMenu(R.menu.options_menu)
public class MainActivity extends ListActivity {

	/**
	 * The Constant SHOW_SEARCH_FIELD_INTENT_PRM to hide or show the search
	 * field at the top of this screen.
	 */
	public static final String SHOW_SEARCH_FIELD_INTENT_PRM = "show_search_field";

	/** The Constant BIRD_ID_ITENT_PRM. */
	public static final String BIRD_ID_ITENT_PRM = "bird_id_intent_prm";

	/** The clicked position in the list. */
	private int clickedPositionInTheList = 0;

	/** The m list view. */
	@ViewById(android.R.id.list)
	ListView mListView;

	/** The show text field. */
	@Extra(SHOW_SEARCH_FIELD_INTENT_PRM)
	boolean showTextField = true;

	/** The search field. */
	@ViewById(R.id.home_search_field)
	AutoCompleteTextView searchField;

	/** The adapter autocomplete text view. */
	private ArrayAdapter<SimpleBird> adapterAutocompleteTextView;

	/**
	 * After views.
	 */
	@AfterViews
	void afterViews() {
		if (OrnidroidServiceFactory.getService(this).hasHistory()) {
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
	 */
	@AfterViews
	void initAutoCompleteField() {
		if (!showTextField) {
			this.searchField.setVisibility(View.GONE);
		}
		// set our adapter
		adapterAutocompleteTextView = new ArrayAdapter<SimpleBird>(this,
				android.R.layout.simple_dropdown_item_1line,
				OrnidroidServiceFactory.getService(this).getQueryResult());
		searchField.setAdapter(adapterAutocompleteTextView);
	}

	/**
	 * On text changes on search field.
	 * 
	 * @param userInput
	 *            the user input
	 */
	@TextChange(R.id.home_search_field)
	void onTextChangesOnSearchField(CharSequence userInput) {
		// query the database based on the user input
		List<SimpleBird> queryResult = OrnidroidServiceFactory.getService(this)
				.getMatchingBirds(userInput.toString());

		// update the adapater
		adapterAutocompleteTextView.notifyDataSetChanged();
		adapterAutocompleteTextView = new OrnidroidAutoCompleteAdapter(
				MainActivity.this, android.R.layout.simple_dropdown_item_1line,
				queryResult);

		searchField.setAdapter(adapterAutocompleteTextView);
	}

	/**
	 * Search field editor action. action when the user tapes Enter
	 */
	@EditorAction(R.id.home_search_field)
	void searchFieldEditorAction() {
		printQueryResults();
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
	 * Autocomplete suggestion clicked.
	 */
	@ItemClick(R.id.home_search_field)
	void autocompleteSuggestionClicked() {
		if (OrnidroidServiceFactory.getService(this).getQueryResult().size() == 1) {
			SimpleBird clickedBird = OrnidroidServiceFactory.getService(this)
					.getQueryResult().get(0);
			printQueryResults();
			startActivity(buildIntentBirdInfoActivity(clickedBird.getId()));
		}
	}

	/**
	 * Print query results in the list view.
	 */
	private final void printQueryResults() {
		setListAdapter(new SearchResultsAdapter(this, OrnidroidServiceFactory
				.getService(this).getQueryResult()));
		searchField.dismissDropDown();
	}

	/**
	 * Search menu clicked.
	 */
	@OptionsItem(R.id.search)
	void searchMenuClicked() {
		MainActivity_.intent(this).start();
	}

	/**
	 * Search multi menu clicked.
	 */
	@OptionsItem(R.id.search_multi)
	void searchMultiMenuClicked() {
		startActivity(new Intent(this, MultiCriteriaSearchActivity_.class));
	}

	/**
	 * Preferences menu clicked.
	 */
	@OptionsItem(R.id.preferences)
	void preferencesMenuClicked() {
		startActivity(new Intent(this, OrnidroidPreferenceActivity.class));
	}

	/**
	 * Home menu clicked.
	 */
	@OptionsItem(R.id.home)
	void homeMenuClicked() {
		HomeActivity_.intent(this).start();
	}

	/**
	 * Help menu clicked.
	 */
	@OptionsItem(R.id.help)
	void helpMenuClicked() {
		HelpActivity_.intent(this).start();
	}
}
