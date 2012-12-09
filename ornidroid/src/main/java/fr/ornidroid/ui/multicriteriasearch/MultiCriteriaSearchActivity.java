package fr.ornidroid.ui.multicriteriasearch;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import fr.ornidroid.R;
import fr.ornidroid.bo.MultiCriteriaSearchFormBean;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;
import fr.ornidroid.ui.AbstractOrnidroidActivity;
import fr.ornidroid.ui.MainActivity;

/**
 * The Class MultiCriteriaSearchActivity.
 */
public class MultiCriteriaSearchActivity extends AbstractOrnidroidActivity
		implements OnClickListener {

	/** The field list. */
	private final List<MultiCriteriaSelectField> fieldList;

	/** The form bean. */
	private final MultiCriteriaSearchFormBean formBean;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The reset form button. */
	private ImageView resetFormButton;

	/** The search show results button. */
	private Button searchShowResultsButton;

	/**
	 * Instantiates a new multi criteria search activity.
	 */
	public MultiCriteriaSearchActivity() {
		super();
		this.ornidroidService = OrnidroidServiceFactory.getService(this);
		this.formBean = new MultiCriteriaSearchFormBean();
		this.fieldList = new ArrayList<MultiCriteriaSelectField>();
	}

	/**
	 * Gets the form bean.
	 * 
	 * @return the form bean
	 */
	public MultiCriteriaSearchFormBean getFormBean() {
		return this.formBean;
	}

	/**
	 * Gets the ornidroid service.
	 * 
	 * @return the ornidroid service
	 */
	public IOrnidroidService getOrnidroidService() {
		return this.ornidroidService;
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
			intent.putExtra(MainActivity.SHOW_SEARCH_FIELD_INTENT_PRM, false);
			startActivity(intent);
		}
		if (v == this.resetFormButton) {
			resetForm();
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
		this.resetFormButton = (ImageView) findViewById(R.id.reset_form_button);
		this.resetFormButton.setOnClickListener(this);

		initSelectField(MultiCriteriaSearchFieldType.CATEGORY);
		initSelectField(MultiCriteriaSearchFieldType.SIZE);
		initSelectField(MultiCriteriaSearchFieldType.FEATHER_COLOUR);
		initSelectField(MultiCriteriaSearchFieldType.BEAK_COLOUR);
		initSelectField(MultiCriteriaSearchFieldType.PAW_COLOUR);
		initSelectField(MultiCriteriaSearchFieldType.HABITAT);
		initSelectField(MultiCriteriaSearchFieldType.BEAK_FORM);
		updateSearchCountResults(this.ornidroidService
				.getMultiSearchCriteriaCountResults(this.formBean));

	}

	/**
	 * Returns the type of the field to which the adapter view belongs.
	 * 
	 * @param parent
	 *            the parent
	 * @return the select type
	 */
	protected MultiCriteriaSearchFieldType getSelectType(
			final AdapterView<?> parent) {
		if (MultiCriteriaSelectField.class.isInstance(parent.getParent())) {
			final MultiCriteriaSelectField field = (MultiCriteriaSelectField) parent
					.getParent();
			return field.getFieldType();

		}
		return null;
	}

	/**
	 * Update the search count results textview.
	 * 
	 * @param countResults
	 *            the count results
	 */
	protected void updateSearchCountResults(final int countResults) {
		this.searchShowResultsButton.setText(this
				.getText(R.string.search_show_results)
				+ Constants.BLANK_STRING
				+ countResults);
		// this.searchCountResults.setText(MultiCriteriaSearchActivity.this
		// .getText(R.string.search_count_results)
		// + Constants.BLANK_STRING + countResults);
	}

	/**
	 * Inits the select field.
	 * 
	 * @param selectFieldType
	 *            the select field type
	 */
	private void initSelectField(
			final MultiCriteriaSearchFieldType selectFieldType) {
		ArrayAdapter<String> dataAdapter = null;
		MultiCriteriaSelectField field = null;
		switch (selectFieldType) {
		case CATEGORY:
			field = (MultiCriteriaSelectField) findViewById(R.id.search_category_field);

			dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item,
					this.ornidroidService.getCategories());

			break;
		case SIZE:
			field = (MultiCriteriaSelectField) findViewById(R.id.search_size_field);
			dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item,
					this.ornidroidService.getSizes());
			break;

		case HABITAT:
			field = (MultiCriteriaSelectField) findViewById(R.id.search_habitat_field);

			dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item,
					this.ornidroidService.getHabitats());
			break;
		case BEAK_FORM:
			field = (MultiCriteriaSelectField) findViewById(R.id.search_beak_form_field);
			dataAdapter = new MyCustomAdapter(this, R.layout.row_spinner_icons,
					this.ornidroidService.getBeakForms());
			break;
		case FEATHER_COLOUR:
			field = (MultiCriteriaSelectField) findViewById(R.id.search_feather_colour_field);
			// TODO : un adapteur custom pour mettre les couleurs ?
			dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item,
					this.ornidroidService.getColours());
			break;
		case BEAK_COLOUR:
			field = (MultiCriteriaSelectField) findViewById(R.id.search_beak_colour_field);
			// TODO : un adapteur custom pour mettre les couleurs ?
			dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item,
					this.ornidroidService.getColours());
			break;
		case PAW_COLOUR:
			field = (MultiCriteriaSelectField) findViewById(R.id.search_paw_colour_field);
			// TODO : un adapteur custom pour mettre les couleurs ?
			dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item,
					this.ornidroidService.getColours());
			break;

		}
		field.setFieldType(selectFieldType);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		field.getSpinner().setAdapter(dataAdapter);
		field.getSpinner().setOnItemSelectedListener(
				new OnSpinnersItemSelected(this));
		this.fieldList.add(field);

	}

	/**
	 * Reset all the select fields in the form.
	 */
	private void resetForm() {
		for (final MultiCriteriaSelectField field : this.fieldList) {
			field.reset();
		}
	}
}
