package fr.ornidroid.ui.picture;

import android.content.Intent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import fr.ornidroid.ui.BirdActivity;
import fr.ornidroid.ui.ScrollableImageActivity;

/**
 * The listener interface for receiving gesture events. The class that is
 * interested in processing a gesture event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addGestureListener<code> method. When
 * the gesture event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see GestureEvent
 * @deprecated
 */
public class BirdActivityGestureListener extends SimpleOnGestureListener {
	/** The Constant SWIPE_MAX_OFF_PATH. */
	private static final int SWIPE_MAX_OFF_PATH = 250;

	/** The Constant SWIPE_MIN_DISTANCE. */
	private static final int SWIPE_MIN_DISTANCE = 120;

	/** The Constant SWIPE_THRESHOLD_VELOCITY. */
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	/** The bird activity. */
	private final BirdActivity birdActivity;
	/** The slide left in. */
	private Animation slideLeftIn;
	/** The slide left out. */
	private Animation slideLeftOut;
	/** The slide right in. */
	private Animation slideRightIn;
	/** The slide right out. */
	private Animation slideRightOut;

	/**
	 * Instantiates a new bird activity gesture listener.
	 * 
	 * @param pBirdActivity
	 *            the bird activity
	 */
	public BirdActivityGestureListener(final BirdActivity pBirdActivity) {
		this.birdActivity = pBirdActivity;
	}

	/**
	 * On double tap. If the double tap occurs on picture activity, open the
	 * full size picture activity which displays the original full size picture
	 * 
	 * @param e
	 *            the e
	 * @return true, if successful
	 */
	@Override
	public boolean onDoubleTap(final MotionEvent e) {
		if (BirdActivity.PICTURES_TAB_NAME.equals(this.birdActivity
				.getCurrentTabTag())) {

			final int displayedPictureId = this.birdActivity
					.getDisplayedPictureId();
			this.birdActivity.getPictureHelper().resetViewFlipper();
			final Intent intentImageFullSize = new Intent(this.birdActivity,
					ScrollableImageActivity.class);
			intentImageFullSize.putExtra(BirdActivity.DISPLAYED_PICTURE_ID,
					displayedPictureId);
			this.birdActivity.startActivity(intentImageFullSize
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Handles the flip between pictures in the PictureActivity (non-Javadoc)
	 * 
	 * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(android
	 * .view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(final MotionEvent e1, final MotionEvent e2,
			final float velocityX, final float velocityY) {
		if (BirdActivity.PICTURES_TAB_NAME.equals(this.birdActivity
				.getCurrentTabTag())) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
					return false;
				}
				// right to left swipe
				if (((e1.getX() - e2.getX()) > SWIPE_MIN_DISTANCE)
						&& (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {

					// photoActivity.getViewFlipper().setInAnimation(
					// slideLeftIn);
					// photoActivity.getViewFlipper().setOutAnimation(
					// slideLeftOut);
					showNextPicture();

				} else if (((e2.getX() - e1.getX()) > SWIPE_MIN_DISTANCE)
						&& (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {

					// photoActivity.getViewFlipper().setInAnimation(
					// slideRightIn);
					// photoActivity.getViewFlipper().setOutAnimation(
					// slideRightOut);
					showPreviousPicture();
				}
			} catch (final Exception e) {
				// Log.e(Constants.LOG_TAG, "Exception occured in onFling",
				// e);
			}
		}
		return false;
	}

	/**
	 * Show next picture and update the displayed picture id.
	 */
	public void showNextPicture() {
		removeBitmapInViewFlipper(this.birdActivity.getDisplayedPictureId());
		this.birdActivity.setDisplayedPictureId(this.birdActivity
				.getDisplayedPictureId() + 1);
		if (this.birdActivity.getDisplayedPictureId() == this.birdActivity
				.getBird().getNumberOfPictures()) {
			this.birdActivity.setDisplayedPictureId(0);
		}
		this.birdActivity.getPictureHelper().insertBitmapInViewFlipper(
				this.birdActivity.getDisplayedPictureId());
		this.birdActivity.getPictureHelper().updateNumberOfPicturesText();
		this.birdActivity.getViewFlipper().setInAnimation(this.slideLeftIn);
		this.birdActivity.getViewFlipper().setOutAnimation(this.slideLeftOut);
		this.birdActivity.getViewFlipper().showNext();
	}

	/**
	 * Show previous picture and update the displayed picture id.
	 */
	public void showPreviousPicture() {
		removeBitmapInViewFlipper(this.birdActivity.getDisplayedPictureId());
		this.birdActivity.setDisplayedPictureId(this.birdActivity
				.getDisplayedPictureId() - 1);
		if (this.birdActivity.getDisplayedPictureId() == -1) {
			this.birdActivity.setDisplayedPictureId(this.birdActivity.getBird()
					.getNumberOfPictures() - 1);

		}
		this.birdActivity.getPictureHelper().insertBitmapInViewFlipper(
				this.birdActivity.getDisplayedPictureId());
		this.birdActivity.getPictureHelper().updateNumberOfPicturesText();
		this.birdActivity.getViewFlipper().setInAnimation(this.slideRightIn);
		this.birdActivity.getViewFlipper().setOutAnimation(this.slideRightOut);
		this.birdActivity.getViewFlipper().showPrevious();

	}

	/**
	 * Memory consumption : Removes the bitmap of the given index in view
	 * flipper.
	 * 
	 * @param index
	 *            the index
	 */
	private void removeBitmapInViewFlipper(final int index) {
		final LinearLayout imageAndDescription = (LinearLayout) this.birdActivity
				.getViewFlipper().getChildAt(index);
		imageAndDescription.removeViewAt(1);
	}
}
