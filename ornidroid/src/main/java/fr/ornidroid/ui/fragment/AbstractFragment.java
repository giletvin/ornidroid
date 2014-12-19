package fr.ornidroid.ui.fragment;

import java.io.File;
import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.event.DownloadZipEvent;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.helper.UIHelper;
import fr.ornidroid.service.IOrnidroidIOService;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidIOServiceImpl;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.activity.AddCustomMediaActivity_;
import fr.ornidroid.ui.activity.NewBirdActivity;
import fr.ornidroid.ui.activity.NewBirdActivity_;

/**
 * The Class AbstractFragment.
 */
@EFragment
public abstract class AbstractFragment extends Fragment implements Runnable {

	@InstanceState
	boolean isDownloadAllRunning = false;

	/**
	 * Gets the current (selected) media file if picture : the displayed image,
	 * if sound, the played mp3.
	 */
	private OrnidroidFile currentMediaFile;

	/** The progress bar1. */
	private ProgressBar downloadAllProgressBar1;
	/** The progress bar2. */
	private ProgressBar downloadAllProgressBar2;

	/** The progress bar. */
	private ProgressDialog progressBar;

	/** The download status. */
	private int downloadStatus;

	/** The ornidroid service. */
	IOrnidroidService ornidroidService = OrnidroidServiceFactory
			.getService(getActivity());

	/** The ornidroid io service. */
	IOrnidroidIOService ornidroidIOService = new OrnidroidIOServiceImpl();
	/** The ornidroid download error. */
	int ornidroidDownloadErrorCode;

	/** The picture layout. */
	@ViewById(R.id.fragment_main_content)
	LinearLayout fragmentMainContent;

	@ViewById(R.id.download_banner)
	View downloadBanner;
	@ViewById(R.id.bt_download_only_for_bird)
	Button btDownloadOnlyForBird;
	@ViewById(R.id.bt_download_all)
	Button btDownloadAll;
	/** The remove custom picture button. */
	@ViewById(R.id.iv_remove_custom_picture)
	ImageView removeCustomPictureButton;
	/** The remove custom picture button. */
	ImageView removeCustomAudioButton;

	@ViewById(R.id.tv_no_media_message)
	TextView noMediaMessage;
	/** The update files button. */
	@ViewById(R.id.iv_update_files_button)
	ImageView updateFilesButton;

