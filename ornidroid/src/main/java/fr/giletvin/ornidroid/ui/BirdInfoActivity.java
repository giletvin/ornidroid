package fr.giletvin.ornidroid.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import fr.giletvin.ornidroid.R;
import fr.giletvin.ornidroid.bo.OrnidroidFileType;
import fr.giletvin.ornidroid.helper.Constants;
import fr.giletvin.ornidroid.service.IOrnidroidService;
import fr.giletvin.ornidroid.service.OrnidroidServiceFactory;

/**
 * Screen with tabs to display info about the selected bird.
 * 
 * This activity has a gesture listener to handle finger swipes and can update
 * the pictures in the sub activity
 */
public class BirdInfoActivity extends TabActivity {

	/**
	 * The listener interface for receiving onBirdTabChange events. The class
	 * that is interested in processing a onBirdTabChange event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addOnBirdTabChangeListener<code> method. When
	 * the onBirdTabChange event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnBirdTabChangeEvent
	 */
	private class OnBirdTabChangeListener implements OnTabChangeListener {

		/** The audio activity. */
		SoundActivity audioActivity;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang
		 * .String)
		 */
		public void onTabChanged(String tabId) {
			// couper le player mp3 si on change de tabulation
			if ((this.audioActivity == null)
					&& SoundActivity.class.isInstance(getCurrentActivity())) {
				this.audioActivity = (SoundActivity) getCurrentActivity();
			}
			if (this.audioActivity != null) {
				this.audioActivity.stopPlayer();
			}
		}

	}

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
		public boolean onDoubleTap(MotionEvent e) {
			if (PictureActivity.class.isInstance(getCurrentActivity())) {
				PictureActivity pictureActivity = (PictureActivity) getCurrentActivity();
				int displayedPictureId = pictureActivity
						.getDisplayedPictureId();
				pictureActivity.resetResources();
				Intent intentImageFullSize = new Intent(getCurrentActivity(),
						FullSizeImageActivity.class);
				intentImageFullSize.putExtra(
						PictureActivity.DISPLAYED_PICTURE_ID,
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
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (PictureActivity.class.isInstance(getCurrentActivity())) {
				try {
					if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
						return false;
					}
					// right to left swipe
					if (((e1.getX() - e2.getX()) > SWIPE_MIN_DISTANCE)
							&& (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {
						PictureActivity photoActivity = (PictureActivity) getCurrentActivity();

						// photoActivity.getViewFlipper().setInAnimation(
						// slideLeftIn);
						// photoActivity.getViewFlipper().setOutAnimation(
						// slideLeftOut);
						photoActivity.showNextPicture();

					} else if (((e2.getX() - e1.getX()) > SWIPE_MIN_DISTANCE)
							&& (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {
						PictureActivity photoActivity = (PictureActivity) getCurrentActivity();
						// photoActivity.getViewFlipper().setInAnimation(
						// slideRightIn);
						// photoActivity.getViewFlipper().setOutAnimation(
						// slideRightOut);
						photoActivity.showPreviousPicture();
					}
				} catch (Exception e) {
					Log.e(Constants.LOG_TAG, "Exception occured in onFling", e);
				}
			}
			return false;
		}
	}

	/** The Constant INTENT_ACTIVITY_TO_OPEN. */
	public static final String INTENT_ACTIVITY_TO_OPEN = "intentActivityToOpenParameter";

	/** The Constant AUDIO_TAB_NAME. */
	private static final String AUDIO_TAB_NAME = "audioTab";

	/** The Constant BIRD_NAMES_TAB_NAME. */
	private static final String BIRD_NAMES_TAB_NAME = "birdNamesTab";

	/** The Constant DETAIL_TAB_NAME. */
	private static final String DETAIL_TAB_NAME = "detailTab";

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

	/** The gesture detector. */
	private GestureDetector gestureDetector;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The tab id to display. */
	private int tabIdToDisplay;

	/** The uri. */
	private Uri uri;

	/** The gesture listener. */
	View.OnTouchListener gestureListener;

	/**
	 * Instantiates a new bird info activity.
	 */
	public BirdInfoActivity() {
		this.ornidroidService = OrnidroidServiceFactory.getService(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.bird_info);

		getTabHost().setOnTabChangedListener(new OnBirdTabChangeListener());

		loadBirdDetails();

		this.tabIdToDisplay = getIntent().getIntExtra(INTENT_ACTIVITY_TO_OPEN,
				OrnidroidFileType.getCode(OrnidroidFileType.PICTURE));

		createTab(PictureActivity.class, R.drawable.ic_tab_pictures,
				PICTURES_TAB_NAME,
				OrnidroidFileType.getCode(OrnidroidFileType.PICTURE));

		createTab(SoundActivity.class, R.drawable.ic_tab_sounds,
				AUDIO_TAB_NAME,
				OrnidroidFileType.getCode(OrnidroidFileType.AUDIO));

		createTab(BirdDetailActivity.class, R.drawable.ic_tab_details,
				DETAIL_TAB_NAME, OrnidroidFileType.getCode(null));
		createTab(BirdNamesActivity.class, R.drawable.ic_tab_bird_names,
				BIRD_NAMES_TAB_NAME, OrnidroidFileType.getCode(null));

		getTabHost().setCurrentTab(this.tabIdToDisplay);

		resizeTabs(getTabHost());

		this.gestureDetector = new GestureDetector(new GestureListener());
		this.gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (BirdInfoActivity.this.gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.search:
			onSearchRequested();
			return true;
		case R.id.preferences:
			startActivity(new Intent(this, OrnidroidPreferenceActivity.class));
			return (true);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.gestureDetector.onTouchEvent(event)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Add info to the intent, which will be passed to the tabbed activity
	 * (picture, sound). The Extra data received in the intent are passed to the
	 * intent of the tabbed activity
	 * 
	 * @param intent
	 *            the intent
	 */
	private void addInfoToIntent(Intent intent) {
		Intent birdInfoActivityIntent = this.getIntent();
		intent.putExtra(
				AbstractDownloadableMediaActivity.BROKEN_LINK_INTENT_PARAM,
				birdInfoActivityIntent
						.getBooleanExtra(
								AbstractDownloadableMediaActivity.BROKEN_LINK_INTENT_PARAM,
								false));

	}

	/**
	 * Creates the tab.
	 * 
	 * @param activityClass
	 *            the activity class
	 * @param tabIconResId
	 *            the tab icon res id
	 * @param tabName
	 *            the tab name
	 * @param tabIndex
	 *            the tab index
	 * @return intent
	 */
	@SuppressWarnings("rawtypes")
	private Intent createTab(Class activityClass, int tabIconResId,
			String tabName, int tabIndex) {
		Intent intent = new Intent().setClass(this, activityClass);
		TabSpec spec = getTabHost().newTabSpec(tabName)
				.setIndicator("", getResources().getDrawable(tabIconResId))
				.setContent(intent);
		if (this.tabIdToDisplay == tabIndex) {
			addInfoToIntent(intent);
		}
		if (this.tabIdToDisplay == OrnidroidFileType
				.getCode(OrnidroidFileType.PICTURE)) {
			// put the displayed picture id in the extra to pass it to the
			// pictureActivityScreen
			intent.putExtra(PictureActivity.DISPLAYED_PICTURE_ID, getIntent()
					.getIntExtra(PictureActivity.DISPLAYED_PICTURE_ID, 0));

		}
		getTabHost().addTab(spec);
		return intent;
	}

	/**
	 * Load bird details, from uri contained in the intent.
	 */
	private void loadBirdDetails() {
		this.uri = getIntent().getData();
		if (null != this.uri) {
			this.ornidroidService.loadBirdDetails(this.uri);
		}
	}

	/**
	 * Resize tabs. Hint found there :
	 * http://groups.google.com/group/android-developers
	 * /browse_thread/thread/c0ce750ca2525637?pli=1
	 * 
	 * @param tabHost
	 *            the tab host
	 */
	private void resizeTabs(TabHost tabHost) {
		TabWidget widget = tabHost.getTabWidget();
		int childCount = widget.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = widget.getChildAt(i);
			child.getLayoutParams().height = TAB_HEIGHT;
		}

	}

}
