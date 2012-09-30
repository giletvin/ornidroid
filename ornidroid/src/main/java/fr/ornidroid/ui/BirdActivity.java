package fr.ornidroid.ui;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.ViewFlipper;
import fr.ornidroid.R;
import fr.ornidroid.bo.AudioOrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.audio.AudioHelper;
import fr.ornidroid.ui.picture.BirdActivityGestureListener;
import fr.ornidroid.ui.picture.PictureHelper;
import fr.ornidroid.ui.views.DetailsViewFactory;
import fr.ornidroid.ui.views.NamesViewFactory;

/**
 * Displays bird details. This is the main activity which contains the tabs.
 * Picture, audio, description and bird names tabs.<br>
 * The tabs are not activities, only views.
 */
public class BirdActivity extends AbstractDownloadableMediaActivity implements
		TabContentFactory, OnClickListener {

	/** The Constant DISPLAYED_PICTURE_ID. */
	public static final String DISPLAYED_PICTURE_ID = "DISPLAYED_PICTURE_ID";

	/** The Constant INTENT_ACTIVITY_TO_OPEN. */
	public static final String INTENT_TAB_TO_OPEN = "intentTabToOpenParameter";

	/** The Constant PICTURES_TAB_NAME. */
	public static final String PICTURES_TAB_NAME = "pictures";
	/** The Constant PLAY_PAUSE_BUTTON. */
	public static final int PLAY_PAUSE_BUTTON = 0;
	/** The Constant STOP_BUTTON. */
	public static final int STOP_BUTTON = 1;
	/** The Constant AUDIO_TAB_NAME. */
	private static final String AUDIO_TAB_NAME = "audioTab";
	/** The Constant BIRD_NAMES_TAB_NAME. */
	private static final String BIRD_NAMES_TAB_NAME = "birdNamesTab";

	/** The Constant DETAIL_TAB_NAME. */
	private static final String DETAIL_TAB_NAME = "detailTab";

	/** The Constant DIALOG_PICTURE_INFO_ID. */
	private static final int DIALOG_PICTURE_INFO_ID = 0;

	/** The Constant TAB_HEIGHT. */
	private static final int TAB_HEIGHT = 50;

	/** The audio control layout. */
	private LinearLayout audioControlLayout;

	/** The audio helper. */
	private final AudioHelper audioHelper;

	/** The audio layout. */
	private LinearLayout audioLayout;

	/** The dialog. */
	private Dialog dialog;
	/** The displayed picture id. */
	private int displayedPictureId;

	/** The gesture detector. */
	private GestureDetector gestureDetector;

	/** The info button. */
	private ImageView infoButton;

	/** The media player. */
	private MediaPlayer mediaPlayer;

	/** The m list view. */
	private ListView mListView;

	/** The number of pictures text view. */
	private TextView numberOfPicturesTextView;

	/** The ok dialog button. */
	private Button okDialogButton;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The picture helper. */
	private final PictureHelper pictureHelper;

	/** The picture layout. */
	private LinearLayout pictureLayout;

	/** The play pause button. */
	private ImageView playPauseButton;

	/** The tab id to display. */
	private int tabIdToDisplay;

	/** The progress bar. */

	private TabHost tabs;

	/** The taxon. */
	private TextView taxon;

	/** The view flipper. */
	private ViewFlipper viewFlipper;

	/** The gesture listener. */
	View.OnTouchListener gestureListener;

	/**
	 * Instantiates a new bird detail activity.
	 */
	public BirdActivity() {
		this.ornidroidService = OrnidroidServiceFactory.getService(this);
		this.audioHelper = new AudioHelper(this);
		this.pictureHelper = new PictureHelper(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String
	 * )
	 */
	public View createTabContent(final String tabName) {

		if (AUDIO_TAB_NAME.equals(tabName)) {
			this.audioLayout = new LinearLayout(this);
			try {
				loadMediaFilesLocally();
			} catch (final OrnidroidException e) {
				// Log.e(Constants.LOG_TAG, "Error reading media files of bird "
				// + this.bird.getTaxon() + " e");
			}
			this.mListView = new ListView(this);

			final SimpleAdapter adapter = new SimpleAdapter(this,
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
					BirdActivity.this.audioHelper.spinThatShit(position);
				}
			});

			this.audioLayout.setOrientation(LinearLayout.VERTICAL);

			this.audioLayout.addView(createAudioControlView());
			this.audioLayout.addView(this.mListView);
			return this.audioLayout;

		} else {
			try {
				loadMediaFilesLocally();
			} catch (final OrnidroidException e) {
				// Log.e(Constants.LOG_TAG, "Error reading media files of bird "
				// + this.bird.getTaxon() + " e");
			}
			this.pictureLayout = new LinearLayout(this);

			// retrieve the displayed picture (when coming back from the
			// zoom)
			this.displayedPictureId = getIntent().getIntExtra(
					DISPLAYED_PICTURE_ID, 0);

			this.pictureLayout.setOrientation(LinearLayout.VERTICAL);

			this.pictureLayout.addView(createHeaderView());

			this.taxon.setText(getBird().getTaxon());

			this.viewFlipper = new ViewFlipper(this);
			this.pictureLayout.addView(this.viewFlipper);

			this.viewFlipper.setInAnimation(this, android.R.anim.fade_in);
			this.viewFlipper.setOutAnimation(this, android.R.anim.fade_out);

			this.pictureHelper.populateViewFlipper();
			this.gestureDetector = new GestureDetector(
					new BirdActivityGestureListener(this));
			this.gestureListener = new View.OnTouchListener() {
				public boolean onTouch(final View v, final MotionEvent event) {
					if (BirdActivity.this.gestureDetector.onTouchEvent(event)) {
						return true;
					}
					return false;
				}
			};
			return this.pictureLayout;

		}

	}

	/**
	 * Gets the current tab tag.
	 * 
	 * @return the current tab tag
	 */
	public String getCurrentTabTag() {
		return this.tabs.getCurrentTabTag();
	}

	/**
	 * Gets the displayed picture id.
	 * 
	 * @return the displayed picture id
	 */
	public int getDisplayedPictureId() {
		return this.displayedPictureId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.ui.AbstractDownloadableMediaActivity#getFileType()
	 */
	@Override
	public OrnidroidFileType getFileType() {
		if ((null != this.tabs)
				&& AUDIO_TAB_NAME.equals(this.tabs.getCurrentTabTag())) {

			return OrnidroidFileType.AUDIO;
		} else {
			return OrnidroidFileType.PICTURE;
		}
	}

	/**
	 * Gets the media player.
	 * 
	 * @return the media player
	 */
	public MediaPlayer getMediaPlayer() {
		return this.mediaPlayer;
	}

	/**
	 * Gets the number of pictures text view.
	 * 
	 * @return the number of pictures text view
	 */
	public TextView getNumberOfPicturesTextView() {
		return this.numberOfPicturesTextView;
	}

	/**
	 * Gets the ok dialog button.
	 * 
	 * @return the ok dialog button
	 */
	public Button getOkDialogButton() {
		return this.okDialogButton;
	}

	/**
	 * Gets the ornidroid service.
	 * 
	 * @return the ornidroid service
	 */
	public IOrnidroidService getOrnidroidService() {
		return this.ornidroidService;
	}

	/**
	 * Gets the picture helper.
	 * 
	 * @return the picture helper
	 */
	public PictureHelper getPictureHelper() {
		return this.pictureHelper;
	}

	/**
	 * Gets the play pause button.
	 * 
	 * @return the play pause button
	 */
	public ImageView getPlayPauseButton() {
		return this.playPauseButton;
	}

	/**
	 * Gets the view flipper.
	 * 
	 * @return the view flipper
	 */
	public ViewFlipper getViewFlipper() {
		return this.viewFlipper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.AbstractDownloadableMediaActivity#onClick(android.view
	 * .View)
	 */
	@Override
	public void onClick(final View v) {
		super.onClick(v);
		if (v == this.infoButton) {
			this.showDialog(DIALOG_PICTURE_INFO_ID);
		}
		if (v == this.okDialogButton) {
			this.dialog.dismiss();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (this.gestureDetector.onTouchEvent(event)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the displayed picture id.
	 * 
	 * @param displayedPictureId
	 *            the new displayed picture id
	 */
	public void setDisplayedPictureId(final int displayedPictureId) {
		this.displayedPictureId = displayedPictureId;
	}

	/**
	 * Sets the ok dialog button.
	 * 
	 * @param okDialogButton
	 *            the new ok dialog button
	 */
	public void setOkDialogButton(final Button okDialogButton) {
		this.okDialogButton = okDialogButton;
	}

	/**
	 * Sets the view flipper.
	 * 
	 * @param viewFlipper
	 *            the new view flipper
	 */
	public void setViewFlipper(final ViewFlipper viewFlipper) {
		this.viewFlipper = viewFlipper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.AbstractDownloadableMediaActivity#getSpecificContentLayout
	 * ()
	 */
	@Override
	protected LinearLayout getSpecificContentLayout() {
		if (AUDIO_TAB_NAME.equals(BirdActivity.this.tabs.getCurrentTabTag())) {
			return this.audioLayout;
		} else {
			return this.pictureLayout;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void hookOnCreate() {
		setContentView(R.layout.bird);
		this.tabIdToDisplay = getIntent().getIntExtra(INTENT_TAB_TO_OPEN,
				OrnidroidFileType.getCode(OrnidroidFileType.PICTURE));
		this.tabs = (TabHost) this.findViewById(R.id.my_tabhost);
		this.tabs.setup();

		// pictures tab
		final TabSpec tspecPicture = this.tabs.newTabSpec(PICTURES_TAB_NAME);
		tspecPicture.setIndicator(BasicConstants.EMPTY_STRING, getResources()
				.getDrawable(R.drawable.ic_tab_pictures));
		tspecPicture.setContent(this);
		this.tabs.addTab(tspecPicture);

		// audio tab
		final TabSpec tspecAudio = this.tabs.newTabSpec(AUDIO_TAB_NAME);
		tspecAudio.setIndicator(BasicConstants.EMPTY_STRING, getResources()
				.getDrawable(R.drawable.ic_tab_sounds));
		tspecAudio.setContent(this);

		this.tabs.addTab(tspecAudio);

		// details
		final TabSpec tspec1 = this.tabs.newTabSpec(DETAIL_TAB_NAME);
		tspec1.setIndicator(BasicConstants.EMPTY_STRING, getResources()
				.getDrawable(R.drawable.ic_tab_details));
		tspec1.setContent(new TabContentFactory() {

			public View createTabContent(final String tag) {

				return new DetailsViewFactory(BirdActivity.this)
						.createContent();
			}
		});
		this.tabs.addTab(tspec1);

		// bird names
		final TabSpec tspec2 = this.tabs.newTabSpec(BIRD_NAMES_TAB_NAME);
		tspec2.setIndicator(BasicConstants.EMPTY_STRING, getResources()
				.getDrawable(R.drawable.ic_tab_bird_names));
		tspec2.setContent(new TabContentFactory() {

			public View createTabContent(final String tag) {

				return new NamesViewFactory(BirdActivity.this).createContent();
			}
		});
		this.tabs.addTab(tspec2);

		this.tabs.setCurrentTab(this.tabIdToDisplay);

		resizeTabs(this.tabs);

	} /*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */

	@Override
	protected Dialog onCreateDialog(final int id) {
		switch (id) {
		case DIALOG_PICTURE_INFO_ID:
			this.dialog = new Dialog(this);
			break;
		default:
			this.dialog = null;
		}
		return this.dialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
	@Override
	protected void onPrepareDialog(final int id, final Dialog dialog) {
		switch (id) {
		case DIALOG_PICTURE_INFO_ID:
			this.pictureHelper.displayPictureInfoInDialog(dialog);
			break;
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
	 * Creates the audio control view to display play/pause stop buttons.
	 * 
	 * @return the layout which displays play/pause stop buttons
	 */
	private View createAudioControlView() {
		this.audioControlLayout = new LinearLayout(this);
		this.audioControlLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		if (this.ornidroidService.getCurrentBird().getNumberOfSounds() > 0) {
			this.audioControlLayout.setPadding(0, 25, 0, 25);
			this.audioControlLayout.setOrientation(LinearLayout.HORIZONTAL);

			this.playPauseButton = new ImageView(this);
			this.playPauseButton.setId(PLAY_PAUSE_BUTTON);
			this.playPauseButton.setImageResource(R.drawable.ic_sound_play);
			this.playPauseButton.setOnClickListener(this.audioHelper);
			this.playPauseButton.setPadding(0, 0, 25, 0);
			this.audioControlLayout.addView(this.playPauseButton);

			final ImageView stopButton = new ImageView(this);
			stopButton.setId(STOP_BUTTON);
			stopButton.setImageResource(R.drawable.ic_sound_stop);
			stopButton.setOnClickListener(this.audioHelper);
			this.audioControlLayout.addView(stopButton);
		} else {
			printDownloadButtonAndInfo();
		}
		return this.audioControlLayout;
	}

	/**
	 * Creates the header view with the taxon, the nb of pictures and the info
	 * button.
	 * 
	 * @return the view
	 */
	private View createHeaderView() {
		// creation of the main header layout
		final LinearLayout headerLayout = new LinearLayout(this);
		headerLayout.setOrientation(LinearLayout.HORIZONTAL);
		headerLayout.setHorizontalGravity(Gravity.RIGHT);
		headerLayout.setWeightSum(2);

		// vertical layout on the left side which contains the name of the bird
		// and the nb of pictures
		final LinearLayout taxonAndNbPicturesLayout = new LinearLayout(this);
		taxonAndNbPicturesLayout.setOrientation(LinearLayout.VERTICAL);
		this.taxon = new TextView(this);
		this.numberOfPicturesTextView = new TextView(this);
		taxonAndNbPicturesLayout.addView(this.taxon);
		taxonAndNbPicturesLayout.addView(this.numberOfPicturesTextView);
		taxonAndNbPicturesLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		headerLayout.addView(taxonAndNbPicturesLayout);

		if (getBird().getNumberOfPictures() > 0) {
			// a layout with a gravity on the right which contains the info
			// button
			final LinearLayout infoButtonLayout = new LinearLayout(this);
			infoButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
			infoButtonLayout.setGravity(Gravity.RIGHT);
			this.infoButton = new ImageView(this);
			this.infoButton.setOnClickListener(this);
			this.infoButton.setImageResource(R.drawable.ic_info);
			infoButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
			infoButtonLayout.addView(this.infoButton);
			headerLayout.addView(infoButtonLayout);
		}

		return headerLayout;
	}

	/**
	 * Resize tabs. Hint found there :
	 * http://groups.google.com/group/android-developers
	 * /browse_thread/thread/c0ce750ca2525637?pli=1
	 * 
	 * @param tabHost
	 *            the tab host
	 */
	private void resizeTabs(final TabHost tabHost) {
		final TabWidget widget = tabHost.getTabWidget();
		final int childCount = widget.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = widget.getChildAt(i);
			child.getLayoutParams().height = TAB_HEIGHT;
		}

	}
}
