package fr.giletvin.ornidroid.download;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.giletvin.ornidroid.bo.OrnidroidFileType;
import fr.giletvin.ornidroid.helper.OrnidroidException;
import fr.giletvin.ornidroid.tests.AbstractTest;

/**
 * The Class DownloadHelperImplTest.
 */
public class DownloadHelperImplTest extends AbstractTest {

	/** The download helper. */
	private DownloadHelperInterface downloadHelper;

	/**
	 * Test download files.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testDownloadFiles() throws OrnidroidException {
		// test with a correct directory value, containing 2 pictures
		List<File> downloadedFiles = downloadHelper.downloadFiles("/tmp/",
				"barge_a_queue_noire", OrnidroidFileType.PICTURE);
		Assert.assertTrue(downloadedFiles.size() == 2);
	}

	/**
	 * Test download files unknown bird.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testDownloadFilesUnknownBird() throws OrnidroidException {

		List<File> downloadedFiles = downloadHelper.downloadFiles("/tmp/",
				"unknown_bird", OrnidroidFileType.PICTURE);
		Assert.assertNotNull(downloadedFiles);
		Assert.assertTrue(downloadedFiles.isEmpty());

	}

	/**
	 * Test read contents.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testReadContents() throws OrnidroidException {
		String[] filesToDownload = downloadHelper.readContentFile(
				ORNIDROID_TEST_WEB_BASE_IMAGE_URL + "/barge_a_queue_noire",
				TEST_DIRECTORY);
		Assert.assertEquals(2, filesToDownload.length);
		Assert.assertEquals("barge_a_queue_noire_1.jpg", filesToDownload[0]);

	}

	/**
	 * Test read contents from unknown directory.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testReadContentsFromUnknownDirectory()
			throws OrnidroidException {
		String[] filesToDownload = downloadHelper.readContentFile(
				ORNIDROID_TEST_WEB_BASE_IMAGE_URL + "/unknown_bird",
				TEST_DIRECTORY);
		Assert.assertNull(filesToDownload);

	}

	/**
	 * Test download file.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testDownloadFile() throws OrnidroidException {
		File downloadedFile = downloadHelper.downloadFile(
				ORNIDROID_TEST_WEB_BASE_IMAGE_URL + "/barge_a_queue_noire",
				"barge_a_queue_noire_1.jpg", TEST_DIRECTORY);

		Assert.assertNotNull(downloadedFile);
		Assert.assertTrue(downloadedFile.exists());
	}

	/**
	 * Test download unknown file.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	public void testDownloadUnknownFile() throws OrnidroidException {
		File downloadedFile = downloadHelper.downloadFile(
				ORNIDROID_TEST_WEB_BASE_IMAGE_URL + "/barge_a_queue_noire",
				"unknownFile.jpg", TEST_DIRECTORY);
		Assert.assertNull(downloadedFile);
	}

	/**
	 * Sets the up.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Before
	public void setUp() throws IOException {
		super.setUp();
		downloadHelper = new DownloadHelperImpl();
	}

}
