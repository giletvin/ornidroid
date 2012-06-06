package fr.giletvin.ornidroid.ui;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.giletvin.ornidroid.R;
import fr.giletvin.ornidroid.bo.Bird;
import fr.giletvin.ornidroid.bo.OrnidroidFileType;
import fr.giletvin.ornidroid.helper.Constants;
import fr.giletvin.ornidroid.helper.OrnidroidException;
import fr.giletvin.ornidroid.service.IOrnidroidIOService;
import fr.giletvin.ornidroid.service.IOrnidroidService;
import fr.giletvin.ornidroid.service.OrnidroidIOServiceImpl;
import fr.giletvin.ornidroid.service.OrnidroidServiceFactory;

/**
 * The Class AbstractDownloadableMediaActivity.
 */
public abstract class AbstractDownloadableMediaActivity extends Activity
		implements Runnable, OnClickListener {
	/** The bird. */
	private Bird bird;

	/** The download from internet button. */
	private ImageView downloadFromInternetButton;

	/** The download info text. */
	private TextView downloadInfoText;

	/**
	 * Gets the bird.
	 * 
	 * @return the bird
	 */
	protected Bird getBird() {
		return bird;
	}

	/** The Constant PROGRESS_BAR_MAX. */
	private static final int PROGRESS_BAR_MAX = 100;
	/** The Constant DOWNLOAD_FINISHED. */
	protected static final int DOWNLOAD_FINISHED = 2;

	/** The Constant DOWNLOAD_STARTED. */
	protected static final int DOWNLOAD_STARTED = 0;

	/** The Constant DOWNLOAD_NOT_STARTED. */
	private static final int DOWNLOAD_NOT_STARTED = 1;

	/** The Constant PROBLEM_DOWNLOAD_ID. */
	protected static final int PROBLEM_DOWNLOAD_ID = 1;

	/** The Constant PROBLEM_DIRECTORY_ID. */
	protected static final int PROBLEM_DIRECTORY_ID = 2;
	/** The progress bar. */
	private ProgressDialog progressBar;

	/** The progress bar status. */
	private int progressBarStatus = 0;

	/** The progress bar handler. */
	private Handler progressBarHandler = new Handler();

	/** The download status. */
	private int downloadStatus;

	/** The download progress. */
	private int downloadProgress = 0;

	/** The ornidroid io service. */
	private final IOrnidroidIOService ornidroidIOService;
	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/**
	 * Instantiates a new abstract downloadable media activity.
	 */
	public AbstractDownloadableMediaActivity() {
		ornidroidService = OrnidroidServiceFactory.getService(this);
		ornidroidIOService = new OrnidroidIOServiceImpl();
	}

	/**
	 * Gets the download status.
	 * 
	 * @return the download status
	 */
	public int getDownloadStatus() {
		return downloadStatus;
	}

	/**
	 * Sets the download status.
	 * 
	 * @param downloadStatus
	 *            the new download status
	 */
	public void setDownloadStatus(int downloadStatus) {
		this.downloadStatus = downloadStatus;
	}

	/**
	 * Start download.
	 */
	protected void startDownload() {
		downloadStatus = DOWNLOAD_NOT_STARTED;
		// prepare for a progress bar dialog
		progressBar = new ProgressDialog(this);
		progressBar.setCancelable(true);
		progressBar.setMessage(this.getResources().getText(
				R.string.download_in_progress));
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBar.show();

		// reset progress bar status
		progressBarStatus = 0;

		new Thread(this).start();
	}

	/**
	 * Start download thread and compute download progress.
	 * 
	 * @return the int : this method is periodically called by the progress bar
	 *         thread. When the download is finished, the return is 100%.
	 */
	protected int startDownloadThreadAndComputeDownloadProgress() {
		if (downloadStatus == DOWNLOAD_NOT_STARTED) {
			// if download is not started, start it in a new thread
			downloadStatus = DOWNLOAD_STARTED;
			new Thread(new Runnable() {
				public void run() {

					try {
						ornidroidIOService.downloadMediaFiles(
								getMediaHomeDirectory(), bird, getFileType());

					} catch (OrnidroidException e) {
						Log.e(Constants.LOG_TAG,
								"Download pb " + e.getErrorType() + " " + e);

					} finally {
						downloadStatus = DOWNLOAD_FINISHED;
					}

				}
			}).start();
		}
		if (downloadStatus != DOWNLOAD_FINISHED) {
			return computeDownloadProgress();
		} else {
			return PROGRESS_BAR_MAX;
		}
	}

	/**
	 * Compute download progress.
	 * 
	 * @return the int
	 */
	private int computeDownloadProgress() {
		return (int) Math.floor(downloadProgress
				+ (PROGRESS_BAR_MAX - downloadProgress) / 5);
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

		while (progressBarStatus < 100) {

			// process some tasks
			progressBarStatus = startDownloadThreadAndComputeDownloadProgress();

			// your computer is too fast, sleep 1 second
			try {

				Thread.sleep(1000);

			} catch (InterruptedException e) {
				Log.e(Constants.LOG_TAG,
						"Exception in the progress bar thread " + e);

			}

			// Update the progress bar
			progressBarHandler.post(new Runnable() {
				public void run() {
					progressBar.setProgress(progressBarStatus);
				}
			});
		}

		// ok, file is downloaded,
		if (progressBarStatus >= 100) {

			// sleep 2 seconds, so that you can see the 100%
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Log.e(Constants.LOG_TAG,
						"Exception in the completed progress bar thread " + e);

			}

			// close the progress bar dialog
			progressBar.dismiss();

			// now that the files have been downloaded, force a reopen of the
			// same screen with an intent
			Intent intent = new Intent(this, BirdInfoActivity.class);
			// put the uri so that the BirdInfoActivity reloads correctly the
			// bird
			intent.setData(getIntent().getData());
			// put an extra info to let the BirdInfoActivity know which tab to
			// open.
			intent.putExtra(BirdInfoActivity.INTENT_ACTIVITY_TO_OPEN,
					OrnidroidFileType.getCode(getFileType()));
			startActivity(intent);
			finish();
		}

	}

	/**
	 * Gets the file type.
	 * 
	 * @return the file type
	 */
	public abstract OrnidroidFileType getFileType();

	/**
	 * Load media files locally.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	protected void loadMediaFilesLocally() throws OrnidroidException {

		File fileDirectory = new File(Constants.getOrnidroidBirdHomeMedia(bird,
				getFileType()));
		ornidroidIOService.checkAndCreateDirectory(fileDirectory);

		ornidroidIOService.loadMediaFiles(getMediaHomeDirectory(), bird,
				getFileType());

	}

	/**
	 * Gets the media home directory.
	 * 
	 * @return the media home directory
	 */
	private String getMediaHomeDirectory() {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bird = ornidroidService.getCurrentBird();
		try {
			loadMediaFilesLocally();
		} catch (OrnidroidException e) {
			Log.e(Constants.LOG_TAG, "Error reading media files of bird "
					+ bird.getTaxon() + " e");
		}
		hookOnCreate();
	}

	/**
	 * Hook on create. Here is the specific onCreate stuff for the subclasses.
	 */
	protected abstract void hookOnCreate();

	/**
	 * Gets the specific content layout.
	 * 
	 * @return the specific content layout
	 */
	protected abstract LinearLayout getSpecificContentLayout();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {

		if (v == downloadFromInternetButton) {
			getSpecificContentLayout().removeAllViews();
			downloadInfoText = new TextView(this);
			downloadInfoText.setText(R.string.download_please_wait);
			getSpecificContentLayout().addView(downloadInfoText);
			startDownload();
		}

	}

	/**
	 * Prints the download button and info.
	 */
	protected void printDownloadButtonAndInfo() {
		getSpecificContentLayout().setOrientation(LinearLayout.VERTICAL);
		getSpecificContentLayout().setGravity(Gravity.CENTER_HORIZONTAL);

		TextView noMediaMessage = new TextView(this);
		switch (getFileType()) {
		case AUDIO:
			noMediaMessage.setText(R.string.no_records);
			break;
		case PICTURE:
			noMediaMessage.setText(R.string.no_pictures);
			break;
		}
		getSpecificContentLayout().addView(noMediaMessage);
		downloadFromInternetButton = new ImageView(this);
		downloadFromInternetButton.setOnClickListener(this);
		downloadFromInternetButton
				.setImageResource(R.drawable.ic_file_download);
		downloadFromInternetButton.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		getSpecificContentLayout().addView(downloadFromInternetButton);
	}
}