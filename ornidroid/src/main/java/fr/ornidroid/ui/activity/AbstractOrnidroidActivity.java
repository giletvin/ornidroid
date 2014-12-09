package fr.ornidroid.ui.activity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import android.app.Activity;
import fr.ornidroid.R;
import fr.ornidroid.helper.Constants;

/**
 * The Class AbstractOrnidroidActivity. Extending this class enables the menu.
 */
@EActivity
@OptionsMenu(R.menu.options_menu)
public abstract class AbstractOrnidroidActivity extends Activity {

	/**
	 * Instantiates a new abstract ornidroid activity.
	 */
	public AbstractOrnidroidActivity() {
		super();
		Constants.initializeConstants(this);
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
		MultiCriteriaSearchActivity_.intent(this).start();
	}

	/**
	 * Preferences menu clicked.
	 */
	@OptionsItem(R.id.preferences)
	void preferencesMenuClicked() {
		OrnidroidPreferenceActivity_.intent(this).start();
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
