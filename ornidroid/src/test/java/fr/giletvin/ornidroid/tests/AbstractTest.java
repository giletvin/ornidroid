package fr.giletvin.ornidroid.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;

/**
 * The Class AbstractTest.
 */
public class AbstractTest {
	/** The Constant TEST_DIRECTORY. */
	public static final String TEST_DIRECTORY = System
			.getProperty("java.io.tmpdir") + "/ornidroid_tests";
	/** The Constant ORNIDROID_TEST_WEB_BASE_RUL. */
	public static final String ORNIDROID_TEST_WEB_BASE_IMAGE_URL = "http://ornidroid.free.fr/tests/images";

	/**
	 * Sets the up.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Before
	public void setUp() throws IOException {
		File testDir = new File(TEST_DIRECTORY);
		FileUtils.deleteDirectory(testDir);
		FileUtils.forceMkdir(testDir);
	}
}
