package fr.ornidroid.ui.components;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * The Class TabsPagerAdapter.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

	/** The Constant PICTURE_TAB. */
	public final static int PICTURE_TAB = 0;

	/** The Constant AUDIO_TAB. */
	public final static int AUDIO_TAB = 1;

	/** The Constant DETAILS_TAB. */
	public final static int DETAILS_TAB = 2;

	/** The Constant WIKIPEDIA_TAB. */
	public final static int WIKIPEDIA_TAB = 3;

	/** The Constant NAMES_TAB. */
	public final static int NAMES_TAB = 4;

	/**
	 * Instantiates a new tabs pager adapter.
	 * 
	 * @param fm
	 *            the fm
	 * @param pActivity
	 *            the activity
	 */
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case PICTURE_TAB:
			// images fragment
			return new ImagesFragment();
		case AUDIO_TAB:
			// audio fragment
			return new AudioFragment();
		case DETAILS_TAB:
			// Details fragment activity
			return new DetailsFragment();
		case WIKIPEDIA_TAB:
			// Wikipedia Fragment
			WikipediaFragment wikipediaFragment = new WikipediaFragment();
			return wikipediaFragment;
		case NAMES_TAB:
			// Names
			return new NamesFragment();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 5;
	}

}
