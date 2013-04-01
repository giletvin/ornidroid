package fr.ornidroid.ui;

import java.io.File;

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
import android.widget.Toast;
import fr.ornidroid.R;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.FileHelper;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.ui.components.OrnidroidHomeDialogPreference;
import fr.ornidroid.ui.multicriteriasearch.HelpDialog;

/**
 * The Class OrnidroidPreferenceActivity.
 */
public class OrnidroidPreferenceActivity extends PreferenceActivity implements
		OnPreferenceClickListener, OnSharedPreferenceChangeListener {

	/** The exception caught during move. */
	private Exception exceptionCaughtDuringMove = null;

	/** The file helper. */
	private final FileHelper fileHelper;

	/** The ornidroid home dialog preference. */
	private OrnidroidHomeDialogPreference ornidroidHomeDialogPreference;

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
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#
	 * onSharedPreferenceChanged(android.content.SharedPreferences,
	 * java.lang.String)
	 */
	public void onSharedPreferenceChanged(final SharedPreferences arg0,
			final String key) {
		// http://androiddev.orkitra.com/?p=771
		if (getResources().getString(R.string.preferences_ornidroid_home_key)
				.equals(key)) {

			try {
				setNewOrnidroidHome(
						this.ornidroidHomeDialogPreference
								.getOldOrnidroidHome(),
						Constants.getOrnidroidHome());
				Toast.makeText(this, R.string.preferences_ornidroid_home_saved,
						Toast.LENGTH_LONG).show();
			} catch (final OrnidroidException e) {
				Log.e(Constants.LOG_TAG, e.getSourceExceptionMessage());
				HelpDialog.showInfoDialog(this, this
						.getString(R.string.help_change_ornidroid_home_title),
						e.getSourceExceptionMessage());
				// Rollback : save old value
				this.ornidroidHomeDialogPreference
						.saveOrnidroidHomeValue(this.ornidroidHomeDialogPreference
								.getOldOrnidroidHome());
			}
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

	/**
	 * Fire long toast.
	 * 
	 * @param toast
	 *            the toast
	 * @throws OrnidroidException
	 */
	private void fireLongToast() throws OrnidroidException {
		try {
			while ((this.fileHelper.getCopyPercentProgress() != 100)
					&& (this.exceptionCaughtDuringMove == null)) {
				final Toast toast = Toast
						.makeText(
								this,
								this.getString(R.string.preferences_ornidroid_home_copying_files)
										+ this.fileHelper
												.getCopyPercentProgress() + "%",
								Toast.LENGTH_SHORT);
				toast.show();
				Thread.sleep(1850);
			}
		} catch (final Exception e) {
			Log.e(Constants.LOG_TAG, e.getMessage(), e);
		}
		if (this.exceptionCaughtDuringMove != null) {
			throw new OrnidroidException(OrnidroidError.CHANGE_ORNIDROID_HOME,
					this.exceptionCaughtDuringMove);
		}
	}

	/**
	 * Sets the new ornidroid home. Copy files from previous installation
	 * directory to the new location
	 * 
	 * @param newOrnidroidHome
	 *            the new new ornidroid home
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	private void setNewOrnidroidHome(final String oldOrnidroidHome,
			final String newOrnidroidHome) throws OrnidroidException {
		this.exceptionCaughtDuringMove = null;
		if (!StringHelper.equals(oldOrnidroidHome, newOrnidroidHome)) {

			final Thread t = new Thread() {
				@Override
				public void run() {
					OrnidroidPreferenceActivity.this.fileHelper
							.initMoveOperation();
					try {
						final File destDir = new File(newOrnidroidHome);
						final File srcDir = new File(oldOrnidroidHome);
						OrnidroidPreferenceActivity.this.fileHelper
								.moveDirectory(srcDir, destDir);
					} catch (final Exception e) {
						OrnidroidPreferenceActivity.this.exceptionCaughtDuringMove = e;
						Log.e(Constants.LOG_TAG, e.getMessage(), e);
					} finally {
						OrnidroidPreferenceActivity.this.fileHelper
								.setMoveComplete();
					}
				}
			};
			t.start();
			fireLongToast();
		}

	}
}
