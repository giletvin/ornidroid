package fr.ornidroid.ui;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.MenuHelper;

/**
 * The Class AbstractOrnidroidActivity. Extending this class enables the menu.
 */
public abstract class AbstractOrnidroidActivity extends Activity {

	/**
	 * Instantiates a new abstract ornidroid activity.
	 */
	public AbstractOrnidroidActivity() {
		super();
		Constants.initializeConstants(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		return MenuHelper.onCreateOptionsMenu(inflater, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		return MenuHelper.onOptionsItemSelected(this, item);
	}
}
