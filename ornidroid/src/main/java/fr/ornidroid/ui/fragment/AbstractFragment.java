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
import fr.ornidroid.event.DownloadOnlyOneBirdEvent;
import fr.ornidroid.event.DownloadZipEvent;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.service.IOrnidroidIOService;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidIOServiceImpl;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.activity.AddCustomMediaActivity_;
import fr.ornidroid.ui.activity.HomeActivity_;
import fr.ornidroid.ui.activity.NewBirdActivity;
import fr.ornidroid.ui.activity.NewBirdActivity_;

/**
 * The Class AbstractFragment.
 */
@EFragment
public abstract class AbstractFragment extends Fragment {

	@InstanceState
	boolean isDownloadInProgress = false;

	/**
	 * Gets the current (selected) media file if picture : the displayed image,
	 * if sound, the played mp3.
	 */
	private OrnidroidFile currentMediaFile;

	/** The progress bar1. */
	private ProgressBar downloadAllProgressBar1;
	/** The progress bar2. */
	private ProgressBar downloadAllProgressBar2;

	@ViewById(R.id.pb_download_in_progress)
	ProgressBar pbDownloadInProgress;

	/** The ornidroid service. */
	IOrnidroidService ornidroidService = OrnidroidServiceFactory
			.getService(getActivity());

	/** The ornidroid io service. */
	IOrnidroidIOService ornidroidIOService = new OrnidroidIOServiceImpl();

	/** Layouts */
	@ViewById(R.id.fragment_main_content)
	LinearLayout fragmentMainContent;
	@ViewById(R.id.download_banner)
	View downloadBanner;
	@ViewById(R.id.bt_download_only_for_bird)
	Button btDownloadOnlyForBird;
	@ViewById(R.id.bt_download_all)
	Button btDownloadAll;
	/** The remove custom picture button. */
	// TODO : refactoring removeCustomMediabutton
	@ViewById(R.id.iv_remove_custom_picture)
	ImageView removeCustomPictureButton;

	@ViewById(R.id.tv_no_media_message)
	TextView noMediaMessage;
	/** The update files button. */
	@ViewById(R.id.iv_update_files_button)
	ImageView updateFilesButton;
	/** The add custom picture button. */
	// TODO : refactoring removeCustomMediabutton
	@ViewById(R.id.iv_add_custom_picture)
	ImageView addCustomPictureButton;

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
	 * Start download.
	 */
	@Background
	void startDownload() {
		if (!isDownloadInProgress) {
			resetScreenBeforeDownload();

			Exception exception = null;
			try {
				AbstractFragment.this.ornidroidIOService
						.downloadMediaFiles(getMediaHomeDirectory(),
								AbstractFragment.this.ornidroidService
										.getCurrentBird(), getFileType());
			} catch (final OrnidroidException e) {
				exception = e;
			} finally {
				// post the event in the EventBus
				EventBus.getDefault().post(
						new DownloadOnlyOneBirdEvent(exception));
			}

		}
	}

	/**
	 * Reset screen before download.
	 */
	@UiThread
	void resetScreenBeforeDownload() {
		// TODO : attention à la gestion du download All : afficher les progress
		// bars
		pbDownloadInProgress.setVisibility(View.VISIBLE);
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

		while (isDownloadInProgress) {
			updateDownloadAllProgressBars();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {

			}
		}
	}

	@UiThread
	void updateDownloadAllProgressBars() {
		// TODO : à revoir
		if (downloadAllProgressBar1 == null) {
			downloadAllProgressBar1 = new ProgressBar(this.getActivity());
			downloadAllProgressBar2 = new ProgressBar(this.getActivity());
			this.downloadAllProgressBar2.setMax(BasicConstants
					.getNbOfFilesInPackage(getFileType()));

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
		if (!isDownloadInProgress) {
			isDownloadInProgress = true;
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
				notEnoughFreeSpaceForPackageDownloadDialog();
			}
		}
	}

