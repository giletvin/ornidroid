package fr.ornidroid.bo;

import java.util.Map;

/**
 * The Class OrnidroidFile.
 */
public abstract class AbstractOrnidroidFile {
	/**
	 * Suffix added to the file name to get the properties. Ex : img.jpg -->
	 * img.jpg.properties
	 */
	public static String PROPERTIES_SUFFIX = ".properties";
	/** The Constant DEFAULT_PROPERTY_VALUE. */
	private static final String DEFAULT_PROPERTY_VALUE = "";

	/** The Constant AUDIO_RECORDIST_PROPERTY. */
	public static final String AUDIO_RECORDIST_PROPERTY = "audio_recordist";

	/** The Constant AUDIO_REMARKS_PROPERTY. */
	public static final String AUDIO_REMARKS_PROPERTY = "audio_remarks";

	/** The Constant AUDIO_DURATION_PROPERTY. */
	public static final String AUDIO_DURATION_PROPERTY = "audio_duration";

	/** The Constant AUDIO_TITLE_PROPERTY. */
	public static final String AUDIO_TITLE_PROPERTY = "audio_title";

	/** The Constant AUDIO_REF_PROPERTY. */
	public static final String AUDIO_REF_PROPERTY = "audio_ref";

	/** The path. */
	private String path;

	/** The type. */
	private OrnidroidFileType type;

	/**
	 * Instantiates a new ornidroid file.
	 */
	protected AbstractOrnidroidFile() {

	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public OrnidroidFileType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(OrnidroidFileType type) {
		this.type = type;
	}

	/** The properties. */
	private Map<String, String> properties;

	/**
	 * Gets the property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the property
	 */
	public String getProperty(String propertyName) {
		String propertyValue = DEFAULT_PROPERTY_VALUE;
		if (null != properties) {
			propertyValue = (properties.get(propertyName) != null) ? properties
					.get(propertyName) : DEFAULT_PROPERTY_VALUE;
		}
		return propertyValue;
	}

	/**
	 * Sets the properties.
	 * 
	 * @param properties
	 *            the properties
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 * 
	 * @param path
	 *            the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return path + " " + type + " " + properties;
	}

	/**
	 * Extract filename from path.
	 * 
	 * @return the string
	 */
	public String extractFilenameFromPath() {
		int lastSlash = getPath().lastIndexOf("/");
		return getPath().substring(lastSlash);
	}
}
