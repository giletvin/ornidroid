package fr.ornidroid.bo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import fr.ornidroid.bo.OrnidroidFile;
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
		audioProperties.put(OrnidroidFile.AUDIO_DURATION_PROPERTY,
				"duration");
		audioProperties.put(OrnidroidFile.AUDIO_RECORDIST_PROPERTY,
				"recordist");
		audioProperties.put(OrnidroidFile.AUDIO_REF_PROPERTY, "ref");
		audioProperties.put(OrnidroidFile.AUDIO_REMARKS_PROPERTY,
				"remarks");
		audioProperties
				.put(OrnidroidFile.AUDIO_TITLE_PROPERTY, "title");
		audioFile.setProperties(audioProperties);

		Map<String, String> mapForScreen = audioFile.getPropertiesForScreen();
		Assert.assertEquals("title duration",
				mapForScreen.get(AudioOrnidroidFile.LINE_1));
		Assert.assertEquals("recordist ref remarks",
				mapForScreen.get(AudioOrnidroidFile.LINE_2));

	}

}
