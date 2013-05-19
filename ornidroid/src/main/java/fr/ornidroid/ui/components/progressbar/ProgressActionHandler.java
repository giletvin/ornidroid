package fr.ornidroid.ui.components.progressbar;


/**
 * The Interface ProgressActionHandler.
 */
public interface ProgressActionHandler {
	/**
	 * Callback to notify state changes.
	 * 
	 */
	public static interface ProgressActionCallback {

		/**
		 * Do action. Action that should be done while the progress bar is
		 * printed.
		 */
		void doAction();

		/**
		 * On action ended.
		 * 
		 * @param loader
		 *            the loader
		 * @param info
		 *            the info
		 */
		void onActionEnded(ProgressActionHandler loader, ProgressActionLoaderInfo info);

	}

	/**
	 * Start action.
	 * 
	 * @param callback
	 *            the callback
	 */
	public void startAction(ProgressActionCallback callback);
}
