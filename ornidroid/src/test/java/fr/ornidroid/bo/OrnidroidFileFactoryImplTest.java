package fr.ornidroid.bo;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;

import fr.ornidroid.helper.SupportedLanguage;

/**
 * The Class OrnidroidFileFactoryImplTest.
 */
public class OrnidroidFileFactoryImplTest {

	/**
	 * Test the creation of a OrnidroidFile of type Picture. Should read the
	 * properties file associated with the picture.
	 */
	@Test
	public void testCreateImageFile() {
		final OrnidroidFileFactoryImpl factory = OrnidroidFileFactoryImpl
				.getFactory();

		OrnidroidFile ornidroidFile;
		// file with a properties file
		try {
			ornidroidFile = factory.createOrnidroidFile(
					"./src/test/resources/images/bird_1/1.jpg",
					OrnidroidFileType.PICTURE,
					SupportedLanguage.FRENCH.getCode());
			Assert.assertNotNull(ornidroidFile);
			Assert.assertEquals(
					"wrong description",
					"mâle",
					ornidroidFile
							.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
			// this property is in the file, but is empty
			Assert.assertEquals("wrong source", "", ornidroidFile
					.getProperty(PictureOrnidroidFile.IMAGE_SOURCE_PROPERTY));
			Assert.assertEquals("wrong licence", "CC", ornidroidFile
					.getProperty(PictureOrnidroidFile.IMAGE_LICENCE_PROPERTY));
			// this property is not in the file
			Assert.assertEquals("wrong author", "", ornidroidFile
					.getProperty(PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY));

		} catch (final FileNotFoundException e) {
			Assert.fail("should not occur");
		}

		// file without properties
		try {
			ornidroidFile = factory.createOrnidroidFile(
					"./src/test/resources/images/bird_1/2.jpg",
					OrnidroidFileType.PICTURE,
					SupportedLanguage.FRENCH.getCode());
			Assert.fail("an exception should have occurred");
		} catch (final FileNotFoundException e) {
			Assert.assertTrue("exception caught, ok", true);
		}

	}

	/**
	 * Test load audio properties.
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void testLoadAudioProperties() throws FileNotFoundException {
		final OrnidroidFileFactoryImpl factory = OrnidroidFileFactoryImpl
				.getFactory();

		OrnidroidFile ornidroidFile;

		// file 1. Complete. Remark are in english although file is loaded in
		// french
		ornidroidFile = factory
				.createOrnidroidFile(
						"./src/test/resources/audio/morus_bassanus/Gannet%20colony%20Hermaness%20July%202010.mp3",
						OrnidroidFileType.AUDIO,
						SupportedLanguage.FRENCH.getCode());
		Assert.assertNotNull(ornidroidFile);

		Assert.assertEquals("wrong recordist", "Dougie Preston", ornidroidFile
				.getProperty(OrnidroidFile.AUDIO_RECORDIST_PROPERTY));
		Assert.assertNotNull("remarks should not be null", ornidroidFile
				.getProperty(OrnidroidFile.AUDIO_REMARKS_PROPERTY));
		Assert.assertNotNull("duration should not be null", ornidroidFile
				.getProperty(OrnidroidFile.AUDIO_DURATION_PROPERTY));
		Assert.assertNotNull("title should not be null", ornidroidFile
				.getProperty(OrnidroidFile.AUDIO_TITLE_PROPERTY));

		Assert.assertNotNull("ref should not be null", ornidroidFile
				.getProperty(OrnidroidFile.AUDIO_REF_PROPERTY));

		// file 2. Remarks are missing
		ornidroidFile = factory
				.createOrnidroidFile(
						"./src/test/resources/audio/morus_bassanus/Morus_bassanus_Helgoland_09_10_2009.mp3",
						OrnidroidFileType.AUDIO,
						SupportedLanguage.ENGLISH.getCode());
		Assert.assertNotNull(ornidroidFile);
		Assert.assertNotNull("recordist should not be null", ornidroidFile
				.getProperty(OrnidroidFile.AUDIO_RECORDIST_PROPERTY));
		Assert.assertEquals("remarks should be empty", "", ornidroidFile
				.getProperty(OrnidroidFile.AUDIO_REMARKS_PROPERTY));
		Assert.assertNotNull("duration should not be null", ornidroidFile
				.getProperty(OrnidroidFile.AUDIO_DURATION_PROPERTY));
		Assert.assertNotNull("title should not be null", ornidroidFile
				.getProperty(OrnidroidFile.AUDIO_TITLE_PROPERTY));

		// file 3. Remarks in french
		ornidroidFile = factory.createOrnidroidFile(
				"./src/test/resources/audio/morus_bassanus/file3.mp3",
				OrnidroidFileType.AUDIO, SupportedLanguage.FRENCH.getCode());
		Assert.assertNotNull(ornidroidFile);

		Assert.assertEquals(
				"french remarks should be loaded",
				"french remarks",
				ornidroidFile
						.getProperty(OrnidroidFile.AUDIO_REMARKS_PROPERTY));

		// same file, remarks in english
		ornidroidFile = factory.createOrnidroidFile(
				"./src/test/resources/audio/morus_bassanus/file3.mp3",
				OrnidroidFileType.AUDIO, SupportedLanguage.ENGLISH.getCode());
		Assert.assertNotNull(ornidroidFile);

		Assert.assertEquals(
				"english remarks should be loaded",
				"english remarks",
				ornidroidFile
						.getProperty(OrnidroidFile.AUDIO_REMARKS_PROPERTY));

	}

	/**
	 * Test load lang properties.
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void testLoadLangProperties() throws FileNotFoundException {
		final OrnidroidFileFactoryImpl factory = OrnidroidFileFactoryImpl
				.getFactory();

		OrnidroidFile ornidroidFile;
		// file with a properties file fr
		ornidroidFile = factory.createOrnidroidFile(
				"./src/test/resources/images/bird_1/1.jpg",
				OrnidroidFileType.PICTURE, SupportedLanguage.FRENCH.getCode());
		Assert.assertNotNull(ornidroidFile);
		Assert.assertEquals("description inattendue", "mâle", ornidroidFile
				.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
		// file with a properties file en
		ornidroidFile = factory.createOrnidroidFile(
				"./src/test/resources/images/bird_1/1.jpg",
				OrnidroidFileType.PICTURE, SupportedLanguage.ENGLISH.getCode());
		Assert.assertNotNull(ornidroidFile);
		Assert.assertEquals(
				"description inattendue",
				"english description",
				ornidroidFile
						.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
		ornidroidFile = factory.createOrnidroidFile(
				"./src/test/resources/images/bird_1/1.jpg",
				OrnidroidFileType.PICTURE, "unknownLang");
		Assert.assertNotNull(ornidroidFile);
		Assert.assertEquals("description inattendue", "", ornidroidFile
				.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));

	}

}
