package fr.ornidroid.ui;

import java.io.File;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import fr.ornidroid.helper.Constants;

/**
 * The Class HandlerThreadOrnidroidHomeMvDirectory.
 */
public class HandlerThreadOrnidroidHomeMvDirectory extends HandlerThread
		implements MoveDirectoryHandler {

	/**
	 * Wrapper permettant de faire passer de l'information a travers les
	 * Messages.
	 * 
	 * @author Cyril Mottier
	 */
	public static class LoaderInfo {

		/** The callback. */
		public MoveDirectoryCallback callback;

		/** The url. */
		public final String fromDirectory;

		/** The to directory. */
		public final String toDirectory;

		/** The exception. */
		private Exception exception;

		/**
		 * Instantiates a new loader info.
		 * 
		 * @param fromDirectory
		 *            the sroe directory
		 * @param toDirectory
		 *            the destination directory
		 * @param callback
		 *            the callback
		 */
		public LoaderInfo(final String fromDirectory, final String toDirectory,
				final MoveDirectoryCallback callback) {
			this.fromDirectory = fromDirectory;
			this.toDirectory = toDirectory;
			this.callback = callback;
		}

		/**
		 * Gets the exception.
		 * 
		 * @return the exception
		 */
		public Exception getException() {
			return this.exception;
		}

		/**
		 * Sets the exception.
		 * 
		 * @param exception
		 *            the new exception
		 */
		public void setException(final Exception exception) {
			this.exception = exception;
		}
	}

	/** The Constant MOVE_ENDED. */
	private static final int MOVE_ENDED = 4;

	/**
	 * "what" id used by the Handler.
	 */
	private static final int MOVE_ORDER = 1;

	/** The m loader callback. */
	private final Callback mLoaderCallback = new Callback() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
		 */
		public boolean handleMessage(final Message msg) {
			switch (msg.what) {
			case MOVE_ORDER:
				HandlerThreadOrnidroidHomeMvDirectory.this.mLoaderHandler
						.removeMessages(MOVE_ORDER);

				final Handler h = HandlerThreadOrnidroidHomeMvDirectory.this.mMainHandler;

				final LoaderInfo loaderInfo = (LoaderInfo) msg.obj;
				if (loaderInfo == null) {
					return false;
				}

				try {

					loaderInfo.callback.getFileHelper().initMoveOperation();
					final File srcDir = new File(loaderInfo.fromDirectory);
					final File destDir = new File(loaderInfo.toDirectory);
					loaderInfo.callback.getFileHelper().moveDirectory(srcDir,
							destDir);
				} catch (final Exception e) {
					// Error occured : keep the exception in the loader info
					loaderInfo.setException(e);

				} finally {
					// always send a stop signal
					Message.obtain(h, MOVE_ENDED, loaderInfo).sendToTarget();
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

			if (HandlerThreadOrnidroidHomeMvDirectory.this.mLoaderHandler == null) {
				// Looper has been stopped
				return false;
			}

			final LoaderInfo loaderInfo = (LoaderInfo) msg.obj;
			if (loaderInfo == null) {
				// this should not occur
				return false;
			}

			final MoveDirectoryCallback callback = loaderInfo.callback;
			if (callback == null) {
				// this should not occur
				return false;
			}

			switch (msg.what) {

			case MOVE_ENDED:
				callback.onMoveEnded(
						HandlerThreadOrnidroidHomeMvDirectory.this, loaderInfo);
				break;

			default:
				return false;
			}

			return true;
		}

	};

	/** Handler permettant de communiquer avec le "main thread". */
	private final Handler mMainHandler;

	/**
	 * Instantiates a new handler thread ornidroid home mv directory.
	 */
	public HandlerThreadOrnidroidHomeMvDirectory() {
		super(Constants.LOG_TAG, Process.THREAD_PRIORITY_BACKGROUND);
		this.mMainHandler = new Handler(Looper.getMainLooper(),
				this.mMainCallback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.ui.ImageLoader#loadImage(java.lang.String,
	 * fr.ornidroid.ui.ImageLoader.ImageLoaderCallback)
	 */
	public void moveDirectory(final String fromDirectory,
			final String toDirectory, final MoveDirectoryCallback callback) {

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
			message.what = MOVE_ORDER;
			message.obj = new LoaderInfo(fromDirectory, toDirectory, callback);

			this.mLoaderHandler.sendMessage(message);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.HandlerThread#quit()
	 */
	@Override
	public boolean quit() {
		if (this.mLoaderHandler != null) {
			this.mLoaderHandler.removeMessages(MOVE_ORDER);
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
}
