package fr.ornidroid.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.Taxon;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;

/**
 * The Class DetailsFragment.
 */
public class NamesFragment extends Fragment {

	/** The m ornidroid service. */
	private IOrnidroidService mOrnidroidService;
	/** The m list view. */
	private ListView mListView;

	/** The scientific name. */
	private TextView scientificName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mOrnidroidService = OrnidroidServiceFactory.getService(getActivity());

		View rootView = inflater.inflate(R.layout.fragment_names, container,
				false);
		this.scientificName = (TextView) rootView
				.findViewById(R.id.names_scientific_name);
		this.mListView = (ListView) rootView.findViewById(R.id.names_list);

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
		return rootView;
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

		// Assign adapter to ListView
		this.mListView.setAdapter(adapter);

	}
}
