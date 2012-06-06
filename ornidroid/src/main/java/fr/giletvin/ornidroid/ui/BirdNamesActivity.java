package fr.giletvin.ornidroid.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import fr.giletvin.ornidroid.R;
import fr.giletvin.ornidroid.bo.Bird;
import fr.giletvin.ornidroid.bo.Taxon;
import fr.giletvin.ornidroid.service.IOrnidroidService;
import fr.giletvin.ornidroid.service.OrnidroidServiceFactory;

/**
 * Displays bird names in different languages.
 */
public class BirdNamesActivity extends Activity {

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The bird. */
	private Bird bird;

	/** The m list view. */
	private ListView mListView;

	/** The scientific name. */
	private TextView scientificName;

	/**
	 * Instantiates a new bird detail activity.
	 */
	public BirdNamesActivity() {
		ornidroidService = OrnidroidServiceFactory.getService(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		scientificName = new TextView(this);
		linearLayout.addView(scientificName);

		mListView = new ListView(this);
		linearLayout.addView(mListView);

		setContentView(linearLayout);

		bird = ornidroidService.getCurrentBird();

		if (null != bird) {
			scientificName.setText(this.getText(R.string.scientific_name)
					+ ": " + bird.getScientificName());
			printBirdNames();
		}

	}

	/**
	 * Prints the bird names.
	 */
	private void printBirdNames() {
		List<Taxon> birdNames = ornidroidService.getNames(bird.getId());
		int nbBirdNames = birdNames.size();
		String[] values = new String[nbBirdNames];
		for (int i = 0; i < nbBirdNames; i++) {
			Taxon taxon = birdNames.get(i);
			values[i] = taxon.getName() + " (" + taxon.getLang() + ")";
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		// Assign adapter to ListView
		mListView.setAdapter(adapter);

	}

}
