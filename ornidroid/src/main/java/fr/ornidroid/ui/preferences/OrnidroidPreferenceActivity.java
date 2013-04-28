package fr.ornidroid.ui.preferences;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import fr.ornidroid.R;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.FileHelper;
import fr.ornidroid.ui.HelpActivity;
import fr.ornidroid.ui.HomeActivity;
import fr.ornidroid.ui.components.HelpDialog;
import fr.ornidroid.ui.components.OrnidroidHomeDialogPreference;
import fr.ornidroid.ui.preferences.MoveDirectoryHandler.MoveDirectoryCallback;

/**
 * The Class OrnidroidPreferenceActivity.
 */
public class OrnidroidPreferenceActivity extends PreferenceActivity implements
		OnPreferenceClickListener, OnSharedPreferenceChangeListener,
		MoveDirectoryCallback {

	/** The file helper. */
	private final FileHelper fileHelper;

	/** The m loader. */
	private HandlerThreadOrnidroidHomeMvDirectory mLoader;

	/** The move ended. */
	private boolean moveEnded;

	/** The ornidroid home dialog preference. */
	private OrnidroidHomeDialogPreference ornidroidHomeDialogPreference;

	/** The progress bar. */
	private ProgressDialog progressBar;

	/**
	 * Instantiates a new ornidroid preference activity.
	 */
	public OrnidroidPreferenceActivity() {
		super();
		Constants.initializeConstants(this);
		this.fileHelper = new FileHelper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.MoveDirectoryHandler.MoveDirectoryCallback#getFileHelper
	 * ()
	 */
	public FileHelper getFileHelper() {
		return this.fileHelper;
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
	 * @see
	 * fr.ornidroid.ui.MoveDirectoryHandler.MoveDirectoryCallback#onMoveEnded
	 * (fr.ornidroid.ui.MoveDirectoryHandler)
	 */
	public void onMoveEnded(final MoveDirectoryHandler loader,
			final LoaderInfo info) {
		killLoader();
		if (info.getException() != null) {
			// an error occured
			// log it
			Log.e(Constants.LOG_TAG, info.getException().getMessage(),
					info.getException());
			// put the old ornidroid home value in the preferences
			OrnidroidPreferenceActivity.this.ornidroidHomeDialogPreference
					.saveOrnidroidHomeValue(OrnidroidPreferenceActivity.this.ornidroidHomeDialogPreference
							.getOldOrnidroidHome());
			// print a dialog box to show the error.
			HelpDialog.showInfoDialog(this,
					this.getString(R.string.help_change_ornidroid_home_title),
					info.getException().getMessage());
		} else {
			// everything is fine.
			HelpDialog.showInfoDialog(this,
					this.getString(R.string.help_change_ornidroid_home_title),
					this.getString(R.string.preferences_ornidroid_home_saved));

		}

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
		this.moveEnded = false;
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
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#
	 * onSharedPreferenceChanged(android.content.SharedPreferences,
	 * java.lang.String)
	 */
	public void onSharedPreferenceChanged(final SharedPreferences arg0,
			final String key) {

		if (getResources().getString(R.string.preferences_ornidroid_home_key)
				.equals(key) && !this.moveEnded) {
			if (this.mLoader == null) {
				this.mLoader = new HandlerThreadOrnidroidHomeMvDirectory();
				this.mLoader.start();
			}

			this.mLoader
					.moveDirectory(
							OrnidroidPreferenceActivity.this.ornidroidHomeDialogPreference
									.getOldOrnidroidHome(), Constants
									.getOrnidroidHome(), this);
			this.progressBar = new ProgressDialog(this);
			this.progressBar.setCancelable(false);
			this.progressBar.setMessage(this.getResources().getText(
					R.string.preferences_ornidroid_home_copying_files));
			this.progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			this.progressBar.show();

		}

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
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		this.ornidroidHomeDialogPreference = (OrnidroidHomeDialogPreference) findPreference(this
				.getResources()
				.getText(R.string.preferences_ornidroid_home_key));
		this.ornidroidHomeDialogPreference.setOnPreferenceClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (this.mLoader != null) {
			killLoader();
		}
	}

	/**
	 * Kill loader.
	 */
	private void killLoader() {
		this.mLoader.quit();
		this.mLoader = null;
		this.progressBar.dismiss();
		this.moveEnded = true;
	}

}
