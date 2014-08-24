package fr.ornidroid.ui.components;

import java.io.IOException;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import fr.ornidroid.R;
import fr.ornidroid.bo.AudioOrnidroidFile;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.ui.NewBirdActivity;

/**
 * The Class AudioFragment.
 */
public class AudioFragment extends AbstractFragment implements OnClickListener {
	/** The player paused. */
	private boolean mPlayerPaused = false;

	/** The m audio layout. */
	private LinearLayout mAudioLayout;
	/** The play pause button. */
	private ImageView mPlayPauseButton;

	/** The m list view. */
	private ListView mListView;

	/** The m stop button. */
	private ImageView mStopButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.components.AbstractFragment#getOnCreateView(android.view
	 * .LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View getOnCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mAudioLayout = (LinearLayout) inflater.inflate(R.layout.fragment_audio,
				container, false);
		try {
			loadMediaFilesLocally();

			LinearLayout headerLayout = (LinearLayout) mAudioLayout
					.findViewById(R.id.audio_header);

			headerLayout.addView(createAudioControlView());

			mListView = (ListView) mAudioLayout.findViewById(R.id.list_audio);

			final SimpleAdapter adapter = new SimpleAdapter(this.getActivity(),
					this.ornidroidService.getCurrentBird().getListAudioFiles(),
					R.layout.audio_list, new String[] {
							AudioOrnidroidFile.LINE_1,
							AudioOrnidroidFile.LINE_2 }, new int[] {
							R.id.audio_line1, R.id.audio_line2 });
			this.mListView.setAdapter(adapter);

			this.mListView.setTextFilterEnabled(true);

			this.mListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(final AdapterView<?> parent,
						final View view, final int position, final long id) {
					AudioFragment.this.spinThatShit(position);
				}
			});

		} catch (final OrnidroidException e) {
			Toast.makeText(
					getActivity(),
					"Error reading media files of bird "
							+ this.ornidroidService.getCurrentBird().getTaxon()
							+ " e", Toast.LENGTH_LONG).show();
		}
		return mAudioLayout;
	}

	/**
	 * Creates the audio control view.
	 * 
	 * @return the view
	 */
	private View createAudioControlView() {
		// TODO : faire tout ça en XML ?
		final LinearLayout audioLayout = new LinearLayout(getActivity());
		audioLayout.setOrientation(LinearLayout.VERTICAL);
		audioLayout.setPadding(0, 25, 25, 10);
		final LinearLayout customMediaButtonsLayout = new LinearLayout(
				getActivity());
		customMediaButtonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		customMediaButtonsLayout.setGravity(Gravity.RIGHT);

		final LinearLayout audioControlLayout = new LinearLayout(getActivity());
		audioControlLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		if (this.ornidroidService.getCurrentBird().getNumberOfSounds() > 0) {
			customMediaButtonsLayout.addView(this.removeCustomAudioButton);
			this.removeCustomAudioButton.setVisibility(View.GONE);
			customMediaButtonsLayout.addView(this.addCustomAudioButton);
			if (!Constants.getAutomaticUpdateCheckPreference()) {
				customMediaButtonsLayout.addView(this.getUpdateFilesButton());
			} else {
				this.checkForUpdates(false);
			}

			audioControlLayout.setPadding(0, 5, 0, 5);
			audioControlLayout.setOrientation(LinearLayout.HORIZONTAL);

			mPlayPauseButton = new ImageView(this.getActivity());
			mPlayPauseButton.setImageResource(R.drawable.ic_sound_play);
			mPlayPauseButton.setOnClickListener(this);
			mPlayPauseButton.setPadding(0, 0, 25, 0);
			audioControlLayout.addView(mPlayPauseButton);

			mStopButton = new ImageView(getActivity());
			mStopButton.setImageResource(R.drawable.ic_sound_stop);
			mStopButton.setOnClickListener(this);
			audioControlLayout.addView(mStopButton);
		} else {
			printDownloadButtonAndInfo();
		}
		audioLayout.addView(customMediaButtonsLayout);
		audioLayout.addView(audioControlLayout);
		return audioLayout;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.components.AbstractFragment#getSpecificContentLayout()
	 */
	@Override
	protected LinearLayout getSpecificContentLayout() {

		return mAudioLayout;
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
				((NewBirdActivity) getActivity()).getMediaPlayer().reset();

				((NewBirdActivity) getActivity()).getMediaPlayer()
						.setDataSource(mp3File.getPath());
				((NewBirdActivity) getActivity()).getMediaPlayer().prepare();
				((NewBirdActivity) getActivity()).getMediaPlayer().start();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.components.AbstractFragment#onClick(android.view.View)
	 */
	public void onClick(final View v) {
		if (v == mStopButton) {
			stopPlayer();
			mPlayerPaused = false;
			changePlayPauseButton(true);
		}
		if (v == mPlayPauseButton) {
			if (((NewBirdActivity) getActivity()).getMediaPlayer().isPlaying()) {
				// Log.d(Constants.LOG_TAG, "Player was playing. Pausing");
				((NewBirdActivity) getActivity()).getMediaPlayer().pause();
				mPlayerPaused = true;
				togglePlayPauseButton();

			} else {
				// Log.d(Constants.LOG_TAG,
				// "Player was paused or stop. Playing now");
				try {
					if (mPlayerPaused) {
						// Log.d(Constants.LOG_TAG,
						// "Player was paused. Resuming");
						((NewBirdActivity) getActivity()).getMediaPlayer()
								.start();
					} else {
						// try to launch the first mp3 of the list
						// Log.d(Constants.LOG_TAG,
						// "Player was stopped. Trying to play the first item");
						spinThatShit(0);
					}
					mPlayerPaused = false;
					togglePlayPauseButton();
				} catch (final IllegalStateException e) {
					// Log.e(Constants.LOG_TAG, "Error with play/pause", e);

				}

			}

		}
		super.onClick(v);
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
			mPlayPauseButton.setImageResource(R.drawable.ic_sound_play);
		} else {
			mPlayPauseButton.setImageResource(R.drawable.ic_sound_pause);

		}
	}

	/**
	 * Stop player.
	 */
	public void stopPlayer() {
		try {
			if ((null != ((NewBirdActivity) getActivity()).getMediaPlayer())
					&& ((NewBirdActivity) getActivity()).getMediaPlayer()
							.isPlaying()) {
				((NewBirdActivity) getActivity()).getMediaPlayer().stop();
				((NewBirdActivity) getActivity()).getMediaPlayer().reset();
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
