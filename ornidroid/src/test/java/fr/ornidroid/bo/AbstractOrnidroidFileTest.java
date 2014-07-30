package fr.ornidroid.bo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import fr.ornidroid.helper.BasicConstants;

/**
 * The Class AbstractOrnidroidFileTest.
 */
public class AbstractOrnidroidFileTest {

	/**
	 * Test extract filename from path.
	 */
	@Test
	public void testExtractFilenameFromPath() {
		final OrnidroidFile file = new AudioOrnidroidFile();
		file.setPath("/tmp/file.mp3");
		Assert.assertEquals("file.mp3", file.extractFilenameFromPath());
	}

	/**
	 * Test get property.
	 */
	@Test
	public void testGetProperty() {
		final OrnidroidFile file = new AudioOrnidroidFile();
		Assert.assertNotNull("getProperty should not be null",
				file.getProperty(PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY));
		final Map<String, String> props = new HashMap<String, String>();
		props.put(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY,
				"description");
		file.setProperties(props);
		Assert.assertNotNull("getProperty should not be null",
				file.getProperty(PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY));
		Assert.assertEquals("wrong property value", "description", file
				.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));

	}

	/**
	 * Test is custom media file.
	 */
	@Test
	public void testIsCustomMediaFile() {
		final OrnidroidFile file = new AudioOrnidroidFile();
		file.setPath("/tmp/file.mp3");
		Assert.assertFalse(file.isCustomMediaFile());
		final OrnidroidFile file2 = new AudioOrnidroidFile();
		file2.setPath("/tmp/" + BasicConstants.CUSTOM_MEDIA_FILE_PREFIX
				+ "file.mp3");
		Assert.assertTrue(file2.isCustomMediaFile());
	}

}
