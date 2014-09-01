package fr.ornidroid.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import fr.ornidroid.tests.AbstractTest;

/**
 * The Class FileHelperTest.
 */
public class FileHelperTest extends AbstractTest {

	/**
	 * Test copy directory.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCopyDirectory() throws IOException {
		final File srcDir = buildOrnidroidHomeTest(TEST_DIRECTORY);
		// copy in the same destination
		try {
			FileHelper.copyDirectory(srcDir, srcDir);
			Assert.fail("an exception should have occurred");
		} catch (final IOException e) {
			Assert.assertTrue(true);
		}

	}

	/**
	 * Test copy directory.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCopyDirectory2() throws IOException {

		final File srcDir = buildOrnidroidHomeTest(TEST_DIRECTORY + "/srcDir");
		final File destDir = new File(TEST_DIRECTORY + "/destDir");

		// copy in an other directory
		try {
			FileHelper.copyDirectory(srcDir, destDir);
			final File[] srcDirContent = srcDir.listFiles();
			final File[] destDirContent = destDir.listFiles();
			List<String> srcDirContentPaths = new ArrayList<String>();

			List<String> destDirContentPaths = new ArrayList<String>();
			Assert.assertTrue(srcDirContent.length == destDirContent.length);
			for (int i = 0; i < srcDirContent.length; i++) {
				srcDirContentPaths.add(srcDirContent[i].getName());
				destDirContentPaths.add(destDirContent[i].getName());
			}
			for (String path : srcDirContentPaths) {
				Assert.assertTrue(destDirContentPaths.contains(path));
			}

		} catch (final IOException e) {
			Assert.fail("no exception should occur");
			e.printStackTrace();
		}

	}

	/**
	 * Test copy directory.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCopyDirectory3() throws IOException {

		final File srcDir = buildOrnidroidHomeTest(TEST_DIRECTORY + "/srcDir");

		final File destDir = buildOrnidroidHomeTest(TEST_DIRECTORY + "/destDir");

		final File existingFileInDestDir = new File(TEST_DIRECTORY
				+ "/destDir/audio/exiting.mp3");
		existingFileInDestDir.createNewFile();

		final File[] srcDirContent = srcDir.listFiles();

		// copy in an existing directory
		try {
			FileHelper.copyDirectory(srcDir, destDir);
			final File[] destDirContent = destDir.listFiles();
			Assert.assertTrue("check merge of the directories",
					srcDirContent.length == (destDirContent.length));
			Assert.assertTrue(srcDir.exists());
		} catch (final IOException e) {
			Assert.fail("no exception should occur");
			e.printStackTrace();
		}

	}

	/**
	 * Test create empty file.
	 */
	@Test
	public void testCreateEmptyFile() {
		final File file = new File(TEST_DIRECTORY + File.separator + "test.txt");
		Assert.assertFalse(file.exists());
		try {
			FileHelper.createEmptyFile(file);
			Assert.assertTrue(file.exists());
			// rewrite on an existing file
			FileHelper.createEmptyFile(file);
			Assert.assertTrue(file.exists());
		} catch (final IOException e) {
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Test force delete.
	 */
	@Test
	public void testForceDelete() {
		final File directory = new File(TEST_DIRECTORY + File.separator
				+ "dir1" + File.separator + "dir2");
		try {
			FileHelper.forceMkdir(directory);
			Assert.assertTrue(directory.exists());
			Assert.assertTrue(directory.isDirectory());
			FileHelper.forceDelete(directory);
			Assert.assertFalse(directory.exists());
		} catch (final IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test force mkdir.
	 */
	@Test
	public void testForceMkdir() {
		final File directory = new File(TEST_DIRECTORY + File.separator
				+ "dir1" + File.separator + "dir2");
		Assert.assertFalse(directory.exists());
		try {
			FileHelper.forceMkdir(directory);
			Assert.assertTrue(directory.exists());
			Assert.assertTrue(directory.isDirectory());
		} catch (final IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test move directory.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testMoveDirectory() throws IOException {
		final File srcDir = buildOrnidroidHomeTest(TEST_DIRECTORY);
		// copy in the same destination
		try {
			FileHelper.moveDirectory(srcDir, srcDir);
			Assert.fail("an exception should have occurred");
		} catch (final IOException e) {
			Assert.assertTrue(true);
		}

	}

	/**
	 * Test move directory2.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testMoveDirectory2() throws IOException {

		final File srcDir = buildOrnidroidHomeTest(TEST_DIRECTORY + "/srcDir");
		final File destDir = new File(TEST_DIRECTORY + "/destDir");
		final File[] srcDirContent = srcDir.listFiles();

		// mv in an other directory
		try {
			FileHelper.moveDirectory(srcDir, destDir);
			final File[] destDirContent = destDir.listFiles();
			Assert.assertTrue(srcDirContent.length == destDirContent.length);
		} catch (final IOException e) {
			Assert.fail("no exception should occur");
			e.printStackTrace();
		}

	}

	/**
	 * Test move directory3.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testMoveDirectory3() throws IOException {

		final File srcDir = buildOrnidroidHomeTest(TEST_DIRECTORY + "/srcDir");

		final File destDir = buildOrnidroidHomeTest(TEST_DIRECTORY + "/destDir");
		final File[] srcDirContent = srcDir.listFiles();

		// mv in an existing directory
		try {
			FileHelper.moveDirectory(srcDir, destDir);
			final File[] destDirContent = destDir.listFiles();
			Assert.assertTrue(srcDirContent.length == destDirContent.length);
			Assert.assertFalse(srcDir.exists());
		} catch (final IOException e) {
			Assert.fail("no exception should occur");
			e.printStackTrace();
		}

	}

	/**
	 * Test parse content file.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testParseContentFile() throws IOException {

		final File contentFile = new File(
				"./src/test/resources/images/bird_1/contents.properties");
		final String[] files = FileHelper.parseContentFile(contentFile);
		Assert.assertEquals("2 files expected", 2, files.length);
		Assert.assertEquals("1.jpg", files[0]);
		Assert.assertEquals("2.jpg", files[1]);

	}

	/**
	 * Test parse content file.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testUnzipFile() throws IOException {
		final File destDir = buildOrnidroidHomeTest(TEST_DIRECTORY + "/destDir");
		final File srcZip = new File("./src/test/resources/audio/audio.zip");
		final File destZip = new File(destDir.getAbsolutePath()
				+ File.separator + "audio.zip");
		FileHelper.doCopyFile(srcZip, destZip);
		FileHelper.unzipFile("audio.zip", destDir.getAbsolutePath());
	}
}
