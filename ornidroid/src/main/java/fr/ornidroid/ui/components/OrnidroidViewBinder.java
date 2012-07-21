package fr.ornidroid.ui.components;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.bo.SimpleBird;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;

/**
 * The Class OrnidroidViewBinder. This class handles the birds displayed in the
 * results list of a search.
 * 
 * the methode setViewValue searches the bird icon from the assets directory
 */
public class OrnidroidViewBinder implements ViewBinder {

	/**
	 * Instantiates a new ornidroid view binder.
	 */
	public OrnidroidViewBinder() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.SimpleAdapter.ViewBinder#setViewValue(android.view.View,
	 * java.lang.Object, java.lang.String)
	 */
	public boolean setViewValue(final View view, final Object data,
			final String textRepresentation) {

		final SimpleBird bird = (SimpleBird) data;
		if (null != bird.getBirdDirectoryName()) {
			// load the bird icon from the assets directory
			final TextView tv = (TextView) view;
			final AssetManager assetManager = Constants.getCONTEXT()
					.getAssets();
			InputStream ins;
			Bitmap bMap;
			try {
				ins = assetManager.open(BasicConstants.BIRD_ICONS_DIRECTORY
						+ File.separator + bird.getBirdDirectoryName()
						+ OrnidroidFileType.PICTURE_EXTENSION);
				bMap = BitmapFactory.decodeStream(ins);

			} catch (final IOException e) {
				bMap = BitmapFactory.decodeResource(Constants.getCONTEXT()
						.getResources(), R.drawable.ic_default_bird_icon);
			}
			tv.setText(textRepresentation);

			// put the bird icon into the ImageView defined in the results
			// layout
			final LinearLayout layout = (LinearLayout) tv.getParent()
					.getParent();
			final ImageView iview = (ImageView) layout.getChildAt(0);
			iview.setImageBitmap(bMap);
			return true;
		} else {
			return false;
		}
	}

}
