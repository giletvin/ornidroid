package fr.ornidroid.ui;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import fr.ornidroid.R;
import fr.ornidroid.helper.MenuHelper;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.components.TabsPagerAdapter;

/**
 * The Class NewBirdActivity.//FragmentActivity
 */
public class NewBirdActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/** The Constant INTENT_ACTIVITY_TO_OPEN. */
	public static final String INTENT_TAB_TO_OPEN = "intentTabToOpenParameter";
	/** The media player. */
	private MediaPlayer mMediaPlayer;
	/** The birdId. */
	private int birdId;
	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The view pager. */
	private ViewPager viewPager;

	/** The m adapter. */
	private TabsPagerAdapter mAdapter;

	/** The action bar. */
	private ActionBar actionBar;

	/**
	 * Instantiates a new new bird activity.
	 */
	public NewBirdActivity() {
		this.ornidroidService = OrnidroidServiceFactory.getService(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_bird_activity);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		// actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_pictures)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_sounds)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_details)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setIcon(R.drawable.ic_tab_wikipedia).setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setIcon(R.drawable.ic_tab_bird_names).setTabListener(this));

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});

		int tabToOpen = getIntent().getIntExtra(INTENT_TAB_TO_OPEN, 0);

		viewPager.setCurrentItem(tabToOpen);
		this.loadBirdDetails();
		this.ornidroidService.getCurrentBird();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.ActionBar.TabListener#onTabReselected(android.app.ActionBar
	 * .Tab, android.app.FragmentTransaction)
	 */
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.ActionBar.TabListener#onTabSelected(android.app.ActionBar
	 * .Tab, android.app.FragmentTransaction)
	 */
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.ActionBar.TabListener#onTabUnselected(android.app.ActionBar
	 * .Tab, android.app.FragmentTransaction)
	 */
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	/**
	 * Load bird details, from bird Id contained in the intent.
	 */
	private void loadBirdDetails() {
		this.birdId = getIntent()
				.getIntExtra(MainActivity.BIRD_ID_ITENT_PRM, 0);
		if (0 != this.birdId) {
			this.ornidroidService.loadBirdDetails(birdId);
		}
	}

	/**
	 * Gets the current tab id.
	 * 
	 * @return the current tab id
	 */
	public int getCurrentTabId() {
		return viewPager.getCurrentItem();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		this.mMediaPlayer = new MediaPlayer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		this.mMediaPlayer.release();
	}

	/**
	 * Gets the media player.
	 * 
	 * @return the media player
	 */
	public MediaPlayer getMediaPlayer() {
		return this.mMediaPlayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		return MenuHelper.onCreateOptionsMenu(inflater, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		return MenuHelper.onOptionsItemSelected(this, item);
	}
}
