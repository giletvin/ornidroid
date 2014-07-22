package fr.ornidroid.bo;

/**
 * The Class SimpleBird.
 */
public class SimpleBird {
	/**
	 * Instantiates a new SimpleBird.
	 */
	protected SimpleBird() {
	}

	/**
	 * Gets the taxon.
	 * 
	 * @return the taxon
	 */
	public String getTaxon() {
		return taxon;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Sets the taxon.
	 * 
	 * @param taxon
	 *            the new taxon
	 */
	protected void setTaxon(String taxon) {
		this.taxon = taxon;
	}

	/** The id. */
	private Integer id;
	/** The taxon. */
	private String taxon;

	/** The bird directory name. */
	private String birdDirectoryName;
	/** The scientific name. */
	private String scientificName;

	/**
	 * Gets the scientific name.
	 * 
	 * @return the scientific name
	 */
	public String getScientificName() {
		return scientificName;
	}

	/**
	 * Sets the scientific name.
	 * 
	 * @param scientificName
	 *            the new scientific name
	 */
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	/**
	 * Gets the bird directory name.
	 * 
	 * @return the bird directory name
	 */
	public String getBirdDirectoryName() {
		return birdDirectoryName;
	}

	/**
	 * Sets the bird directory name.
	 * 
	 * @param birdDirectoryName
	 *            the new bird directory name
	 */
	protected void setBirdDirectoryName(String birdDirectoryName) {
		this.birdDirectoryName = birdDirectoryName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return taxon;
	}
}
