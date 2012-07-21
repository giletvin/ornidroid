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
	 * Sets the taxon.
	 * 
	 * @param taxon
	 *            the new taxon
	 */
	protected void setTaxon(String taxon) {
		this.taxon = taxon;
	}

	/** The taxon. */
	private String taxon;

	/** The bird directory name. */
	private String birdDirectoryName;

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
