package fr.ornidroid.ui;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import fr.ornidroid.ui.components.OrnidroidHomeDialogPreference;
import fr.ornidroid.ui.multicriteriasearch.HelpDialog;

/**
 * The Class OrnidroidPreferenceActivity.
 */
public class OrnidroidPreferenceActivity extends PreferenceActivity implements
		OnPreferenceClickListener, OnSharedPreferenceChangeListener, Runnable {

	/** The Constant COPY_FINISHED. */
	private static final int COPY_FINISHED = 2;

	/** The Constant COPY_NOT_STARTED. */
	private static final int COPY_NOT_STARTED = 1;

	/** The Constant COPY_STARTED. */
	private static final int COPY_STARTED = 0;

	/** The Constant PROGRESS_BAR_MAX. */
	private static final int PROGRESS_BAR_MAX = 100;

	/** The download status. */
	private int copyStatus;

	/** The exception caught during move. */
	private Exception exceptionCaughtDuringMove = null;

	/** The file helper. */
	private final FileHelper fileHelper;

	private Handler innerHandler;

	/** The ornidroid home dialog preference. */
	private OrnidroidHomeDialogPreference ornidroidHomeDialogPreference;

	/** The progress bar. */
	private ProgressDialog progressBar;

	/** The progress bar handler. */
	private final Handler progressBarHandler = new Handler();

	/** The progress bar status. */
	private int progressBarStatus = 0;

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
			this.copyStatus = COPY_NOT_STARTED;
			// prepare for a progress bar dialog
			this.progressBar = new ProgressDialog(this);
			this.progressBar.setCancelable(true);
			this.progressBar.setMessage(this.getResources().getText(
					R.string.preferences_ornidroid_home_copying_files));
			this.progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			this.progressBar.setProgress(0);
			this.progressBar.setMax(PROGRESS_BAR_MAX);
			this.progressBar.show();

			// reset progress bar status
			this.progressBarStatus = 0;
			new Thread(this).start();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Looper.prepare();
		this.innerHandler = new Handler();

		final Message message = this.innerHandler.obtainMessage();

		this.innerHandler.dispatchMessage(message);

		Looper.loop();

		while (this.copyStatus != COPY_FINISHED) {

			// process some tasks
			this.progressBarStatus = startCopyFilesThreadAndComputeDownloadProgress();

			// your computer is too fast, sleep 1 second
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				// Log.e(Constants.LOG_TAG,
				// "Exception in the progress bar thread " + e);
			}
			// Update the progress bar
			this.progressBarHandler.post(new Runnable() {
				public void run() {
					OrnidroidPreferenceActivity.this.progressBar
							.setProgress(OrnidroidPreferenceActivity.this.progressBarStatus);
				}
			});
		}
		// TODO :
		// http://cyrilmottier.com/2011/07/18/android-et-la-programmation-concurrente-partie-2/
		// http://www.aviyehuda.com/blog/2010/12/20/android-multithreading-in-a-ui-environment/
		if (this.exceptionCaughtDuringMove != null) {
			HelpDialog.showInfoDialog(this,
					this.getString(R.string.help_change_ornidroid_home_title),
					this.exceptionCaughtDuringMove.getMessage());
		}
		// ok, the content of ornidroidhome is moved to the new directory
		if (this.copyStatus == COPY_FINISHED) {

			// sleep 2 seconds, so that you can see the 100%
			try {
				Thread.sleep(2000);
			} catch (final InterruptedException e) {
				// Log.e(Constants.LOG_TAG,
				// "Exception in the completed progress bar thread " + e);
			}
			// close the progress bar dialog
			this.progressBar.dismiss();

		}

	}

	@Override
	protected void onDestroy() {

		innerHandler.getLooper().quit();

		super.onDestroy();

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
	 * Compute download progress.
	 * 
	 * @return the int
	 */
	private int computeDownloadProgress() {
		return this.fileHelper.getCopyPercentProgress();
	}

	/**
	 * Start copy files thread and compute download progress.
	 * 
	 * @return the int
	 */
	private int startCopyFilesThreadAndComputeDownloadProgress() {
		if (this.copyStatus == COPY_NOT_STARTED) {
			// if download is not started, start it in a new thread
			this.copyStatus = COPY_STARTED;
			new Thread(new Runnable() {
				public void run() {

					OrnidroidPreferenceActivity.this.fileHelper
							.initMoveOperation();
					try {
						final File srcDir = new File(
								OrnidroidPreferenceActivity.this.ornidroidHomeDialogPreference
										.getOldOrnidroidHome());
						final File destDir = new File(Constants
								.getOrnidroidHome());
						OrnidroidPreferenceActivity.this.fileHelper
								.moveDirectory(srcDir, destDir);
					} catch (final Exception e) {
						// set exception to fire a dialog error to the user
						OrnidroidPreferenceActivity.this.exceptionCaughtDuringMove = e;
						// log it
						Log.e(Constants.LOG_TAG, e.getMessage(), e);
						// put the old ornidroid home value in the preferences
						OrnidroidPreferenceActivity.this.ornidroidHomeDialogPreference
								.saveOrnidroidHomeValue(OrnidroidPreferenceActivity.this.ornidroidHomeDialogPreference
										.getOldOrnidroidHome());

					} finally {
						OrnidroidPreferenceActivity.this.fileHelper
								.setMoveComplete();
						OrnidroidPreferenceActivity.this.copyStatus = COPY_FINISHED;
					}
				}
			}).start();
		}

		if (this.copyStatus != COPY_FINISHED) {
			return computeDownloadProgress();
		} else {
			return PROGRESS_BAR_MAX;
		}
	}
}
