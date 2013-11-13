package fr.ornidroid.download;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.FileHelper;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.tests.AbstractTest;

/**
 * The Class DownloadHelperImplTest.
 */
public class DownloadHelperImplTest extends AbstractTest {

	/** The download helper. */
	private DownloadHelperInterface downloadHelper;

	/**
	 * Sets the up.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	@Before
	public void setUp() throws IOException {
		super.setUp();
		this.downloadHelper = new DownloadHelperImpl();
	}

	/**
	 * Test download file.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testDownloadFile() throws OrnidroidException {
		final File downloadedFile = this.downloadHelper.downloadFile(
				ORNIDROID_TEST_WEB_BASE_IMAGE_URL + "/barge_a_queue_noire",
				"barge_a_queue_noire_1.jpg", TEST_DIRECTORY);

		Assert.assertNotNull(downloadedFile);
		Assert.assertTrue(downloadedFile.exists());
	}

	/**
	 * Test download files.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testDownloadFiles() throws OrnidroidException {
		// test with a correct directory value, containing 2 pictures
		final List<File> downloadedFiles = this.downloadHelper.downloadFiles(
				"/tmp/", "barge_a_queue_noire", OrnidroidFileType.PICTURE);
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
		try {
			this.downloadHelper.downloadFiles("/tmp/", "unknown_bird",
					OrnidroidFileType.PICTURE);
			Assert.fail("an exception should have occurred");
		} catch (final OrnidroidException e) {
			Assert.assertEquals(
					0,
					e.getErrorType()
							.compareTo(
									OrnidroidError.ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST));
		} catch (final Throwable t) {
			Assert.fail("no other exception should occur");
		}

	}

	/**
	 * Test download files with a file already downloaded before.
	 * 
	 * 
	 */
	@Test
	public void testDownloadFilesWithDelta() throws Exception {
		final File testDir = new File(TEST_DIRECTORY + File.separator
				+ "barge_a_queue_noire");
		File existingFile = new File(testDir.getAbsolutePath() + File.separator
				+ "barge_a_queue_noire_1.jpg");
		FileHelper.forceMkdir(testDir);
		// create the already existing file
		FileHelper.createEmptyFile(existingFile);
		FileHelper.createEmptyFile(new File(existingFile.getAbsolutePath()
				+ ".properties"));

		Assert.assertEquals(0, existingFile.length());
		// we should only download the second file
		// test with a correct directory value, containing 2 pictures
		final List<File> downloadedFiles = this.downloadHelper.downloadFiles(
				TEST_DIRECTORY, "barge_a_queue_noire",
				OrnidroidFileType.PICTURE);
		Assert.assertTrue(downloadedFiles.size() == 2);
		existingFile = new File(existingFile.getAbsolutePath());
		Assert.assertEquals(0, existingFile.length());

		final File newFile = new File(testDir + File.separator
				+ "barge_a_queue_noire_2.jpg");
		Assert.assertTrue(newFile.length() > 0);
	}

	/**
	 * Test download unknown directory.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testDownloadUnknownDirectory() {

		try {
			this.downloadHelper.downloadFile(ORNIDROID_TEST_WEB_BASE_IMAGE_URL
					+ "/unknown_directory", "unknownFile.jpg", TEST_DIRECTORY);
			Assert.fail("an exception should have occurred");

		} catch (final OrnidroidException e) {
			Assert.assertTrue(
					"Error is of type link broken",
					e.getErrorType()
							.equals(OrnidroidError.ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST));
		}
	}

	/**
	 * Test download unknown file.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testDownloadUnknownFile() {
		try {
			this.downloadHelper
					.downloadFile(ORNIDROID_TEST_WEB_BASE_IMAGE_URL
							+ "/barge_a_queue_noire", "unknownFile.jpg",
							TEST_DIRECTORY);
			Assert.fail("an exception should have occurred");
		} catch (final OrnidroidException e) {
			Assert.assertEquals(
					0,
					e.getErrorType()
							.compareTo(
									OrnidroidError.ORNIDROID_DOWNLOAD_ERROR_MEDIA_DOES_NOT_EXIST));
		} catch (final Throwable t) {
			Assert.fail("no other exception should occur");
		}

	}

	/**
	 * Test read contents.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testReadContents() throws OrnidroidException {
		final String[] filesToDownload = this.downloadHelper.readContentFile(
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
	public void testReadContentsFromUnknownDirectory() {

		try {
			this.downloadHelper.readContentFile(
					ORNIDROID_TEST_WEB_BASE_IMAGE_URL + "/unknown_bird",
					TEST_DIRECTORY);
			Assert.fail("an exception should have occurred");
		} catch (final OrnidroidException e) {
			Assert.assertTrue(true);
		}
	}

}
