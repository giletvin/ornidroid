package fr.ornidroid.bo;

/**
 * The Class MultiCriteriaSearchFormBean. This class handles the values selected
 * by the user in the multi criteria search screen.
 */
public class MultiCriteriaSearchFormBean {

	/** The category id. */
	private Integer categoryId;

	/**
	 * Gets the category id.
	 * 
	 * @return the category id
	 */
	public Integer getCategoryId() {
		return this.categoryId != null ? this.categoryId : 0;
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

}
