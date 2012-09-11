package fr.ornidroid.ui.audio;

import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;
import fr.ornidroid.ui.BirdActivity;

/**
 * The listener interface for receiving audioControlButton events. The class
 * that is interested in processing a audioControlButton event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addAudioControlButtonListener<code> method. When
 * the audioControlButton event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see AudioControlButtonEvent
 */
public class AudioControlButtonListener implements OnClickListener {

	private final BirdActivity activity;
	private final MediaPlayer mediaPlayer;

	/** The player paused. */
	private boolean playerPaused = false;

	public AudioControlButtonListener(final BirdActivity pActivity) {
		this.activity = pActivity;
		this.mediaPlayer = this.activity.getMediaPlayer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(final View v) {
		if (v.getId() == BirdActivity.STOP_BUTTON) {
			this.activity.stopPlayer();
			this.playerPaused = false;
			this.activity.changePlayPauseButton(true);
		}
		if (v.getId() == BirdActivity.PLAY_PAUSE_BUTTON) {
			if (this.mediaPlayer.isPlaying()) {
				// Log.d(Constants.LOG_TAG, "Player was playing. Pausing");
				this.mediaPlayer.pause();
				this.playerPaused = true;
				togglePlayPauseButton();

			} else {
				// Log.d(Constants.LOG_TAG,
				// "Player was paused or stop. Playing now");
				try {
					if (this.playerPaused) {
						// Log.d(Constants.LOG_TAG,
						// "Player was paused. Resuming");
						this.mediaPlayer.start();
					} else {
						// try to launch the first mp3 of the list
						// Log.d(Constants.LOG_TAG,
						// "Player was stopped. Trying to play the first item");
						this.activity.spinThatShit(0);
					}
					this.playerPaused = false;
					togglePlayPauseButton();
				} catch (final IllegalStateException e) {
					// Log.e(Constants.LOG_TAG, "Error with play/pause", e);

				}

			}

		}
	}

	/**
	 * Toggle play pause button according to the status of the media player.
	 */
	private void togglePlayPauseButton() {
		if (this.playerPaused) {
			this.activity.changePlayPauseButton(true);
		} else {
			this.activity.changePlayPauseButton(false);
		}
	}
}