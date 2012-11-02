package fr.ornidroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.MultiCriteriaSearchFormBean;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.components.MultiCriteriaSelectField;

/**
 * The Class MultiCriteriaSearchActivity.
 */
public class MultiCriteriaSearchActivity extends AbstractOrnidroidActivity
		implements OnClickListener {

	/**
	 * This inner class handles the clicks on the spinners items.
	 */
	public class OnSpinnersItemSelected implements OnItemSelectedListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(
		 * android.widget.AdapterView, android.view.View, int, long)
		 */
		public void onItemSelected(final AdapterView<?> parent,
				final View view, final int pos, final long id) {
			final SelectFields selectType = getSelectType(parent);
			switch (selectType) {
			case CATEGORY:
				MultiCriteriaSearchActivity.this.formBean
						.setCategoryId(MultiCriteriaSearchActivity.this.ornidroidService
								.getCategoryId(parent.getItemAtPosition(pos)
										.toString()));
				break;
			case HABITAT:
				MultiCriteriaSearchActivity.this.formBean
						.setHabitatId(MultiCriteriaSearchActivity.this.ornidroidService
								.getHabitatId(parent.getItemAtPosition(pos)
										.toString()));
				break;
			default:
				break;
			}

			updateSearchCountResults(MultiCriteriaSearchActivity.this.ornidroidService
					.getMultiSearchCriteriaCountResults(MultiCriteriaSearchActivity.this.formBean));

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected
		 * (android .widget.AdapterView)
		 */
		public void onNothingSelected(final AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	}

	/**
	 * This enum lists the fields of the multi criteria search
	 */
	private enum SelectFields {
		/** The category. */
		CATEGORY,
		/** The habitat. */
		HABITAT;
	}

	/** The category field. */
	private MultiCriteriaSelectField categoryField;

	/** The form bean. */
	private final MultiCriteriaSearchFormBean formBean;

	/** The habitat field. */
	private MultiCriteriaSelectField habitatField;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;
	/** The search count results. */
	private TextView searchCountResults;

	/** The search show results button. */
	private Button searchShowResultsButton;

	/**
	 * Instantiates a new multi criteria search activity.
	 */
	public MultiCriteriaSearchActivity() {
		super();
		Constants.initializeConstants(this);
		this.ornidroidService = OrnidroidServiceFactory.getService(this);
		this.formBean = new MultiCriteriaSearchFormBean();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(final View v) {
		if (v == this.searchShowResultsButton) {
			this.ornidroidService
					.getBirdMatchesFromMultiSearchCriteria(this.formBean);

			final Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);

			startActivity(intent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multicriteriasearch);
		setTitle(R.string.menu_search_multi);
		this.searchShowResultsButton = (Button) findViewById(R.id.search_show_results_button);
		this.searchShowResultsButton.setOnClickListener(this);
		this.searchCountResults = (TextView) findViewById(R.id.search_count_results);

		initSelectField(SelectFields.CATEGORY);
		initSelectField(SelectFields.HABITAT);

	}

	/**
	 * Gets the select type.
	 * 
	 * @param parent
	 *            the parent
	 * @return the select type
	 */
	private SelectFields getSelectType(final AdapterView<?> parent) {
		if (parent == this.categoryField.getSpinner()) {
			return SelectFields.CATEGORY;
		}
		if (parent == this.habitatField.getSpinner()) {
			return SelectFields.HABITAT;
		}
		return null;
	}

	/**
	 * Inits the select field.
	 * 
	 * @param selectFieldType
	 *            the select field type
	 */
	private void initSelectField(final SelectFields selectFieldType) {
		switch (selectFieldType) {
		case CATEGORY:
			this.categoryField = (MultiCriteriaSelectField) findViewById(R.id.search_category_field);
			this.categoryField.setDataAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, this.ornidroidService
							.getCategories()));
			this.categoryField.getSpinner().setOnItemSelectedListener(
					new OnSpinnersItemSelected());
			break;
		case HABITAT:
			this.habitatField = (MultiCriteriaSelectField) findViewById(R.id.search_habitat_field);
			this.habitatField.setDataAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, this.ornidroidService
							.getHabitats()));
			this.habitatField.getSpinner().setOnItemSelectedListener(
					new OnSpinnersItemSelected());
			break;
		}
	}

	/**
	 * Update the search count results textview.
	 * 
	 * @param countResults
	 *            the count results
	 */
	private void updateSearchCountResults(final int countResults) {
		this.searchCountResults.setText(MultiCriteriaSearchActivity.this
				.getText(R.string.search_count_results)
				+ Constants.BLANK_STRING + countResults);
	}
}
