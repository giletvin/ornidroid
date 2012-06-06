package fr.giletvin.ornidroid.ui;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import fr.giletvin.ornidroid.R;
import fr.giletvin.ornidroid.bo.AbstractOrnidroidFile;
import fr.giletvin.ornidroid.bo.OrnidroidFileType;
import fr.giletvin.ornidroid.bo.PictureOrnidroidFile;

/**
 * Displays a picture.
 */
public class PictureActivity extends AbstractDownloadableMediaActivity
		implements OnClickListener {

	/** The Constant DISPLAYED_PICTURE_ID. */
	public static final String DISPLAYED_PICTURE_ID = "DISPLAYED_PICTURE_ID";
	/** The Constant DIALOG_PICTURE_INFO_ID. */
	private static final int DIALOG_PICTURE_INFO_ID = 0;

	/** The view flipper. */
	private ViewFlipper viewFlipper;
	/** The slide left in. */
	private Animation slideLeftIn;

	/** The slide left out. */
	private Animation slideLeftOut;

	/** The slide right in. */
	private Animation slideRightIn;

	/** The slide right out. */
	private Animation slideRightOut;

	/** The taxon. */
	private TextView taxon;

	/** The number of pictures. */
	private TextView numberOfPictures;

	/** The info button. */
	private ImageView infoButton;

	/** The displayed picture id. */
	private int displayedPictureId;

	/** The dialog. */
	private Dialog dialog;

	/** The ok dialog button. */
	private Button okDialogButton;

	/** The linear layout. */
	private LinearLayout linearLayout;

	/**
	 * Gets the displayed picture id.
	 * 
	 * @return the displayed picture id
	 */
	public int getDisplayedPictureId() {
		return displayedPictureId;
	}

	/**
	 * Instantiates a new picture activity.
	 */
	public PictureActivity() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.ui.AbstractDownloadableMediaActivity#hookOnCreate()
	 */
	@Override
	protected void hookOnCreate() {

		if (getBird() == null) {
			finish();
		} else {
			linearLayout = new LinearLayout(this);

			// retrieve the displayed picture (when coming back from the zoom)
			displayedPictureId = getIntent().getIntExtra(
					PictureActivity.DISPLAYED_PICTURE_ID, 0);

			linearLayout.setOrientation(LinearLayout.VERTICAL);

			linearLayout.addView(createHeaderView());
			setContentView(linearLayout);

			taxon.setText(getBird().getTaxon());

			viewFlipper = new ViewFlipper(this);
			linearLayout.addView(viewFlipper);

			viewFlipper.setInAnimation(this, android.R.anim.fade_in);
			viewFlipper.setOutAnimation(this, android.R.anim.fade_out);

			populateViewFlipper();

		}
	}

	/**
	 * Creates the header view with the taxon, the nb of pictures and the info
	 * button.
	 * 
	 * @return the view
	 */
	private View createHeaderView() {
		// creation of the main header layout
		LinearLayout headerLayout = new LinearLayout(this);
		headerLayout.setOrientation(LinearLayout.HORIZONTAL);
		headerLayout.setHorizontalGravity(Gravity.RIGHT);
		headerLayout.setWeightSum(2);

		// vertical layout on the left side which contains the name of the bird
		// and the nb of pictures
		LinearLayout taxonAndNbPicturesLayout = new LinearLayout(this);
		taxonAndNbPicturesLayout.setOrientation(LinearLayout.VERTICAL);
		taxon = new TextView(this);
		numberOfPictures = new TextView(this);
		taxonAndNbPicturesLayout.addView(taxon);
		taxonAndNbPicturesLayout.addView(numberOfPictures);
		taxonAndNbPicturesLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		headerLayout.addView(taxonAndNbPicturesLayout);

		if (getBird().getNumberOfPictures() > 0) {
			// a layout with a gravity on the right which contains the info
			// button
			LinearLayout infoButtonLayout = new LinearLayout(this);
			infoButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
			infoButtonLayout.setGravity(Gravity.RIGHT);
			infoButton = new ImageView(this);
			infoButton.setOnClickListener(this);
			infoButton.setImageResource(R.drawable.ic_info);
			infoButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
			infoButtonLayout.addView(infoButton);
			headerLayout.addView(infoButtonLayout);
		}

		return headerLayout;
	}

	/**
	 * When coming to this screen with a given picture id to display. set the
	 * cursor of the view flipper on the good image to display
	 */
	private void displayFixedPicture() {
		for (int i = 0; i < displayedPictureId; i++) {
			viewFlipper.showNext();
		}
	}

	/**
	 * Returns the formatted text which displays the number of pictures for this
	 * bird and the current picture number.
	 * 
	 * @return the text
	 */
	private void updateNumberOfPicturesText() {
		StringBuilder sb = new StringBuilder();
		sb.append(displayedPictureId + 1);
		sb.append("/");
		sb.append(getBird().getNumberOfPictures());

		numberOfPictures.setText(sb.toString());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOG_PICTURE_INFO_ID:
			displayPictureInfoInDialog(dialog);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_PICTURE_INFO_ID:
			dialog = new Dialog(this);
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	/**
	 * Show next picture and update the displayed picture id.
	 */
	protected void showNextPicture() {
		removeBitmapInViewFlipper(displayedPictureId);
		displayedPictureId++;
		if (displayedPictureId == getBird().getNumberOfPictures()) {
			displayedPictureId = 0;
		}
		insertBitmapInViewFlipper(displayedPictureId);
		updateNumberOfPicturesText();
		viewFlipper.setInAnimation(slideLeftIn);
		viewFlipper.setOutAnimation(slideLeftOut);
		viewFlipper.showNext();
	}

	/**
	 * Show previous picture and update the displayed picture id.
	 */
	protected void showPreviousPicture() {
		removeBitmapInViewFlipper(displayedPictureId);
		displayedPictureId--;
		if (displayedPictureId == -1) {
			displayedPictureId = getBird().getNumberOfPictures() - 1;
		}
		insertBitmapInViewFlipper(displayedPictureId);
		updateNumberOfPicturesText();
		viewFlipper.setInAnimation(slideRightIn);
		viewFlipper.setOutAnimation(slideRightOut);
		viewFlipper.showPrevious();

	}

	/**
	 * Display picture info in dialog.
	 * 
	 * @param dialog
	 *            the dialog
	 */
	private void displayPictureInfoInDialog(Dialog dialog) {
		dialog.setContentView(R.layout.picture_info_dialog);
		dialog.setTitle(R.string.dialog_picture_title);
		AbstractOrnidroidFile displayedPicture = getBird().getPictures().get(
				displayedPictureId);
		displayLineInDialog(dialog, displayedPicture,
				R.id.dialog_picture_description,
				R.string.dialog_picture_description,
				PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY);
		displayLineInDialog(dialog, displayedPicture,
				R.id.dialog_picture_author, R.string.dialog_picture_author,
				PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY);
		displayLineInDialog(dialog, displayedPicture,
				R.id.dialog_picture_source, R.string.dialog_picture_source,
				PictureOrnidroidFile.IMAGE_SOURCE_PROPERTY);
		displayLineInDialog(dialog, displayedPicture,
				R.id.dialog_picture_licence, R.string.dialog_picture_licence,
				PictureOrnidroidFile.IMAGE_LICENCE_PROPERTY);

		okDialogButton = (Button) dialog.findViewById(R.id.dialog_ok_button);
		okDialogButton.setOnClickListener(this);

	}

	/**
	 * Display line in dialog.<br/>
	 * Example : <br/>
	 * source : http://www.wikipedia.org
	 * 
	 * @param dialog
	 *            dialog
	 * @param displayedPicture
	 *            the displayed picture
	 * @param textViewResId
	 *            the text view res id
	 * @param labelResourceId
	 *            the label resource id
	 * @param propertyName
	 *            the property name
	 */
	private void displayLineInDialog(Dialog dialog,
			AbstractOrnidroidFile displayedPicture, int textViewResId,
			int labelResourceId, String propertyName) {
		String propertyValue = displayedPicture.getProperty(propertyName);
		if (StringUtils.isNotBlank(propertyValue)) {
			TextView textView = (TextView) dialog.findViewById(textViewResId);
			textView.setText(this.getString(labelResourceId) + ": "
					+ displayedPicture.getProperty(propertyName));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		super.onClick(v);
		if (v == infoButton) {
			this.showDialog(DIALOG_PICTURE_INFO_ID);
		}
		if (v == okDialogButton) {
			dialog.dismiss();
		}
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
		if (getBird().getNumberOfPictures() > 0) {
			List<AbstractOrnidroidFile> listPictures = getBird().getPictures();
			for (AbstractOrnidroidFile picture : listPictures) {
				LinearLayout imageAndDescription = new LinearLayout(this);
				imageAndDescription.setOrientation(LinearLayout.VERTICAL);

				TextView description = new TextView(this);
				description
						.setText(picture
								.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
				description.setTextAppearance(this,
						android.R.style.TextAppearance_Small);
				imageAndDescription.addView(description);
				viewFlipper.addView(imageAndDescription);
				displayFixedPicture();
				updateNumberOfPicturesText();
			}
			insertBitmapInViewFlipper(displayedPictureId);
		} else {
			printDownloadButtonAndInfo();
		}
	}

	/**
	 * Memory consumption : Insert the bitmap of the given index in view
	 * flipper.
	 * 
	 * @param index
	 *            the index
	 */
	private void insertBitmapInViewFlipper(int index) {
		LinearLayout imageAndDescription = (LinearLayout) viewFlipper
				.getChildAt(index);
		ImageView imagePicture = new ImageView(this);
		Bitmap bMap = BitmapFactory.decodeFile(getBird().getPicture(index)
				.getPath());
		imagePicture.setImageBitmap(bMap);
		imageAndDescription.addView(imagePicture);
	}

	/**
	 * Memory consumption : Removes the bitmap of the given index in view
	 * flipper.
	 * 
	 * @param index
	 *            the index
	 */
	private void removeBitmapInViewFlipper(int index) {
		LinearLayout imageAndDescription = (LinearLayout) viewFlipper
				.getChildAt(index);
		imageAndDescription.removeViewAt(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.giletvin.ornidroid.ui.AbstractDownloadableMediaActivity#getFileType()
	 */
	@Override
	public OrnidroidFileType getFileType() {
		return OrnidroidFileType.PICTURE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.giletvin.ornidroid.ui.AbstractDownloadableMediaActivity#
	 * getSpecificContentLayout()
	 */
	@Override
	protected LinearLayout getSpecificContentLayout() {
		return linearLayout;
	}

	/**
	 * Reset resources.
	 */
	protected void resetResources() {
		viewFlipper = null;
	}

}
