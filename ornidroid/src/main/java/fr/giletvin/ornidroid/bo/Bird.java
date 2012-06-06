package fr.giletvin.ornidroid.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Class Bird.
 */
public class Bird extends SimpleBird {

	/**
	 * Instantiates a new bird.
	 */
	protected Bird() {
		super();
	}

	/** The description. */
	private String description;

	/** The scientific name. */
	private String scientificName;

	/** The scientific family. */
	private String scientificFamily;

	/** The id. */
	private Integer id;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the scientific family.
	 * 
	 * @return the scientific family
	 */
	public String getScientificFamily() {
		return scientificFamily;
	}

	/**
	 * Sets the scientific family.
	 * 
	 * @param scientificFamily
	 *            the new scientific family
	 */
	public void setScientificFamily(String scientificFamily) {
		this.scientificFamily = scientificFamily;
	}

	/**
	 * Gets the scientific order.
	 * 
	 * @return the scientific order
	 */
	public String getScientificOrder() {
		return scientificOrder;
	}

	/**
	 * Sets the scientific order.
	 * 
	 * @param scientificOrder
	 *            the new scientific order
	 */
	public void setScientificOrder(String scientificOrder) {
		this.scientificOrder = scientificOrder;
	}

	/** The scientific order. */
	private String scientificOrder;

	/** The pictures. */
	private List<AbstractOrnidroidFile> pictures;

	/** The sounds. */
	private List<AbstractOrnidroidFile> sounds;

	/**
	 * Gets the list of pictures.
	 * 
	 * @return the pictures.
	 */
	public List<AbstractOrnidroidFile> getPictures() {
		// if (pictures == null) {
		// setPictures(new ArrayList<AbstractOrnidroidFile>());
		// }
		return pictures;
	}

	/**
	 * Gets the picture.
	 * 
	 * @param pictureNumber
	 *            the picture number
	 * @return the picture
	 */
	public AbstractOrnidroidFile getPicture(int pictureNumber) {
		if ((pictures != null) && (pictures.size() > pictureNumber)) {
			return pictures.get(pictureNumber);
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
		return sounds;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	protected void setDescription(String description) {
		this.description = description;
	}

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
	protected void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	/**
	 * Gets the number of pictures.
	 * 
	 * @return the number of pictures, 0 if list of pictures is null
	 */
	public int getNumberOfPictures() {
		if (pictures != null) {
			return pictures.size();
		}
		return 0;
	}

	/**
	 * Gets the number of sounds.
	 * 
	 * @return the number of sounds
	 */
	public int getNumberOfSounds() {
		if (sounds != null) {
			return sounds.size();
		}
		return 0;
	}

	/**
	 * Gets the sound.
	 * 
	 * @param soundNumber
	 *            the sound number
	 * @return the sound, can be null if soundNumber is not valid
	 */
	public AbstractOrnidroidFile getSound(int soundNumber) {
		if ((sounds != null) && (sounds.size() > soundNumber)) {
			return sounds.get(soundNumber);
		} else {
			return null;
		}
	}

	/**
	 * Sets the pictures.
	 * 
	 * @param pictures
	 *            the new pictures
	 */
	public void setPictures(List<AbstractOrnidroidFile> pictures) {
		this.pictures = pictures;
	}

	/**
	 * Sets the sounds.
	 * 
	 * @param sounds
	 *            the new sounds
	 */
	public void setSounds(List<AbstractOrnidroidFile> sounds) {
		this.sounds = sounds;
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
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (null != sounds) {
			for (AbstractOrnidroidFile audioFile : sounds) {
				AudioOrnidroidFile ornidroidAudioFile = (AudioOrnidroidFile) audioFile;
				list.add(ornidroidAudioFile.getPropertiesForScreen());
			}
		}
		return list;
	}

}
