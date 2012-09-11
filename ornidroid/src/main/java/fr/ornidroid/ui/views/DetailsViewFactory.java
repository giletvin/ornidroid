package fr.ornidroid.ui.views;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;

/**
 * A factory for creating DetailsView objects.
 */
public class DetailsViewFactory {

	/** The activity. */
	private final Activity activity;
	/** The description. */
	private TextView description;
	/** The order and family. */
	private TextView orderAndFamily;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/**
	 * Instantiates a new details view factory.
	 * 
	 * @param pActivity
	 *            the activity
	 */
	public DetailsViewFactory(final Activity pActivity) {
		this.activity = pActivity;
		this.ornidroidService = OrnidroidServiceFactory
				.getService(this.activity);
	}

	/**
	 * Creates a new DetailsView object.
	 * 
	 * @return the view
	 */
	public View createContent() {
		final LinearLayout linearLayout = new LinearLayout(this.activity);

		linearLayout.setOrientation(LinearLayout.VERTICAL);

		this.orderAndFamily = new TextView(this.activity);
		this.orderAndFamily.setPadding(0, 0, 0, 20);
		linearLayout.addView(this.orderAndFamily);
		this.description = new TextView(this.activity);
		linearLayout.addView(this.description);

		if (this.ornidroidService.getCurrentBird() != null) {

			printBirdOrderAndFamily(this.ornidroidService.getCurrentBird());
			this.description.setText(this.ornidroidService.getCurrentBird()
					.getDescription());

		}
		return linearLayout;
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
			this.orderAndFamily.setText(this.activity
					.getText(R.string.scientific_order)
					+ ": "
					+ bird.getScientificOrder()
					+ "\n\n"
					+ this.activity.getText(R.string.scientific_family)
					+ ": "
					+ bird.getScientificFamily());
		}
	}

}
