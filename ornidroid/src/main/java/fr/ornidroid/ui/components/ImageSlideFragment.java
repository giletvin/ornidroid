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
import android.widget.TextView;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.PictureOrnidroidFile;
import fr.ornidroid.ui.picture.PictureHelper;

/**
 * The Class ImageSlideFragment.
 */
public class ImageSlideFragment extends Fragment {

	/** The ornidroid picture file. */
	private OrnidroidFile ornidroidPictureFile;
	/** General left padding. */
	private static final int LEFT_PADDING = 25;

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
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		final TextView description = new TextView(getActivity());
		description.setPadding(LEFT_PADDING, 0, 0, 0);
		description.setText(this.ornidroidPictureFile
				.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
		description.setTextAppearance(getActivity(),
				android.R.style.TextAppearance_Small);
		layout.addView(description);
		layout.addView(image);

		return layout;
	}
}
