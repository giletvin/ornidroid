package fr.ornidroid.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
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
public class ScrollableImageActivity extends Activity {
	/** The Constant DISPLAYED_PICTURE_ID. */
	public static final String DISPLAYED_PICTURE_ID = "DISPLAYED_PICTURE_ID";
	/** The image. */
	private ImageView image;

	/** The my. */
	private float curX, curY, mx, my;

	/** The bm large image. */
	private Bitmap bmLargeImage;
	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

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

		this.displayedPictureId = getIntent().getIntExtra(DISPLAYED_PICTURE_ID,
				0);
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
		image.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent event) {

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

	}

}
