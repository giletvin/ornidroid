package fr.giletvin.ornidroid.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import fr.giletvin.ornidroid.bo.AbstractOrnidroidFile;
import fr.giletvin.ornidroid.bo.Bird;
import fr.giletvin.ornidroid.service.IOrnidroidService;
import fr.giletvin.ornidroid.service.OrnidroidServiceFactory;

/**
 * Displays a full sized image. Mainly inspired by
 * http://www.anddev.org/large_image_scrolling_using_low_level_touch_events
 * -t11182.html
 */
public class FullSizeImageActivity extends Activity {

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The display width. */
	private int displayWidth;

	/** The display height. */
	private int displayHeight;

	/** The bm large image. */
	private Bitmap bmLargeImage;

	/** The displayed picture id. */
	private int displayedPictureId;

	/** The gesture detector. */
	private GestureDetector gestureDetector;

	/**
	 * Instantiates a new picture activity.
	 */
	public FullSizeImageActivity() {
		ornidroidService = OrnidroidServiceFactory.getService(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		displayedPictureId = getIntent().getIntExtra(
				PictureActivity.DISPLAYED_PICTURE_ID, 0);
		Bird bird = ornidroidService.getCurrentBird();
		if (bird == null) {
			finish();
		}
		AbstractOrnidroidFile picture = bird.getPicture(displayedPictureId);
		if (null != picture) {
			bmLargeImage = BitmapFactory.decodeFile(picture.getPath());
		}
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();

		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
		gestureDetector = new GestureDetector(new GestureListener(this));
		setContentView(new SampleView(this));
	}

	/**
	 * The Class SampleView.
	 */
	private class SampleView extends View {
		/** The display rect. */
		private Rect displayRect;

		/** The scroll rect. */
		private Rect scrollRect;

		/** The scroll rect x. */
		private int scrollRectX = 0; // current left location of scroll rect

		/** The scroll rect y. */
		private int scrollRectY = 0; // current top location of scroll rect

		/** The scroll by x. */
		private float scrollByX = 0; // x amount to scroll by

		/** The scroll by y. */
		private float scrollByY = 0; // y amount to scroll by

		/** The start x. */
		private float startX = 0; // track x from one ACTION_MOVE to the next

		/** The start y. */
		private float startY = 0; // track y from one ACTION_MOVE to the next

		/**
		 * Instantiates a new sample view.
		 * 
		 * @param pContext
		 *            the context
		 */
		public SampleView(Context pContext) {
			super(pContext);
			displayRect = new Rect(0, 0, displayWidth, displayHeight);
			scrollRect = new Rect(0, 0, displayWidth, displayHeight);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
		 */
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (gestureDetector.onTouchEvent(event)) {
				return true;
			} else {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getRawX();
					startY = event.getRawY();
					break;

				case MotionEvent.ACTION_MOVE:
					float x = event.getRawX();
					float y = event.getRawY();
					scrollByX = x - startX;
					scrollByY = y - startY;
					startX = x;
					startY = y;
					invalidate();
					break;
				}
				return true;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View#onDraw(android.graphics.Canvas)
		 */
		@Override
		protected void onDraw(Canvas canvas) {
			if (null != bmLargeImage) {
				// Our move updates are calculated in ACTION_MOVE in the
				// opposite
				// direction
				// from how we want to move the scroll rect. Think of this as
				// dragging to
				// the left being the same as sliding the scroll rect to the
				// right.
				int newScrollRectX = scrollRectX - (int) scrollByX;
				int newScrollRectY = scrollRectY - (int) scrollByY;

				if (bmLargeImage.getWidth() >= displayWidth) {

					// Don't scroll off the left or right edges of the bitmap.
					if (newScrollRectX < 0)
						newScrollRectX = 0;
					else if (newScrollRectX > (bmLargeImage.getWidth() - displayWidth))
						newScrollRectX = (bmLargeImage.getWidth() - displayWidth);
				}

				if (bmLargeImage.getHeight() >= displayHeight) {
					// Don't scroll off the top or bottom edges of the bitmap.
					if (newScrollRectY < 0)
						newScrollRectY = 0;
					else if (newScrollRectY > (bmLargeImage.getHeight() - displayHeight))
						newScrollRectY = (bmLargeImage.getHeight() - displayHeight);
				}
				// We have our updated scroll rect coordinates, set them and
				// draw.
				scrollRect.set(newScrollRectX, newScrollRectY, newScrollRectX
						+ displayWidth, newScrollRectY + displayHeight);
				Paint paint = new Paint();
				canvas.drawBitmap(bmLargeImage, scrollRect, displayRect, paint);

				// Reset current scroll coordinates to reflect the latest
				// updates,
				// so we can repeat this update process.
				scrollRectX = newScrollRectX;
				scrollRectY = newScrollRectY;
			}
		}

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
		GestureListener(Context pContext) {
			this.context = pContext;

		}

		/**
		 * return to the calling activity
		 * 
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onDoubleTap(android.view.MotionEvent)
		 */
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// deallocate the bitmap and request for a gc.
			bmLargeImage = null;
			System.gc();
			Intent intentBirdInfo = new Intent(context, BirdInfoActivity.class);
			intentBirdInfo.putExtra(PictureActivity.DISPLAYED_PICTURE_ID,
					displayedPictureId);
			startActivity(intentBirdInfo);
			return true;
		}
	}

}
