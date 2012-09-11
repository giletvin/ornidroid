package fr.ornidroid.ui.views;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.Taxon;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;

/**
 * A factory for creating NamesView objects.
 */
public class NamesViewFactory {

	/** The activity. */
	private final Activity activity;
	/** The m list view. */
	private ListView mListView;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The scientific name. */
	private TextView scientificName;

	/**
	 * Instantiates a new names view factory.
	 * 
	 * @param pActivity
	 *            the activity
	 */
	public NamesViewFactory(final Activity pActivity) {
		this.activity = pActivity;
		this.ornidroidService = OrnidroidServiceFactory
				.getService(this.activity);
	}

	/**
	 * Creates a new NamesView object.
	 * 
	 * @return the view
	 */
	public View createContent() {
		final LinearLayout linearLayout = new LinearLayout(this.activity);

		linearLayout.setOrientation(LinearLayout.VERTICAL);

		this.scientificName = new TextView(this.activity);
		linearLayout.addView(this.scientificName);

		this.mListView = new ListView(this.activity);
		linearLayout.addView(this.mListView);

		if (null != this.ornidroidService.getCurrentBird()) {
			this.scientificName.setText(this.activity
					.getText(R.string.scientific_name)
					+ ": "
					+ this.ornidroidService.getCurrentBird()
							.getScientificName());
			printBirdNames();
		}
		return linearLayout;
	}

	/**
	 * Prints the bird names.
	 */
	private void printBirdNames() {
		final List<Taxon> birdNames = this.ornidroidService
				.getNames(this.ornidroidService.getCurrentBird().getId());
		final int nbBirdNames = birdNames.size();
		final String[] values = new String[nbBirdNames];
		for (int i = 0; i < nbBirdNames; i++) {
			final Taxon taxon = birdNames.get(i);
			values[i] = taxon.getName() + " (" + taxon.getLang() + ")";
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.activity, android.R.layout.simple_list_item_1,
				android.R.id.text1, values);

		// Assign adapter to ListView
		this.mListView.setAdapter(adapter);

	}
}
