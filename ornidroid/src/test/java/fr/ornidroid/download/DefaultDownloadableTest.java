package fr.ornidroid.download;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import fr.ornidroid.download.DefaultDownloadable;
import fr.ornidroid.download.Downloadable;
import fr.ornidroid.tests.AbstractTest;

/**
 * The Class DefaultDownloadableTest.
 * http://stackoverflow.com/questions/2172152/cant-run-junit-4-test
 * -case-in-eclipse
 */
public class DefaultDownloadableTest extends AbstractTest {

	/**
	 * Test error web site download.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testErrorWebSiteDownload() throws Exception {
		final URL url = new URL(
				"http://unknownwebsite.free.fr/tmp/not_found_file.jpg");

		final Downloadable fixture = new DefaultDownloadable(url,
				TEST_DIRECTORY);
		fixture.download();
		Assert.assertFalse(fixture.getFile().exists());
	}

	/**
	 * Test not found download.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testNotFoundDownload() throws Exception {
		final URL url = new URL(ORNIDROID_TEST_WEB_BASE_IMAGE_URL
				+ "/not_found_file.jpg");

		final Downloadable fixture = new DefaultDownloadable(url,
				TEST_DIRECTORY);
		fixture.download();
		Assert.assertFalse(fixture.getFile().exists());
	}

	/**
	 * Test real download.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testRealDownload() throws Exception {

		final URL url = new URL(ORNIDROID_TEST_WEB_BASE_IMAGE_URL
				+ "/barge_a_queue_noire/barge_a_queue_noire_1.jpg");

		final Downloadable fixture = new DefaultDownloadable(url,
				TEST_DIRECTORY);
		fixture.download();
		Assert.assertTrue(fixture.getFile().exists());

	}

	/**
	 * Test real download.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testRealDownloadRawFiles() throws Exception {

		final URL url = new URL(ORNIDROID_TEST_WEB_BASE_IMAGE_URL
				+ "/barge_a_queue_noire/contents.properties");

		final Downloadable fixture = new DefaultDownloadable(url,
				TEST_DIRECTORY);
		fixture.download();
		Assert.assertTrue(fixture.getFile().exists());

	}

}
