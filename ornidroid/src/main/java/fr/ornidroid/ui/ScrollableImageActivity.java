package fr.ornidroid.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import fr.ornidroid.R;
import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.picture.PictureHelper;

/**
 * The Class ScrollableImageActivity.
 */
public class ScrollableImageActivity extends Activity {

	/** The image. */
	private ImageView image;

	/** The my. */
	private float curX, curY, mx, my;

	/** The bm large image. */
	private Bitmap bmLargeImage;
	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;
	/** The gesture detector. */
	private GestureDetector gestureDetector;
	/** The gesture listener. */
	// private View.OnTouchListener gestureListener;

	/** The displayed picture id. */
	private int displayedPictureId;

	/**
	 * Instantiates a new scrollable image activity.
	 */
	public ScrollableImageActivity() {
		super();
		Constants.initializeConstants(this);
		this.ornidroidService = OrnidroidServiceFactory.getService(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.scrollableimage);

		image = (ImageView) this.findViewById(R.id.scrollableimage);

		this.displayedPictureId = getIntent().getIntExtra(
				BirdActivity.DISPLAYED_PICTURE_ID, 0);
		final Bird bird = this.ornidroidService.getCurrentBird();
		if (bird == null) {
			finish();
		}
		final AbstractOrnidroidFile picture = bird
				.getPicture(this.displayedPictureId);
		if (null != picture) {
			this.bmLargeImage = PictureHelper.loadBitmap(picture,
					getResources());
		}

		image.setImageBitmap(bmLargeImage);
		image.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent event) {
				// checks the double tap before to go back to the previous
				// screen
				if (ScrollableImageActivity.this.gestureDetector
						.onTouchEvent(event)) {
					return true;
				}

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

				return true;
			}
		});
		this.gestureDetector = new GestureDetector(this, new GestureListener(
				this));

	}

	/**
	 * The listener interface for receiving gesture events. The class that is
	 * interested in processing a gesture event implements this interface, and
	 * the object created with that class is registered with a component using
	 * the component's <code>addGestureListener<code> method. When
	 * the gesture event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see GestureEvent
	 */
	class GestureListener extends SimpleOnGestureListener {

		/** The context. */
		private final Context context;

		/**
		 * Instantiates a new gesture listener.
		 * 
		 * @param pContext
		 *            the context
		 */
		GestureListener(final Context pContext) {
			this.context = pContext;

		}

		/**
		 * return to the calling activity.
		 * 
		 * @param e
		 *            the e
		 * @return true, if successful
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onDoubleTap(android.view.MotionEvent)
		 */
		@Override
		public boolean onDoubleTap(final MotionEvent e) {
			// deallocate the bitmap and request for a gc.
			ScrollableImageActivity.this.bmLargeImage = null;
			System.gc();
			final Intent intentBirdInfo = new Intent(this.context,
					BirdActivity.class);
			intentBirdInfo.putExtra(BirdActivity.DISPLAYED_PICTURE_ID,
					ScrollableImageActivity.this.displayedPictureId);
			startActivity(intentBirdInfo
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		}

	}

}
