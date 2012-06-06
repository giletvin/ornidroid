package fr.giletvin.ornidroid.ui;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.giletvin.ornidroid.R;
import fr.giletvin.ornidroid.bo.Bird;
import fr.giletvin.ornidroid.service.IOrnidroidService;
import fr.giletvin.ornidroid.service.OrnidroidServiceFactory;

/**
 * Displays bird details.
 */
public class BirdDetailActivity extends Activity {

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The description. */
	private TextView description;
	/** The order and family. */
	private TextView orderAndFamily;

	/** The bird. */
	private Bird bird;

	/**
	 * Instantiates a new bird detail activity.
	 */
	public BirdDetailActivity() {
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

		orderAndFamily = new TextView(this);
		orderAndFamily.setPadding(0, 0, 0, 20);
		linearLayout.addView(orderAndFamily);
		description = new TextView(this);
		linearLayout.addView(description);

		setContentView(linearLayout);

		bird = ornidroidService.getCurrentBird();

		if (bird == null) {
			finish();
		} else {
			printBirdOrderAndFamily(bird);
			description.setText(bird.getDescription());

		}
	}

	/**
	 * Prints the bird order and family.
	 * 
	 * @param bird
	 *            the bird
	 */
	private void printBirdOrderAndFamily(Bird bird) {
		if (StringUtils.isNotBlank(bird.getScientificFamily())
				&& StringUtils.isNotBlank(bird.getScientificOrder())) {
			orderAndFamily.setText(this.getText(R.string.scientific_order)
					+ ": " + bird.getScientificOrder() + "\n\n"
					+ this.getText(R.string.scientific_family) + ": "
					+ bird.getScientificFamily());
		}
	}
}
