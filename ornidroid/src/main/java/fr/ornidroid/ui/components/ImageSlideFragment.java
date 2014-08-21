package fr.ornidroid.ui.components;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.ui.picture.PictureHelper;

/**
 * The Class ImageSlideFragment.
 */
public class ImageSlideFragment extends Fragment {

	/** The ornidroid picture file. */
	private OrnidroidFile ornidroidPictureFile;

	/**
	 * Sets the ornidroid picture file.
	 * 
	 * @param ornidroidPictureFile
	 *            the new ornidroid picture file
	 */
	public void setOrnidroidPictureFile(OrnidroidFile ornidroidPictureFile) {
		this.ornidroidPictureFile = ornidroidPictureFile;
	}

	/**
	 * Instantiates a new image slide fragment.
	 */
	public ImageSlideFragment() {

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

		Bitmap bMap = PictureHelper.loadBitmap(this.ornidroidPictureFile,
				getActivity().getResources());
		if (bMap != null) {
			image.setImageBitmap(bMap);

		}

		LinearLayout layout = new LinearLayout(getActivity());

		layout.setGravity(Gravity.CENTER);
		layout.addView(image);

		return layout;
	}
}
