package fr.ornidroid.bo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.bo.AudioOrnidroidFile;

/**
 * The Class AudioOrnidroidFileTest.
 */
public class AudioOrnidroidFileTest {

	/**
	 * Test get properties for screen.
	 */
	@Test
	public void testGetPropertiesForScreen() {
		AudioOrnidroidFile audioFile = new AudioOrnidroidFile();
		Map<String, String> audioProperties = new HashMap<String, String>();
		audioProperties.put(AbstractOrnidroidFile.AUDIO_DURATION_PROPERTY,
				"duration");
		audioProperties.put(AbstractOrnidroidFile.AUDIO_RECORDIST_PROPERTY,
				"recordist");
		audioProperties.put(AbstractOrnidroidFile.AUDIO_REF_PROPERTY, "ref");
		audioProperties.put(AbstractOrnidroidFile.AUDIO_REMARKS_PROPERTY,
				"remarks");
		audioProperties
				.put(AbstractOrnidroidFile.AUDIO_TITLE_PROPERTY, "title");
		audioFile.setProperties(audioProperties);

		Map<String, String> mapForScreen = audioFile.getPropertiesForScreen();
		Assert.assertEquals("title duration",
				mapForScreen.get(AudioOrnidroidFile.LINE_1));
		Assert.assertEquals("recordist ref remarks",
				mapForScreen.get(AudioOrnidroidFile.LINE_2));

	}

}
