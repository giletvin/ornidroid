package fr.ornidroid.ui.components;

import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.bo.PictureOrnidroidFile;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.ui.picture.PictureHelper;

/**
 * The Class ImagesFragment.
 */
public class ImagesFragment extends AbstractFragment {

	/** The picture layout. */
	private LinearLayout pictureLayout;

	/** General left padding. */
	private static final int LEFT_PADDING = 25;

	/** General right padding. */
	private static final int RIGHT_PADDING = 20;

	/** The Constant DISPLAYED_PICTURE_ID. */
	public static final String DISPLAYED_PICTURE_ID = "DISPLAYED_PICTURE_ID";

	/** The displayed picture id. */
	private int displayedPictureId;

	/**
	 * Gets the displayed picture id.
	 * 
	 * @return the displayed picture id
	 */
	public int getDisplayedPictureId() {
		return displayedPictureId;
	}

	/** The number of pictures text view. */
	private TextView numberOfPicturesTextView;
	/** The gesture detector. */
	private GestureDetector gestureDetector;
	/** The taxon. */
	private TextView taxon;
	/** The view flipper. */
	private ViewFlipper viewFlipper;

	/** The gesture listener. */
	View.OnTouchListener gestureListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.components.AbstractFragment#getOnCreateView(android.view
	 * .LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View getOnCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// View rootView = inflater.inflate(R.layout.fragment_images,
		// container, false);
		//
		// return rootView;

		try {
			loadMediaFilesLocally();
		} catch (final OrnidroidException e) {
			// Log.e(Constants.LOG_TAG, "Error reading media files of bird "
			// + this.bird.getTaxon() + " e");
		}

		this.pictureLayout = new LinearLayout(getActivity());

		// retrieve the displayed picture (when coming back from the
		// zoom)
		this.displayedPictureId = getActivity().getIntent().getIntExtra(
				DISPLAYED_PICTURE_ID, 0);

		this.pictureLayout.setOrientation(LinearLayout.VERTICAL);

		this.pictureLayout.addView(getHeaderView());

		taxon.setText(ornidroidService.getCurrentBird().getTaxon());

		this.viewFlipper = new ViewFlipper(getActivity());
		this.pictureLayout.addView(this.viewFlipper);

		this.viewFlipper.setInAnimation(getActivity(), android.R.anim.fade_in);
		this.viewFlipper
				.setOutAnimation(getActivity(), android.R.anim.fade_out);

		populateViewFlipper();

		// TODO: gestion du swipe sur le viewflipper + double tap
		// this.gestureDetector = new GestureDetector(getActivity(),
		// new NewBirdActivityGestureListener(this));
		// this.gestureListener = new View.OnTouchListener() {
		// public boolean onTouch(final View v, final MotionEvent event) {
		// if (ImagesFragment.this.gestureDetector.onTouchEvent(event)) {
		// return true;
		// }
		// return false;
		// }
		// };
		return pictureLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.ui.components.AbstractFragment#getFileType()
	 */
	@Override
	public OrnidroidFileType getFileType() {
		return OrnidroidFileType.PICTURE;
	}

	/**
	 * Creates the header view with the taxon, the nb of pictures and the info
	 * button.
	 * 
	 * @return the view
	 */
	public View getHeaderView() {
		// creation of the main header layout
		final LinearLayout headerLayout = new LinearLayout(getActivity());
		headerLayout.setOrientation(LinearLayout.HORIZONTAL);
		headerLayout.setHorizontalGravity(Gravity.RIGHT);
		headerLayout.setWeightSum(2);
		headerLayout.setPadding(LEFT_PADDING, 10, 5, 5);

		// vertical layout on the left side which contains the name of the bird
		// and the nb of pictures
		final LinearLayout taxonAndNbPicturesLayout = new LinearLayout(
				getActivity());

		taxonAndNbPicturesLayout.setOrientation(LinearLayout.VERTICAL);
		this.taxon = new TextView(getActivity());
		this.numberOfPicturesTextView = new TextView(getActivity());
		taxonAndNbPicturesLayout.addView(this.taxon);
		taxonAndNbPicturesLayout.addView(this.numberOfPicturesTextView);

		taxonAndNbPicturesLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		headerLayout.addView(taxonAndNbPicturesLayout);

		if (ornidroidService.getCurrentBird().getNumberOfPictures() > 0) {
			// a layout with a gravity on the right which contains the info
			// button
			final LinearLayout infoButtonLayout = new LinearLayout(
					getActivity());
			infoButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
			infoButtonLayout.setGravity(Gravity.RIGHT);
			infoButtonLayout.setPadding(5, 10, 5, RIGHT_PADDING);

			// add button to add custom media files
			infoButtonLayout.addView(removeCustomPictureButton);

			infoButtonLayout.addView(getAddCustomPictureButton());
			if (!Constants.getAutomaticUpdateCheckPreference()) {

				infoButtonLayout.addView(getUpdateFilesButton());
			} else {
				checkForUpdates(false);
			}

			// info button
			setInfoButton(new ImageView(getActivity()));
			getInfoButton().setPadding(20, 0, 0, 0);
			getInfoButton().setOnClickListener(this);
			getInfoButton().setImageResource(R.drawable.ic_info);
			infoButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
			infoButtonLayout.addView(getInfoButton());

			headerLayout.addView(infoButtonLayout);
		}

		return headerLayout;
	}

