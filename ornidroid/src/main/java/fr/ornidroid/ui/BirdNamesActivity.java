package fr.ornidroid.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.Taxon;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;

/**
 * Displays bird names in different languages.
 */
public class BirdNamesActivity extends Activity {

	/** The bird. */
	private Bird bird;

	/** The m list view. */
	private ListView mListView;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The scientific name. */
	private TextView scientificName;

	/**
	 * Instantiates a new bird detail activity.
	 */
	public BirdNamesActivity() {
		this.ornidroidService = OrnidroidServiceFactory.getService(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		this.scientificName = new TextView(this);
		linearLayout.addView(this.scientificName);

		this.mListView = new ListView(this);
		linearLayout.addView(this.mListView);

		setContentView(linearLayout);

		this.bird = this.ornidroidService.getCurrentBird();

		if (null != this.bird) {
			this.scientificName.setText(this.getText(R.string.scientific_name)
					+ ": " + this.bird.getScientificName());
			printBirdNames();
		}

	}

	/**
	 * Prints the bird names.
	 */
	private void printBirdNames() {
		final List<Taxon> birdNames = this.ornidroidService.getNames(this.bird
				.getId());
		final int nbBirdNames = birdNames.size();
		final String[] values = new String[nbBirdNames];
		for (int i = 0; i < nbBirdNames; i++) {
			final Taxon taxon = birdNames.get(i);
			values[i] = taxon.getName() + " (" + taxon.getLang() + ")";
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		// Assign adapter to ListView
		this.mListView.setAdapter(adapter);

	}

}
