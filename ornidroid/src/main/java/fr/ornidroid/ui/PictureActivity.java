package fr.ornidroid.ui;

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
import fr.ornidroid.R;
import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.bo.PictureOrnidroidFile;

/**
 * Displays a picture.
 */
public class PictureActivity extends AbstractDownloadableMediaActivity
		implements OnClickListener {

	/** The Constant DISPLAYED_PICTURE_ID. */
	public static final String DISPLAYED_PICTURE_ID = "DISPLAYED_PICTURE_ID";
	/** The Constant DIALOG_PICTURE_INFO_ID. */
	private static final int DIALOG_PICTURE_INFO_ID = 0;

	/** The dialog. */
	private Dialog dialog;
	/** The displayed picture id. */
	private int displayedPictureId;

	/** The info button. */
	private ImageView infoButton;

	/** The linear layout. */
	private LinearLayout linearLayout;

	/** The number of pictures. */
	private TextView numberOfPictures;

	/** The ok dialog button. */
	private Button okDialogButton;

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

	/** The view flipper. */
	private ViewFlipper viewFlipper;

	/**
	 * Instantiates a new picture activity.
	 */
	public PictureActivity() {
		super();
	}

	/**
	 * Gets the displayed picture id.
	 * 
	 * @return the displayed picture id
	 */
	public int getDisplayedPictureId() {
		return this.displayedPictureId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.ui.AbstractDownloadableMediaActivity#getFileType()
	 */
	@Override
	public OrnidroidFileType getFileType() {
		return OrnidroidFileType.PICTURE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(final View v) {
		super.onClick(v);
		if (v == this.infoButton) {
			this.showDialog(DIALOG_PICTURE_INFO_ID);
		}
		if (v == this.okDialogButton) {
			this.dialog.dismiss();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.ui.AbstractDownloadableMediaActivity#
	 * getSpecificContentLayout()
	 */
	@Override
	protected LinearLayout getSpecificContentLayout() {
		return this.linearLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.ui.AbstractDownloadableMediaActivity#hookOnCreate()
	 */
	@Override
	protected void hookOnCreate() {

		if (getBird() == null) {
			finish();
		} else {
			this.linearLayout = new LinearLayout(this);

			// retrieve the displayed picture (when coming back from the zoom)
			this.displayedPictureId = getIntent().getIntExtra(
					PictureActivity.DISPLAYED_PICTURE_ID, 0);

			this.linearLayout.setOrientation(LinearLayout.VERTICAL);

			this.linearLayout.addView(createHeaderView());
			setContentView(this.linearLayout);

			this.taxon.setText(getBird().getTaxon());

			this.viewFlipper = new ViewFlipper(this);
			this.linearLayout.addView(this.viewFlipper);

			this.viewFlipper.setInAnimation(this, android.R.anim.fade_in);
			this.viewFlipper.setOutAnimation(this, android.R.anim.fade_out);

			populateViewFlipper();

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(final int id) {
		switch (id) {
		case DIALOG_PICTURE_INFO_ID:
			this.dialog = new Dialog(this);
			break;
		default:
			this.dialog = null;
		}
		return this.dialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
	@Override
	protected void onPrepareDialog(final int id, final Dialog dialog) {
		switch (id) {
		case DIALOG_PICTURE_INFO_ID:
			displayPictureInfoInDialog(dialog);
			break;
		}
	}

	/**
	 * Reset resources.
	 */
	protected void resetResources() {
		this.viewFlipper = null;
	}

	/**
	 * Show next picture and update the displayed picture id.
	 */
	protected void showNextPicture() {
		removeBitmapInViewFlipper(this.displayedPictureId);
		this.displayedPictureId++;
		if (this.displayedPictureId == getBird().getNumberOfPictures()) {
			this.displayedPictureId = 0;
		}
		insertBitmapInViewFlipper(this.displayedPictureId);
		updateNumberOfPicturesText();
		this.viewFlipper.setInAnimation(this.slideLeftIn);
		this.viewFlipper.setOutAnimation(this.slideLeftOut);
		this.viewFlipper.showNext();
	}

	/**
	 * Show previous picture and update the displayed picture id.
	 */
	protected void showPreviousPicture() {
		removeBitmapInViewFlipper(this.displayedPictureId);
		this.displayedPictureId--;
		if (this.displayedPictureId == -1) {
			this.displayedPictureId = getBird().getNumberOfPictures() - 1;
		}
		insertBitmapInViewFlipper(this.displayedPictureId);
		updateNumberOfPicturesText();
		this.viewFlipper.setInAnimation(this.slideRightIn);
		this.viewFlipper.setOutAnimation(this.slideRightOut);
		this.viewFlipper.showPrevious();

	}

	/**
	 * Creates the header view with the taxon, the nb of pictures and the info
	 * button.
	 * 
	 * @return the view
	 */
	private View createHeaderView() {
		// creation of the main header layout
		final LinearLayout headerLayout = new LinearLayout(this);
		headerLayout.setOrientation(LinearLayout.HORIZONTAL);
		headerLayout.setHorizontalGravity(Gravity.RIGHT);
		headerLayout.setWeightSum(2);

		// vertical layout on the left side which contains the name of the bird
		// and the nb of pictures
		final LinearLayout taxonAndNbPicturesLayout = new LinearLayout(this);
		taxonAndNbPicturesLayout.setOrientation(LinearLayout.VERTICAL);
		this.taxon = new TextView(this);
		this.numberOfPictures = new TextView(this);
		taxonAndNbPicturesLayout.addView(this.taxon);
		taxonAndNbPicturesLayout.addView(this.numberOfPictures);
		taxonAndNbPicturesLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		headerLayout.addView(taxonAndNbPicturesLayout);

		if (getBird().getNumberOfPictures() > 0) {
			// a layout with a gravity on the right which contains the info
			// button
			final LinearLayout infoButtonLayout = new LinearLayout(this);
			infoButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
			infoButtonLayout.setGravity(Gravity.RIGHT);
			this.infoButton = new ImageView(this);
			this.infoButton.setOnClickListener(this);
			this.infoButton.setImageResource(R.drawable.ic_info);
			infoButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
			infoButtonLayout.addView(this.infoButton);
			headerLayout.addView(infoButtonLayout);
		}

		return headerLayout;
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
	private void displayLineInDialog(final Dialog dialog,
			final AbstractOrnidroidFile displayedPicture,
			final int textViewResId, final int labelResourceId,
			final String propertyName) {
		final String propertyValue = displayedPicture.getProperty(propertyName);
		if (StringUtils.isNotBlank(propertyValue)) {
			final TextView textView = (TextView) dialog
					.findViewById(textViewResId);
			textView.setText(this.getString(labelResourceId) + ": "
					+ displayedPicture.getProperty(propertyName));
		}
	}

	/**
	 * Display picture info in dialog.
	 * 
	 * @param dialog
	 *            the dialog
	 */
	private void displayPictureInfoInDialog(final Dialog dialog) {
		dialog.setContentView(R.layout.picture_info_dialog);
		dialog.setTitle(R.string.dialog_picture_title);
		final AbstractOrnidroidFile displayedPicture = getBird().getPictures()
				.get(this.displayedPictureId);
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

		this.okDialogButton = (Button) dialog
				.findViewById(R.id.dialog_ok_button);
		this.okDialogButton.setOnClickListener(this);

	}

	/**
	 * Memory consumption : Insert the bitmap of the given index in view
	 * flipper.
	 * 
	 * @param index
	 *            the index
	 */
	private void insertBitmapInViewFlipper(final int index) {
		final LinearLayout imageAndDescription = (LinearLayout) this.viewFlipper
				.getChildAt(index);
		final ImageView imagePicture = new ImageView(this);
		final Bitmap bMap = BitmapFactory.decodeFile(getBird()
				.getPicture(index).getPath());
		imagePicture.setImageBitmap(bMap);
		imageAndDescription.addView(imagePicture);
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
			final List<AbstractOrnidroidFile> listPictures = getBird()
					.getPictures();
			for (final AbstractOrnidroidFile picture : listPictures) {
				final LinearLayout imageAndDescription = new LinearLayout(this);
				imageAndDescription.setOrientation(LinearLayout.VERTICAL);

				final TextView description = new TextView(this);
				description
						.setText(picture
								.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
				description.setTextAppearance(this,
						android.R.style.TextAppearance_Small);
				imageAndDescription.addView(description);
				this.viewFlipper.addView(imageAndDescription);
				displayFixedPicture();
				updateNumberOfPicturesText();
			}
			insertBitmapInViewFlipper(this.displayedPictureId);
		} else {
			printDownloadButtonAndInfo();
		}
	}

	/**
	 * Memory consumption : Removes the bitmap of the given index in view
	 * flipper.
	 * 
	 * @param index
	 *            the index
	 */
	private void removeBitmapInViewFlipper(final int index) {
		final LinearLayout imageAndDescription = (LinearLayout) this.viewFlipper
				.getChildAt(index);
		imageAndDescription.removeViewAt(1);
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
		sb.append("/");
		sb.append(getBird().getNumberOfPictures());

		this.numberOfPictures.setText(sb.toString());

	}

}
