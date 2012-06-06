package fr.giletvin.ornidroid.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import fr.giletvin.ornidroid.helper.I18nHelper;

/**
 * The Class OrnidroidFileFactoryImpl.
 */
public class OrnidroidFileFactoryImpl {

	/** The Constant DEFAULT_VALUE. */
	private static final String DEFAULT_VALUE = "";

	/** The LANGUAG e_ separator. Ex image_description_fr */
	private static String LANGUAGE_SEPARATOR = "_";

	/** The factory. */
	private static OrnidroidFileFactoryImpl factory;

	/**
	 * Instantiates a new ornidroid file factory impl.
	 */
	private OrnidroidFileFactoryImpl() {

	}

	/**
	 * Gets the OrnidroidFileFactoryImpl factory.
	 * 
	 * @return the OrnidroidFileFactoryImpl factory
	 */
	public static OrnidroidFileFactoryImpl getFactory() {
		if (factory == null) {
			factory = new OrnidroidFileFactoryImpl();
		}
		return factory;
	}

	/**
	 * Creates the ornidroid file.
	 * 
	 * @param path
	 *            the path
	 * @param fileType
	 *            the file type
	 * @param lang
	 *            the lang
	 * @return the ornidroid file
	 * @throws FileNotFoundException
	 *             if the file has no properties file: this reveals a bad
	 *             installation or a pb in the downloading of the files
	 */
	public AbstractOrnidroidFile createOrnidroidFile(String path,
			OrnidroidFileType fileType, String lang)
			throws FileNotFoundException {
		AbstractOrnidroidFile file = null;
		switch (fileType) {
		case AUDIO:
			file = new AudioOrnidroidFile();
			break;
		case PICTURE:
			file = new PictureOrnidroidFile();
			break;
		}
		file.setPath(path);
		file.setType(fileType);

		file.setProperties(handleProperties(file, lang));

		return file;
	}

	/**
	 * Handle properties of the newly created File.
	 * 
	 * @param file
	 *            the file
	 * @param lang
	 *            the lang
	 * @return the map containing the keys and values of the properties of the
	 *         file
	 * @throws FileNotFoundException
	 *             if the file has no properties file
	 */
	private Map<String, String> handleProperties(AbstractOrnidroidFile file,
			String lang) throws FileNotFoundException {
		Map<String, String> properties = new HashMap<String, String>();
		switch (file.getType()) {
		case AUDIO:
			properties = initAudioProperties(file, lang);
			break;
		case PICTURE:
			properties = initImageProperties(file, lang);
			break;

		}
		return properties;
	}

	/**
	 * Inits the image properties.
	 * 
	 * @param file
	 *            the file
	 * @param lang
	 *            the lang
	 * @return the map, never null but can be empty
	 * @throws FileNotFoundException
	 *             if the file has no properties file
	 */
	private Map<String, String> initImageProperties(AbstractOrnidroidFile file,
			String lang) throws FileNotFoundException {
		Map<String, String> ornidroidFileProperties = new HashMap<String, String>();
		Properties properties = loadPropertiesFile(file);
		if (properties != null) {
			String description = (String) properties.getProperty(
					PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY
							+ LANGUAGE_SEPARATOR + lang, DEFAULT_VALUE);
			ornidroidFileProperties.put(
					PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY,
					description);
			String source = (String) properties.getProperty(
					PictureOrnidroidFile.IMAGE_SOURCE_PROPERTY, DEFAULT_VALUE);
			ornidroidFileProperties.put(
					PictureOrnidroidFile.IMAGE_SOURCE_PROPERTY, source);
			String author = (String) properties.getProperty(
					PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY, DEFAULT_VALUE);
			ornidroidFileProperties.put(
					PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY, author);
			String licence = (String) properties.getProperty(
					PictureOrnidroidFile.IMAGE_LICENCE_PROPERTY, DEFAULT_VALUE);
			ornidroidFileProperties.put(
					PictureOrnidroidFile.IMAGE_LICENCE_PROPERTY, licence);
		}
		return ornidroidFileProperties;
	}

	/**
	 * Load properties file.
	 * 
	 * @param file
	 *            the file
	 * @return the properties
	 * @throws FileNotFoundException
	 *             if the file has no properties file
	 */
	private Properties loadPropertiesFile(AbstractOrnidroidFile file)
			throws FileNotFoundException {
		Properties properties = null;
		File propertiesFile = new File(file.getPath()
				+ AbstractOrnidroidFile.PROPERTIES_SUFFIX);
		if (propertiesFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream(propertiesFile);
				properties = new Properties();
				properties.load(fis);
			} catch (FileNotFoundException e) {
				// should not occur
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new FileNotFoundException("properties file not found "
					+ file.getPath() + AbstractOrnidroidFile.PROPERTIES_SUFFIX);
		}
		return properties;
	}

	/**
	 * Inits the audio properties.
	 * 
	 * @param file
	 *            the file
	 * @param lang
	 *            the lang
	 * @return the map
	 * @throws FileNotFoundException
	 *             if the file has no properties file
	 */
	private Map<String, String> initAudioProperties(AbstractOrnidroidFile file,
			String lang) throws FileNotFoundException {
		Properties properties = loadPropertiesFile(file);
		Map<String, String> ornidroidFileProperties = new HashMap<String, String>();
		if (properties != null) {
			String recordist = (String) properties
					.get(AbstractOrnidroidFile.AUDIO_RECORDIST_PROPERTY);
			ornidroidFileProperties.put(
					AbstractOrnidroidFile.AUDIO_RECORDIST_PROPERTY, recordist);
			String duration = (String) properties
					.get(AbstractOrnidroidFile.AUDIO_DURATION_PROPERTY);
			ornidroidFileProperties.put(
					AbstractOrnidroidFile.AUDIO_DURATION_PROPERTY, duration);
			String title = (String) properties
					.get(AbstractOrnidroidFile.AUDIO_TITLE_PROPERTY);
			ornidroidFileProperties.put(
					AbstractOrnidroidFile.AUDIO_TITLE_PROPERTY, title);
			String ref = (String) properties
					.get(AbstractOrnidroidFile.AUDIO_REF_PROPERTY);
			ornidroidFileProperties.put(
					AbstractOrnidroidFile.AUDIO_REF_PROPERTY, ref);
			String remarks = (String) properties
					.get(AbstractOrnidroidFile.AUDIO_REMARKS_PROPERTY
							+ LANGUAGE_SEPARATOR + lang);
			if (StringUtils.isBlank(remarks)
					&& !I18nHelper.ENGLISH.equals(lang)) {
				// english is the default language for audio remarks (mainly
				// from xeno canto)
				remarks = (String) properties
						.get(AbstractOrnidroidFile.AUDIO_REMARKS_PROPERTY
								+ LANGUAGE_SEPARATOR + I18nHelper.ENGLISH);
			}
			ornidroidFileProperties.put(
					AbstractOrnidroidFile.AUDIO_REMARKS_PROPERTY, remarks);
		}

		return ornidroidFileProperties;
	}

}
