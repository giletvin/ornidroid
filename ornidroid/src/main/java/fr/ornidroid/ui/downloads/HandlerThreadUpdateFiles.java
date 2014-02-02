package fr.ornidroid.ui.downloads;

import java.util.List;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.ui.AbstractDownloadableMediaActivity;

/**
 * The Class HandlerThreadUpdateFiles.
 */
public class HandlerThreadUpdateFiles extends HandlerThread implements
		CheckUpdateFilesHandler {

	/** The Constant CHECK_FOR_UPDATES_ENDED. */
	private static final int CHECK_FOR_UPDATES_ENDED = 4;

	/** The Constant CHECK_FOR_UPDATES_ORDER. */
	private static final int CHECK_FOR_UPDATES_ORDER = 1;

	/** The m loader callback. */
	private final Callback mLoaderCallback = new Callback() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
		 */
		public boolean handleMessage(final Message msg) {
			switch (msg.what) {
			case CHECK_FOR_UPDATES_ORDER:
				HandlerThreadUpdateFiles.this.mLoaderHandler
						.removeMessages(CHECK_FOR_UPDATES_ORDER);

				final Handler h = HandlerThreadUpdateFiles.this.mMainHandler;

				final UpdateFilesLoaderInfo loaderInfo = (UpdateFilesLoaderInfo) msg.obj;
				if (loaderInfo == null) {
					return false;
				}

				try {
					// ICI LE CODE METIER
					doCheckForUpdates(loaderInfo);

				} catch (final Exception e) {
					// Error occured : keep the exception in the loader info
					loaderInfo.setException(e);

				} finally {
					// always send a stop signal
					Message.obtain(h, CHECK_FOR_UPDATES_ENDED, loaderInfo)
							.sendToTarget();
				}
			}

			return true;
		}
	};

	/** The m loader handler. */
	private Handler mLoaderHandler;

	/** The m main callback. */
	private final Callback mMainCallback = new Callback() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
		 */
		public boolean handleMessage(final Message msg) {
			// here we use the callback methods from the calling activity ...

			if (HandlerThreadUpdateFiles.this.mLoaderHandler == null) {
				// Looper has been stopped
				return false;
			}

			final UpdateFilesLoaderInfo loaderInfo = (UpdateFilesLoaderInfo) msg.obj;
			if (loaderInfo == null) {
				// this should not occur
				return false;
			}

			final UpdateFilesCallback callback = loaderInfo.getCallback();
			if (callback == null) {
				// this should not occur
				return false;
			}

			switch (msg.what) {

			case CHECK_FOR_UPDATES_ENDED:
				callback.onUpdateFilesEnded(HandlerThreadUpdateFiles.this,
						loaderInfo);
				break;

			default:
				return false;
			}

			return true;
		}

	};

	/** Handler permettant de communiquer avec le "main thread". */
	private final Handler mMainHandler;

	/** The activity. */
	private final AbstractDownloadableMediaActivity activity;

	/**
	 * Instantiates a new handler thread update files.
	 * 
	 * @param activity
	 *            the activity
	 */
	public HandlerThreadUpdateFiles(AbstractDownloadableMediaActivity activity) {
		super(Constants.LOG_TAG, Process.THREAD_PRIORITY_BACKGROUND);
		this.mMainHandler = new Handler(Looper.getMainLooper(),
				this.mMainCallback);
		this.activity = activity;
	}

	/**
	 * Do check for updates.
	 * 
	 * @param loaderInfo
	 *            the loader info
	 */
	private void doCheckForUpdates(UpdateFilesLoaderInfo loaderInfo) {
		boolean updatesToDo = false;
		try {
			List<String> filesToDownload = activity.getOrnidroidIOService()
					.filesToUpdate(activity.getMediaHomeDirectory(),
							activity.getBird(), activity.getFileType());
			updatesToDo = (filesToDownload.size() > 0);
		} catch (final OrnidroidException e) {
			loaderInfo.setException(e);
		}
		loaderInfo.setUpdatesAvailable(updatesToDo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.HandlerThread#quit()
	 */
	@Override
	public boolean quit() {
		if (this.mLoaderHandler != null) {
			this.mLoaderHandler.removeMessages(CHECK_FOR_UPDATES_ORDER);
			this.mLoaderHandler = null;
		}
		return super.quit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.HandlerThread#onLooperPrepared()
	 */
	@Override
	protected void onLooperPrepared() {
		super.onLooperPrepared();
		synchronized (this) {
			this.mLoaderHandler = new Handler(getLooper(), this.mLoaderCallback);
			notifyAll();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.downloads.CheckUpdateFilesHandler#checkForUpdates(boolean
	 * , fr.ornidroid.ui.downloads.CheckUpdateFilesHandler.UpdateFilesCallback)
	 */
	public void checkForUpdates(boolean manualCheck,
			UpdateFilesCallback callback) {
		if (!isAlive()) {
			return;
		}

		synchronized (this) {
			while (isAlive() && (this.mLoaderHandler == null)) {
				try {
					wait();
				} catch (final InterruptedException e) {
				}
			}
		}

		if (this.mLoaderHandler != null) {
			final Message message = this.mLoaderHandler.obtainMessage();
			message.what = CHECK_FOR_UPDATES_ORDER;
			message.obj = new UpdateFilesLoaderInfo(manualCheck, callback);

			this.mLoaderHandler.sendMessage(message);
		}

	}
}
