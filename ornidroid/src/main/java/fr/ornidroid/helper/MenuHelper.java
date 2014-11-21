package fr.ornidroid.helper;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import fr.ornidroid.R;
import fr.ornidroid.ui.HelpActivity;
import fr.ornidroid.ui.MainActivity;
import fr.ornidroid.ui.activity.HomeActivity_;
import fr.ornidroid.ui.multicriteriasearch.MultiCriteriaSearchActivity;
import fr.ornidroid.ui.preferences.OrnidroidPreferenceActivity;

/**
 * The Class MenuHelper.
 */
public class MenuHelper {

	/**
	 * On create options menu.
	 * 
	 * @param inflater
	 *            the inflater
	 * @param menu
	 *            the menu
	 * @return true, if successful
	 */
	public static boolean onCreateOptionsMenu(final MenuInflater inflater,
			final Menu menu) {
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	/**
	 * On options item selected.
	 * 
	 * @param activity
	 *            the activity
	 * @param item
	 *            the item
	 * @return true, if successful
	 */
	public static boolean onOptionsItemSelected(Activity activity,
			final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.search:
			activity.startActivity(new Intent(activity, MainActivity.class));
			return true;
		case R.id.search_multi:
			activity.startActivity(new Intent(activity,
					MultiCriteriaSearchActivity.class));
			return (true);
		case R.id.preferences:
			activity.startActivity(new Intent(activity,
					OrnidroidPreferenceActivity.class));
			return (true);
		case R.id.help:
			activity.startActivity(new Intent(activity, HelpActivity.class));
			return (true);
		case R.id.home:
			activity.startActivity(new Intent(activity, HomeActivity_.class));
			return (true);
		default:
			return true;
		}
	}
}
