package fr.ornidroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import fr.ornidroid.R;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.ui.components.OrnidroidHomeDialogPreference;
import fr.ornidroid.ui.multicriteriasearch.HelpDialog;

/**
 * The Class OrnidroidPreferenceActivity.
 */
public class OrnidroidPreferenceActivity extends PreferenceActivity implements
		OnPreferenceClickListener {

	/** The ornidroid home dialog preference. */
	private OrnidroidHomeDialogPreference ornidroidHomeDialogPreference;

	/**
	 * Instantiates a new ornidroid preference activity.
	 */
	public OrnidroidPreferenceActivity() {
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
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.search:
			onSearchRequested();
			return true;
		case R.id.preferences:
			startActivity(new Intent(this, OrnidroidPreferenceActivity.class));
			return (true);
		case R.id.help:
			startActivity(new Intent(this, HelpActivity.class));
			return (true);
		case R.id.home:
			startActivity(new Intent(this, HomeActivity.class));
			return (true);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * On preference click.
	 * 
	 * @param arg0
	 *            the arg0
	 * @return true, if successful
	 */
	public boolean onPreferenceClick(final Preference arg0) {
		HelpDialog.showInfoDialog(
				this,
				this.getResources().getString(
						R.string.help_change_ornidroid_home_title),
				this.getResources().getString(
						R.string.help_change_ornidroid_home_content));
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.ornidroid_preferences);
		this.ornidroidHomeDialogPreference = (OrnidroidHomeDialogPreference) findPreference(this
				.getResources()
				.getText(R.string.preferences_ornidroid_home_key));
		this.ornidroidHomeDialogPreference.setOnPreferenceClickListener(this);

		// TODO quand l'utilisateur fait un mv depuis des fs différents, c'est
		// très long et aucun retour n'est fait à l'utilisateur
		// voir comment fonctionne setOnPreferenceChangeListener. Implémenter
		// une progress bar ici ?
	}
}
