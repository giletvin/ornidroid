package fr.ornidroid.ui.components;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.service.IOrnidroidIOService;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidIOServiceImpl;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.AddCustomMediaActivity;
import fr.ornidroid.ui.NewBirdActivity;
import fr.ornidroid.ui.components.progressbar.DoubleProgressBarDialog;
import fr.ornidroid.ui.downloads.CheckForUpdateFilesLoaderInfo;
import fr.ornidroid.ui.downloads.HandlerForCheckUpdateFilesThread;
import fr.ornidroid.ui.downloads.HandlerForDownloadZipPackageThread;
import fr.ornidroid.ui.threads.GenericTaskHandler;
import fr.ornidroid.ui.threads.GenericTaskHandler.GenericTaskCallback;
import fr.ornidroid.ui.threads.HandlerGenericThread;
import fr.ornidroid.ui.threads.LoaderInfo;

/**
 * The Class AbstractFragment.
 */
public abstract class AbstractFragment extends Fragment implements Runnable,
		OnClickListener, GenericTaskCallback {
	/**
	 * Gets the current (selected) media file if picture : the displayed image,
	 * if sound, the played mp3.
	 */
	private OrnidroidFile currentMediaFile;
	/** The m loader. */
	private HandlerGenericThread handlerThread;

	/** The handler download zip thread. */
	private HandlerGenericThread handlerDownloadZipThread;

	/** The progress bar. */
	private ProgressDialog progressBar;
	private DoubleProgressBarDialog progressBarDownloadPackage;
	/** The download status. */
	private int downloadStatus;
	/** The download from internet button. */
	private Button downloadFromInternetButton;

	/** The download info text. */
	private TextView downloadInfoText;
	/** The ornidroid service. */
	IOrnidroidService ornidroidService = OrnidroidServiceFactory
			.getService(getActivity());

	/** The ornidroid io service. */
	IOrnidroidIOService ornidroidIOService = new OrnidroidIOServiceImpl();
	/** The ornidroid download error. */
	private int ornidroidDownloadErrorCode;
	/** The remove custom picture button. */
	ImageView removeCustomPictureButton;
	/** The remove custom picture button. */
	ImageView removeCustomAudioButton;

	/** The update files button. */
	private ImageView updateFilesButton;

	/** The add custom picture button. */
	private ImageView addCustomPictureButton;
	/** The add custom audio button. */
	ImageView addCustomAudioButton;

	/** The download all from internet button. */
	private Button downloadAllFromInternetButton;
	/** The Constant DOWNLOAD_ERROR_INTENT_PARAM. */
	public static final String DOWNLOAD_ERROR_INTENT_PARAM = "DOWNLOAD_ERROR_INTENT_PARAM";

	/** The Constant DIALOG_PICTURE_INFO_ID. */
	protected static final int DIALOG_PICTURE_INFO_ID = 0;

	/** The Constant DOWNLOAD_FINISHED. */
	protected static final int DOWNLOAD_FINISHED = 2;

	/** The Constant DOWNLOAD_STARTED. */
	protected static final int DOWNLOAD_STARTED = 0;

	/** The Constant PROBLEM_DIRECTORY_ID. */
	protected static final int PROBLEM_DIRECTORY_ID = 2;

	/** The Constant PROBLEM_DOWNLOAD_ID. */
	protected static final int PROBLEM_DOWNLOAD_ID = 1;

	/** The Constant DOWNLOAD_NOT_STARTED. */
	private static final int DOWNLOAD_NOT_STARTED = 1;

	/**
	 * Gets the file type.
	 * 
	 * @return the file type
	 */
	public abstract OrnidroidFileType getFileType();

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
		case WIKIPEDIA_PAGE:
			mediaHomeDirectory = Constants.getOrnidroidHomeWikipedia();
		}
		return mediaHomeDirectory;
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
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public final View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		this.ornidroidDownloadErrorCode = getActivity().getIntent()
				.getIntExtra(DOWNLOAD_ERROR_INTENT_PARAM, 0);

		this.addCustomPictureButton = new ImageView(getActivity());
		this.addCustomPictureButton.setOnClickListener(this);
		this.addCustomPictureButton.setImageResource(R.drawable.ic_add);
		this.addCustomPictureButton.setPadding(20, 0, 20, 0);

		this.removeCustomPictureButton = new ImageView(getActivity());
		this.removeCustomPictureButton.setOnClickListener(this);
		this.removeCustomPictureButton.setImageResource(R.drawable.ic_remove);
		this.removeCustomPictureButton.setPadding(20, 0, 20, 0);
		this.addCustomAudioButton = new ImageView(getActivity());
		this.addCustomAudioButton.setOnClickListener(this);
		this.addCustomAudioButton.setImageResource(R.drawable.ic_add);
		this.addCustomAudioButton.setPadding(20, 0, 20, 0);

		this.removeCustomAudioButton = new ImageView(getActivity());
		this.removeCustomAudioButton.setOnClickListener(this);
		this.removeCustomAudioButton.setImageResource(R.drawable.ic_remove);
		this.removeCustomAudioButton.setPadding(20, 0, 20, 0);
		return getOnCreateView(inflater, container, savedInstanceState);

	}

	/**
	 * Gets the on create view.
	 * 
	 * @param inflater
	 *            the inflater
	 * @param container
	 *            the container
	 * @param savedInstanceState
	 *            the saved instance state
	 * @return the on create view
	 */
	public abstract View getOnCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

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
			final Intent intent = new Intent(getActivity(),
					NewBirdActivity.class);
			// put the uri so that the BirdInfoActivity reloads correctly the
			// bird
			intent.setData(getActivity().getIntent().getData());
			// put an extra info to let the BirdInfoActivity know which tab to
			// open.
			intent.putExtra(NewBirdActivity.INTENT_TAB_TO_OPEN,
					OrnidroidFileType.getCode(getFileType()));
			intent.putExtra(DOWNLOAD_ERROR_INTENT_PARAM,
					this.ornidroidDownloadErrorCode);
			startActivity(intent);
			getActivity().finish();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(final View v) {

		if (v == this.downloadFromInternetButton) {
			startDownload();
		}
		if (v == this.downloadAllFromInternetButton) {
			Dialog dialog = new AlertDialog.Builder(this.getActivity())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.warning)
					.setMessage(R.string.download_zip_package_warn_detail)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int whichButton) {
									dialog.dismiss();
									startDownloadAll();
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int whichButton) {
									dialog.dismiss();
								}
							}).create();
			dialog.show();
		}
		if ((v == this.addCustomPictureButton)
				|| (v == this.addCustomAudioButton)) {
			final Intent intent = new Intent(getActivity(),
					AddCustomMediaActivity.class);
			intent.putExtra(OrnidroidFileType.FILE_TYPE_INTENT_PARAM_NAME,
					getFileType());
			intent.putExtra(Constants.BIRD_DIRECTORY_PARAMETER_NAME,
					this.ornidroidService.getCurrentBird()
							.getBirdDirectoryName());
			startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
		if ((v == this.removeCustomPictureButton)
				|| (v == this.removeCustomAudioButton)) {
			try {
				this.ornidroidIOService
						.removeCustomMediaFile(this.currentMediaFile);
				Toast.makeText(
						getActivity(),
						this.getResources().getString(
								R.string.remove_custom_media_success),
						Toast.LENGTH_LONG).show();
				final Intent intent = new Intent(getActivity(),
						NewBirdActivity.class);
				// put the uri so that the BirdInfoActivity reloads correctly
				// the bird
				intent.setData(getActivity().getIntent().getData());
				// put an extra info to let the BirdInfoActivity know which tab
				// to open
				intent.putExtra(NewBirdActivity.INTENT_TAB_TO_OPEN,
						OrnidroidFileType.getCode(getFileType()));
				startActivity(intent);
				getActivity().finish();
			} catch (final OrnidroidException e) {
				Toast.makeText(
						getActivity(),
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
						AbstractFragment.this.ornidroidIOService
								.downloadMediaFiles(getMediaHomeDirectory(),
										AbstractFragment.this.ornidroidService
												.getCurrentBird(),
										getFileType());

					} catch (final OrnidroidException e) {
						// Log.e(Constants.LOG_TAG,
						// "Download pb " + e.getErrorType() + " "
						// + e.getSourceException());
						// keep it in the field, to get it back when the
						// activity is reloaded.
						AbstractFragment.this.ornidroidDownloadErrorCode = OrnidroidError
								.getErrorCode(e.getErrorType());

					} finally {
						AbstractFragment.this.downloadStatus = DOWNLOAD_FINISHED;
					}

				}
			}).start();
		}

	}

	/**
	 * Start download.
	 */
	protected void startDownload() {
		resetScreenBeforeDownload();
		this.downloadStatus = DOWNLOAD_NOT_STARTED;
		// prepare for a progress bar dialog
		this.progressBar = new ProgressDialog(getActivity());
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
	 * Reset screen before download.
	 */
	private void resetScreenBeforeDownload() {
		getSpecificContentLayout().removeAllViews();
		this.downloadInfoText = new TextView(getActivity());
		this.downloadInfoText.setText(R.string.download_please_wait);
		this.downloadInfoText.setPadding(5, 10, 5, 20);
		getSpecificContentLayout().addView(this.downloadInfoText);
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
		if (this.handlerThread == null) {
			this.handlerThread = new HandlerForCheckUpdateFilesThread(
					ornidroidIOService, ornidroidService,
					getMediaHomeDirectory(), getFileType(), manualCheck);
			this.handlerThread.start();
		}
		this.handlerThread.genericTask(this);
	}

	/**
	 * Gets the screen orientation.
	 * 
	 * @return the screen orientation
	 */
	private int getScreenOrientation() {
		Display getOrient = getActivity().getWindowManager()
				.getDefaultDisplay();
		int orientation;
		if (getOrient.getWidth() < getOrient.getHeight()) {
			orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		} else {
			orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}
		return orientation;
	}

	/**
	 * Start download all.
	 */
	public void startDownloadAll() {

		resetScreenBeforeDownload();
		if (this.ornidroidIOService.isEnoughFreeSpace(getFileType())) {
			// lock the screen rotation to avoid onDestroy call
			getActivity().setRequestedOrientation(getScreenOrientation());
			if (this.handlerDownloadZipThread == null) {
				this.handlerDownloadZipThread = new HandlerForDownloadZipPackageThread(
						ornidroidIOService, Constants.getOrnidroidHome(),
						getFileType());
				this.handlerDownloadZipThread.start();
			}
			this.handlerDownloadZipThread.genericTask(this);
			this.progressBarDownloadPackage = new DoubleProgressBarDialog(
					getActivity(), getFileType());

			this.progressBarDownloadPackage.setCancelable(false);

			this.progressBarDownloadPackage.show();

			Runnable runnableUpdateProgressBar = new Runnable() {

				public void run() {
					while (progressBarDownloadPackage.isShowing()) {
						progressBarDownloadPackage
								.setProgressDownload(ornidroidIOService
										.getZipDownloadProgressPercent(getFileType()));
						progressBarDownloadPackage
								.setProgressInstall(ornidroidIOService
										.getInstallProgressPercent(getFileType()));
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {

						}
					}
				}

			};

			new Thread(runnableUpdateProgressBar).start();
		} else {
			Dialog dialog = new AlertDialog.Builder(this.getActivity())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.download_zip_package_error)
					.setMessage(R.string.download_zip_not_enough_space)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int whichButton) {
									dialog.dismiss();
									reloadActivity();
								}
							}).create();
			dialog.show();
		}
	}

	/**
	 * 
	 * Gets the specific content layout.
	 * 
	 * @return the specific content layout
	 */
	protected abstract LinearLayout getSpecificContentLayout();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.threads.GenericTaskHandler.GenericTaskCallback#onTaskEnded
	 * (fr.ornidroid.ui.threads.GenericTaskHandler,
	 * fr.ornidroid.ui.threads.LoaderInfo)
	 */
	public void onTaskEnded(GenericTaskHandler taskHandler,
			LoaderInfo loaderInfo) {
		switch (taskHandler.getThreadType()) {
		case CHECK_UPDATE:
			onCheckUpdateTaskEnded(loaderInfo);
			break;
		case DOWNLOAD_ZIP:
			onDownloadZipPackageTaskEnded(loaderInfo);
			break;
		default:
			break;
		}

	}

	/**
	 * On download zip package task ended.
	 * 
	 * @param loaderInfo
	 *            the loader info
	 */
	private void onDownloadZipPackageTaskEnded(LoaderInfo loaderInfo) {
		try {
			progressBarDownloadPackage.dismiss();
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			if (loaderInfo.getException() != null) {
				String downloadErrorText = getActivity().getResources()
						.getString(R.string.download_zip_package_error_detail)
						+ BasicConstants.CARRIAGE_RETURN
						+ loaderInfo.getException().toString();
				Dialog dialog = new AlertDialog.Builder(this.getActivity())
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.download_zip_package_error)
						.setMessage(downloadErrorText)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog,
											final int whichButton) {
										dialog.dismiss();
										reloadActivity();
									}
								}).create();
				dialog.show();
			} else {
				reloadActivity();
			}
		} catch (java.lang.IllegalArgumentException e) {
			// this should not occur since the screen is locked during the
			// download thread
			// if this happens, it means that the activity was destroyed earlier
		}

	}

	/**
	 * Reload activity.
	 */
	private void reloadActivity() {
		final Intent intentBirdInfo = new Intent(this.getActivity(),
				NewBirdActivity.class);
		intentBirdInfo.putExtra(NewBirdActivity.INTENT_TAB_TO_OPEN,
				OrnidroidFileType.getCode(this.getFileType()));
		startActivity(intentBirdInfo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	/**
	 * On check update task ended.
	 * 
	 * @param loaderInfo
	 *            the loader info
	 */
	private void onCheckUpdateTaskEnded(LoaderInfo loaderInfo) {
		if (loaderInfo.getException() != null) {
			Toast.makeText(getActivity(), R.string.updates_check_error,
					Toast.LENGTH_LONG).show();
		} else {
			CheckForUpdateFilesLoaderInfo info = (CheckForUpdateFilesLoaderInfo) loaderInfo;
			if (info.isUpdatesAvailable()) {
				AlertDialog dialog = new AlertDialog.Builder(this.getActivity())
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
				dialog.show();
			} else {
				if (info.isManualCheck()) {
					Toast.makeText(getActivity(), R.string.updates_none,
							Toast.LENGTH_LONG).show();
				}
			}
		}
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
	 * Gets the update files button.
	 * 
	 * @return the update files button
	 */
	public ImageView getUpdateFilesButton() {
		this.updateFilesButton = new ImageView(getActivity());
		this.updateFilesButton.setOnClickListener(this);
		this.updateFilesButton
				.setImageResource(R.drawable.ic_check_for_updates);
		return this.updateFilesButton;
	}

	/**
	 * Sets the currently selected media file and makes the remove media button
	 * appear or disappear accordingly.
	 * 
	 * @param selectedFile
	 *            the new selected file
	 */
	public void setCurrentMediaFile(final OrnidroidFile selectedFile) {
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
			case WIKIPEDIA_PAGE:
				// do nothing
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
	 * Gets the current media file.
	 * 
	 * @return the current media file
	 */
	public OrnidroidFile getCurrentMediaFile() {
		return currentMediaFile;
	}

	/**
	 * Prints the download button and info.
	 */
	public void printDownloadButtonAndInfo() {
		getSpecificContentLayout().setOrientation(LinearLayout.VERTICAL);
		getSpecificContentLayout().setGravity(Gravity.CENTER_HORIZONTAL);

		final TextView noMediaMessage = new TextView(getActivity());
		noMediaMessage.setPadding(5, 10, 5, 20);
		noMediaMessage.setGravity(Gravity.CENTER_HORIZONTAL);

		getSpecificContentLayout().addView(noMediaMessage);
		this.downloadFromInternetButton = new Button(getActivity());
		this.downloadFromInternetButton.setOnClickListener(this);

		this.downloadFromInternetButton.setText(R.string.download_birds_file);
		this.downloadFromInternetButton.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		getSpecificContentLayout().addView(this.downloadFromInternetButton);

		this.downloadAllFromInternetButton = new Button(getActivity());

		this.downloadAllFromInternetButton.setOnClickListener(this);
		this.downloadAllFromInternetButton
				.setText(R.string.download_zip_package);
		this.downloadAllFromInternetButton.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		getSpecificContentLayout().addView(this.downloadAllFromInternetButton);

		final TextView manualDownloadInfo = new TextView(getActivity());
		manualDownloadInfo.setPadding(5, 10, 5, 20);
		manualDownloadInfo.setGravity(Gravity.CENTER_HORIZONTAL);
		manualDownloadInfo.setText(R.string.download_manual);
		Linkify.addLinks(manualDownloadInfo, Linkify.ALL);
		getSpecificContentLayout().addView(manualDownloadInfo);

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
			case WIKIPEDIA_PAGE:
				noMediaMessage.setText(R.string.no_wiki);
				break;
			}

			break;
		default:
			noMediaMessage.setText(R.string.unknown_error);
			break;
		}

	}
}
