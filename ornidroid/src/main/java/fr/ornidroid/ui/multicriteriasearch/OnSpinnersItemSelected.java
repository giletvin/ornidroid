package fr.ornidroid.ui.multicriteriasearch;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This class handles the clicks on the spinners items in the
 * MultiCriteriaSearchActivity.
 */
public class OnSpinnersItemSelected implements OnItemSelectedListener {

	/** The activity. */
	private final MultiCriteriaSearchActivity activity;

	/**
	 * Instantiates a new on spinners item selected.
	 * 
	 * @param pActivity
	 *            the activity
	 */
	public OnSpinnersItemSelected(final MultiCriteriaSearchActivity pActivity) {
		super();
		this.activity = pActivity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(
	 * android.widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemSelected(final AdapterView<?> parent, final View view,
			final int pos, final long id) {
		final MultiCriteriaSearchFieldType selectType = this.activity
				.getSelectType(parent);
		switch (selectType) {
		case CATEGORY:
			this.activity.getFormBean().setCategoryId(
					this.activity.getOrnidroidService().getCategoryId(
							parent.getItemAtPosition(pos).toString()));
			break;
		case HABITAT:
			this.activity.getFormBean().setHabitatId(
					this.activity.getOrnidroidService().getHabitatId(
							parent.getItemAtPosition(pos).toString()));
			break;
		case BEAK_FORM:
			this.activity.getFormBean().setBeakFormId(
					this.activity.getOrnidroidService().getBeakFormId(
							parent.getItemAtPosition(pos).toString()));
			break;
		case SIZE:
			this.activity.getFormBean().setSizeId(
					this.activity.getOrnidroidService().getSizeId(
							parent.getItemAtPosition(pos).toString()));
			break;

		}

		this.activity.updateSearchCountResults(this.activity
				.getOrnidroidService().getMultiSearchCriteriaCountResults(
						this.activity.getFormBean()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected
	 * (android .widget.AdapterView)
	 */
	public void onNothingSelected(final AdapterView<?> arg0) {
		// not implemented
	}
}
