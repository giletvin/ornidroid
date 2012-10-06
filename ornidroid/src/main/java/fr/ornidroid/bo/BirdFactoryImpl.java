package fr.ornidroid.bo;

/**
 * The Class BirdFactoryImpl.
 */
public class BirdFactoryImpl {

	/**
	 * Instantiates a new bird factory impl.
	 * 
	 */
	public BirdFactoryImpl() {

	}

	/**
	 * Creates the bird.
	 * 
	 * @param id
	 *            the id
	 * @param taxon
	 *            the taxon
	 * @param scientificName
	 *            the scientific name
	 * @param birdDirectoryName
	 *            the directory name
	 * @param description
	 *            the description
	 * @param distribution
	 *            the distribution
	 * @param scientificOrder
	 *            the scientific order
	 * @param scientificFamily
	 *            the scientific family
	 * @return the bird
	 */
	public Bird createBird(final Integer id, final String taxon,
			final String scientificName, final String birdDirectoryName,
			final String description, final String distribution,
			final String scientificOrder, final String scientificFamily) {
		final Bird bird = new Bird();
		bird.setId(id);
		bird.setTaxon(taxon);
		bird.setDescription(description);
		bird.setDistribution(distribution);
		bird.setScientificName(scientificName);
		bird.setScientificOrder(scientificOrder);
		bird.setScientificFamily(scientificFamily);
		bird.setBirdDirectoryName(birdDirectoryName);
		return bird;
	}

	/**
	 * Creates the simple bird. Only the taxon and the directory name are set
	 * 
	 * @param taxon
	 *            the taxon
	 * @param birdDirectoryName
	 *            the bird directory name
	 * @return the bird
	 */
	public SimpleBird createSimpleBird(final String taxon,
			final String birdDirectoryName) {
		final SimpleBird bird = new SimpleBird();
		bird.setTaxon(taxon);

		bird.setBirdDirectoryName(birdDirectoryName);
		return bird;
	}

}
