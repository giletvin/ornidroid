package fr.ornidroid.ui.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.graphics.Bitmap;
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
 */
@EActivity(R.layout.scrollableimage)
public class ScrollableImageActivity extends Activity {
	/** The Constant DISPLAYED_PICTURE_ID. */
	public static final String DISPLAYED_PICTURE_ID = "DISPLAYED_PICTURE_ID";
	/** The image. */
	@ViewById(R.id.scrollableimage)
	ImageView image;

	/** The my. */
	private float curX, curY, mx, my;

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
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			mx = event.getX();
			my = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			curX = event.getX();
			curY = event.getY();
			image.scrollBy((int) (mx - curX), (int) (my - curY));
			mx = curX;
			my = curY;
			break;
		case MotionEvent.ACTION_UP:
			curX = event.getX();
			curY = event.getY();
			image.scrollBy((int) (mx - curX), (int) (my - curY));
			break;
		}
	}

}
