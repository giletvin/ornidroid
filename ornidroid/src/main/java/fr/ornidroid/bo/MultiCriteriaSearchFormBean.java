package fr.ornidroid.bo;

/**
 * The Class MultiCriteriaSearchFormBean. This class handles the values selected
 * by the user in the multi criteria search screen.
 */
public class MultiCriteriaSearchFormBean {

	/** The beak form id. */
	private Integer beakFormId;

	/** The category id. */
	private Integer categoryId;

	/** The feather colour id. */
	private Integer featherColourId;

	/** The habitat id. */
	private Integer habitatId;

	/** The size id. */
	private Integer sizeId;

	/**
	 * Gets the beak form id.
	 * 
	 * @return the beak form id
	 */
	public Integer getBeakFormId() {
		return this.beakFormId != null ? this.beakFormId : 0;
	}

	/**
	 * Gets the category id.
	 * 
	 * @return the category id
	 */
	public Integer getCategoryId() {
		return this.categoryId != null ? this.categoryId : 0;
	}

	/**
	 * Gets the feather colour id.
	 * 
	 * @return the feather colour id
	 */
	public Integer getFeatherColourId() {
		return this.featherColourId != null ? this.featherColourId : 0;
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
	 * Gets the size id.
	 * 
	 * @return the size id
	 */
	public Integer getSizeId() {
		return this.sizeId != null ? this.sizeId : 0;
	}

	/**
	 * Sets the beak form id.
	 * 
	 * @param pBeakFormId
	 *            the new beak form id
	 */
	public void setBeakFormId(final Integer pBeakFormId) {
		this.beakFormId = pBeakFormId;

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
	 * Sets the feather colour id.
	 * 
	 * @param featherColourId
	 *            the new feather colour id
	 */
	public void setFeatherColourId(final Integer featherColourId) {
		this.featherColourId = featherColourId;
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

	/**
	 * Sets the size id.
	 * 
	 * @param sizeId
	 *            the new size id
	 */
	public void setSizeId(final Integer sizeId) {
		this.sizeId = sizeId;
	}

}
