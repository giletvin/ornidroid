package fr.ornidroid.ui.picture;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.bo.PictureOrnidroidFile;
import fr.ornidroid.ui.BirdActivity;

/**
 * This class handles the view flipper, updates the number of pictures text view
 */
public class PictureHelper {

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
		final Bitmap bMap = BitmapFactory.decodeFile(this.birdActivity
				.getBird().getPicture(index).getPath());
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
	 * Returns the formatted text which displays the number of pictures for this
	 * bird and the current picture number.
	 * 
	 * @return the text
	 */
	public void updateNumberOfPicturesText() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.birdActivity.getDisplayedPictureId() + 1);
		sb.append("/");
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
}
