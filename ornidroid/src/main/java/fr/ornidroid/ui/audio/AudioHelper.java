package fr.ornidroid.ui.audio;

import java.io.IOException;

import android.view.View;
import android.view.View.OnClickListener;
import fr.ornidroid.R;
import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.ui.BirdActivity;

/**
 * This class handles the audio stuff of the BirdActivity. MediaPlayer,
 * play/pause the mp3 files, toggles the buttons, etc ...
 */
public class AudioHelper implements OnClickListener {

	/** The bird activity. */
	private final BirdActivity birdActivity;

	/** The player paused. */
	private boolean playerPaused = false;

	/**
	 * Instantiates a new audio helper.
	 * 
	 * @param pActivity
	 *            the activity
	 */
	public AudioHelper(final BirdActivity pActivity) {
		this.birdActivity = pActivity;

	}

	/**
	 * Change play pause button.
	 * 
	 * @param printPlayIcon
	 *            the print play icon : if true, print play icon, if false,
	 *            pause button
	 */
	public void changePlayPauseButton(final boolean printPlayIcon) {
		if (printPlayIcon) {
			this.birdActivity.getPlayPauseButton().setImageResource(
					R.drawable.ic_sound_play);
		} else {
			this.birdActivity.getPlayPauseButton().setImageResource(
					R.drawable.ic_sound_pause);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(final View v) {
		if (v.getId() == BirdActivity.STOP_BUTTON) {
			stopPlayer();
			this.playerPaused = false;
			changePlayPauseButton(true);
		}
		if (v.getId() == BirdActivity.PLAY_PAUSE_BUTTON) {
			if (this.birdActivity.getMediaPlayer().isPlaying()) {
				// Log.d(Constants.LOG_TAG, "Player was playing. Pausing");
				this.birdActivity.getMediaPlayer().pause();
				this.playerPaused = true;
				togglePlayPauseButton();

			} else {
				// Log.d(Constants.LOG_TAG,
				// "Player was paused or stop. Playing now");
				try {
					if (this.playerPaused) {
						// Log.d(Constants.LOG_TAG,
						// "Player was paused. Resuming");
						this.birdActivity.getMediaPlayer().start();
					} else {
						// try to launch the first mp3 of the list
						// Log.d(Constants.LOG_TAG,
						// "Player was stopped. Trying to play the first item");
						spinThatShit(0);
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
	 * Spin that shit. Plays the mp3 at the given position.
	 * 
	 * @param position
	 *            the position
	 */
	public void spinThatShit(final int position) {
		// Log.i(Constants.LOG_TAG, "Reading mp3 number " + position);
		final AbstractOrnidroidFile mp3File = this.birdActivity
				.getOrnidroidService().getCurrentBird().getSound(position);
		if (null != mp3File) {
			try {
				this.birdActivity.getMediaPlayer().reset();
				this.birdActivity.getMediaPlayer().setDataSource(
						mp3File.getPath());
				this.birdActivity.getMediaPlayer().prepare();
				this.birdActivity.getMediaPlayer().start();
				changePlayPauseButton(false);
			} catch (final IOException e) {
				// Log.e(Constants.LOG_TAG, "Could not open file " + mp3File
				// + " for playback.", e);
			} catch (final IllegalArgumentException e2) {
				// Log.e(Constants.LOG_TAG, "Error on setDataSource " + mp3File
				// + " for playback.", e2);
			}
		}
	}

	/**
	 * Stop player.
	 */
	public void stopPlayer() {
		try {
			if ((null != this.birdActivity.getMediaPlayer())
					&& this.birdActivity.getMediaPlayer().isPlaying()) {
				this.birdActivity.getMediaPlayer().stop();
				this.birdActivity.getMediaPlayer().reset();
			}
		} catch (final IllegalStateException e) {
			// Log.w(Constants.LOG_TAG,
			// "Illegal State on the media player while trying to stop it");
		}
	}

	/**
	 * Toggle play pause button according to the status of the media player.
	 */
	private void togglePlayPauseButton() {
		if (this.playerPaused) {
			changePlayPauseButton(true);
		} else {
			changePlayPauseButton(false);
		}
	}
}
