package fr.ornidroid.ui.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.ListView;
import fr.ornidroid.helper.BasicConstants;

/**
 * The Class ListPreferenceMultiSelect.
 * 
 * @author declanshanaghy http://blog.350nice.com/wp/archives/240 MultiChoice
 *         Preference Widget for Android
 * @contributor matiboy Added support for check all/none and custom separator
 *              defined in XML. IMPORTANT: The following attributes MUST be
 *              defined (probably inside attr.xml) for the code to even compile
 *              <declare-styleable name="ListPreferenceMultiSelect"> <attr
 *              format="string" name="checkAll" /> <attr format="string"
 *              name="separator" /> </declare-styleable> Whether you decide to
 *              then use those attributes is up to you.
 */
public class ListPreferenceMultiSelect extends ListPreference {

	/** The separator. */
	private String separator;

	/** The Constant DEFAULT_SEPARATOR. */
	public static final String DEFAULT_SEPARATOR = BasicConstants.COMMA_STRING;

	/** The check all key. */
	private String checkAllKey = null;

	/** The m clicked dialog entry indices. */
	private boolean[] mClickedDialogEntryIndices;

	// Constructor
	/**
	 * Instantiates a new list preference multi select.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public ListPreferenceMultiSelect(Context context, AttributeSet attrs) {
		super(context, attrs);
		separator = DEFAULT_SEPARATOR;
		// Initialize the array of boolean to the same size as number of entries
		mClickedDialogEntryIndices = new boolean[getEntries().length];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.preference.ListPreference#setEntries(java.lang.CharSequence[])
	 */
	@Override
	public void setEntries(CharSequence[] entries) {
		super.setEntries(entries);
		// Initialize the array of boolean to the same size as number of entries
		mClickedDialogEntryIndices = new boolean[entries.length];
	}

	/**
	 * Instantiates a new list preference multi select.
	 * 
	 * @param context
	 *            the context
	 */
	public ListPreferenceMultiSelect(Context context) {
		this(context, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.preference.ListPreference#onPrepareDialogBuilder(android.app.
	 * AlertDialog.Builder)
	 */
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		CharSequence[] entries = getEntries();
		CharSequence[] entryValues = getEntryValues();
		if (entries == null || entryValues == null
				|| entries.length != entryValues.length) {
			throw new IllegalStateException(
					"ListPreference requires an entries array and an entryValues array which are both the same length");
		}

		restoreCheckedEntries();
		builder.setMultiChoiceItems(entries, mClickedDialogEntryIndices,
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which,
							boolean val) {
						if (isCheckAllValue(which) == true) {
							checkAll(dialog, val);
						}
						mClickedDialogEntryIndices[which] = val;
					}
				});
	}

	/**
	 * Checks if is check all value.
	 * 
	 * @param which
	 *            the which
	 * @return true, if is check all value
	 */
	private boolean isCheckAllValue(int which) {
		final CharSequence[] entryValues = getEntryValues();
		if (checkAllKey != null) {
			return entryValues[which].equals(checkAllKey);
		}
		return false;
	}

	/**
	 * Check all.
	 * 
	 * @param dialog
	 *            the dialog
	 * @param val
	 *            the val
	 */
	private void checkAll(DialogInterface dialog, boolean val) {
		ListView lv = ((AlertDialog) dialog).getListView();
		int size = lv.getCount();
		for (int i = 0; i < size; i++) {
			lv.setItemChecked(i, val);
			mClickedDialogEntryIndices[i] = val;
		}
	}

	/**
	 * Parses the stored value.
	 * 
	 * @param val
	 *            the val
	 * @return the string[]
	 */
	public String[] parseStoredValue(CharSequence val) {
		if ("".equals(val)) {
			return null;
		} else {
			return ((String) val).split(separator);
		}
	}

	/**
	 * Restore checked entries.
	 */
	private void restoreCheckedEntries() {
		CharSequence[] entryValues = getEntryValues();

		// Explode the string read in sharedpreferences
		String[] vals = parseStoredValue(getValue());

		if (vals != null) {
			List<String> valuesList = Arrays.asList(vals);

			for (int i = 0; i < entryValues.length; i++) {
				CharSequence entry = entryValues[i];
				if (valuesList.contains(entry)) {
					mClickedDialogEntryIndices[i] = true;
				}
			}
			// }
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.ListPreference#onDialogClosed(boolean)
	 */
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// super.onDialogClosed(positiveResult);
		ArrayList<String> values = new ArrayList<String>();

		CharSequence[] entryValues = getEntryValues();
		if (positiveResult && entryValues != null) {
			for (int i = 0; i < entryValues.length; i++) {
				if (mClickedDialogEntryIndices[i] == true) {
					// Don't save the state of check all option - if any
					String val = (String) entryValues[i];
					if (checkAllKey == null
							|| (val.equals(checkAllKey) == false)) {
						values.add(val);
					}
				}
			}

			if (callChangeListener(values)) {
				setValue(join(values, separator));
			}
		}
	}

	// Credits to kurellajunior on this post
	// http://snippets.dzone.com/posts/show/91
	/**
	 * Join.
	 * 
	 * @param pColl
	 *            the coll
	 * @param separator
	 *            the separator
	 * @return the string
	 */
	protected static String join(Iterable<? extends Object> pColl,
			String separator) {
		Iterator<? extends Object> oIter;
		if (pColl == null || (!(oIter = pColl.iterator()).hasNext()))
			return BasicConstants.EMPTY_STRING;
		StringBuilder oBuilder = new StringBuilder(String.valueOf(oIter.next()));
		while (oIter.hasNext())
			oBuilder.append(separator).append(oIter.next());
		return oBuilder.toString();
	}

}