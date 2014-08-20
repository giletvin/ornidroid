package fr.ornidroid.ui.components;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import fr.ornidroid.R;

/**
 * The Class ImageSlidesFragmentAdapter.
 */
public class ImageSlidesFragmentAdapter extends FragmentPagerAdapter {

	/** The Images. */
	private int[] Images = new int[] { R.drawable.bec_canard,
			R.drawable.bec_crochu, R.drawable.bec_autre, R.drawable.bec_mouette

	};

	// protected static final int[] ICONS = new int[] { R.drawable.marker,
	// R.drawable.marker, R.drawable.marker, R.drawable.marker };

	/** The m count. */
	private int mCount = Images.length;

	/**
	 * Instantiates a new image slides fragment adapter.
	 * 
	 * @param fm
	 *            the fm
	 */
	public ImageSlidesFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		ImageSlideFragment imageFragment = new ImageSlideFragment();
		imageFragment.setImageResourceId(Images[position]);
		return imageFragment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return mCount;
	}

	// public int getIconResId(int index) {
	// return ICONS[index % ICONS.length];
	// }

	/**
	 * Sets the count.
	 * 
	 * @param count
	 *            the new count
	 */
	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
}