	/** The add custom picture button. */
	@ViewById(R.id.iv_add_custom_picture)
	ImageView addCustomPictureButton;
	/** The add custom audio button. */
	ImageView addCustomAudioButton;

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
		if (this.ornidroidService.getCurrentBird() != null) {
			final File fileDirectory = new File(
					Constants.getOrnidroidBirdHomeMedia(
							this.ornidroidService.getCurrentBird(),
							getFileType()));
			this.ornidroidIOService.checkAndCreateDirectory(fileDirectory);

			this.ornidroidIOService.loadMediaFiles(getMediaHomeDirectory(),
					this.ornidroidService.getCurrentBird(), getFileType());
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

			UIHelper.unlockScreenOrientation(getActivity());
			// now that the files have been downloaded, force a reopen of the
			// same screen with an intent
			final Intent intent = new Intent(getActivity(),
					NewBirdActivity_.class);
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

	@Click(R.id.bt_download_only_for_bird)
	void downloadFromInternetButtonClicked() {
		startDownload();
	}

	@Click(R.id.bt_download_all)
	void downloadAllButtonClicked() {
		Dialog dialog = new AlertDialog.Builder(this.getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.warning)
				.setMessage(R.string.download_zip_package_warn_detail)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								dialog.dismiss();
								startDownloadAll();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								dialog.dismiss();
							}
						}).create();
		dialog.show();
	}

	// TODO : faire un bouton commun aux sons et images
	@Click(R.id.iv_add_custom_picture)
	void addCustomPictureClicked() {
		final Intent intent = new Intent(getActivity(),
				AddCustomMediaActivity_.class);
		intent.putExtra(OrnidroidFileType.FILE_TYPE_INTENT_PARAM_NAME,
				getFileType());
		intent.putExtra(Constants.BIRD_DIRECTORY_PARAMETER_NAME,
				this.ornidroidService.getCurrentBird().getBirdDirectoryName());
		startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	@Click(R.id.iv_remove_custom_picture)
	void removeCustomPictureClicked() {
		try {
			this.ornidroidIOService
					.removeCustomMediaFile(this.currentMediaFile);
			Toast.makeText(
					getActivity(),
					this.getResources().getString(
							R.string.remove_custom_media_success),
					Toast.LENGTH_LONG).show();
			final Intent intent = new Intent(getActivity(),
					NewBirdActivity_.class);
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

	@Click(R.id.iv_update_files_button)
	void updateFilesButtonClicked() {
		checkForUpdates(true);
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
		UIHelper.lockScreenOrientation(getActivity());
		this.progressBar.show();

		// reset download status
		this.downloadStatus = DOWNLOAD_NOT_STARTED;

		new Thread(this).start();
	}

	/**
	 * Reset screen before download.
	 */
	@UiThread
	void resetScreenBeforeDownload() {
		this.btDownloadOnlyForBird.setVisibility(View.GONE);
		this.btDownloadAll.setVisibility(View.GONE);
		this.noMediaMessage.setText(R.string.download_please_wait);
	}

	/**
	 * Check for updates. Fires a dialog box if updates are found. If manual
	 * check, let the user know that no updates are found.
	 * 
	 * @param manualCheck
	 *            true if the user checks manually.
	 * 
	 */
	@Background
	void checkForUpdates(final boolean manualCheck) {
		Exception exception = null;
		boolean updatesToDo = false;
		List<String> filesToDownload;
		try {
			filesToDownload = this.ornidroidIOService.filesToUpdate(
					getMediaHomeDirectory(), ornidroidService.getCurrentBird(),
					getFileType());
			updatesToDo = (filesToDownload.size() > 0);

		} catch (OrnidroidException e) {
			exception = e;
		}
		onCheckUpdateTaskEnded(manualCheck, updatesToDo, exception);
	}

	@Background(delay = 2000)
	void manageDownloadAllProgressBars() {

		while (isDownloadAllRunning) {
			updateDownloadAllProgressBars();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {

			}
		}
	}

	@UiThread
	void updateDownloadAllProgressBars() {
		if (downloadAllProgressBar1 == null) {
			downloadAllProgressBar1 = new ProgressBar(this.getActivity());
			downloadAllProgressBar2 = new ProgressBar(this.getActivity());
			this.downloadAllProgressBar2.setMax(BasicConstants
					.getNbOfFilesInPackage(getFileType()));
			getSpecificContentLayout().addView(downloadAllProgressBar1);
			getSpecificContentLayout().addView(downloadAllProgressBar2);
		}
		downloadAllProgressBar1.setProgress(ornidroidIOService
				.getZipDownloadProgressPercent(getFileType()));
		downloadAllProgressBar1.setProgress(ornidroidIOService
				.getInstallProgressPercent(getFileType()));
	}

	/**
	 * Start download all.
	 */
	@Background
	void startDownloadAll() {
		if (!isDownloadAllRunning) {

			isDownloadAllRunning = true;

			resetScreenBeforeDownload();
			if (this.ornidroidIOService.isEnoughFreeSpace(getFileType())) {
				Exception exception = null;
				try {
					manageDownloadAllProgressBars();

					String zipname = this.ornidroidIOService
							.getZipname(getFileType());
					try {
						this.ornidroidIOService.downloadZipPackage(zipname,
								Constants.getOrnidroidHome());
					} catch (OrnidroidException e) {
						exception = e;
					}
				} finally {
					// post the event in the EventBus
					EventBus.getDefault().post(new DownloadZipEvent(exception));
				}

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
	}

	/**
	 * 
	 * Gets the specific content layout.
	 * 
	 * @return the specific content layout
	 */
	protected abstract LinearLayout getSpecificContentLayout();

	/**
	 * On download zip package task ended.
	 * 
	 * @param loaderInfo
	 *            the loader info
	 */
	@UiThread
	void onEventMainThread(DownloadZipEvent event) {
		isDownloadAllRunning = false;
		if (event.exception != null) {
			String downloadErrorText = getActivity().getResources().getString(
					R.string.download_zip_package_error_detail)
					+ BasicConstants.CARRIAGE_RETURN
					+ event.exception.toString();
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
	}

	/**
	 * Reload activity.
	 */
	private void reloadActivity() {
		if (this.getActivity() != null) {
			final Intent intentBirdInfo = new Intent(this.getActivity(),
					NewBirdActivity_.class);
			intentBirdInfo.putExtra(NewBirdActivity.INTENT_TAB_TO_OPEN,
					OrnidroidFileType.getCode(this.getFileType()));
			startActivity(intentBirdInfo
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
	}

	/**
	 * On check update task ended.
	 * 
	 * @param manuelCheck
	 *            the manuel check
	 * @param updatesToDo
	 *            the updates to do
	 * @param exception
	 *            the exception
	 */
	@UiThread
	void onCheckUpdateTaskEnded(boolean manuelCheck, boolean updatesToDo,
			Exception exception) {
		if (exception != null) {
			Toast.makeText(getActivity(), R.string.updates_check_error,
					Toast.LENGTH_LONG).show();
		} else {
			if (updatesToDo) {
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
				if (manuelCheck) {
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
	// TODO : a eliminer. Conservé uniquement pour la compilation du fragment
	// audio
	@Deprecated
	public ImageView getUpdateFilesButton() {
		this.updateFilesButton = new ImageView(getActivity());
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		EventBus.getDefault().register(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
