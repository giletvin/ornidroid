package fr.ornidroid.ui.views;

import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;

/**
 * A factory for creating DetailsView objects.
 */
public class DetailsViewFactory {

	/** The activity. */
	private final Activity activity;
	/** The category. */
	private TextView category;
	/** The description. */
	private TextView description;
	/** The distribution. */
	private TextView distribution;

	/** The oiseaux net link. */
	private TextView oiseauxNetLink;
	/** The order and family. */
	private TextView orderAndFamily;

	/** The ornidroid service. */
	private final IOrnidroidService ornidroidService;

	/** The description. */
	private TextView size;

	/** The wikipedia link. */
	private TextView wikipediaLink;

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
		linearLayout.setPadding(5, 10, 5, 5);
		this.category = new TextView(this.activity);
		this.category.setPadding(5, 0, 5, 20);
		linearLayout.addView(this.category);
		this.orderAndFamily = new TextView(this.activity);
		this.orderAndFamily.setPadding(5, 0, 5, 20);
		linearLayout.addView(this.orderAndFamily);
		this.size = new TextView(this.activity);
		this.size.setPadding(5, 0, 5, 20);
		linearLayout.addView(this.size);

		this.description = new TextView(this.activity);
		this.description.setPadding(5, 0, 5, 20);
		linearLayout.addView(this.description);

		this.distribution = new TextView(this.activity);
		this.distribution.setPadding(5, 0, 5, 20);
		linearLayout.addView(this.distribution);

		this.wikipediaLink = new TextView(this.activity);
		this.wikipediaLink.setPadding(5, 0, 5, 20);

		linearLayout.addView(this.wikipediaLink);
		this.oiseauxNetLink = new TextView(this.activity);
		this.oiseauxNetLink.setPadding(5, 0, 5, 20);

		linearLayout.addView(this.oiseauxNetLink);

		if (this.ornidroidService.getCurrentBird() != null) {
			printBirdCategory(this.ornidroidService.getCurrentBird());
			printBirdOrderAndFamily(this.ornidroidService.getCurrentBird());
			printBirdSize(this.ornidroidService.getCurrentBird());
			printBirdDescription(this.ornidroidService.getCurrentBird());
			printBirdDistributionAndBehaviour(this.ornidroidService
					.getCurrentBird());
			printHttpLinks();
		}
		// return linearLayout;
		final ScrollView scrollView = new ScrollView(this.activity);
		scrollView.addView(linearLayout);
		return scrollView;

	}

	/**
	 * Prints the bird category.
	 * 
	 * @param currentBird
	 *            the current bird
	 */
	private void printBirdCategory(final Bird currentBird) {
		this.category.setText(this.activity.getText(R.string.search_category)
				+ BasicConstants.COLUMN_STRING + currentBird.getCategory());
	}

	/**
	 * Prints the bird description.
	 * 
	 * @param bird
	 *            the bird
	 */
	private void printBirdDescription(final Bird bird) {
		if (StringHelper.isNotBlank(bird.getDescription())) {
			this.description.setText(this.activity
					.getText(R.string.description)
					+ BasicConstants.COLUMN_STRING
					+ BasicConstants.CARRIAGE_RETURN
					+ BasicConstants.CARRIAGE_RETURN + bird.getDescription());

		}
	}

	/**
	 * Prints the bird distribution and behaviour.
	 * 
	 * @param bird
	 *            the bird
	 */
	private void printBirdDistributionAndBehaviour(final Bird bird) {
		final StringBuilder distributionAndBehaviour = new StringBuilder();
		distributionAndBehaviour.append(this.activity
				.getText(R.string.distribution));
		distributionAndBehaviour.append(BasicConstants.COLUMN_STRING);
		distributionAndBehaviour.append(BasicConstants.CARRIAGE_RETURN);
		distributionAndBehaviour.append(BasicConstants.CARRIAGE_RETURN);
		distributionAndBehaviour.append(bird.getHabitat());
		if (StringHelper.isNotBlank(bird.getDistribution())) {
			distributionAndBehaviour.append(BasicConstants.CARRIAGE_RETURN);
			distributionAndBehaviour.append(BasicConstants.CARRIAGE_RETURN);
			distributionAndBehaviour.append(bird.getDistribution());
		}
		this.distribution.setText(distributionAndBehaviour.toString());
	}

	/**
	 * Prints the bird order and family.
	 * 
	 * @param bird
	 *            the bird
	 */
	private void printBirdOrderAndFamily(final Bird bird) {
		if (StringHelper.isNotBlank(bird.getScientificFamily())
				&& StringHelper.isNotBlank(bird.getScientificOrder())) {
			this.orderAndFamily
					.setText(this.activity.getText(R.string.scientific_order)
							+ BasicConstants.COLUMN_STRING
							+ bird.getScientificOrder()
							+ BasicConstants.CARRIAGE_RETURN
							+ BasicConstants.CARRIAGE_RETURN
							+ this.activity.getText(R.string.scientific_family)
							+ BasicConstants.COLUMN_STRING
							+ bird.getScientificFamily());
		}
	}

	/**
	 * Prints the bird size.
	 * 
	 * @param currentBird
	 *            the current bird
	 */
	private void printBirdSize(final Bird currentBird) {

		this.size.setText(this.activity.getText(R.string.search_size)
				+ BasicConstants.COLUMN_STRING + currentBird.getSize()
				+ BasicConstants.BLANK_STRING
				+ this.activity.getText(R.string.cm));

	}

	/**
	 * Prints the http links.
	 */
	private void printHttpLinks() {
		final String wikipedia = this.ornidroidService
				.getWikipediaLink(this.ornidroidService.getCurrentBird());

		this.wikipediaLink.setText(Html.fromHtml(wikipedia));
		this.wikipediaLink.setMovementMethod(LinkMovementMethod.getInstance());
		final String oiseauxNet = this.ornidroidService
				.getOiseauxNetLink(this.ornidroidService.getCurrentBird());
		if (StringHelper.isNotBlank(oiseauxNet)) {
			this.oiseauxNetLink.setText(Html.fromHtml(oiseauxNet));
			this.oiseauxNetLink.setMovementMethod(LinkMovementMethod
					.getInstance());
		}
	}

}
