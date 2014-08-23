package fr.ornidroid.ui.components;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.PictureOrnidroidFile;
import fr.ornidroid.ui.picture.PictureHelper;

/**
 * ImageSlideFragment. Sub fragment in the Images Fragment which handles the
 * picture and its description.
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
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.fragment_picture, container, false);
		ImageView image = (ImageView) layout.findViewById(R.id.picture);

		Bitmap bMap = PictureHelper.loadBitmap(this.ornidroidPictureFile,
				getActivity().getResources());
		if (bMap != null) {
			image.setImageBitmap(bMap);

		}

		final TextView description = (TextView) layout
				.findViewById(R.id.picture_description);
		description.setPadding(LEFT_PADDING, 0, 0, 0);
		description.setText(this.ornidroidPictureFile
				.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
		description.setTextAppearance(getActivity(),
				android.R.style.TextAppearance_Small);

		return layout;
	}
}
