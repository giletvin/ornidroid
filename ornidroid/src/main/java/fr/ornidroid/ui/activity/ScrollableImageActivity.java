package fr.ornidroid.ui.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.ImageView;
import fr.ornidroid.R;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.picture.PictureHelper;

/**
 * The Class ScrollableImageActivity.
 * https://androidcookbook.com/Recipe.seam?recipeId=2273
 */
@EActivity(R.layout.scrollableimage)
public class ScrollableImageActivity extends Activity {
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	/** The Constant DISPLAYED_PICTURE_ID. */
	public static final String DISPLAYED_PICTURE_ID = "DISPLAYED_PICTURE_ID";
	/** The image. */
	@ViewById(R.id.scrollableimage)
	ImageView image;

	/** The bm large image. */
	private Bitmap bmLargeImage;
	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService = OrnidroidServiceFactory
			.getService(this);;

	/** The displayed picture id. */
	@Extra(DISPLAYED_PICTURE_ID)
	int displayedPictureId = 0;

	/**
	 * After views.
	 */
	@AfterViews
	public void afterViews() {
		Constants.initializeConstants(this);

		final Bird bird = this.ornidroidService.getCurrentBird();
		if (bird == null) {
			finish();
		}
		final OrnidroidFile picture = bird.getPicture(this.displayedPictureId);
		if (null != picture) {
			this.bmLargeImage = PictureHelper.loadBitmap(picture,
					getResources());
		}

		Display display = getWindowManager().getDefaultDisplay();

		int screenHeight = display.getHeight();
		double imageWidth = bmLargeImage.getWidth();
		double imageHeight = bmLargeImage.getHeight();
		double scaleHeight = screenHeight / imageHeight;
		Long scaledWidth = Math.round(imageWidth * scaleHeight);

		this.bmLargeImage = Bitmap.createScaledBitmap(this.bmLargeImage,
				scaledWidth.intValue(), screenHeight, true);
		image.setImageBitmap(bmLargeImage);

	}

	/**
	 * Image touched.
	 * 
	 * @param event
	 *            the event
	 */
	@Touch(R.id.scrollableimage)
	public void imageTouched(MotionEvent event) {

		// make the image scalable as a matrix
		image.setScaleType(ImageView.ScaleType.MATRIX);
		float scale;

		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN: // first finger down only
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());

			mode = DRAG;
			break;
		case MotionEvent.ACTION_UP: // first finger lifted
		case MotionEvent.ACTION_POINTER_UP: // second finger lifted
			mode = NONE;

			break;
		case MotionEvent.ACTION_POINTER_DOWN: // second finger down
			oldDist = spacing(event); // calculates the distance between two
										// points where user touched.

			// minimal distance between both the fingers
			if (oldDist > 5f) {
				savedMatrix.set(matrix);
				midPoint(mid, event); // sets the mid-point of the straight line
										// between two points where user
										// touched.
				mode = ZOOM;

			}
			break;

		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) { // movement of first finger
				matrix.set(savedMatrix);
				if (image.getLeft() >= -392) {
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);
				}
			} else if (mode == ZOOM) { // pinch zooming
				float newDist = spacing(event);

				if (newDist > 5f) {
					matrix.set(savedMatrix);
					scale = newDist / oldDist; // thinking I need to play around
												// with this value to limit it**
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}

		// Perform the transformation
		image.setImageMatrix(matrix);

	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

}
