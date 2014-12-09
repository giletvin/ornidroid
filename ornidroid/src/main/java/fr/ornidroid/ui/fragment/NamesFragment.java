package fr.ornidroid.ui.fragment;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.Taxon;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;

/**
 * The Class NamesFragment.
 */
@EFragment(R.layout.fragment_names)
public class NamesFragment extends ListFragment {

	/** The m ornidroid service. */
	private IOrnidroidService mOrnidroidService = OrnidroidServiceFactory
			.getService(getActivity());

	/** The scientific name. */
	@ViewById(R.id.names_scientific_name)
	TextView scientificName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater
	 * , android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Association du layout pour ce Fragment
		return inflater.inflate(R.layout.fragment_names, container, false);

	}

	/**
	 * After views.
	 */
	@AfterViews
	void afterViews() {
		if (null != mOrnidroidService.getCurrentBird()) {
			final StringBuilder sb = new StringBuilder();
			final String scientificName2 = StringHelper
					.isBlank(mOrnidroidService.getCurrentBird()
							.getScientificName2()) ? BasicConstants.EMPTY_STRING
					: " - "
							+ mOrnidroidService.getCurrentBird()
									.getScientificName2();
			sb.append(getActivity().getText(R.string.scientific_name))
					.append(BasicConstants.COLUMN_STRING)
					.append(mOrnidroidService.getCurrentBird()
							.getScientificName()).append(scientificName2);
			this.scientificName.setText(sb.toString());
			printBirdNames();
		}
	}

	/**
	 * Prints the bird names.
	 */
	private void printBirdNames() {
		final List<Taxon> birdNames = this.mOrnidroidService
				.getNames(this.mOrnidroidService.getCurrentBird().getId());
		final int nbBirdNames = birdNames.size();
		final String[] values = new String[nbBirdNames];
		for (int i = 0; i < nbBirdNames; i++) {
			final Taxon taxon = birdNames.get(i);
			values[i] = taxon.getName() + " (" + taxon.getLang() + ")";
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, values);
		setListAdapter(adapter);
	}
}
