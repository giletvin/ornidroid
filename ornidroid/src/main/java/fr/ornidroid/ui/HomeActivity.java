package fr.ornidroid.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.service.IOrnidroidIOService;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidIOServiceImpl;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.multicriteriasearch.MultiCriteriaSearchActivity;

/**
 * The Class HomeActivity. Start screen of the application
 */
public class HomeActivity extends AbstractOrnidroidActivity implements
		OnTouchListener {

	/** The Constant DIALOG_ORNIDROID_DATABASE_NOT_FOUND. */
	private static final int DIALOG_ORNIDROID_DATABASE_NOT_FOUND_ID = 1;

	/** The Constant DIALOG_ORNIDROID_HOME_NOT_FOUND_ID. */
	private static final int DIALOG_ORNIDROID_HOME_NOT_FOUND_ID = 0;

	/** The about link. */
	private TextView aboutLink;
	private TextView helpLink;

	/** The multi criteria search link. */
	private TextView multiCriteriaSearchLink;

	/** The ornidroid io service. */
	private final IOrnidroidIOService ornidroidIOService;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The search link. */
	private TextView searchLink;

	/**
	 * Instantiates a new home activity.
	 */
	public HomeActivity() {
		super();
		this.ornidroidIOService = new OrnidroidIOServiceImpl();
		this.ornidroidService = OrnidroidServiceFactory.getService(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		setTitle(R.string.app_name);

		this.searchLink = (TextView) findViewById(R.id.menu_search);
		this.searchLink.setOnTouchListener(this);
		this.aboutLink = (TextView) findViewById(R.id.menu_about);
		this.aboutLink.setOnTouchListener(this);
		this.helpLink = (TextView) findViewById(R.id.menu_help);
		this.helpLink.setOnTouchListener(this);
		this.multiCriteriaSearchLink = (TextView) findViewById(R.id.menu_search_multi);
		this.multiCriteriaSearchLink.setOnTouchListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	public boolean onTouch(final View v, final MotionEvent event) {
		if (v == this.searchLink) {
			return launchActivity(MainActivity.class);
		}
		if (v == this.aboutLink) {
			return launchActivity(AboutActivity.class);
		}
		if (v == this.helpLink) {
			return launchActivity(HelpActivity.class);
		}
		if (v == this.multiCriteriaSearchLink) {
			return launchActivity(MultiCriteriaSearchActivity.class);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(final int id) {
		Dialog dialog;
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DIALOG_ORNIDROID_HOME_NOT_FOUND_ID:
			builder.setMessage(
					this.getText(R.string.dialog_alert_ornidroid_home_not_found))
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									dialog.cancel();
								}
							});

			break;
		case DIALOG_ORNIDROID_DATABASE_NOT_FOUND_ID:
			builder.setMessage(
					this.getText(R.string.dialog_alert_ornidroid_database_not_found))
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									dialog.cancel();
								}
							});

			break;

		default:
			builder.setMessage(
					this.getText(R.string.dialog_alert_ornidroid_home_not_found))
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									dialog.cancel();
								}
							});
		}
		dialog = builder.create();
		return dialog;
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
			switch (e.getErrorType()) {
			case DATABASE_NOT_FOUND:
				showDialog(DIALOG_ORNIDROID_DATABASE_NOT_FOUND_ID);
				break;
			case ORNIDROID_HOME_NOT_FOUND:
				showDialog(DIALOG_ORNIDROID_HOME_NOT_FOUND_ID);
				break;
			default:
				showDialog(DIALOG_ORNIDROID_HOME_NOT_FOUND_ID);
				break;
			}

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
		startActivity(intent);
		return true;
	}
}
