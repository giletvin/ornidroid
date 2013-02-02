package fr.ornidroid.tests;

import java.io.File;
import java.io.IOException;

import org.junit.Before;

import fr.ornidroid.helper.BasicConstants;
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
