package fr.ornidroid.ui.multicriteriasearch;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.ui.components.HelpDialog;

/**
 * The Class MultiCriteriaSelectField.
 * http://kevindion.com/2011/01/custom-xml-attributes-for-android-widgets/
 */
public class MultiCriteriaSelectField extends LinearLayout implements
		OnClickListener {

	/** The custom icon. */
	private boolean customIcon = false;

	/** The field type. */
	private MultiCriteriaSearchFieldType fieldType;
	/** The help icon. */
	private final ImageView helpIcon;

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
		this.helpIcon = new ImageView(context);
		final LinearLayout layoutTextIcon = initTextIconLayout(context);

		this.spinner = new Spinner(context);
		this.spinner.setVisibility(View.GONE);
		final LayoutParams spinnerLayoutParams = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		spinnerLayoutParams.setMargins(30, 10, 30, 0);
		this.spinner.setLayoutParams(spinnerLayoutParams);
		this.spinner.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.custom_spinner));
		this.addView(layoutTextIcon);
		this.addView(this.spinner);

		// add click behaviour on the text and icon
		layoutTextIcon.setOnClickListener(this);

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
	 * Expand or collapse the field.
	 * 
	 * @param expand
	 *            the expand
	 */
	public void expand(final boolean expand) {
		if (expand) {
			MultiCriteriaSelectField.this.spinner.setVisibility(View.VISIBLE);
			if (!this.customIcon) {
				MultiCriteriaSelectField.this.icon
						.setImageResource(R.drawable.ic_up);
			}
		} else {
			MultiCriteriaSelectField.this.spinner.setVisibility(View.GONE);
			if (!this.customIcon) {
				MultiCriteriaSelectField.this.icon
						.setImageResource(R.drawable.ic_down);
			}
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(final View v) {
		if (this.spinner.getVisibility() == View.VISIBLE) {
			expand(false);
		} else {
			expand(true);
		}
	}

	/**
	 * Reset.
	 */
	public void reset() {
		this.spinner.setSelection(0);
	}

	/**
	 * Sets the field type. Depending on the type, the help icon is visible or
	 * not.
	 * 
	 * @param fieldType
	 *            the new field type
	 */
	public void setFieldType(final MultiCriteriaSearchFieldType fieldType) {
		this.fieldType = fieldType;
		boolean helpEnabled;
		switch (this.fieldType) {
		case CATEGORY:
			helpEnabled = true;
			break;
		case HABITAT:
			helpEnabled = true;
			break;
		case FEATHER_COLOUR:
			helpEnabled = true;
			break;
		default:
			helpEnabled = false;
			break;
		}
		if (!helpEnabled) {
			this.helpIcon.setVisibility(View.GONE);
		}
	}

	/**
	 * Sets the icon resource.
	 * 
	 * @param resId
	 *            the new icon resource
	 */
	public void setIconResource(final int resId) {
		this.icon.setImageResource(resId);
		this.customIcon = true;
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
		layoutTextIcon.setLayoutParams(paramsLayoutTextIcon);

		// add the icon on the left side
		final LayoutParams paramsLayoutIcon = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.icon.setLayoutParams(paramsLayoutIcon);
		this.icon.setImageResource(R.drawable.ic_down);
		layoutTextIcon.addView(this.icon);

		// add a layout for the text
		final LinearLayout layoutText = new LinearLayout(context);
		layoutText.setGravity(Gravity.CENTER);
		final LayoutParams paramsLayoutText = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		paramsLayoutText.leftMargin = 20;
		layoutText.setLayoutParams(paramsLayoutText);
		layoutText.addView(this.textView);
		layoutTextIcon.addView(layoutText);

		// add the help icon

		this.helpIcon.setImageResource(R.drawable.ic_help);
		final LinearLayout layoutHelpIcon = new LinearLayout(context);
		final LayoutParams paramsLayoutHelpIcon = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		paramsLayoutHelpIcon.setMargins(0, 0, 20, 0);
		layoutHelpIcon.setHorizontalGravity(Gravity.RIGHT);

		layoutHelpIcon.setLayoutParams(paramsLayoutHelpIcon);
		layoutHelpIcon.addView(this.helpIcon);
		layoutTextIcon.addView(layoutHelpIcon);
		// add click behaviour on the help icon
		this.helpIcon.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				switch (MultiCriteriaSelectField.this.fieldType) {
				case CATEGORY:
					HelpDialog.showInfoDialog(
							context,
							context.getResources().getString(
									R.string.search_category),
							context.getResources().getString(
									R.string.search_category_help));
					break;
				case HABITAT:
					HelpDialog.showInfoDialog(
							context,
							context.getResources().getString(
									R.string.search_habitat),
							context.getResources().getString(
									R.string.search_habitat_help));

					break;
				case FEATHER_COLOUR:
					HelpDialog.showInfoDialog(
							context,
							context.getResources().getString(
									R.string.search_feather_colour),
							context.getResources().getString(
									R.string.search_feather_colour_help));

					break;
				default:
					HelpDialog.showInfoDialog(context, context.getResources()
							.getString(R.string.search_no_help),
							BasicConstants.EMPTY_STRING);

					break;
				}

			}
		});

		return layoutTextIcon;
	}
}