	@UiThread
	void notEnoughFreeSpaceForPackageDownloadDialog() {
		Dialog dialog = new AlertDialog.Builder(this.getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.download_zip_package_error)
				.setMessage(R.string.download_zip_not_enough_space)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								dialog.dismiss();
								reloadActivity();
							}
						}).create();
		dialog.show();
	}

	/**
	 * On download zip package task ended.
	 * 
	 * @param loaderInfo
	 *            the loader info
	 */
	@UiThread
	void onEventMainThread(DownloadZipEvent event) {
		isDownloadInProgress = false;
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
	 * On download only for one bird ended.
	 * 
	 * @param event
	 *            the event
	 */
	@UiThread
	void onEventMainThread(DownloadOnlyOneBirdEvent event) {
		isDownloadInProgress = false;
		if (event.exception != null) {
			String downloadErrorText = getErrorMessage((OrnidroidException) event.exception);

			Dialog dialog = new AlertDialog.Builder(this.getActivity())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.download_birds_file)
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
	 * Sets the currently selected media file and makes the remove media button
	 * appear or disappear accordingly.
	 * 
	 * @param selectedFile
	 *            the new selected file
	 */
	public void setCurrentMediaFile(final OrnidroidFile selectedFile) {
		this.currentMediaFile = selectedFile;
		if (this.currentMediaFile != null) {
			if (this.currentMediaFile.isCustomMediaFile()) {
				removeCustomPictureButton.setVisibility(View.VISIBLE);
			} else {
				removeCustomPictureButton.setVisibility(View.GONE);
			}
		} else {
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
	public String getErrorMessage(OrnidroidException ornidroidException) {
		OrnidroidError ornidroidError = ornidroidException.getErrorType();
		String msg = null;
		switch (ornidroidError) {
		case ORNIDROID_CONNECTION_PROBLEM:
			msg = getActivity().getResources().getString(
					R.string.dialog_alert_connection_problem);
			break;
		case ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST:
			msg = getActivity().getResources().getString(
					R.string.no_resources_online);

			break;
		case NO_ERROR:
			switch (getFileType()) {
			case AUDIO:
				msg = getActivity().getResources().getString(
						R.string.no_records);

				break;
			case PICTURE:
				msg = getActivity().getResources().getString(
						R.string.no_pictures);

				break;
			case WIKIPEDIA_PAGE:
				msg = getActivity().getResources().getString(R.string.no_wiki);

				break;
			}
			break;
		default:
			msg = getActivity().getResources()
					.getString(R.string.unknown_error);
			break;
		}
		return msg;

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

	/**
	 * Common after views.
	 * 
	 * @return true, if successful and media are available. False in case of pb
	 *         or no media to display. Download banner is automatically
	 *         displayed
	 */
	boolean commonAfterViews() {
		boolean success = true;
		if (this.ornidroidService.getCurrentBird() == null) {
			// Github : #118
			final Intent intent = new Intent(getActivity(), HomeActivity_.class);
			startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
		if (Constants.getAutomaticUpdateCheckPreference()) {
			updateFilesButton.setVisibility(View.GONE);
			checkForUpdates(false);
		}
		try {
			loadMediaFilesLocally();

			switch (getFileType()) {
			case PICTURE:
				success = (ornidroidService.getCurrentBird()
						.getNumberOfPictures() > 0);
				break;
			case AUDIO:
				success = (ornidroidService.getCurrentBird()
						.getNumberOfSounds() > 0);
				break;
			case WIKIPEDIA_PAGE:
				success = (ornidroidService.getCurrentBird().getWikipediaPage() != null);
				break;
			}

			if (!success) {
				fragmentMainContent.setVisibility(View.GONE);
				downloadBanner.setVisibility(View.VISIBLE);
			} else {
				fragmentMainContent.setVisibility(View.VISIBLE);
				downloadBanner.setVisibility(View.GONE);

			}
		} catch (final OrnidroidException e) {
			success = false;
			Toast.makeText(
					getActivity(),
					"Error reading media files of bird "
							+ this.ornidroidService.getCurrentBird().getTaxon()
							+ " e", Toast.LENGTH_LONG).show();
		}
		return success;
	}
}
