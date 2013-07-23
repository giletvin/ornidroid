package fr.ornidroid.bo;

import java.util.Map;

/**
 * The Class OrnidroidFile.
 */
public abstract class AbstractOrnidroidFile {
	/** The Constant AUDIO_DURATION_PROPERTY. */
	public static final String AUDIO_DURATION_PROPERTY = "audio_duration";
	/** The Constant AUDIO_RECORDIST_PROPERTY. */
	public static final String AUDIO_RECORDIST_PROPERTY = "audio_recordist";
	/** The Constant AUDIO_REF_PROPERTY. */
	public static final String AUDIO_REF_PROPERTY = "audio_ref";

	/** The Constant AUDIO_REMARKS_PROPERTY. */
	public static final String AUDIO_REMARKS_PROPERTY = "audio_remarks";

	/** The Constant AUDIO_TITLE_PROPERTY. */
	public static final String AUDIO_TITLE_PROPERTY = "audio_title";

	/** The LANGUAG e_ separator. Ex image_description_fr */
	public static String LANGUAGE_SEPARATOR = "_";

	/**
	 * Suffix added to the file name to get the properties. Ex : img.jpg -->
	 * img.jpg.properties
	 */
	public static String PROPERTIES_SUFFIX = ".properties";

	/** The Constant DEFAULT_PROPERTY_VALUE. */
	private static final String DEFAULT_PROPERTY_VALUE = "";

	/** The path. */
	private String path;

	/** The properties. */
	private Map<String, String> properties;

	/** The type. */
	private OrnidroidFileType type;

	/**
	 * Instantiates a new ornidroid file.
	 */
	protected AbstractOrnidroidFile() {

	}

	/**
	 * Extract filename from path.
	 * 
	 * @return the string
	 */
	public String extractFilenameFromPath() {
		final int lastSlash = getPath().lastIndexOf("/");
		return getPath().substring(lastSlash);
	}

	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Gets the property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the property
	 */
	public String getProperty(final String propertyName) {
		String propertyValue = DEFAULT_PROPERTY_VALUE;
		if (null != this.properties) {
			propertyValue = (this.properties.get(propertyName) != null) ? this.properties
					.get(propertyName) : DEFAULT_PROPERTY_VALUE;
		}
		return propertyValue;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public OrnidroidFileType getType() {
		return this.type;
	}

	/**
	 * Sets the path.
	 * 
	 * @param path
	 *            the new path
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/**
	 * Sets the properties.
	 * 
	 * @param properties
	 *            the properties
	 */
	public void setProperties(final Map<String, String> properties) {
		this.properties = properties;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(final OrnidroidFileType type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.path + " " + this.type + " " + this.properties;
	}
}
