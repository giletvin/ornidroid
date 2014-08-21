package fr.ornidroid.ui.components;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import fr.ornidroid.bo.Bird;

/**
 * The Class ImageSlidesFragmentAdapter.
 */
public class ImageSlidesFragmentAdapter extends FragmentPagerAdapter {

	/** The current bird. */
	private Bird currentBird;

	/**
	 * Sets the current bird.
	 * 
	 * @param currentBird
	 *            the new current bird
	 */
	public void setCurrentBird(Bird currentBird) {
		this.currentBird = currentBird;
	}

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
		ImageSlideFragment imageSlideFragment = new ImageSlideFragment();
		imageSlideFragment.setOrnidroidPictureFile(this.currentBird
				.getPicture(position));

		return imageSlideFragment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.currentBird.getNumberOfPictures();
	}

}
