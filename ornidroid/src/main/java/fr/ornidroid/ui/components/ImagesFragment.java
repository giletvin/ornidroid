package fr.ornidroid.ui.components;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.bo.PictureOrnidroidFile;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.ui.ScrollableImageActivity;
import fr.ornidroid.ui.activity.HomeActivity;
import fr.ornidroid.ui.picture.PictureHelper;

/**
 * The Class ImagesFragment.
 */
public class ImagesFragment extends AbstractFragment implements OnClickListener {

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

	/** The number of pictures text view. */
	private TextView numberOfPicturesTextView;

	/** The taxon. */
	private TextView taxon;

	/** The gesture listener. */
	View.OnTouchListener gestureListener;

	/** The zoom button. */
	private ImageView zoomButton;
	/** The info button. */
	private ImageView infoButton;

	/** The next button. */
	private ImageView nextButton;

	/** The m picture. */
	private ImageView mPicture;

	/** The previous button. */
	private ImageView previousButton;

	/** The m picture description. */
	private TextView mPictureDescription;

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
		if (this.ornidroidService.getCurrentBird() == null) {
			// Github : #118
			final Intent intent = new Intent(getActivity(), HomeActivity.class);
			startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return null;
		}

		pictureLayout = (LinearLayout) inflater.inflate(
				R.layout.fragment_images, container, false);

		try {
			loadMediaFilesLocally();
			// retrieve the displayed picture (when coming back from the
			// zoom)
			this.displayedPictureId = getActivity().getIntent().getIntExtra(
					DISPLAYED_PICTURE_ID, 0);

			//
			LinearLayout headerLayout = (LinearLayout) pictureLayout
					.findViewById(R.id.images_header);

			headerLayout.addView(getHeaderView());

			this.taxon.setText(ornidroidService.getCurrentBird().getTaxon());
			this.mPictureDescription = (TextView) pictureLayout
					.findViewById(R.id.picture_description);
			this.nextButton = (ImageView) pictureLayout
					.findViewById(R.id.next_button);
			this.nextButton.setOnClickListener(this);
			this.zoomButton = (ImageView) pictureLayout
					.findViewById(R.id.zoom_button);
			this.zoomButton.setOnClickListener(this);
			this.previousButton = (ImageView) pictureLayout
					.findViewById(R.id.previous_button);
			this.previousButton.setOnClickListener(this);

			this.mPicture = (ImageView) pictureLayout
					.findViewById(R.id.picture);
			this.mPicture.setOnClickListener(this);

			if (ornidroidService.getCurrentBird().getNumberOfPictures() == 0) {
				pictureLayout.removeAllViews();
				printDownloadButtonAndInfo();
			} else {
				loadImage(0);
			}
		} catch (final OrnidroidException e) {
			Toast.makeText(
					getActivity(),
					"Error reading media files of bird "
							+ this.ornidroidService.getCurrentBird().getTaxon()
							+ " e", Toast.LENGTH_LONG).show();
		}

		return pictureLayout;
	}

	/**
	 * Load image and change picture description.
	 * 
	 * @param imagePosition
	 *            the image position
	 */
	private void loadImage(int imagePosition) {
		if (imagePosition < 0) {
			imagePosition = this.ornidroidService.getCurrentBird()
					.getNumberOfPictures() - 1;
		}
		if (imagePosition >= this.ornidroidService.getCurrentBird()
				.getNumberOfPictures()) {
			imagePosition = 0;
		}
		OrnidroidFile pictureFile = ornidroidService.getCurrentBird()
				.getPicture(imagePosition);
		this.mPictureDescription.setText(pictureFile
				.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));

		Bitmap bMap = PictureHelper.loadBitmap(pictureFile, getActivity()
				.getResources());
		if (bMap != null) {
			this.mPicture.setImageBitmap(bMap);

		}
		setCurrentMediaFilePosition(imagePosition);
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
		// TODO : faire tout ça en XML ?

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
			this.infoButton = new ImageView(getActivity());
			this.infoButton.setPadding(20, 0, 0, 0);
			this.infoButton.setOnClickListener(this);
			this.infoButton.setImageResource(R.drawable.ic_info);
			infoButtonLayout.addView(this.infoButton);

			infoButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
			headerLayout.addView(infoButtonLayout);
		}

		updateNumberOfPicturesText();
		return headerLayout;
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
	 * Sets the current media file postion.
	 * 
	 * @param currentItem
	 *            the new current media file postion
	 */
	public void setCurrentMediaFilePosition(int currentItem) {
		this.displayedPictureId = currentItem;
		final OrnidroidFile picture = this.ornidroidService.getCurrentBird()
				.getPicture(currentItem);
		setCurrentMediaFile(picture);
		updateNumberOfPicturesText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.components.AbstractFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(final View v) {
		if (v == this.infoButton) {
			FragmentManager fm = getActivity().getSupportFragmentManager();
			PictureInfoDialog pictureInfoDialog = new PictureInfoDialog();
			pictureInfoDialog.setOrnidroidFile(getCurrentMediaFile());
			pictureInfoDialog.show(fm, "pictureInfoDialog");
		}

		if (v == this.zoomButton || v == this.mPicture) {
			final Intent intentImageFullSize = new Intent(getActivity(),
					ScrollableImageActivity.class);
			intentImageFullSize.putExtra(
					ScrollableImageActivity.DISPLAYED_PICTURE_ID,
					displayedPictureId);
			getActivity().startActivity(
					intentImageFullSize
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
		if (v == this.nextButton) {
			loadImage(displayedPictureId + 1);

		}
		if (v == this.previousButton) {
			loadImage(displayedPictureId - 1);
		}

		super.onClick(v);
	}

}
