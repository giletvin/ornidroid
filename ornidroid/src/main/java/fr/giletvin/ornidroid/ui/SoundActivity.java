package fr.giletvin.ornidroid.ui;

import java.io.IOException;

import android.media.MediaPlayer;
import android.util.Log;
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
import fr.giletvin.ornidroid.helper.Constants;

/**
 * The Class SoundActivity.
 */
public class SoundActivity extends AbstractDownloadableMediaActivity {

	/** The Constant PLAY_PAUSE_BUTTON. */
	private static final int PLAY_PAUSE_BUTTON = 0;

	/** The Constant STOP_BUTTON. */
	private static final int STOP_BUTTON = 1;

	/** The m list view. */
	private ListView mListView;

	/** The media player. */
	private MediaPlayer mediaPlayer;

	/** The play pause button. */
	private ImageView playPauseButton;

	private LinearLayout audioControlLayout;

	/**
	 * Instantiates a new sound activity.
	 */
	public SoundActivity() {
		super();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.ui.AbstractDownloadableMediaActivity#hookOnCreate()
	 */
	@Override
	protected void hookOnCreate() {

		mListView = new ListView(this);

		SimpleAdapter adapter = new SimpleAdapter(this, getBird()
				.getListAudioFiles(), R.layout.audio_list, new String[] {
				AudioOrnidroidFile.LINE_1, AudioOrnidroidFile.LINE_2 },
				new int[] { R.id.audio_line1, R.id.audio_line2 });
		mListView.setAdapter(adapter);

		mListView.setTextFilterEnabled(true);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				spinThatShit(position);
			}
		});

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		linearLayout.addView(createAudioControlView());
		linearLayout.addView(mListView);

		setContentView(linearLayout);

	}

	/**
	 * Creates the audio control view to display play/pause stop buttons.
	 * 
	 * @return the layout which displays play/pause stop buttons
	 */
	private View createAudioControlView() {
		audioControlLayout = new LinearLayout(this);
		audioControlLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		if (getBird().getNumberOfSounds() > 0) {
			audioControlLayout.setPadding(0, 25, 0, 25);
			audioControlLayout.setOrientation(LinearLayout.HORIZONTAL);

			playPauseButton = new ImageView(this);
			playPauseButton.setId(PLAY_PAUSE_BUTTON);
			playPauseButton.setImageResource(R.drawable.ic_sound_play);
			playPauseButton
					.setOnClickListener(new AudioControlButtonListener());
			playPauseButton.setPadding(0, 0, 25, 0);
			audioControlLayout.addView(playPauseButton);

			ImageView stopButton = new ImageView(this);
			stopButton.setId(STOP_BUTTON);
			stopButton.setImageResource(R.drawable.ic_sound_stop);
			stopButton.setOnClickListener(new AudioControlButtonListener());
			audioControlLayout.addView(stopButton);
		} else {
			printDownloadButtonAndInfo();
		}
		return audioControlLayout;
	}

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
		public void onClick(View v) {
			if (v.getId() == STOP_BUTTON) {
				stopPlayer();
				playerPaused = false;
				changePlayPauseButton(true);
			}
			if (v.getId() == PLAY_PAUSE_BUTTON) {
				if (mediaPlayer.isPlaying()) {
					Log.d(Constants.LOG_TAG, "Player was playing. Pausing");
					mediaPlayer.pause();
					playerPaused = true;
					togglePlayPauseButton();

				} else {
					Log.d(Constants.LOG_TAG,
							"Player was paused or stop. Playing now");
					try {
						if (playerPaused) {
							Log.d(Constants.LOG_TAG,
									"Player was paused. Resuming");
							mediaPlayer.start();
						} else {
							// try to launch the first mp3 of the list
							Log.d(Constants.LOG_TAG,
									"Player was stopped. Trying to play the first item");
							spinThatShit(0);
						}
						playerPaused = false;
						togglePlayPauseButton();
					} catch (IllegalStateException e) {
						Log.e(Constants.LOG_TAG, "Error with play/pause", e);

					}

				}

			}
		}

		/**
		 * Toggle play pause button according to the status of the media player.
		 */
		private void togglePlayPauseButton() {
			if (playerPaused) {
				changePlayPauseButton(true);
			} else {
				changePlayPauseButton(false);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		mediaPlayer.release();
	}

	/**
	 * Change play pause button.
	 * 
	 * @param printPlayIcon
	 *            the print play icon : if true, print play icon, if false,
	 *            pause button
	 */
	public void changePlayPauseButton(boolean printPlayIcon) {
		if (printPlayIcon) {
			playPauseButton.setImageResource(R.drawable.ic_sound_play);
		} else {
			playPauseButton.setImageResource(R.drawable.ic_sound_pause);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		mediaPlayer = new MediaPlayer();
	}

	/**
	 * Spin that shit.
	 * 
	 * @param position
	 *            the position
	 */
	private void spinThatShit(int position) {
		Log.i(Constants.LOG_TAG, "Reading mp3 number " + position);
		AbstractOrnidroidFile mp3File = getBird().getSound(position);
		if (null != mp3File) {
			try {
				mediaPlayer.reset();
				mediaPlayer.setDataSource(mp3File.getPath());
				mediaPlayer.prepare();
				mediaPlayer.start();
				changePlayPauseButton(false);
			} catch (IOException e) {
				Log.e(Constants.LOG_TAG, "Could not open file " + mp3File
						+ " for playback.", e);
			} catch (IllegalArgumentException e2) {
				Log.e(Constants.LOG_TAG, "Error on setDataSource " + mp3File
						+ " for playback.", e2);
			}
		}
	}

	/**
	 * Stop player.
	 */
	protected void stopPlayer() {
		try {
			if (null != mediaPlayer && mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				mediaPlayer.reset();
			}
		} catch (IllegalStateException e) {
			Log.w(Constants.LOG_TAG,
					"Illegal State on the media player while trying to stop it");
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
		return audioControlLayout;
	}

}
