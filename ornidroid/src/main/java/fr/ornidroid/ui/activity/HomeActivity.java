package fr.ornidroid.ui.activity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.service.IOrnidroidIOService;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidIOServiceImpl;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.AboutActivity;
import fr.ornidroid.ui.AbstractOrnidroidActivity;
import fr.ornidroid.ui.HelpActivity;
import fr.ornidroid.ui.MainActivity;
import fr.ornidroid.ui.multicriteriasearch.MultiCriteriaSearchActivity;
import fr.ornidroid.ui.preferences.OrnidroidPreferenceActivity;

/**
 * The Class HomeActivity. Start screen of the application
 */
@EActivity(R.layout.home)
public class HomeActivity extends AbstractOrnidroidActivity {

	/** The about link. */
	@ViewById(R.id.menu_about)
	TextView aboutLink;

	/** The help link. */
	@ViewById(R.id.menu_help)
	TextView helpLink;

	/** The multi criteria search link. */
	@ViewById(R.id.menu_search_multi)
	TextView multiCriteriaSearchLink;

	/** The ornidroid io service. */
	private final IOrnidroidIOService ornidroidIOService;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The preferences link. */
	@ViewById(R.id.menu_preferences)
	TextView preferencesLink;

	/** The search link. */
	@ViewById(R.id.menu_search)
	TextView searchLink;

	/**
	 * Instantiates a new home activity.
	 */
	public HomeActivity() {
		super();
		this.ornidroidIOService = new OrnidroidIOServiceImpl();
		this.ornidroidService = OrnidroidServiceFactory.getService(this);
	}

	/**
	 * Click search link.
	 */
	@Click(R.id.menu_search)
	void clickSearchLink() {
		launchActivity(MainActivity.class);

	}

	/**
	 * Click search multi link.
	 */
	@Click(R.id.menu_search_multi)
	void clickSearchMultiLink() {
		launchActivity(MultiCriteriaSearchActivity.class);
	}

	/**
	 * Click about link.
	 */
	@Click(R.id.menu_about)
	void clickAboutLink() {
		launchActivity(AboutActivity.class);
	}

	/**
	 * Click help link.
	 */
	@Click(R.id.menu_help)
	void clickHelpLink() {
		launchActivity(HelpActivity.class);
	}

	/**
	 * Click preferences link.
	 */
	@Click(R.id.menu_preferences)
	void clickPreferencesLink() {
		launchActivity(OrnidroidPreferenceActivity.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		checkOrnidroidHomeDirectory();
		String releaseNotes = ornidroidService.getReleaseNotes();
		if (StringHelper.isNotBlank(releaseNotes)) {
			Dialog dialog = new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.release_notes)
					.setMessage(releaseNotes)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int whichButton) {
									dialog.dismiss();
								}
							}).create();
			dialog.show();
		}
	}

	/**
	 * Check ornidroid home directory.
	 */
	private void checkOrnidroidHomeDirectory() {
		try {
			this.ornidroidIOService.checkOrnidroidHome(Constants
					.getOrnidroidHome());

			// check the ornidroid.sqlite file.
			this.ornidroidService.createDbIfNecessary();

		} catch (final OrnidroidException e) {
			StringBuffer sbuf = new StringBuffer();
			if (e.getSourceExceptionMessage() != null) {
				sbuf.append(BasicConstants.CARRIAGE_RETURN);
				sbuf.append(e.getSourceExceptionMessage());
			}
			Dialog dialog;
			switch (e.getErrorType()) {
			case DATABASE_NOT_FOUND:
				sbuf.insert(
						0,
						this.getText(R.string.dialog_alert_ornidroid_database_not_found));
				dialog = new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.warning)
						.setMessage(sbuf.toString())
						.setNegativeButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog,
											final int id) {
										dialog.cancel();
									}
								}).create();
				break;
			case ORNIDROID_HOME_NOT_FOUND:
				sbuf.insert(
						0,
						this.getText(R.string.dialog_alert_ornidroid_home_not_found));
				dialog = new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.warning)
						.setMessage(sbuf.toString())
						.setNegativeButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog,
											final int id) {
										dialog.cancel();
									}
								}).create();
				break;
			default:
				sbuf.insert(
						0,
						this.getText(R.string.dialog_alert_ornidroid_home_not_found));
				dialog = new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.warning)
						.setMessage(sbuf.toString())
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog,
											final int whichButton) {
										dialog.dismiss();
									}
								}).create();

				break;
			}
			dialog.show();

		}
	}

	/**
	 * Launch activity.
	 * 
	 * @param activityClass
	 *            the activity class
	 * @return true, if successful
	 */
	@SuppressWarnings("rawtypes")
	private boolean launchActivity(final Class activityClass) {
		final Intent intent = new Intent(getApplicationContext(), activityClass);
		startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		return true;
	}
}
