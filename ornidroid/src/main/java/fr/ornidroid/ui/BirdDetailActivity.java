package fr.ornidroid.ui;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;

/**
 * Displays bird details.
 */
public class BirdDetailActivity extends Activity {

	/** The bird. */
	private Bird bird;

	/** The description. */
	private TextView description;
	/** The order and family. */
	private TextView orderAndFamily;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/**
	 * Instantiates a new bird detail activity.
	 */
	public BirdDetailActivity() {
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

		this.orderAndFamily = new TextView(this);
		this.orderAndFamily.setPadding(0, 0, 0, 20);
		linearLayout.addView(this.orderAndFamily);
		this.description = new TextView(this);
		linearLayout.addView(this.description);

		setContentView(linearLayout);

		this.bird = this.ornidroidService.getCurrentBird();

		if (this.bird == null) {
			finish();
		} else {
			printBirdOrderAndFamily(this.bird);
			this.description.setText(this.bird.getDescription());

		}
	}

	/**
	 * Prints the bird order and family.
	 * 
	 * @param bird
	 *            the bird
	 */
	private void printBirdOrderAndFamily(final Bird bird) {
		if (StringUtils.isNotBlank(bird.getScientificFamily())
				&& StringUtils.isNotBlank(bird.getScientificOrder())) {
			this.orderAndFamily.setText(this.getText(R.string.scientific_order)
					+ ": " + bird.getScientificOrder() + "\n\n"
					+ this.getText(R.string.scientific_family) + ": "
					+ bird.getScientificFamily());
		}
	}
}
