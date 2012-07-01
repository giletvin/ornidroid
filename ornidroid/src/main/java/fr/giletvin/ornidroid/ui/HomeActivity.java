package fr.giletvin.ornidroid.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import fr.giletvin.ornidroid.R;
import fr.giletvin.ornidroid.helper.Constants;
import fr.giletvin.ornidroid.helper.OrnidroidException;
import fr.giletvin.ornidroid.service.IOrnidroidIOService;
import fr.giletvin.ornidroid.service.OrnidroidIOServiceImpl;

/**
 * The Class HomeActivity. Start screen of the application
 */
public class HomeActivity extends AbstractOrnidroidActivity implements
		OnTouchListener {

	/** The Constant DIALOG_DOWNLOAD_CONNECTION_PROBLEM_ID. */
	private static final int DIALOG_DOWNLOAD_CONNECTION_PROBLEM_ID = 4;

	/** The Constant DIALOG_DOWNLOAD_ERROR_ID. */
	private static final int DIALOG_DOWNLOAD_ERROR_ID = 2;

	/** The Constant DIALOG_DOWNLOAD_MEDIA_DOES_NOT_EXIST_ID. */
	private static final int DIALOG_DOWNLOAD_MEDIA_DOES_NOT_EXIST_ID = 3;

	/** The Constant DIALOG_ORNIDROID_DATABASE_NOT_FOUND. */
	private static final int DIALOG_ORNIDROID_DATABASE_NOT_FOUND_ID = 1;

	/** The Constant DIALOG_ORNIDROID_HOME_NOT_FOUND_ID. */
	private static final int DIALOG_ORNIDROID_HOME_NOT_FOUND_ID = 0;

	/** The about link. */
	private TextView aboutLink;

	/** The ornidroid io service. */
	private final IOrnidroidIOService ornidroidIOService;
	/** The search link. */
	private TextView searchLink;

	/**
	 * Instantiates a new home activity.
	 */
	public HomeActivity() {
		super();
		Constants.initializeConstants(this);
		this.ornidroidIOService = new OrnidroidIOServiceImpl();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		setTitle(R.string.app_name);

		this.searchLink = (TextView) findViewById(R.id.menu_search);
		this.searchLink.setOnTouchListener(this);
		this.aboutLink = (TextView) findViewById(R.id.menu_about);
		this.aboutLink.setOnTouchListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if (v == this.searchLink) {
			return launchActivity(MainActivity.class);
		}
		if (v == this.aboutLink) {
			return launchActivity(AboutActivity.class);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DIALOG_ORNIDROID_HOME_NOT_FOUND_ID:
			builder.setMessage(
					this.getText(R.string.dialog_alert_ornidroid_home_not_found))
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			break;
		case DIALOG_ORNIDROID_DATABASE_NOT_FOUND_ID:
			builder.setMessage(
					this.getText(R.string.dialog_alert_ornidroid_database_not_found))
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			break;
		case DIALOG_DOWNLOAD_ERROR_ID:
			builder.setMessage(
					this.getText(R.string.dialog_alert_download_error))
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			break;
		case DIALOG_DOWNLOAD_MEDIA_DOES_NOT_EXIST_ID:
			builder.setMessage(
					this.getText(R.string.dialog_alert_database_not_found_online))
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			break;
		case DIALOG_DOWNLOAD_CONNECTION_PROBLEM_ID:
			builder.setMessage(
					this.getText(R.string.dialog_alert_connection_problem))
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			break;
		default:
			builder.setMessage(
					this.getText(R.string.dialog_alert_ornidroid_home_not_found))
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
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

			// check the ornidroid.sqlite file. If it doesnt exist, try to
			// download
			// it from internet
			this.ornidroidIOService.checkOrnidroidDatabase(
					Constants.getOrnidroidHome(), Constants.DB_NAME);
		} catch (OrnidroidException e) {
			switch (e.getErrorType()) {
			case DATABASE_NOT_FOUND:
				showDialog(DIALOG_ORNIDROID_DATABASE_NOT_FOUND_ID);
				break;
			case ORNIDROID_HOME_NOT_FOUND:
				showDialog(DIALOG_ORNIDROID_HOME_NOT_FOUND_ID);
				break;
			case ORNIDROID_DOWNLOAD_ERROR:
				showDialog(DIALOG_DOWNLOAD_ERROR_ID);
				break;
			case ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST:
				showDialog(DIALOG_DOWNLOAD_MEDIA_DOES_NOT_EXIST_ID);
				break;
			case ORNIDROID_CONNECTION_PROBLEM:
				showDialog(DIALOG_DOWNLOAD_CONNECTION_PROBLEM_ID);
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
	private boolean launchActivity(Class activityClass) {
		Intent intent = new Intent(getApplicationContext(), activityClass);
		startActivity(intent);
		return true;
	}
}
