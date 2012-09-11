package fr.ornidroid.ui;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
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
import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.bo.AudioOrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.bo.PictureOrnidroidFile;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.audio.AudioControlButtonListener;
import fr.ornidroid.ui.views.DetailsViewFactory;
import fr.ornidroid.ui.views.NamesViewFactory;

/**
 * Displays bird details.
 */
public class BirdActivity extends AbstractDownloadableMediaActivity implements
		TabContentFactory, OnClickListener {
	/**
	 * The listener interface for receiving gesture events. The class that is
	 * interested in processing a gesture event implements this interface, and
	 * the object created with that class is registered with a component using
	 * the component's <code>addGestureListener<code> method. When
	 * the gesture event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see GestureEvent
	 */
	class GestureListener extends SimpleOnGestureListener {

		/**
		 * On double tap. If the double tap occurs on picture activity, open the
		 * full size picture activity which displays the original full size
		 * picture
		 * 
		 * @param e
		 *            the e
		 * @return true, if successful
		 */
		@Override
		public boolean onDoubleTap(final MotionEvent e) {
			if (PICTURES_TAB_NAME.equals(BirdActivity.this.tabs
					.getCurrentTabTag())) {

				final int displayedPictureId = getDisplayedPictureId();
				resetResources();
				final Intent intentImageFullSize = new Intent(
						BirdActivity.this, FullSizeImageActivity.class);
				intentImageFullSize.putExtra(DISPLAYED_PICTURE_ID,
						displayedPictureId);
				startActivity(intentImageFullSize);
				return true;
			} else {
				return false;
			}
		}

		/*
		 * Handles the flip between pictures in the PictureActivity
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.view.GestureDetector.SimpleOnGestureListener#onFling(android
		 * .view.MotionEvent, android.view.MotionEvent, float, float)
		 */
		@Override
		public boolean onFling(final MotionEvent e1, final MotionEvent e2,
				final float velocityX, final float velocityY) {
			if (PICTURES_TAB_NAME.equals(BirdActivity.this.tabs
					.getCurrentTabTag())) {
				try {
					if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
						return false;
					}
					// right to left swipe
					if (((e1.getX() - e2.getX()) > SWIPE_MIN_DISTANCE)
							&& (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {

						// photoActivity.getViewFlipper().setInAnimation(
						// slideLeftIn);
						// photoActivity.getViewFlipper().setOutAnimation(
						// slideLeftOut);
						showNextPicture();

					} else if (((e2.getX() - e1.getX()) > SWIPE_MIN_DISTANCE)
							&& (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {

						// photoActivity.getViewFlipper().setInAnimation(
						// slideRightIn);
						// photoActivity.getViewFlipper().setOutAnimation(
						// slideRightOut);
						showPreviousPicture();
					}
				} catch (final Exception e) {
					// Log.e(Constants.LOG_TAG, "Exception occured in onFling",
					// e);
				}
			}
			return false;
		}
	}

	/** The Constant DISPLAYED_PICTURE_ID. */
	public static final String DISPLAYED_PICTURE_ID = "DISPLAYED_PICTURE_ID";

	/** The Constant INTENT_ACTIVITY_TO_OPEN. */
	public static final String INTENT_ACTIVITY_TO_OPEN = "intentActivityToOpenParameter";

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

	/** The Constant EMPTY. */
	private static final String EMPTY = "";

	/** The Constant PICTURES_TAB_NAME. */
	private static final String PICTURES_TAB_NAME = "pictures";
	/** The Constant SWIPE_MAX_OFF_PATH. */
	private static final int SWIPE_MAX_OFF_PATH = 250;
	/** The Constant SWIPE_MIN_DISTANCE. */
	private static final int SWIPE_MIN_DISTANCE = 120;

	/** The Constant SWIPE_THRESHOLD_VELOCITY. */
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	/** The Constant TAB_HEIGHT. */
	private static final int TAB_HEIGHT = 50;

	/** The audio control layout. */
	private LinearLayout audioControlLayout;

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

	/** The number of pictures. */
	private TextView numberOfPictures;

	/** The ok dialog button. */
	private Button okDialogButton;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The picture layout. */
	private LinearLayout pictureLayout;

	/** The play pause button. */
	private ImageView playPauseButton;

	/** The slide left in. */
	private Animation slideLeftIn;

	/** The slide left out. */
	private Animation slideLeftOut;

	/** The slide right in. */
	private Animation slideRightIn;

	/** The slide right out. */
	private Animation slideRightOut;

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
					spinThatShit(position);
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

			populateViewFlipper();
			this.gestureDetector = new GestureDetector(new GestureListener());
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
	 * Gets the ornidroid service.
	 * 
	 * @return the ornidroid service
	 */
	public IOrnidroidService getOrnidroidService() {
		return this.ornidroidService;
	}

	/**
	 * Gets the play pause button.
	 * 
	 * @return the play pause button
	 */
	public ImageView getPlayPauseButton() {
		return this.playPauseButton;
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
	 * Spin that shit.
	 * 
	 * @param position
	 *            the position
	 */
	public void spinThatShit(final int position) {
		// Log.i(Constants.LOG_TAG, "Reading mp3 number " + position);
		final AbstractOrnidroidFile mp3File = this.ornidroidService
				.getCurrentBird().getSound(position);
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

	/**
	 * Stop player.
	 */
	public void stopPlayer() {
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
		this.tabIdToDisplay = getIntent().getIntExtra(INTENT_ACTIVITY_TO_OPEN,
				OrnidroidFileType.getCode(OrnidroidFileType.PICTURE));
		this.tabs = (TabHost) this.findViewById(R.id.my_tabhost);
		this.tabs.setup();

		final TabSpec tspecPicture = this.tabs.newTabSpec(PICTURES_TAB_NAME);
		tspecPicture.setIndicator(EMPTY,
				getResources().getDrawable(R.drawable.ic_tab_pictures));
		tspecPicture.setContent(this);
		this.tabs.addTab(tspecPicture);

		final TabSpec tspecAudio = this.tabs.newTabSpec(AUDIO_TAB_NAME);
		tspecAudio.setIndicator(EMPTY,
				getResources().getDrawable(R.drawable.ic_tab_sounds));
		tspecAudio.setContent(this);

		this.tabs.addTab(tspecAudio);

		// details
		final TabSpec tspec1 = this.tabs.newTabSpec(DETAIL_TAB_NAME);
		tspec1.setIndicator(EMPTY,
				getResources().getDrawable(R.drawable.ic_tab_details));
		tspec1.setContent(new TabContentFactory() {

			public View createTabContent(final String tag) {

				return new DetailsViewFactory(BirdActivity.this)
						.createContent();
			}
		});
		this.tabs.addTab(tspec1);
		final TabSpec tspec2 = this.tabs.newTabSpec(BIRD_NAMES_TAB_NAME);
		tspec2.setIndicator(EMPTY,
				getResources().getDrawable(R.drawable.ic_tab_bird_names));
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
			displayPictureInfoInDialog(dialog);
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
	 * Reset resources.
	 */
	protected void resetResources() {
		this.viewFlipper = null;
	}

	/**
	 * Show next picture and update the displayed picture id.
	 */
	protected void showNextPicture() {
		removeBitmapInViewFlipper(this.displayedPictureId);
		this.displayedPictureId++;
		if (this.displayedPictureId == getBird().getNumberOfPictures()) {
			this.displayedPictureId = 0;
		}
		insertBitmapInViewFlipper(this.displayedPictureId);
		updateNumberOfPicturesText();
		this.viewFlipper.setInAnimation(this.slideLeftIn);
		this.viewFlipper.setOutAnimation(this.slideLeftOut);
		this.viewFlipper.showNext();
	}

	/**
	 * Show previous picture and update the displayed picture id.
	 */
	protected void showPreviousPicture() {
		removeBitmapInViewFlipper(this.displayedPictureId);
		this.displayedPictureId--;
		if (this.displayedPictureId == -1) {
			this.displayedPictureId = getBird().getNumberOfPictures() - 1;
		}
		insertBitmapInViewFlipper(this.displayedPictureId);
		updateNumberOfPicturesText();
		this.viewFlipper.setInAnimation(this.slideRightIn);
		this.viewFlipper.setOutAnimation(this.slideRightOut);
		this.viewFlipper.showPrevious();

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
			this.playPauseButton
					.setOnClickListener(new AudioControlButtonListener(this));
			this.playPauseButton.setPadding(0, 0, 25, 0);
			this.audioControlLayout.addView(this.playPauseButton);

			final ImageView stopButton = new ImageView(this);
			stopButton.setId(STOP_BUTTON);
			stopButton.setImageResource(R.drawable.ic_sound_stop);
			stopButton.setOnClickListener(new AudioControlButtonListener(this));
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
		this.numberOfPictures = new TextView(this);
		taxonAndNbPicturesLayout.addView(this.taxon);
		taxonAndNbPicturesLayout.addView(this.numberOfPictures);
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
	 * When coming to this screen with a given picture id to display. set the
	 * cursor of the view flipper on the good image to display
	 */
	private void displayFixedPicture() {
		for (int i = 0; i < this.displayedPictureId; i++) {
			this.viewFlipper.showNext();
		}
	}

	/**
	 * Display line in dialog.<br/>
	 * Example : <br/>
	 * source : http://www.wikipedia.org
	 * 
	 * @param dialog
	 *            dialog
	 * @param displayedPicture
	 *            the displayed picture
	 * @param textViewResId
	 *            the text view res id
	 * @param labelResourceId
	 *            the label resource id
	 * @param propertyName
	 *            the property name
	 */
	private void displayLineInDialog(final Dialog dialog,
			final AbstractOrnidroidFile displayedPicture,
			final int textViewResId, final int labelResourceId,
			final String propertyName) {
		final String propertyValue = displayedPicture.getProperty(propertyName);
		if (StringUtils.isNotBlank(propertyValue)) {
			final TextView textView = (TextView) dialog
					.findViewById(textViewResId);
			textView.setText(this.getString(labelResourceId) + ": "
					+ displayedPicture.getProperty(propertyName));
		}
	}

	/**
	 * Display picture info in dialog.
	 * 
	 * @param dialog
	 *            the dialog
	 */
	private void displayPictureInfoInDialog(final Dialog dialog) {
		dialog.setContentView(R.layout.picture_info_dialog);
		dialog.setTitle(R.string.dialog_picture_title);
		final AbstractOrnidroidFile displayedPicture = getBird().getPictures()
				.get(this.displayedPictureId);
		displayLineInDialog(dialog, displayedPicture,
				R.id.dialog_picture_description,
				R.string.dialog_picture_description,
				PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY);
		displayLineInDialog(dialog, displayedPicture,
				R.id.dialog_picture_author, R.string.dialog_picture_author,
				PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY);
		displayLineInDialog(dialog, displayedPicture,
				R.id.dialog_picture_source, R.string.dialog_picture_source,
				PictureOrnidroidFile.IMAGE_SOURCE_PROPERTY);
		displayLineInDialog(dialog, displayedPicture,
				R.id.dialog_picture_licence, R.string.dialog_picture_licence,
				PictureOrnidroidFile.IMAGE_LICENCE_PROPERTY);

		this.okDialogButton = (Button) dialog
				.findViewById(R.id.dialog_ok_button);
		this.okDialogButton.setOnClickListener(this);

	}

	/**
	 * Memory consumption : Insert the bitmap of the given index in view
	 * flipper.
	 * 
	 * @param index
	 *            the index
	 */
	private void insertBitmapInViewFlipper(final int index) {
		final LinearLayout imageAndDescription = (LinearLayout) this.viewFlipper
				.getChildAt(index);
		final ImageView imagePicture = new ImageView(this);
		final Bitmap bMap = BitmapFactory.decodeFile(getBird()
				.getPicture(index).getPath());
		imagePicture.setImageBitmap(bMap);
		imageAndDescription.addView(imagePicture);
	}

	/**
	 * Update view flipper with the pictures of the bird. If the bird doesn't
	 * have pictures, instead of the view flipper, show a button to ask if the
	 * user wants to download pictures from the web site.
	 * 
	 * To limit memory consumption, the bitmaps are not loaded yet (except the
	 * first to display). The bitmaps are loaded and deallocated on the fly when
	 * the user flips the view flipper.
	 * 
	 */
	private void populateViewFlipper() {
		if (getBird().getNumberOfPictures() > 0) {
			final List<AbstractOrnidroidFile> listPictures = getBird()
					.getPictures();
			for (final AbstractOrnidroidFile picture : listPictures) {
				final LinearLayout imageAndDescription = new LinearLayout(this);
				imageAndDescription.setOrientation(LinearLayout.VERTICAL);

				final TextView description = new TextView(this);
				description
						.setText(picture
								.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
				description.setTextAppearance(this,
						android.R.style.TextAppearance_Small);
				imageAndDescription.addView(description);
				this.viewFlipper.addView(imageAndDescription);
				displayFixedPicture();
				updateNumberOfPicturesText();
			}
			insertBitmapInViewFlipper(this.displayedPictureId);
		} else {
			printDownloadButtonAndInfo();
		}
	}

	/**
	 * Memory consumption : Removes the bitmap of the given index in view
	 * flipper.
	 * 
	 * @param index
	 *            the index
	 */
	private void removeBitmapInViewFlipper(final int index) {
		final LinearLayout imageAndDescription = (LinearLayout) this.viewFlipper
				.getChildAt(index);
		imageAndDescription.removeViewAt(1);
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

	/**
	 * Returns the formatted text which displays the number of pictures for this
	 * bird and the current picture number.
	 * 
	 * @return the text
	 */
	private void updateNumberOfPicturesText() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.displayedPictureId + 1);
		sb.append("/");
		sb.append(getBird().getNumberOfPictures());

		this.numberOfPictures.setText(sb.toString());

	}
}
