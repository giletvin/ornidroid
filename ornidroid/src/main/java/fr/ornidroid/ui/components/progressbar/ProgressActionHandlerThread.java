package fr.ornidroid.ui.components.progressbar;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import fr.ornidroid.helper.Constants;

/**
 * The Class ProgressActionHandlerThread.
 */
public class ProgressActionHandlerThread extends HandlerThread implements
		ProgressActionHandler {

	/** The Constant ACTION_ENDED. */
	private static final int ACTION_ENDED = 4;

	/**
	 * "what" id used by the Handler.
	 */
	private static final int ACTION_ORDER = 1;

	/** The m loader callback. */
	private final Callback mLoaderCallback = new Callback() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
		 */
		public boolean handleMessage(final Message msg) {
			switch (msg.what) {
			case ACTION_ORDER:
				ProgressActionHandlerThread.this.mLoaderHandler
						.removeMessages(ACTION_ORDER);

				final Handler h = ProgressActionHandlerThread.this.mMainHandler;

				final ProgressActionLoaderInfo loaderInfo = (ProgressActionLoaderInfo) msg.obj;
				if (loaderInfo == null) {
					return false;
				}

				try {
					// call the specific action code
					loaderInfo.callback.doAction();
				} catch (final Exception e) {
					// Error occured : keep the exception in the loader info
					loaderInfo.setException(e);

				} finally {
					// always send a stop signal
					Message.obtain(h, ACTION_ENDED, loaderInfo).sendToTarget();
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

			if (ProgressActionHandlerThread.this.mLoaderHandler == null) {
				// Looper has been stopped
				return false;
			}

			final ProgressActionLoaderInfo loaderInfo = (ProgressActionLoaderInfo) msg.obj;
			if (loaderInfo == null) {
				// this should not occur
				return false;
			}

			final ProgressActionCallback callback = loaderInfo.callback;
			if (callback == null) {
				// this should not occur
				return false;
			}

			switch (msg.what) {

			case ACTION_ENDED:
				callback.onActionEnded(ProgressActionHandlerThread.this,
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

	/**
	 * Instantiates a new handler thread search query.
	 */
	public ProgressActionHandlerThread() {
		super(Constants.LOG_TAG, Process.THREAD_PRIORITY_BACKGROUND);
		this.mMainHandler = new Handler(Looper.getMainLooper(),
				this.mMainCallback);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.HandlerThread#quit()
	 */
	@Override
	public boolean quit() {
		if (this.mLoaderHandler != null) {
			this.mLoaderHandler.removeMessages(ACTION_ORDER);
			this.mLoaderHandler = null;
		}
		return super.quit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.multicriteriasearch.ProgressDialogHandler#doSomething
	 * (fr.ornidroid
	 * .ui.multicriteriasearch.ProgressDialogHandler.ProgressDialogCallback)
	 */
	public void startAction(final ProgressActionCallback callback) {
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
			message.what = ACTION_ORDER;
			message.obj = new ProgressActionLoaderInfo(callback);

			this.mLoaderHandler.sendMessage(message);
		}

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
