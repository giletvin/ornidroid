package fr.ornidroid.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import fr.ornidroid.ui.fragment.AudioFragment_;
import fr.ornidroid.ui.fragment.DetailsFragment_;
import fr.ornidroid.ui.fragment.ImagesFragment_;
import fr.ornidroid.ui.fragment.NamesFragment_;
import fr.ornidroid.ui.fragment.WikipediaFragment_;

/**
 * The Class BirdActivityTabsPagerAdapter.
 */
public class BirdActivityTabsPagerAdapter extends FragmentPagerAdapter {

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
	 */
	public BirdActivityTabsPagerAdapter(FragmentManager fm) {
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
			return new ImagesFragment_();
		case AUDIO_TAB:
			// audio fragment
			return new AudioFragment_();
		case DETAILS_TAB:
			// Details fragment activity
			return new DetailsFragment_();
		case WIKIPEDIA_TAB:
			// Wikipedia Fragment
			return new WikipediaFragment_();
		case NAMES_TAB:
			// Names
			return new NamesFragment_();
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
