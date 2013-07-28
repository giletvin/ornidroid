package fr.ornidroid.tests;

import java.io.File;
import java.io.IOException;

import org.junit.Before;

import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.FileHelper;

/**
 * The Class AbstractTest.
 */
public class AbstractTest {
	/** The Constant ORNIDROID_TEST_WEB_BASE. */
	public static final String ORNIDROID_TEST_WEB_BASE = "http://ornidroid.free.fr/tests";

	/** The Constant ORNIDROID_TEST_WEB_BASE_RUL. */
	public static final String ORNIDROID_TEST_WEB_BASE_IMAGE_URL = ORNIDROID_TEST_WEB_BASE
			+ "/images";
	/** The Constant TEST_DIRECTORY. */
	public static final String TEST_DIRECTORY = System
			.getProperty("java.io.tmpdir") + "/ornidroid_tests";

	/**
	 * Builds the ornidroid home test, with audio and images directory and one
	 * empty file in each directory named file1.jpg and file1.mp3 and their
	 * corresponding properties files
	 * 
	 * @param path
	 *            the path
	 * @return the file
	 * @throws IOException
	 */
	public File buildOrnidroidHomeTest(final String path) throws IOException {
		final File ornidroidHome = new File(path);
		final File ornidroidHomeImages = new File(path + File.separator
				+ Constants.IMAGES_DIRECTORY);
		final File ornidroidHomeAudio = new File(path + File.separator
				+ Constants.AUDIO_DIRECTORY);

		ornidroidHomeImages.mkdirs();
		ornidroidHomeAudio.mkdirs();
		FileHelper.createEmptyFile(new File(path + File.separator
				+ Constants.IMAGES_DIRECTORY + File.separator + "file1.jpg"));

		FileHelper.createEmptyFile(new File(path + File.separator
				+ Constants.IMAGES_DIRECTORY + File.separator
				+ "file1.jpg.properties"));

		FileHelper.createEmptyFile(new File(path + File.separator
				+ Constants.AUDIO_DIRECTORY + File.separator + "file1.mp3"));

		FileHelper.createEmptyFile(new File(path + File.separator
				+ Constants.AUDIO_DIRECTORY + File.separator
				+ "file1.mp3.properties"));
		return ornidroidHome;
	}

	/**
	 * Sets the up.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Before
	public void setUp() throws IOException {
		BasicConstants.setJunitContext(true);
		final File testDir = new File(TEST_DIRECTORY);
		FileHelper.forceDelete(testDir);
		FileHelper.forceMkdir(testDir);
	}
}