	/**
	 * Update view flipper with the pictures of the bird. If the bird doesn't
	 * have pictures, instead of the view flipper, show a button to ask if the
	 * user wants to download pictures from the web site.
	 * 
	 * To limit memory consumption, the bitmaps are not loaded yet (except the
	 * first to display). The bitmaps are loaded and deallocated on the fly when
	 * the user flips the view flipper.
	 * 
	 */
	private void populateViewFlipper() {
		// PictureHelper.resetLoadedBitmaps();
		if (this.ornidroidService.getCurrentBird().getNumberOfPictures() > 0) {
			final List<OrnidroidFile> listPictures = this.ornidroidService
					.getCurrentBird().getPictures();
			for (final OrnidroidFile picture : listPictures) {
				final LinearLayout imageAndDescription = new LinearLayout(
						getActivity());
				imageAndDescription.setOrientation(LinearLayout.VERTICAL);

				final TextView description = new TextView(getActivity());
				description.setPadding(LEFT_PADDING, 0, 0, 0);
				description
						.setText(picture
								.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
				description.setTextAppearance(getActivity(),
						android.R.style.TextAppearance_Small);
				imageAndDescription.addView(description);

				this.viewFlipper.addView(imageAndDescription);
			}
			displayFixedPicture();
			updateNumberOfPicturesText();
			insertBitmapInViewFlipper(this.displayedPictureId);
		} else {
			printDownloadButtonAndInfo();
		}
	}

	/**
	 * When coming to this screen with a given picture id to display. set the
	 * cursor of the view flipper on the good image to display
	 */
	private void displayFixedPicture() {
		for (int i = 0; i < this.displayedPictureId; i++) {
			this.viewFlipper.showNext();
		}
	}

	/**
	 * Returns the formatted text which displays the number of pictures for this
	 * bird and the current picture number.
	 * 
	 * @return the text
	 */
	private void updateNumberOfPicturesText() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.displayedPictureId + 1);
		sb.append(BasicConstants.SLASH_STRING);
		sb.append(this.ornidroidService.getCurrentBird().getNumberOfPictures());

		this.numberOfPicturesTextView.setText(sb.toString());

	}

	/**
	 * Memory consumption : Insert the bitmap of the given index in view
	 * flipper.
	 * 
	 * @param index
	 *            the index
	 */
	public void insertBitmapInViewFlipper(final int index) {
		final LinearLayout imageAndDescription = (LinearLayout) this.viewFlipper
				.getChildAt(index);
		final ImageView imagePicture = new ImageView(getActivity());
		final OrnidroidFile picture = this.ornidroidService.getCurrentBird()
				.getPicture(index);
		setCurrentMediaFile(picture);

		Bitmap bMap = PictureHelper.loadBitmap(picture, getActivity()
				.getResources());
		if (bMap != null) {
			imagePicture.setImageBitmap(bMap);
			imageAndDescription.addView(imagePicture);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.components.AbstractFragment#getSpecificContentLayout()
	 */
	@Override
	protected LinearLayout getSpecificContentLayout() {
		return this.pictureLayout;
	}

	/**
	 * Reset resources. Set the viewFlipper to null
	 */
	public void resetViewFlipper() {
		this.viewFlipper = null;
	}
}
