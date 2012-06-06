package fr.giletvin.ornidroid.ui.components;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;
import fr.giletvin.ornidroid.helper.BasicConstants;

/**
 * The Class OrnidroidAutoCompleteTextView.<br>
 * Extends AutoCompleteTextView to add the icon on the right side to delete the
 * content of the text field. Code found here
 * http://stackoverflow.com/questions/3554377/handling-click-events
 * -on-a-drawable-within-an-edittext
 * 
 */
public class OrnidroidAutoCompleteTextView extends AutoCompleteTextView {

	/** The d right. */
	private Drawable dRight;

	/** The r bounds. */
	private Rect rBounds;

	/**
	 * Instantiates a new my auto complet text view.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 * @param defStyle
	 *            the def style
	 */
	public OrnidroidAutoCompleteTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Instantiates a new my auto complet text view.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public OrnidroidAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Instantiates a new my auto complet text view.
	 * 
	 * @param context
	 *            the context
	 */
	public OrnidroidAutoCompleteTextView(Context context) {
		super(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.TextView#setCompoundDrawables(android.graphics.drawable
	 * .Drawable, android.graphics.drawable.Drawable,
	 * android.graphics.drawable.Drawable, android.graphics.drawable.Drawable)
	 */
	@Override
	public void setCompoundDrawables(Drawable left, Drawable top,
			Drawable right, Drawable bottom) {
		if (right != null) {
			dRight = right;
		}
		super.setCompoundDrawables(left, top, right, bottom);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.TextView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_UP && dRight != null) {
			rBounds = dRight.getBounds();
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			// System.out.println("x:/y: "+x+"/"+y);
			// System.out.println("bounds: "+bounds.left+"/"+bounds.right+"/"+bounds.top+"/"+bounds.bottom);
			// check to make sure the touch event was within the bounds of the
			// drawable
			if (x >= (this.getRight() - rBounds.width())
					&& x <= (this.getRight() - this.getPaddingRight())
					&& y >= this.getPaddingTop()
					&& y <= (this.getHeight() - this.getPaddingBottom())) {

				this.setText(BasicConstants.EMPTY_STRING);
				// use this to prevent the keyboard from coming up
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		return super.onTouchEvent(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		dRight = null;
		rBounds = null;
		super.finalize();
	}

}
