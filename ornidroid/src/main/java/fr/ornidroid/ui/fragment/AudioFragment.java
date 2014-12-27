package fr.ornidroid.ui.fragment;

import java.io.IOException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import fr.ornidroid.R;
import fr.ornidroid.bo.AudioOrnidroidFile;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.ui.activity.NewBirdActivity;

/**
 * The Class AudioFragment.
 */
@EFragment(R.layout.fragment_audio)
public class AudioFragment extends AbstractFragment {
	/** The player paused. */
	private boolean mPlayerPaused = false;

	/** The seek bar. */
	@ViewById(R.id.SeekBar01)
	SeekBar seekBar;

	/** The play pause button. */
	@ViewById(R.id.iv_play_button)
	ImageView mPlayPauseButton;
	@ViewById(R.id.iv_pause_button)
	ImageView mPauseButton;
	@ViewById(R.id.iv_stop_button)
	/** The m stop button. */
	ImageView mStopButton;

	/** The m list view. */
	@ViewById(R.id.list_audio)
	ListView mListView;

	/** The handler. */
	private final Handler seekBarHandler = new Handler();

	@AfterViews
	void afterViews() {
		if (commonAfterViews()) {
			final SimpleAdapter adapter = new SimpleAdapter(this.getActivity(),
					this.ornidroidService.getCurrentBird().getListAudioFiles(),
					R.layout.audio_list, new String[] {
							AudioOrnidroidFile.LINE_1,
							AudioOrnidroidFile.LINE_2 }, new int[] {
							R.id.audio_line1, R.id.audio_line2 });
			this.mListView.setAdapter(adapter);
			this.mListView.setTextFilterEnabled(true);
		}
	}

	@Touch(R.id.SeekBar01)
	void seekBarTouched() {
		// TODO : tester la seek bar.
		getMediaPlayer().seekTo(seekBar.getProgress());
	}

	@ItemClick(R.id.list_audio)
	void listAudioItemClick(int position) {
		spinThatShit(position);
	}

	/**
	 * Gets the media player.
	 * 
	 * @return the media player
	 */
	private MediaPlayer getMediaPlayer() {
		if (getActivity() != null) {
			return ((NewBirdActivity) getActivity()).getMediaPlayer();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.ui.components.AbstractFragment#getFileType()
	 */
	@Override
	public OrnidroidFileType getFileType() {
		return OrnidroidFileType.AUDIO;
	}

	/**
	 * Spin that shit. Plays the mp3 at the given position.
	 * 
	 * @param position
	 *            the position
	 */
	public void spinThatShit(final int position) {
		// Log.i(Constants.LOG_TAG, "Reading mp3 number " + position);
		final OrnidroidFile mp3File = ornidroidService.getCurrentBird()
				.getSound(position);
		setCurrentMediaFile(mp3File);

		if (null != mp3File) {
			try {
				getMediaPlayer().reset();

				getMediaPlayer().setDataSource(mp3File.getPath());
				getMediaPlayer().prepare();
				getMediaPlayer().start();
				seekBar.setMax(getMediaPlayer().getDuration());
				startPlayProgressUpdater();
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
	 * Start play progress updater.
	 */
	private void startPlayProgressUpdater() {
		try {
			if (getMediaPlayer() != null) {

				if (getMediaPlayer().isPlaying() || mPlayerPaused) {
					seekBar.setProgress(getMediaPlayer().getCurrentPosition());
					Runnable notification = new Runnable() {
						public void run() {
							startPlayProgressUpdater();
						}
					};
					seekBarHandler.postDelayed(notification, 1000);
				}
			}
		} catch (IllegalStateException illegalStateException) {
			// under the carpet
		}
	}

	@Click(R.id.iv_stop_button)
	void stopButtonClicked() {
		stopPlayer();
		mPlayerPaused = false;
		changePlayPauseButton(true);
	}

	@Click({ R.id.iv_play_button, R.id.iv_pause_button })
	void playPauseButtonClicked() {
		if (getMediaPlayer().isPlaying()) {
			// Log.d(Constants.LOG_TAG, "Player was playing. Pausing");
			getMediaPlayer().pause();
			mPlayerPaused = true;
			togglePlayPauseButton();

		} else {
			// Log.d(Constants.LOG_TAG,
			// "Player was paused or stop. Playing now");
			try {
				if (mPlayerPaused) {
					// Log.d(Constants.LOG_TAG,
					// "Player was paused. Resuming");
					getMediaPlayer().start();
				} else {
					// try to launch the first mp3 of the list
					// Log.d(Constants.LOG_TAG,
					// "Player was stopped. Trying to play the first item");
					spinThatShit(0);
					mListView
							.performItemClick(
									mListView.getAdapter().getView(0, null,
											null), 0, 0);
					mListView.requestFocusFromTouch();
					mListView.setSelection(0);

				}
				mPlayerPaused = false;
				togglePlayPauseButton();
			} catch (final IllegalStateException e) {
				// Log.e(Constants.LOG_TAG, "Error with play/pause", e);

			}

		}
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
			mPlayPauseButton.setVisibility(View.VISIBLE);
			mPauseButton.setVisibility(View.GONE);
		} else {
			mPlayPauseButton.setVisibility(View.GONE);
			mPauseButton.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Stop player.
	 */
	public void stopPlayer() {
		try {
			if ((null != getMediaPlayer()) && getMediaPlayer().isPlaying()) {
				getMediaPlayer().stop();
				getMediaPlayer().reset();
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
		if (mPlayerPaused) {
			changePlayPauseButton(true);
		} else {
			changePlayPauseButton(false);
		}
	}
}
