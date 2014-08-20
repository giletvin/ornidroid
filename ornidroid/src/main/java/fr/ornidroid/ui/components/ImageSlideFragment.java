package fr.ornidroid.ui.components;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * The Class ImageSlideFragment.
 */
public class ImageSlideFragment extends Fragment {

	/** The image resource id. */
	int imageResourceId;

	/**
	 * Instantiates a new image slide fragment.
	 */
	public ImageSlideFragment() {

	}

	/**
	 * Sets the image resource id.
	 * 
	 * @param imageResourceId
	 *            the new image resource id
	 */
	public void setImageResourceId(int imageResourceId) {
		this.imageResourceId = imageResourceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ImageView image = new ImageView(getActivity());
		image.setImageResource(imageResourceId);

		LinearLayout layout = new LinearLayout(getActivity());
		// layout.setLayoutParams(new LayoutParams());

		layout.setGravity(Gravity.CENTER);
		layout.addView(image);

		return layout;
	}
}
