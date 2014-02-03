package fr.ornidroid.ui;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import fr.ornidroid.R;
import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.service.IOrnidroidIOService;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidIOServiceImpl;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.downloads.CheckForUpdateFilesLoaderInfo;
import fr.ornidroid.ui.downloads.GenericTaskHandler;
import fr.ornidroid.ui.downloads.GenericTaskHandler.GenericTaskCallback;
import fr.ornidroid.ui.downloads.HandlerForCheckUpdateFilesThread;
import fr.ornidroid.ui.downloads.HandlerGenericThread;
import fr.ornidroid.ui.downloads.LoaderInfo;

/**
 * The Class AbstractDownloadableMediaActivity.
 */
public abstract class AbstractDownloadableMediaActivity extends
		AbstractOrnidroidActivity implements Runnable, OnClickListener,
		GenericTaskCallback {
	/** The Constant DOWNLOAD_ERROR_INTENT_PARAM. */
	public static final String DOWNLOAD_ERROR_INTENT_PARAM = "DOWNLOAD_ERROR_INTENT_PARAM";

	/** The Constant DIALOG_PICTURE_INFO_ID. */
	protected static final int DIALOG_PICTURE_INFO_ID = 0;

	/** The Constant DIALOG_UPDATES_AVAILABLE. */
	public static final int DIALOG_UPDATES_AVAILABLE = 1;

	/** The Constant DOWNLOAD_FINISHED. */
	protected static final int DOWNLOAD_FINISHED = 2;

	/** The Constant DOWNLOAD_STARTED. */
	protected static final int DOWNLOAD_STARTED = 0;

	/** The Constant PROBLEM_DIRECTORY_ID. */
	protected static final int PROBLEM_DIRECTORY_ID = 2;

	/** The Constant PROBLEM_DOWNLOAD_ID. */
	protected static final int PROBLEM_DOWNLOAD_ID = 1;

	/** The Constant DOWNLOAD_BUTTON_ID. */
	private static final int DOWNLOAD_BUTTON_ID = 1109;

	/** The Constant DOWNLOAD_NOT_STARTED. */
	private static final int DOWNLOAD_NOT_STARTED = 1;

	/** The add custom audio button. */
	private ImageView addCustomAudioButton;

	/** The add custom picture button. */
	private ImageView addCustomPictureButton;
	/** The bird. */
	private Bird bird;

	/**
	 * Gets the current (selected) media file if picture : the displayed image,
	 * if sound, the played mp3.
	 */
	private AbstractOrnidroidFile currentMediaFile;

	/** The dialog. */
	private Dialog dialog;

	/** The download from internet button. */
	private ImageView downloadFromInternetButton;
	/** The download info text. */
	private TextView downloadInfoText;

	/** The download status. */
	private int downloadStatus;

	/** The m loader. */
	private HandlerGenericThread mLoader;

	/** The info button. */
	private ImageView infoButton;

	/** The ok dialog button. */
	private Button okDialogButton;

	/** The ornidroid download error. */
	private int ornidroidDownloadErrorCode;
	/** The ornidroid io service. */
	private final IOrnidroidIOService ornidroidIOService;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The progress bar. */
	private ProgressDialog progressBar;

	/** The remove custom picture button. */
	private ImageView removeCustomAudioButton;

	/** The remove custom picture button. */
	private ImageView removeCustomPictureButton;

	/** The update files button. */
	private ImageView updateFilesButton;

	/** The uri. */
	private Uri uri;

	/**
	 * Instantiates a new abstract downloadable media activity.
	 */
	public AbstractDownloadableMediaActivity() {
		super();
		this.ornidroidService = OrnidroidServiceFactory.getService(this);
		this.ornidroidIOService = new OrnidroidIOServiceImpl();
	}

	/**
	 * Gets the ornidroid io service.
	 * 
	 * @return the ornidroid io service
	 */
	public IOrnidroidIOService getOrnidroidIOService() {
		return ornidroidIOService;
	}

	/**
	 * Check for updates. Fires a dialog box if updates are found. If manual
	 * check, let the user know that no updates are found.
	 * 
	 * @param manualCheck
	 *            true if the user checks manually.
	 * 
	 */
	public void checkForUpdates(final boolean manualCheck) {
		if (this.mLoader == null) {
			this.mLoader = new HandlerForCheckUpdateFilesThread(this,
					manualCheck);
			this.mLoader.start();
		}
		this.mLoader.genericTask(this);
	}

	/**
	 * Gets the custom audio button.
	 * 
	 * @return the adds the custom audio button
	 */
	public ImageView getAddCustomAudioButton() {

		return this.addCustomAudioButton;
	}

	/**
	 * Gets the add the custom picture button.
	 * 
	 * @return the adds the custom picture button
	 */
	public ImageView getAddCustomPictureButton() {

		return this.addCustomPictureButton;
	}

	/**
	 * Gets the bird.
	 * 
	 * @return the bird
	 */
	public Bird getBird() {
		return this.bird;
	}

	/**
	 * Gets the dialog.
	 * 
	 * @return the dialog
	 */
	public Dialog getDialog() {
		return this.dialog;
	}

	/**
	 * Gets the download status.
	 * 
	 * @return the download status
	 */
	public int getDownloadStatus() {
		return this.downloadStatus;
	}

	/**
	 * Gets the file type.
	 * 
	 * @return the file type
	 */
	public abstract OrnidroidFileType getFileType();

	/**
	 * Gets the info button.
	 * 
	 * @return the info button
	 */
	public ImageView getInfoButton() {
		return this.infoButton;
	}

	/**
	 * Gets the ok dialog button.
	 * 
	 * @return the ok dialog button
	 */
	public Button getOkDialogButton() {
		return this.okDialogButton;
	}

	/**
	 * Gets the removes the custom audio button.
	 * 
	 * @return the removes the custom audio button
	 */
	public ImageView getRemoveCustomAudioButton() {

		return this.removeCustomAudioButton;
	}

	/**
	 * Gets the removes the custom picture button.
	 * 
	 * @return the removes the custom picture button
	 */
	public ImageView getRemoveCustomPictureButton() {

		return this.removeCustomPictureButton;
	}

	/**
	 * Gets the update files button.
	 * 
	 * @return the update files button
	 */
	public ImageView getUpdateFilesButton() {
		this.updateFilesButton = new ImageView(this);
		this.updateFilesButton.setOnClickListener(this);
		this.updateFilesButton
				.setImageResource(R.drawable.ic_check_for_updates);
		return this.updateFilesButton;
	}

	/**
	 * Load media files locally.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	public void loadMediaFilesLocally() throws OrnidroidException {

		final File fileDirectory = new File(
				Constants.getOrnidroidBirdHomeMedia(
						this.ornidroidService.getCurrentBird(), getFileType()));
		this.ornidroidIOService.checkAndCreateDirectory(fileDirectory);

		this.ornidroidIOService.loadMediaFiles(getMediaHomeDirectory(),
				this.ornidroidService.getCurrentBird(), getFileType());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(final View v) {
		if (v == this.infoButton) {
			this.showDialog(DIALOG_PICTURE_INFO_ID);
		}
		if (v == this.okDialogButton) {
			this.dialog.dismiss();
		}
		if (v.getId() == DOWNLOAD_BUTTON_ID) {
			startDownload();
		}
		if ((v == this.addCustomPictureButton)
				|| (v == this.addCustomAudioButton)) {
			final Intent intent = new Intent(getApplicationContext(),
					AddCustomMediaActivity.class);
			intent.putExtra(OrnidroidFileType.FILE_TYPE_INTENT_PARAM_NAME,
					getFileType());
			intent.putExtra(Constants.BIRD_DIRECTORY_PARAMETER_NAME, getBird()
					.getBirdDirectoryName());
			startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
		if ((v == this.removeCustomPictureButton)
				|| (v == this.removeCustomAudioButton)) {
			try {
				this.ornidroidIOService
						.removeCustomMediaFile(this.currentMediaFile);
				Toast.makeText(
						this,
						this.getResources().getString(
								R.string.remove_custom_media_success),
						Toast.LENGTH_LONG).show();
				final Intent intent = new Intent(this, BirdActivity.class);
				// put the uri so that the BirdInfoActivity reloads correctly
				// the bird
				intent.setData(getIntent().getData());
				// put an extra info to let the BirdInfoActivity know which tab
				// to open
				intent.putExtra(BirdActivity.INTENT_TAB_TO_OPEN,
						OrnidroidFileType.getCode(getFileType()));
				startActivity(intent);
				finish();
			} catch (final OrnidroidException e) {
				Toast.makeText(
						this,
						this.getResources().getString(
								R.string.add_custom_media_error)
								+ e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
		if (v == this.updateFilesButton) {
			checkForUpdates(true);
		}

	}

	/**
	 * Prints the download button and info.
	 */
	public void printDownloadButtonAndInfo() {
		getSpecificContentLayout().setOrientation(LinearLayout.VERTICAL);
		getSpecificContentLayout().setGravity(Gravity.CENTER_HORIZONTAL);

		final TextView noMediaMessage = new TextView(this);
		noMediaMessage.setPadding(5, 10, 5, 20);

		getSpecificContentLayout().addView(noMediaMessage);

		final OrnidroidError ornidroidError = OrnidroidError
				.getOrnidroidError(this.ornidroidDownloadErrorCode);

		switch (ornidroidError) {

		case ORNIDROID_CONNECTION_PROBLEM:
			noMediaMessage.setText(R.string.dialog_alert_connection_problem);
			break;
		case ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST:
			noMediaMessage.setText(R.string.no_resources_online);
			break;
		case NO_ERROR:
			switch (getFileType()) {
			case AUDIO:
				noMediaMessage.setText(R.string.no_records);
				break;
			case PICTURE:
				noMediaMessage.setText(R.string.no_pictures);
				break;

			}

			this.downloadFromInternetButton = new ImageView(this);
			this.downloadFromInternetButton.setId(DOWNLOAD_BUTTON_ID);
			this.downloadFromInternetButton.setOnClickListener(this);
			this.downloadFromInternetButton
					.setImageResource(R.drawable.ic_file_download);
			this.downloadFromInternetButton.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			getSpecificContentLayout().addView(this.downloadFromInternetButton);
			break;
		default:
			noMediaMessage.setText(R.string.unknown_error);
			break;
		}

	}

	/*
	 * This thread handles the progress bar, and launches in an other thread the
	 * download of the pictures. When the download is completed, show the 100 %
	 * progress bar and forces a reopen of the activity.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		while (this.downloadStatus != DOWNLOAD_FINISHED) {

			startDownloadThreadAndComputeDownloadProgress();

			// your computer is too fast, sleep 1 second
			try {

				Thread.sleep(1000);

			} catch (final InterruptedException e) {
				// Log.e(Constants.LOG_TAG,
				// "Exception in the progress bar thread " + e);

			}

		}

		// ok, file is downloaded,
		if (this.downloadStatus == DOWNLOAD_FINISHED) {

			// sleep 2 seconds, so that you can see the 100%
			try {
				Thread.sleep(2000);
			} catch (final InterruptedException e) {
				// Log.e(Constants.LOG_TAG,
				// "Exception in the completed progress bar thread " + e);

			}

			// close the progress bar dialog
			this.progressBar.dismiss();

			// now that the files have been downloaded, force a reopen of the
			// same screen with an intent
			final Intent intent = new Intent(this, BirdActivity.class);
			// put the uri so that the BirdInfoActivity reloads correctly the
			// bird
			intent.setData(getIntent().getData());
			// put an extra info to let the BirdInfoActivity know which tab to
			// open.
			intent.putExtra(BirdActivity.INTENT_TAB_TO_OPEN,
					OrnidroidFileType.getCode(getFileType()));
			intent.putExtra(
					AbstractDownloadableMediaActivity.DOWNLOAD_ERROR_INTENT_PARAM,
					this.ornidroidDownloadErrorCode);
			startActivity(intent);
			finish();
		}

	}

	/**
	 * Sets the currently selected media file and makes the remove media button
	 * appear or disappear accordingly.
	 * 
	 * @param selectedFile
	 *            the new selected file
	 */
	public void setCurrentMediaFile(final AbstractOrnidroidFile selectedFile) {
		this.currentMediaFile = selectedFile;
		if (this.currentMediaFile != null) {
			ImageView removeButton = null;
			switch (selectedFile.getType()) {
			case AUDIO:
				removeButton = this.removeCustomAudioButton;
				break;
			case PICTURE:
				removeButton = this.removeCustomPictureButton;
				break;
			}
			if (this.currentMediaFile.isCustomMediaFile()) {
				removeButton.setVisibility(View.VISIBLE);
			} else {
				removeButton.setVisibility(View.GONE);
			}
		} else {
			this.removeCustomAudioButton.setVisibility(View.GONE);
			this.removeCustomPictureButton.setVisibility(View.GONE);
		}
	}

	/**
	 * Sets the dialog.
	 * 
	 * @param dialog
	 *            the new dialog
	 */
	public void setDialog(final Dialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * Sets the download status.
	 * 
	 * @param downloadStatus
	 *            the new download status
	 */
	public void setDownloadStatus(final int downloadStatus) {
		this.downloadStatus = downloadStatus;
	}

	/**
	 * Sets the info button.
	 * 
	 * @param infoButton
	 *            the new info button
	 */
	public void setInfoButton(final ImageView infoButton) {
		this.infoButton = infoButton;
	}

	/**
	 * Sets the ok dialog button.
	 * 
	 * @param okDialogButton
	 *            the new ok dialog button
	 */
	public void setOkDialogButton(final Button okDialogButton) {
		this.okDialogButton = okDialogButton;
	}

	/**
	 * 
	 * Gets the specific content layout.
	 * 
	 * @return the specific content layout
	 */
	protected abstract LinearLayout getSpecificContentLayout();

	/**
	 * Hook on create. Here is the specific onCreate stuff for the subclasses.
	 */
	protected abstract void hookOnCreate();

	/**
	 * Hook on create dialog.
	 * 
	 * @param id
	 *            the id
	 */
	protected abstract void hookOnCreateDialog(final int id);

	/**
	 * Hook on prepare dialog.
	 * 
	 * @param id
	 *            the id
	 * @param dialog
	 *            the dialog
	 */
	protected abstract void hookOnPrepareDialog(final int id,
			final Dialog dialog);

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadBirdDetails();
		this.bird = this.ornidroidService.getCurrentBird();

		this.ornidroidDownloadErrorCode = getIntent().getIntExtra(
				DOWNLOAD_ERROR_INTENT_PARAM, 0);

		this.addCustomPictureButton = new ImageView(this);
		this.addCustomPictureButton.setOnClickListener(this);
		this.addCustomPictureButton.setImageResource(R.drawable.ic_add);
		this.addCustomPictureButton.setPadding(20, 0, 20, 0);

		this.removeCustomPictureButton = new ImageView(this);
		this.removeCustomPictureButton.setOnClickListener(this);
		this.removeCustomPictureButton.setImageResource(R.drawable.ic_remove);
		this.removeCustomPictureButton.setPadding(20, 0, 20, 0);

		this.addCustomAudioButton = new ImageView(this);
		this.addCustomAudioButton.setOnClickListener(this);
		this.addCustomAudioButton.setImageResource(R.drawable.ic_add);
		this.addCustomAudioButton.setPadding(20, 0, 20, 0);

		this.removeCustomAudioButton = new ImageView(this);
		this.removeCustomAudioButton.setOnClickListener(this);
		this.removeCustomAudioButton.setImageResource(R.drawable.ic_remove);
		this.removeCustomAudioButton.setPadding(20, 0, 20, 0);

		hookOnCreate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(final int id) {
		hookOnCreateDialog(id);
		if (this.dialog == null) {
			switch (id) {
			case DIALOG_UPDATES_AVAILABLE:
				this.dialog = new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.updates_available)
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog,
											final int whichButton) {
									}
								})
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog,
											final int whichButton) {
										startDownload();
									}
								}).create();
				break;
			default:
				this.dialog = null;
			}
		}
		return this.dialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
	@Override
	protected void onPrepareDialog(final int id, final Dialog dialog) {
		hookOnPrepareDialog(id, dialog);
	}

	/**
	 * Sets the ornidroid download error code.
	 * 
	 * @param ornidroidDownloadErrorCode
	 *            the new ornidroid download error code
	 */
	protected void setOrnidroidDownloadErrorCode(
			final int ornidroidDownloadErrorCode) {
		this.ornidroidDownloadErrorCode = ornidroidDownloadErrorCode;
	}

	/**
	 * Start download.
	 */
	protected void startDownload() {
		getSpecificContentLayout().removeAllViews();
		this.downloadInfoText = new TextView(this);
		this.downloadInfoText.setText(R.string.download_please_wait);
		this.downloadInfoText.setPadding(5, 10, 5, 20);
		getSpecificContentLayout().addView(this.downloadInfoText);
		this.downloadStatus = DOWNLOAD_NOT_STARTED;
		// prepare for a progress bar dialog
		this.progressBar = new ProgressDialog(this);
		this.progressBar.setCancelable(true);
		this.progressBar.setMessage(this.getResources().getText(
				R.string.download_in_progress));
		this.progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.progressBar.show();

		// reset download status
		this.downloadStatus = DOWNLOAD_NOT_STARTED;

		new Thread(this).start();
	}

	/**
	 * Gets the media home directory.
	 * 
	 * @return the media home directory
	 */
	public String getMediaHomeDirectory() {
		String mediaHomeDirectory = null;
		switch (getFileType()) {
		case AUDIO:
			mediaHomeDirectory = Constants.getOrnidroidHomeAudio();
			break;
		case PICTURE:
			mediaHomeDirectory = Constants.getOrnidroidHomeImages();
			break;
		}
		return mediaHomeDirectory;
	}

	/**
	 * Load bird details, from uri contained in the intent.
	 */
	private void loadBirdDetails() {
		this.uri = getIntent().getData();
		if (null != this.uri) {
			this.ornidroidService.loadBirdDetails(this.uri);
		}
	}

	/**
	 * Start download thread and compute download progress.
	 * 
	 * @return this method is periodically called by the progress bar thread.
	 *         When the download is finished, the return is true.
	 */
	private void startDownloadThreadAndComputeDownloadProgress() {
		if (this.downloadStatus == DOWNLOAD_NOT_STARTED) {
			// if download is not started, start it in a new thread
			this.downloadStatus = DOWNLOAD_STARTED;
			new Thread(new Runnable() {
				public void run() {

					try {
						AbstractDownloadableMediaActivity.this.ornidroidIOService
								.downloadMediaFiles(
										getMediaHomeDirectory(),
										AbstractDownloadableMediaActivity.this.bird,
										getFileType());

					} catch (final OrnidroidException e) {
						// Log.e(Constants.LOG_TAG,
						// "Download pb " + e.getErrorType() + " "
						// + e.getSourceException());
						// keep it in the field, to get it back when the
						// activity is reloaded.
						AbstractDownloadableMediaActivity.this.ornidroidDownloadErrorCode = OrnidroidError
								.getErrorCode(e.getErrorType());

					} finally {
						AbstractDownloadableMediaActivity.this.downloadStatus = DOWNLOAD_FINISHED;
					}

				}
			}).start();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.downloads.GenericTaskHandler.GenericTaskCallback#onTaskEnded
	 * (fr.ornidroid.ui.downloads.GenericTaskHandler,
	 * fr.ornidroid.ui.downloads.LoaderInfo)
	 */
	public void onTaskEnded(GenericTaskHandler loader, LoaderInfo loaderInfo) {
		if (loaderInfo.getException() != null) {
			Toast.makeText(this, R.string.updates_check_error,
					Toast.LENGTH_LONG).show();
		} else {
			CheckForUpdateFilesLoaderInfo info = (CheckForUpdateFilesLoaderInfo) loaderInfo;
			if (info.isUpdatesAvailable()) {
				this.showDialog(DIALOG_UPDATES_AVAILABLE);
			} else {
				if (info.isManualCheck()) {
					Toast.makeText(this, R.string.updates_none,
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (this.mLoader != null) {
			this.mLoader.quit();
			this.mLoader = null;
		}
	}

}