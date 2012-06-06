package fr.giletvin.ornidroid.bo;

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
	 * @param scientificOrder
	 *            the scientific order
	 * @param scientificFamily
	 *            the scientific family
	 * @return the bird
	 */
	public Bird createBird(Integer id, String taxon, String scientificName,
			String birdDirectoryName, final String description,
			final String scientificOrder, String scientificFamily) {
		Bird bird = new Bird();
		bird.setId(id);
		bird.setTaxon(taxon);
		bird.setDescription(description);
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
	public SimpleBird createSimpleBird(String taxon, String birdDirectoryName) {
		SimpleBird bird = new SimpleBird();
		bird.setTaxon(taxon);

		bird.setBirdDirectoryName(birdDirectoryName);
		return bird;
	}

}
