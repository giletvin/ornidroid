package fr.ornidroid.ui.threads;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import fr.ornidroid.helper.Constants;

/**
 * The Class HandlerGenericThread.
 */
public abstract class HandlerGenericThread extends HandlerThread implements
		GenericTaskHandler {

	/** The Constant TASK_ENDED. */
	private static final int TASK_ENDED = 4;

	/** The Constant TASK_ORDER. */
	private static final int TASK_ORDER = 1;

	/** The m loader callback. */
	private final Callback mLoaderCallback = new Callback() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
		 */
		public boolean handleMessage(final Message msg) {
			switch (msg.what) {
			case TASK_ORDER:
				HandlerGenericThread.this.mLoaderHandler
						.removeMessages(TASK_ORDER);

				final Handler h = HandlerGenericThread.this.mMainHandler;

				final LoaderInfo loaderInfo = (LoaderInfo) msg.obj;
				if (loaderInfo == null) {
					return false;
				}

				try {
					// ICI LE CODE METIER
					doTask(loaderInfo);

				} catch (final Exception e) {
					// Error occured : keep the exception in the loader info
					loaderInfo.setException(e);

				} finally {
					// always send a stop signal
					Message.obtain(h, TASK_ENDED, loaderInfo).sendToTarget();
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

			if (HandlerGenericThread.this.mLoaderHandler == null) {
				// Looper has been stopped
				return false;
			}

			final LoaderInfo loaderInfo = (LoaderInfo) msg.obj;
			if (loaderInfo == null) {
				// this should not occur
				return false;
			}

			final GenericTaskCallback callback = loaderInfo.getCallback();
			if (callback == null) {
				// this should not occur
				return false;
			}

			switch (msg.what) {

			case TASK_ENDED:
				callback.onTaskEnded(HandlerGenericThread.this, loaderInfo);
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
	 * Instantiates a new handler thread update files.
	 * 
	 */
	public HandlerGenericThread() {

		super(Constants.LOG_TAG, Process.THREAD_PRIORITY_BACKGROUND);
		this.mMainHandler = new Handler(Looper.getMainLooper(),
				this.mMainCallback);
	}

	/**
	 * Do task.
	 * 
	 * @param loaderInfo
	 *            the loader info
	 */
	protected abstract void doTask(LoaderInfo loaderInfo);

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.HandlerThread#quit()
	 */
	@Override
	public boolean quit() {
		if (this.mLoaderHandler != null) {
			this.mLoaderHandler.removeMessages(TASK_ORDER);
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

	/**
	 * Generic task.
	 * 
	 * @param callback
	 *            the callback
	 */
	public void genericTask(GenericTaskCallback callback) {
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
			message.what = TASK_ORDER;
			message.obj = getLoaderInfo(callback);

			this.mLoaderHandler.sendMessage(message);
		}

	}

	/**
	 * Gets the loader info.
	 * 
	 * @param callback
	 *            the callback
	 * 
	 * @return the loader info
	 */
	protected abstract LoaderInfo getLoaderInfo(GenericTaskCallback callback);
}
