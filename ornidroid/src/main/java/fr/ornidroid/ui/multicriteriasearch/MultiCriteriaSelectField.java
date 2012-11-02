package fr.ornidroid.ui.multicriteriasearch;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
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
		this.spinner = new Spinner(context);
		this.addView(this.textView);
		this.addView(this.spinner);
		this.textView.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				if (MultiCriteriaSelectField.this.spinner.getVisibility() == View.VISIBLE) {
					MultiCriteriaSelectField.this.spinner
							.setVisibility(View.GONE);
				} else {
					MultiCriteriaSelectField.this.spinner
							.setVisibility(View.VISIBLE);
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
	 * Sets the data adapter.
	 * 
	 * @param dataAdapter
	 *            the new data adapter
	 */
	public void setDataAdapter(final ArrayAdapter<String> dataAdapter) {
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinner.setAdapter(dataAdapter);
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

}
