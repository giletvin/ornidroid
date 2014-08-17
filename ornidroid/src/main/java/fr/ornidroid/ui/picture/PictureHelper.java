package fr.ornidroid.ui.picture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.PictureOrnidroidFile;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.ui.BirdActivity;

/**
 * This class handles the view flipper, updates the number of pictures text view
 */
public class PictureHelper {

	/** The loaded bitmaps. */
	private final static Map<String, Bitmap> LOADED_BITMAPS = new HashMap<String, Bitmap>();
	/**
	 * General left padding
	 */
	private static final int LEFT_PADDING = 25;
	/**
	 * General right padding
	 */
	private static final int RIGHT_PADDING = 20;

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
	 * @deprecated
	 */
	public void displayPictureInfoInDialog(final Dialog dialog) {
		dialog.setContentView(R.layout.picture_info_dialog);
		dialog.setTitle(R.string.dialog_picture_title);
		final OrnidroidFile displayedPicture = this.birdActivity.getBird()
				.getPictures().get(this.birdActivity.getDisplayedPictureId());
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
	 * Creates the header view with the taxon, the nb of pictures and the info
	 * button.
	 * 
	 * @return the view
	 */
	public View getHeaderView() {
		// creation of the main header layout
		final LinearLayout headerLayout = new LinearLayout(this.birdActivity);
		headerLayout.setOrientation(LinearLayout.HORIZONTAL);
		headerLayout.setHorizontalGravity(Gravity.RIGHT);
		headerLayout.setWeightSum(2);
		headerLayout.setPadding(LEFT_PADDING, 10, 5, 5);

		// vertical layout on the left side which contains the name of the bird
		// and the nb of pictures
		final LinearLayout taxonAndNbPicturesLayout = new LinearLayout(
				this.birdActivity);

		taxonAndNbPicturesLayout.setOrientation(LinearLayout.VERTICAL);
		this.birdActivity.setTaxon(new TextView(this.birdActivity));
		this.birdActivity.setNumberOfPicturesTextView(new TextView(
				this.birdActivity));
		taxonAndNbPicturesLayout.addView(this.birdActivity.getTaxon());
		taxonAndNbPicturesLayout.addView(this.birdActivity
				.getNumberOfPicturesTextView());

		taxonAndNbPicturesLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		headerLayout.addView(taxonAndNbPicturesLayout);

		if (this.birdActivity.getBird().getNumberOfPictures() > 0) {
			// a layout with a gravity on the right which contains the info
			// button
			final LinearLayout infoButtonLayout = new LinearLayout(
					this.birdActivity);
			infoButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
			infoButtonLayout.setGravity(Gravity.RIGHT);
			infoButtonLayout.setPadding(5, 10, 5, RIGHT_PADDING);

			// add button to add custom media files
			infoButtonLayout.addView(this.birdActivity
					.getRemoveCustomPictureButton());

			infoButtonLayout.addView(this.birdActivity
					.getAddCustomPictureButton());
			if (!Constants.getAutomaticUpdateCheckPreference()) {

				infoButtonLayout.addView(this.birdActivity
						.getUpdateFilesButton());
			} else {
				this.birdActivity.checkForUpdates(false);
			}

			// info button
			this.birdActivity.setInfoButton(new ImageView(this.birdActivity));
			this.birdActivity.getInfoButton().setPadding(20, 0, 0, 0);
			this.birdActivity.getInfoButton().setOnClickListener(
					this.birdActivity);
			this.birdActivity.getInfoButton().setImageResource(
					R.drawable.ic_info);
			infoButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
			infoButtonLayout.addView(this.birdActivity.getInfoButton());

			headerLayout.addView(infoButtonLayout);
		}

		return headerLayout;
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
		final OrnidroidFile picture = this.birdActivity.getBird().getPicture(
				index);
		this.birdActivity.setCurrentMediaFile(picture);

		Bitmap bMap = loadBitmap(picture, this.birdActivity.getResources());
		if (bMap != null) {
			imagePicture.setImageBitmap(bMap);
			imageAndDescription.addView(imagePicture);
		}

	}

	/**
	 * Load bitmap, either from the HashMap in memory or from the sdcard.
	 * 
	 * @param picture
	 *            the picture
	 * @param resources
	 *            the resources
	 * @return the bitmap
	 */
	public static Bitmap loadBitmap(final OrnidroidFile picture,
			final Resources resources) {
		// try to load the bitmap from cache first
		Bitmap bMap = LOADED_BITMAPS.get(picture.getPath());
		if (bMap == null) {
			// if not in the cache, load the image from sdcard and put the
			// Bitmap in the cache

			try {
				bMap = BitmapFactory.decodeFile(picture.getPath(),
						getBitmapDecodeOptions());
			} catch (final OutOfMemoryError e) {
				// reset the HashMap
				// http://stackoverflow.com/questions/7138645/catching-outofmemoryerror-in-decoding-bitmap
				// try to load another time after a gc
				PictureHelper.resetLoadedBitmaps();
				System.gc();
				try {

					bMap = BitmapFactory.decodeFile(picture.getPath(),
							getBitmapDecodeOptions());
				} catch (final OutOfMemoryError e2) {
					bMap = null;
				}
			}
			if (null == bMap) {
				// error image
				bMap = BitmapFactory.decodeResource(resources,
						R.drawable.error_image);
			} else {
				// ok, bitmap loaded and put in the cache
				LOADED_BITMAPS.put(picture.getPath(), bMap);
			}

		}
		return bMap;
	}

	/**
	 * Gets the bitmap decode options.
	 * 
	 * @return the bitmap decode options
	 */
	private static Options getBitmapDecodeOptions() {
		// http://stackoverflow.com/questions/19678665/bitmapfactory-decodefile-out-of-memory-with-images-2400x2400
		BitmapFactory.Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inDither = true;
		return options;
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
		// PictureHelper.resetLoadedBitmaps();
		if (this.birdActivity.getBird().getNumberOfPictures() > 0) {
			final List<OrnidroidFile> listPictures = this.birdActivity
					.getBird().getPictures();
			for (final OrnidroidFile picture : listPictures) {
				final LinearLayout imageAndDescription = new LinearLayout(
						this.birdActivity);
				imageAndDescription.setOrientation(LinearLayout.VERTICAL);

				final TextView description = new TextView(this.birdActivity);
				description.setPadding(LEFT_PADDING, 0, 0, 0);
				description
						.setText(picture
								.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
				description.setTextAppearance(this.birdActivity,
						android.R.style.TextAppearance_Small);
				imageAndDescription.addView(description);

				this.birdActivity.getViewFlipper().addView(imageAndDescription);
			}
			displayFixedPicture();
			updateNumberOfPicturesText();
			insertBitmapInViewFlipper(this.birdActivity.getDisplayedPictureId());
		} else {
			this.birdActivity.printDownloadButtonAndInfo();
		}
	}

	/**
	 * Reset loaded bitmaps.
	 */
	public static void resetLoadedBitmaps() {
		for (Bitmap b : LOADED_BITMAPS.values()) {
			if (null != b) {
				b.recycle();
			}
		}
		LOADED_BITMAPS.clear();
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
	 * @deprecated
	 */
	private void displayLineInDialog(final Dialog dialog,
			final OrnidroidFile displayedPicture, final int textViewResId,
			final int labelResourceId, final String propertyName) {
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
