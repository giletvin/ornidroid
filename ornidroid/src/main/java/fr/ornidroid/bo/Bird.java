package fr.ornidroid.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Class Bird.
 */
public class Bird extends SimpleBird {

	/** The description. */
	private String description;

	/** The distribution. */
	private String distribution;

	/** The habitat. */
	private String habitat;

	/** The id. */
	private Integer id;

	/** The pictures. */
	private List<AbstractOrnidroidFile> pictures;

	/** The scientific family. */
	private String scientificFamily;

	/** The scientific name. */
	private String scientificName;

	/** The scientific order. */
	private String scientificOrder;

	/** The sounds. */
	private List<AbstractOrnidroidFile> sounds;

	/**
	 * Instantiates a new bird.
	 */
	protected Bird() {
		super();
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Gets the distribution.
	 * 
	 * @return the distribution
	 */
	public String getDistribution() {
		return this.distribution;
	}

	/**
	 * Gets the habitat.
	 * 
	 * @return the habitat
	 */
	public String getHabitat() {
		return this.habitat;
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
	 * Gets the list of the properties of the audio files. This is to be
	 * displayed in the screen.
	 * 
	 * @return the list containing maps of properties of the audio file. One map
	 *         per audio file.
	 * @see AudioOrnidroidFile AudioOrnidroidFile the keys of the map are
	 *      defined in this class
	 */
	public List<Map<String, String>> getListAudioFiles() {
		final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (null != this.sounds) {
			for (final AbstractOrnidroidFile audioFile : this.sounds) {
				final AudioOrnidroidFile ornidroidAudioFile = (AudioOrnidroidFile) audioFile;
				list.add(ornidroidAudioFile.getPropertiesForScreen());
			}
		}
		return list;
	}

	/**
	 * Gets the number of pictures.
	 * 
	 * @return the number of pictures, 0 if list of pictures is null
	 */
	public int getNumberOfPictures() {
		if (this.pictures != null) {
			return this.pictures.size();
		}
		return 0;
	}

	/**
	 * Gets the number of sounds.
	 * 
	 * @return the number of sounds
	 */
	public int getNumberOfSounds() {
		if (this.sounds != null) {
			return this.sounds.size();
		}
		return 0;
	}

	/**
	 * Gets the picture.
	 * 
	 * @param pictureNumber
	 *            the picture number
	 * @return the picture
	 */
	public AbstractOrnidroidFile getPicture(final int pictureNumber) {
		if ((this.pictures != null) && (this.pictures.size() > pictureNumber)) {
			return this.pictures.get(pictureNumber);
		} else {
			return null;
		}
	}

	/**
	 * Gets the list of pictures.
	 * 
	 * @return the pictures.
	 */
	public List<AbstractOrnidroidFile> getPictures() {
		// if (pictures == null) {
		// setPictures(new ArrayList<AbstractOrnidroidFile>());
		// }
		return this.pictures;
	}

	/**
	 * Gets the scientific family.
	 * 
	 * @return the scientific family
	 */
	public String getScientificFamily() {
		return this.scientificFamily;
	}

	/**
	 * Gets the scientific name.
	 * 
	 * @return the scientific name
	 */
	public String getScientificName() {
		return this.scientificName;
	}

	/**
	 * Gets the scientific order.
	 * 
	 * @return the scientific order
	 */
	public String getScientificOrder() {
		return this.scientificOrder;
	}

	/**
	 * Gets the sound.
	 * 
	 * @param soundNumber
	 *            the sound number
	 * @return the sound, can be null if soundNumber is not valid
	 */
	public AbstractOrnidroidFile getSound(final int soundNumber) {
		if ((this.sounds != null) && (this.sounds.size() > soundNumber)) {
			return this.sounds.get(soundNumber);
		} else {
			return null;
		}
	}

	/**
	 * Gets the sounds.
	 * 
	 * @return the sounds
	 */
	public List<AbstractOrnidroidFile> getSounds() {
		return this.sounds;
	}

	/**
	 * Sets the distribution.
	 * 
	 * @param distribution
	 *            the new distribution
	 */
	public void setDistribution(final String distribution) {
		this.distribution = distribution;
	}

	/**
	 * Sets the habitat.
	 * 
	 * @param habitat
	 *            the new habitat
	 */
	public void setHabitat(final String habitat) {
		this.habitat = habitat;
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
	 * Sets the pictures.
	 * 
	 * @param pictures
	 *            the new pictures
	 */
	public void setPictures(final List<AbstractOrnidroidFile> pictures) {
		this.pictures = pictures;
	}

	/**
	 * Sets the scientific family.
	 * 
	 * @param scientificFamily
	 *            the new scientific family
	 */
	public void setScientificFamily(final String scientificFamily) {
		this.scientificFamily = scientificFamily;
	}

	/**
	 * Sets the scientific order.
	 * 
	 * @param scientificOrder
	 *            the new scientific order
	 */
	public void setScientificOrder(final String scientificOrder) {
		this.scientificOrder = scientificOrder;
	}

	/**
	 * Sets the sounds.
	 * 
	 * @param sounds
	 *            the new sounds
	 */
	public void setSounds(final List<AbstractOrnidroidFile> sounds) {
		this.sounds = sounds;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	protected void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Sets the scientific name.
	 * 
	 * @param scientificName
	 *            the new scientific name
	 */
	protected void setScientificName(final String scientificName) {
		this.scientificName = scientificName;
	}

}
