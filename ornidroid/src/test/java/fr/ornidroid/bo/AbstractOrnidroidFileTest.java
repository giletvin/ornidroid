package fr.ornidroid.bo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.bo.AudioOrnidroidFile;
import fr.ornidroid.bo.PictureOrnidroidFile;

/**
 * The Class AbstractOrnidroidFileTest.
 */
public class AbstractOrnidroidFileTest {

	/**
	 * Test get property.
	 */
	@Test
	public void testGetProperty() {
		AbstractOrnidroidFile file = new AudioOrnidroidFile();
		Assert.assertNotNull("getProperty should not be null",
				file.getProperty(PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY));
		Map<String, String> props = new HashMap<String, String>();
		props.put(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY,
				"description");
		file.setProperties(props);
		Assert.assertNotNull("getProperty should not be null",
				file.getProperty(PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY));
		Assert.assertEquals("wrong property value", "description", file
				.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));

	}

}
