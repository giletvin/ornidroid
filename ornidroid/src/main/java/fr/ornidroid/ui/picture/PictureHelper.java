package fr.ornidroid.ui.picture;

import java.util.List;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.bo.PictureOrnidroidFile;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.ui.BirdActivity;

/**
 * This class handles the view flipper, updates the number of pictures text view
 */
public class PictureHelper {

	/**
	 * Try decode bitmap.
	 * 
	 * @param bitmapPath
	 *            the bitmap path
	 * @param resource
	 *            the resource
	 * @return the bitmap. If an OutOfMemoryError occurs, returns a default
	 *         error image to avoid NullPointerExceptions
	 */
	public static Bitmap tryDecodeBitmap(final String bitmapPath,
			final Resources resource) {
		Bitmap bMap = null;
		try {
			bMap = BitmapFactory.decodeFile(bitmapPath);
		} catch (final OutOfMemoryError e) {
			// http://stackoverflow.com/questions/7138645/catching-outofmemoryerror-in-decoding-bitmap
			// try to load another time after a gc
			System.gc();
			try {
				bMap = BitmapFactory.decodeFile(bitmapPath);
			} catch (final OutOfMemoryError e2) {
				bMap = null;
			}
		}
		if (null == bMap) {
			// error image
			bMap = BitmapFactory.decodeResource(resource,
					R.drawable.error_image);
		}
		return bMap;
	}

	/** The bird activity. */
	private final BirdActivity birdActivity;

	/**
	 * Instantiates a new picture helper.
	 * 
	 * @param pBirdActivity
	 *            the bird activity
	 */
	public PictureHelper(final BirdActivity pBirdActivity) {
		this.birdActivity = pBirdActivity;
	}

	/**
	 * Display picture info in dialog.
	 * 
	 * @param dialog
	 *            the dialog
	 */
	public void displayPictureInfoInDialog(final Dialog dialog) {
		dialog.setContentView(R.layout.picture_info_dialog);
		dialog.setTitle(R.string.dialog_picture_title);
		final AbstractOrnidroidFile displayedPicture = this.birdActivity
				.getBird().getPictures()
				.get(this.birdActivity.getDisplayedPictureId());
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

		this.birdActivity.setOkDialogButton((Button) dialog
				.findViewById(R.id.dialog_ok_button));
		this.birdActivity.getOkDialogButton().setOnClickListener(
				this.birdActivity);

	}

	/**
	 * Memory consumption : Insert the bitmap of the given index in view
	 * flipper.
	 * 
	 * @param index
	 *            the index
	 */
	public void insertBitmapInViewFlipper(final int index) {
		final LinearLayout imageAndDescription = (LinearLayout) this.birdActivity
				.getViewFlipper().getChildAt(index);
		final ImageView imagePicture = new ImageView(this.birdActivity);
		final Bitmap bMap = tryDecodeBitmap(this.birdActivity.getBird()
				.getPicture(index).getPath(), this.birdActivity.getResources());
		if (bMap != null) {
			imagePicture.setImageBitmap(bMap);
			imageAndDescription.addView(imagePicture);
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
	public void populateViewFlipper() {
		if (this.birdActivity.getBird().getNumberOfPictures() > 0) {
			final List<AbstractOrnidroidFile> listPictures = this.birdActivity
					.getBird().getPictures();
			for (final AbstractOrnidroidFile picture : listPictures) {
				final LinearLayout imageAndDescription = new LinearLayout(
						this.birdActivity);
				imageAndDescription.setOrientation(LinearLayout.VERTICAL);

				final TextView description = new TextView(this.birdActivity);
				description
						.setText(picture
								.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
				description.setTextAppearance(this.birdActivity,
						android.R.style.TextAppearance_Small);
				imageAndDescription.addView(description);
				this.birdActivity.getViewFlipper().addView(imageAndDescription);
				displayFixedPicture();
				updateNumberOfPicturesText();
			}
			insertBitmapInViewFlipper(this.birdActivity.getDisplayedPictureId());
		} else {
			this.birdActivity.printDownloadButtonAndInfo();
		}
	}

	/**
	 * Reset resources. Set the viewFlipper to null
	 */
	public void resetViewFlipper() {
		this.birdActivity.setViewFlipper(null);
	}

	/**
	 * Returns the formatted text which displays the number of pictures for this
	 * bird and the current picture number.
	 * 
	 * @return the text
	 */
	public void updateNumberOfPicturesText() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.birdActivity.getDisplayedPictureId() + 1);
		sb.append(BasicConstants.SLASH_STRING);
		sb.append(this.birdActivity.getBird().getNumberOfPictures());

		this.birdActivity.getNumberOfPicturesTextView().setText(sb.toString());

	}

	/**
	 * When coming to this screen with a given picture id to display. set the
	 * cursor of the view flipper on the good image to display
	 */
	private void displayFixedPicture() {
		for (int i = 0; i < this.birdActivity.getDisplayedPictureId(); i++) {
			this.birdActivity.getViewFlipper().showNext();
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
		if (StringHelper.isNotBlank(propertyValue)) {
			final TextView textView = (TextView) dialog
					.findViewById(textViewResId);
			textView.setText(this.birdActivity.getString(labelResourceId)
					+ BasicConstants.COLUMN_STRING
					+ displayedPicture.getProperty(propertyName));
		}
	}
}
