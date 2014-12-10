package fr.ornidroid.ui.fragment;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.service.IOrnidroidService;
import fr.ornidroid.service.OrnidroidServiceFactory;

/**
 * The Class DetailsFragment.
 */
@EFragment(R.layout.fragment_details)
public class DetailsFragment extends Fragment {

	/** The m ornidroid service. */
	private IOrnidroidService mOrnidroidService = OrnidroidServiceFactory
			.getService(getActivity());

	/** The category. */
	@ViewById(R.id.details_category)
	TextView category;

	/** The order and family. */
	@ViewById(R.id.details_order_family)
	TextView orderAndFamily;

	/** The size. */
	@ViewById(R.id.details_size)
	TextView size;

	/** The description. */
	@ViewById(R.id.details_description)
	TextView description;

	/** The distribution. */
	@ViewById(R.id.details_distribution)
	TextView distribution;

	/** The list countries. */
	@ViewById(R.id.details_countries)
	TextView listCountries;

	/** The xeno canto map link. */
	@ViewById(R.id.details_xeno_canto)
	TextView xenoCantoMapLink;

	/** The wikipedia link. */
	@ViewById(R.id.details_wikipedia)
	TextView wikipediaLink;

	/** The oiseaux net link. */
	@ViewById(R.id.details_oiseaux_net)
	TextView oiseauxNetLink;

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
		// Association du layout pour ce Fragment
		return inflater.inflate(R.layout.fragment_details, container, false);

	}

	@AfterViews
	public void afterViews() {
		if (mOrnidroidService.getCurrentBird() != null) {
			printBirdCategory(mOrnidroidService.getCurrentBird());
			printBirdOrderAndFamily(mOrnidroidService.getCurrentBird());
			printBirdSize(mOrnidroidService.getCurrentBird());
			printBirdDescription(mOrnidroidService.getCurrentBird());
			printBirdDistributionAndBehaviour(mOrnidroidService
					.getCurrentBird());
			fillCountriesList();
			printHttpLinks();

		}
	}

	/**
	 * Fill countries list.
	 */
	private void fillCountriesList() {
		final List<String> listCountries = this.mOrnidroidService
				.getGeographicDistribution(this.mOrnidroidService
						.getCurrentBird().getId());
		if (!listCountries.isEmpty()) {
			StringBuffer sbuf = new StringBuffer(getActivity().getText(
					R.string.geographic_distribution)
					+ BasicConstants.COLUMN_STRING
					+ BasicConstants.CARRIAGE_RETURN
					+ BasicConstants.CARRIAGE_RETURN);

			for (String country : listCountries) {
				sbuf.append(country + BasicConstants.CARRIAGE_RETURN);
			}

			this.listCountries.setText(sbuf.toString());
		}

	}

	/**
	 * Prints the bird category.
	 * 
	 * @param currentBird
	 *            the current bird
	 */
	private void printBirdCategory(final Bird currentBird) {
		this.category.setText(getActivity().getText(R.string.search_category)
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
			this.description.setText(getActivity()
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
		distributionAndBehaviour.append(getActivity().getText(
				R.string.distribution));
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
					.setText(getActivity().getText(R.string.scientific_order)
							+ BasicConstants.COLUMN_STRING
							+ bird.getScientificOrder()
							+ BasicConstants.CARRIAGE_RETURN
							+ BasicConstants.CARRIAGE_RETURN
							+ getActivity().getText(R.string.scientific_family)
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

		this.size.setText(getActivity().getText(R.string.search_size)
				+ BasicConstants.COLUMN_STRING + currentBird.getSize()
				+ BasicConstants.BLANK_STRING
				+ getActivity().getText(R.string.cm));

	}

	/**
	 * Prints the http links.
	 */
	private void printHttpLinks() {
		final String xenoCantoUrl = this.mOrnidroidService
				.getXenoCantoMapUrl(this.mOrnidroidService.getCurrentBird());
		this.xenoCantoMapLink.setText(Html.fromHtml(xenoCantoUrl));
		this.xenoCantoMapLink.setMovementMethod(LinkMovementMethod
				.getInstance());

		final String wikipedia = this.mOrnidroidService
				.getWikipediaLink(this.mOrnidroidService.getCurrentBird());

		this.wikipediaLink.setText(Html.fromHtml(wikipedia));
		this.wikipediaLink.setMovementMethod(LinkMovementMethod.getInstance());
		final String oiseauxNet = this.mOrnidroidService
				.getOiseauxNetLink(this.mOrnidroidService.getCurrentBird());
		if (StringHelper.isNotBlank(oiseauxNet)) {
			this.oiseauxNetLink.setText(Html.fromHtml(oiseauxNet));
			this.oiseauxNetLink.setMovementMethod(LinkMovementMethod
					.getInstance());
		}
	}
}
