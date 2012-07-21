package fr.giletvin.ornidroid.ui;

import java.io.IOException;

import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import fr.giletvin.ornidroid.R;
import fr.giletvin.ornidroid.bo.AbstractOrnidroidFile;
import fr.giletvin.ornidroid.bo.AudioOrnidroidFile;
import fr.giletvin.ornidroid.bo.OrnidroidFileType;

/**
 * The Class SoundActivity.
 */
public class SoundActivity extends AbstractDownloadableMediaActivity {

	/**
	 * The listener interface for receiving audioControlButton events. The class
	 * that is interested in processing a audioControlButton event implements
	 * this interface, and the object created with that class is registered with
	 * a component using the component's
	 * <code>addAudioControlButtonListener<code> method. When
	 * the audioControlButton event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see AudioControlButtonEvent
	 */
	private class AudioControlButtonListener implements OnClickListener {

		/** The player paused. */
		private boolean playerPaused = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		public void onClick(final View v) {
			if (v.getId() == STOP_BUTTON) {
				stopPlayer();
				this.playerPaused = false;
				changePlayPauseButton(true);
			}
			if (v.getId() == PLAY_PAUSE_BUTTON) {
				if (SoundActivity.this.mediaPlayer.isPlaying()) {
					// Log.d(Constants.LOG_TAG, "Player was playing. Pausing");
					SoundActivity.this.mediaPlayer.pause();
					this.playerPaused = true;
					togglePlayPauseButton();

				} else {
					// Log.d(Constants.LOG_TAG,
					// "Player was paused or stop. Playing now");
					try {
						if (this.playerPaused) {
							// Log.d(Constants.LOG_TAG,
							// "Player was paused. Resuming");
							SoundActivity.this.mediaPlayer.start();
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

	/** The Constant PLAY_PAUSE_BUTTON. */
	private static final int PLAY_PAUSE_BUTTON = 0;

	/** The Constant STOP_BUTTON. */
	private static final int STOP_BUTTON = 1;

	private LinearLayout audioControlLayout;

	/** The media player. */
	private MediaPlayer mediaPlayer;

	/** The m list view. */
	private ListView mListView;

	/** The play pause button. */
	private ImageView playPauseButton;

	/**
	 * Instantiates a new sound activity.
	 */
	public SoundActivity() {
		super();

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
			this.playPauseButton.setImageResource(R.drawable.ic_sound_play);
		} else {
			this.playPauseButton.setImageResource(R.drawable.ic_sound_pause);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.ui.AbstractDownloadableMediaActivity#getFileType()
	 */
	@Override
	public OrnidroidFileType getFileType() {
		return OrnidroidFileType.AUDIO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.giletvin.ornidroid.ui.AbstractDownloadableMediaActivity#
	 * getSpecificContentLayout()
	 */
	@Override
	protected LinearLayout getSpecificContentLayout() {
		return this.audioControlLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.ui.AbstractDownloadableMediaActivity#hookOnCreate()
	 */
	@Override
	protected void hookOnCreate() {

		this.mListView = new ListView(this);

		final SimpleAdapter adapter = new SimpleAdapter(this, getBird()
				.getListAudioFiles(), R.layout.audio_list, new String[] {
				AudioOrnidroidFile.LINE_1, AudioOrnidroidFile.LINE_2 },
				new int[] { R.id.audio_line1, R.id.audio_line2 });
		this.mListView.setAdapter(adapter);

		this.mListView.setTextFilterEnabled(true);

		this.mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				spinThatShit(position);
			}
		});

		final LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		linearLayout.addView(createAudioControlView());
		linearLayout.addView(this.mListView);

		setContentView(linearLayout);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		this.mediaPlayer = new MediaPlayer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		this.mediaPlayer.release();
	}

	/**
	 * Stop player.
	 */
	protected void stopPlayer() {
		try {
			if ((null != this.mediaPlayer) && this.mediaPlayer.isPlaying()) {
				this.mediaPlayer.stop();
				this.mediaPlayer.reset();
			}
		} catch (final IllegalStateException e) {
			// Log.w(Constants.LOG_TAG,
			// "Illegal State on the media player while trying to stop it");
		}
	}

	/**
	 * Creates the audio control view to display play/pause stop buttons.
	 * 
	 * @return the layout which displays play/pause stop buttons
	 */
	private View createAudioControlView() {
		this.audioControlLayout = new LinearLayout(this);
		this.audioControlLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		if (getBird().getNumberOfSounds() > 0) {
			this.audioControlLayout.setPadding(0, 25, 0, 25);
			this.audioControlLayout.setOrientation(LinearLayout.HORIZONTAL);

			this.playPauseButton = new ImageView(this);
			this.playPauseButton.setId(PLAY_PAUSE_BUTTON);
			this.playPauseButton.setImageResource(R.drawable.ic_sound_play);
			this.playPauseButton
					.setOnClickListener(new AudioControlButtonListener());
			this.playPauseButton.setPadding(0, 0, 25, 0);
			this.audioControlLayout.addView(this.playPauseButton);

			final ImageView stopButton = new ImageView(this);
			stopButton.setId(STOP_BUTTON);
			stopButton.setImageResource(R.drawable.ic_sound_stop);
			stopButton.setOnClickListener(new AudioControlButtonListener());
			this.audioControlLayout.addView(stopButton);
		} else {
			printDownloadButtonAndInfo();
		}
		return this.audioControlLayout;
	}

	/**
	 * Spin that shit.
	 * 
	 * @param position
	 *            the position
	 */
	private void spinThatShit(final int position) {
		// Log.i(Constants.LOG_TAG, "Reading mp3 number " + position);
		final AbstractOrnidroidFile mp3File = getBird().getSound(position);
		if (null != mp3File) {
			try {
				this.mediaPlayer.reset();
				this.mediaPlayer.setDataSource(mp3File.getPath());
				this.mediaPlayer.prepare();
				this.mediaPlayer.start();
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

}
