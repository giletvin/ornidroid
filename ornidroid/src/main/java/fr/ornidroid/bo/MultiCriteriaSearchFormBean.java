package fr.ornidroid.bo;

/**
 * The Class MultiCriteriaSearchFormBean. This class handles the values selected
 * by the user in the multi criteria search screen.
 */
public class MultiCriteriaSearchFormBean {

	/** The category id. */
	private Integer categoryId;

	/** The habitat id. */
	private Integer habitatId;

	/**
	 * Gets the category id.
	 * 
	 * @return the category id
	 */
	public Integer getCategoryId() {
		return this.categoryId != null ? this.categoryId : 0;
	}

	/**
	 * Gets the habitat id.
	 * 
	 * @return the habitat id
	 */
	public Integer getHabitatId() {
		return this.habitatId != null ? this.habitatId : 0;
	}

	/**
	 * Sets the category id.
	 * 
	 * @param pCategoryId
	 *            the new category id
	 */
	public void setCategoryId(final Integer pCategoryId) {
		this.categoryId = pCategoryId;

	}

	/**
	 * Sets the habitat id.
	 * 
	 * @param pHabitatId
	 *            the new habitat id
	 */
	public void setHabitatId(final Integer pHabitatId) {
		this.habitatId = pHabitatId;

	}

}
