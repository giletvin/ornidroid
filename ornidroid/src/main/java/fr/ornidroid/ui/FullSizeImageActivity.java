package fr.ornidroid.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.picture.PictureHelper;

/**
 * Displays a full sized image. Mainly inspired by
 * http://www.anddev.org/large_image_scrolling_using_low_level_touch_events
 * -t11182.html
 */
public class FullSizeImageActivity extends Activity {

	/**
	 * The Class SampleView.
	 */
	private class SampleView extends View {
		/** The display rect. */
		private final Rect displayRect;

		/** The scroll by x. */
		private float scrollByX = 0; // x amount to scroll by

		/** The scroll by y. */
		private float scrollByY = 0; // y amount to scroll by

		/** The scroll rect. */
		private final Rect scrollRect;

		/** The scroll rect x. */
		private int scrollRectX = 0; // current left location of scroll rect

		/** The scroll rect y. */
		private int scrollRectY = 0; // current top location of scroll rect

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
		public SampleView(final Context pContext) {
			super(pContext);
			this.displayRect = new Rect(0, 0,
					FullSizeImageActivity.this.displayWidth,
					FullSizeImageActivity.this.displayHeight);
			this.scrollRect = new Rect(0, 0,
					FullSizeImageActivity.this.displayWidth,
					FullSizeImageActivity.this.displayHeight);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
		 */
		@Override
		public boolean onTouchEvent(final MotionEvent event) {
			if (FullSizeImageActivity.this.gestureDetector.onTouchEvent(event)) {
				return true;
			} else {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					this.startX = event.getRawX();
					this.startY = event.getRawY();
					break;

				case MotionEvent.ACTION_MOVE:
					final float x = event.getRawX();
					final float y = event.getRawY();
					this.scrollByX = x - this.startX;
					this.scrollByY = y - this.startY;
					this.startX = x;
					this.startY = y;
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
		protected void onDraw(final Canvas canvas) {
			if (null != FullSizeImageActivity.this.bmLargeImage) {
				// Our move updates are calculated in ACTION_MOVE in the
				// opposite
				// direction
				// from how we want to move the scroll rect. Think of this as
				// dragging to
				// the left being the same as sliding the scroll rect to the
				// right.
				int newScrollRectX = this.scrollRectX - (int) this.scrollByX;
				int newScrollRectY = this.scrollRectY - (int) this.scrollByY;

				if (FullSizeImageActivity.this.bmLargeImage.getWidth() >= FullSizeImageActivity.this.displayWidth) {

					// Don't scroll off the left or right edges of the bitmap.
					if (newScrollRectX < 0) {
						newScrollRectX = 0;
					} else if (newScrollRectX > (FullSizeImageActivity.this.bmLargeImage
							.getWidth() - FullSizeImageActivity.this.displayWidth)) {
						newScrollRectX = (FullSizeImageActivity.this.bmLargeImage
								.getWidth() - FullSizeImageActivity.this.displayWidth);
					}
				}

				if (FullSizeImageActivity.this.bmLargeImage.getHeight() >= FullSizeImageActivity.this.displayHeight) {
					// Don't scroll off the top or bottom edges of the bitmap.
					if (newScrollRectY < 0) {
						newScrollRectY = 0;
					} else if (newScrollRectY > (FullSizeImageActivity.this.bmLargeImage
							.getHeight() - FullSizeImageActivity.this.displayHeight)) {
						newScrollRectY = (FullSizeImageActivity.this.bmLargeImage
								.getHeight() - FullSizeImageActivity.this.displayHeight);
					}
				}
				// We have our updated scroll rect coordinates, set them and
				// draw.
				this.scrollRect.set(newScrollRectX, newScrollRectY,
						newScrollRectX
								+ FullSizeImageActivity.this.displayWidth,
						newScrollRectY
								+ FullSizeImageActivity.this.displayHeight);
				final Paint paint = new Paint();
				canvas.drawBitmap(FullSizeImageActivity.this.bmLargeImage,
						this.scrollRect, this.displayRect, paint);

				// Reset current scroll coordinates to reflect the latest
				// updates,
				// so we can repeat this update process.
				this.scrollRectX = newScrollRectX;
				this.scrollRectY = newScrollRectY;
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
		GestureListener(final Context pContext) {
			this.context = pContext;

		}

		/**
		 * return to the calling activity
		 * 
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onDoubleTap(android.view.MotionEvent)
		 */
		@Override
		public boolean onDoubleTap(final MotionEvent e) {
			// deallocate the bitmap and request for a gc.
			if (null != FullSizeImageActivity.this.bmLargeImage) {
				FullSizeImageActivity.this.bmLargeImage.recycle();
			}
			FullSizeImageActivity.this.bmLargeImage = null;
			System.gc();
			final Intent intentBirdInfo = new Intent(this.context,
					BirdActivity.class);
			intentBirdInfo.putExtra(BirdActivity.DISPLAYED_PICTURE_ID,
					FullSizeImageActivity.this.displayedPictureId);
			startActivity(intentBirdInfo
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.view.GestureDetector.SimpleOnGestureListener#onFling(android
		 * .view.MotionEvent, android.view.MotionEvent, float, float)
		 */
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// consume the on fling event so that nothing occurs
			return true;
		}
	}

	/** The bm large image. */
	private Bitmap bmLargeImage;

	/** The displayed picture id. */
	private int displayedPictureId;

	/** The display height. */
	private int displayHeight;

	/** The display width. */
	private int displayWidth;

	/** The gesture detector. */
	private GestureDetector gestureDetector;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/**
	 * Instantiates a new picture activity.
	 */
	public FullSizeImageActivity() {
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
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.displayedPictureId = getIntent().getIntExtra(
				BirdActivity.DISPLAYED_PICTURE_ID, 0);
		final Bird bird = this.ornidroidService.getCurrentBird();
		if (bird == null) {
			finish();
		}
		final AbstractOrnidroidFile picture = bird
				.getPicture(this.displayedPictureId);
		if (null != picture) {
			this.bmLargeImage = PictureHelper.tryDecodeBitmap(
					picture.getPath(), getResources());
		}
		final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();

		this.displayWidth = display.getWidth();
		this.displayHeight = display.getHeight();
		this.gestureDetector = new GestureDetector(new GestureListener(this));
		setContentView(new SampleView(this));
	}

}
