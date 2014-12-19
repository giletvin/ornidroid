package fr.ornidroid.ui.components.progressbar;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.BasicConstants;

/**
 * The Class DoubleProgressBarDialog.
 */
@Deprecated
public class DoubleProgressBarDialog extends Dialog {

	/** The progress bar1. */
	private ProgressBar progressBar1;

	/** The progress bar2. */
	private ProgressBar progressBar2;

	/**
	 * Instantiates a new double progress bar dialog.
	 * 
	 * @param context
	 *            the context
	 */
	public DoubleProgressBarDialog(final Context context,
			OrnidroidFileType fileType) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.double_progress_bar);
		this.progressBar1 = (ProgressBar) findViewById(R.id.progressbar1);
		this.progressBar2 = (ProgressBar) findViewById(R.id.progressbar2);
		this.progressBar2
				.setMax(BasicConstants.getNbOfFilesInPackage(fileType));

	}

	/**
	 * Sets the progress download.
	 * 
	 * @param zipDownloadProgressPercent
	 *            the new progress download
	 */
	public void setProgressDownload(int zipDownloadProgressPercent) {
		progressBar1.setProgress(zipDownloadProgressPercent);
	}

	/**
	 * Sets the progress install.
	 * 
	 * @param installationProgressPercent
	 *            the new progress install
	 */
	public void setProgressInstall(int installationProgressPercent) {
		progressBar2.setProgress(installationProgressPercent);

	}

}