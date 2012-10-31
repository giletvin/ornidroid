package fr.ornidroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.MultiCriteriaSearchFormBean;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;

/**
 * The Class MultiCriteriaSearchActivity.
 */
public class MultiCriteriaSearchActivity extends AbstractOrnidroidActivity
		implements OnClickListener {

	/** The form bean. */
	private final MultiCriteriaSearchFormBean formBean;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The search by scientific order and family criteria title. */
	private TextView searchByCategory;

	/** The search by scientific order and family criterias. */
	private View searchByCategoryCriterias;

	/** The search by category spinner. */
	private Spinner searchByCategorySpinner;

	/** The search by habitat. */
	private TextView searchByHabitat;

	/** The search by habitat criterias. */
	private View searchByHabitatCriterias;

	/** The search by habitat spinner. */
	private Spinner searchByHabitatSpinner;

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
		// setVisible or invisible the criteria view linked to the clicked
		// textView
		if (v == this.searchByCategory) {
			if (this.searchByCategoryCriterias.getVisibility() == View.VISIBLE) {
				this.searchByCategoryCriterias.setVisibility(View.GONE);
			} else {
				this.searchByCategoryCriterias.setVisibility(View.VISIBLE);
			}

		}
		if (v == this.searchByHabitat) {
			if (this.searchByHabitatCriterias.getVisibility() == View.VISIBLE) {
				this.searchByHabitatCriterias.setVisibility(View.GONE);
			} else {
				this.searchByHabitatCriterias.setVisibility(View.VISIBLE);
			}

		}
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
		this.searchByCategory = (TextView) findViewById(R.id.search_category);
		this.searchByCategory.setOnClickListener(this);
		this.searchByCategoryCriterias = findViewById(R.id.search_category_criterias);
		this.searchByCategorySpinner = (Spinner) findViewById(R.id.search_criteria_category);

		final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				this.ornidroidService.getCategories());
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		this.searchByCategorySpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(final AdapterView<?> parent,
							final View view, final int pos, final long id) {
						MultiCriteriaSearchActivity.this.formBean
								.setCategoryId(MultiCriteriaSearchActivity.this.ornidroidService
										.getCategoryId(parent
												.getItemAtPosition(pos)
												.toString()));

						updateSearchCountResults(MultiCriteriaSearchActivity.this.ornidroidService
								.getMultiSearchCriteriaCountResults(MultiCriteriaSearchActivity.this.formBean));

					}

					public void onNothingSelected(final AdapterView<?> arg0) {

					}
				});
		this.searchByCategorySpinner.setAdapter(dataAdapter);

		this.searchByHabitat = (TextView) findViewById(R.id.search_habitat);
		this.searchByHabitat.setOnClickListener(this);
		this.searchByHabitatCriterias = findViewById(R.id.search_habitat_criterias);
		this.searchByHabitatSpinner = (Spinner) findViewById(R.id.search_criteria_habitat);
		final ArrayAdapter<String> dataHabitatAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				this.ornidroidService.getHabitats());
		dataHabitatAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		this.searchByHabitatSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(final AdapterView<?> parent,
							final View view, final int pos, final long id) {
						MultiCriteriaSearchActivity.this.formBean
								.setHabitatId(MultiCriteriaSearchActivity.this.ornidroidService
										.getHabitatId(parent.getItemAtPosition(
												pos).toString()));

						updateSearchCountResults(MultiCriteriaSearchActivity.this.ornidroidService
								.getMultiSearchCriteriaCountResults(MultiCriteriaSearchActivity.this.formBean));

					}

					public void onNothingSelected(final AdapterView<?> arg0) {

					}
				});
		this.searchByHabitatSpinner.setAdapter(dataHabitatAdapter);
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
