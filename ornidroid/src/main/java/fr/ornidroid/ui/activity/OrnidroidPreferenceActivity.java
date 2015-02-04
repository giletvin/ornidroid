package fr.ornidroid.ui.activity;

import java.io.File;
import java.io.IOException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.sharedpreferences.Pref;

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
import de.greenrobot.event.EventBus;
import fr.ornidroid.R;
import fr.ornidroid.event.EventType;
import fr.ornidroid.event.GenericEvent;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.FileHelper;
import fr.ornidroid.ui.components.HelpDialog;
import fr.ornidroid.ui.components.OrnidroidHomeDialogPreference;
import fr.ornidroid.ui.preferences.MyPrefs_;

/**
 * The Class OrnidroidPreferenceActivity.
 */
@EActivity
@OptionsMenu(R.menu.options_menu)
public class OrnidroidPreferenceActivity extends PreferenceActivity implements
		OnPreferenceClickListener, OnSharedPreferenceChangeListener {

	/** The is changing ornidroid home. */
	@InstanceState
	boolean isChangingOrnidroidHome = false;

	/** The my prefs. */
	@Pref
	MyPrefs_ myPrefs;

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

		if (getResources().getString(R.string.preferences_ornidroid_home_key)
				.equals(key)) {

			doChangeOrnidroidHome(
					OrnidroidPreferenceActivity.this.ornidroidHomeDialogPreference
							.getOldOrnidroidHome(), Constants
							.getOrnidroidHome());
			showProgressBar();

		}

	}

	/**
	 * Do change ornidroid home.
	 * 
	 * @param oldOrnidroidHome
	 *            the old ornidroid home
	 * @param ornidroidHome
	 *            the ornidroid home
	 */
	@Background
	void doChangeOrnidroidHome(String oldOrnidroidHome, String ornidroidHome) {
		if (!isChangingOrnidroidHome) {
			isChangingOrnidroidHome = true;
			Exception caughtException = null;
			try {
				final File srcDir = new File(oldOrnidroidHome);
				final File destDir = new File(ornidroidHome);
				FileHelper.moveDirectory(srcDir, destDir);
				myPrefs.edit().ornidroidHome().put(ornidroidHome);
			} catch (final IOException e) {
				Log.e(Constants.LOG_TAG, e.toString());
				caughtException = e;
			} finally {

				// post the OrnidroidHomeChangedEvent event in the EventBus
				EventBus.getDefault().post(
						new GenericEvent(EventType.ORNIDROID_HOME_CHANGED,
								caughtException));

			}
		}
	}

	/**
	 * On event main thread. Look for OrnidroidHomeChangedEvent Show a dialog to
	 * the user
	 * 
	 * @param event
	 *            the event
	 */
	public void onEventMainThread(GenericEvent event) {
		if (EventType.ORNIDROID_HOME_CHANGED.equals(event.eventType)) {
			isChangingOrnidroidHome = false;
			if (null != progressBar && progressBar.isShowing()) {
				progressBar.hide();
			}
			if (null != event.getException()) {
				// print a dialog box to show the error.
				HelpDialog.showInfoDialog(this, this
						.getString(R.string.help_change_ornidroid_home_title),
						event.getExceptionMessage());
			} else {
				// everything is fine.
				HelpDialog
						.showInfoDialog(
								this,
								this.getString(R.string.help_change_ornidroid_home_title),
								this.getString(R.string.preferences_ornidroid_home_saved));
			}
		}
	}

	/**
	 * Show progress bar.
	 */
	void showProgressBar() {
		if (progressBar == null) {
			progressBar = new ProgressDialog(this);
			progressBar.setCancelable(false);
			progressBar.setMessage(this.getResources().getText(
					R.string.preferences_ornidroid_home_copying_files));
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}
		progressBar.show();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
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
	 * After views.
	 */
	@AfterViews
	void afterViews() {
		if (isChangingOrnidroidHome) {
			showProgressBar();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (progressBar != null) {
			progressBar.hide();
			progressBar = null;
		}
		EventBus.getDefault().unregister(this);
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
		startActivity(new Intent(this, OrnidroidPreferenceActivity_.class));
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
