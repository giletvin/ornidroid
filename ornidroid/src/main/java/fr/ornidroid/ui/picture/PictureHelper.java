package fr.ornidroid.ui.picture;

import java.util.HashMap;
import java.util.Map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFile;

/**
 * The Class PictureHelper.
 */
public class PictureHelper {

	/** The loaded bitmaps. */
	private final static Map<String, Bitmap> LOADED_BITMAPS = new HashMap<String, Bitmap>();

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
	 * Reset loaded bitmaps.
	 */
	public static void resetLoadedBitmaps() {
		// TODO : Pb avec l'appel à recycle
		// for (Bitmap b : LOADED_BITMAPS.values()) {
		// if (null != b) {
		// // b.recycle();
		// }
		// }TODO : Pb avec l'appel à recycle
		LOADED_BITMAPS.clear();
	}

}
