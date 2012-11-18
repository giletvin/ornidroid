package fr.ornidroid.ui.multicriteriasearch;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import fr.ornidroid.R;

/**
 * The Class MultiCriteriaSelectField.
 * http://kevindion.com/2011/01/custom-xml-attributes-for-android-widgets/
 */
public class MultiCriteriaSelectField extends LinearLayout {

	/** The field type. */
	private MultiCriteriaSearchFieldType fieldType;

	/** The icon. */
	private final ImageView icon;

	/** The spinner. */
	private final Spinner spinner;

	/** The text view. */
	private final TextView textView;

	/**
	 * Instantiates a new multi criteria select field.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public MultiCriteriaSelectField(final Context context,
			final AttributeSet attrs) {
		super(context, attrs);

		this.textView = new TextView(context);
		this.icon = new ImageView(context);
		final LinearLayout layoutTextIcon = initTextIconLayout(context);

		this.spinner = new Spinner(context);
		this.spinner.setVisibility(View.GONE);
		this.spinner.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		this.addView(layoutTextIcon);
		this.addView(this.spinner);

		// add click behaviour on the text and icon
		layoutTextIcon.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				if (MultiCriteriaSelectField.this.spinner.getVisibility() == View.VISIBLE) {
					MultiCriteriaSelectField.this.spinner
							.setVisibility(View.GONE);
					MultiCriteriaSelectField.this.icon
							.setImageResource(R.drawable.ic_down);
				} else {
					MultiCriteriaSelectField.this.spinner
							.setVisibility(View.VISIBLE);
					MultiCriteriaSelectField.this.icon
							.setImageResource(R.drawable.ic_up);
				}
			}
		});

		final TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.MultiCriteriaSelectField);

		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i) {
			final int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.MultiCriteriaSelectField_text:
				final String myText = a.getString(attr);
				this.textView.setText(myText);
				break;
			case R.styleable.MultiCriteriaSelectField_spinnerPrompt:
				final String spinnerPrompt = a.getString(attr);
				this.spinner.setPrompt(spinnerPrompt);
				break;

			case R.styleable.MultiCriteriaSelectField_textBackground:
				final boolean textBackground = a.getBoolean(attr, false);
				if (textBackground) {
					this.textView
							.setBackgroundResource(R.color.mcs_text_background);
				}
				break;
			case R.styleable.MultiCriteriaSelectField_expand:
				final boolean expand = a.getBoolean(attr, false);
				if (expand) {
					this.spinner.setVisibility(View.VISIBLE);
				}
				break;

			}
		}
		a.recycle();

	}

	/**
	 * Gets the field type.
	 * 
	 * @return the field type
	 */
	public MultiCriteriaSearchFieldType getFieldType() {
		return this.fieldType;
	}

	/**
	 * Gets the spinner.
	 * 
	 * @return the spinner
	 */
	public Spinner getSpinner() {
		return this.spinner;
	}

	/**
	 * Sets the field type.
	 * 
	 * @param fieldType
	 *            the new field type
	 */
	public void setFieldType(final MultiCriteriaSearchFieldType fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * Inits the icon Laoyout.
	 * 
	 * @param context
	 *            the context
	 * @return the linear layout
	 */
	private LinearLayout initIconLayout(final Context context) {
		final LinearLayout layoutIcon = new LinearLayout(context);
		final LayoutParams paramsLayoutIcon = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		paramsLayoutIcon.gravity = Gravity.RIGHT;
		layoutIcon.setLayoutParams(paramsLayoutIcon);

		this.icon.setImageResource(R.drawable.ic_down);
		layoutIcon.addView(this.icon);
		return layoutIcon;
	}

	/**
	 * Inits the text icon layout.
	 * 
	 * @param context
	 *            the context
	 * @return the linear layout
	 */
	private LinearLayout initTextIconLayout(final Context context) {
		final LinearLayout layoutTextIcon = new LinearLayout(context);
		final LayoutParams paramsLayoutTextIcon = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		paramsLayoutTextIcon.gravity = Gravity.CENTER_HORIZONTAL;
		layoutTextIcon.setLayoutParams(paramsLayoutTextIcon);

		final LinearLayout layoutIcon = initIconLayout(context);

		layoutTextIcon.addView(this.textView);
		layoutTextIcon.addView(layoutIcon);
		return layoutTextIcon;
	}

}
