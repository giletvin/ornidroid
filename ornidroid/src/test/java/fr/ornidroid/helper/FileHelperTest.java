package fr.ornidroid.helper;

import java.io.File;
import java.io.IOException;

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
			(new FileHelper()).copyDirectory(srcDir, srcDir);
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
			(new FileHelper()).copyDirectory(srcDir, destDir);
			final File[] srcDirContent = srcDir.listFiles();
			final File[] destDirContent = destDir.listFiles();
			Assert.assertTrue(srcDirContent.length == destDirContent.length);
			for (int i = 0; i < srcDirContent.length; i++) {
				Assert.assertTrue(srcDirContent[i].getName().equals(
						destDirContent[i].getName()));
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
			(new FileHelper()).copyDirectory(srcDir, destDir);
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
	 * Test count files.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCountFiles() throws IOException {
		final File srcDir = buildOrnidroidHomeTest(TEST_DIRECTORY);
		final int nbFiles = FileHelper.countFiles(srcDir);
		Assert.assertTrue(2 == nbFiles);
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
			(new FileHelper()).moveDirectory(srcDir, srcDir);
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
			(new FileHelper()).moveDirectory(srcDir, destDir);
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
			(new FileHelper()).moveDirectory(srcDir, destDir);
			final File[] destDirContent = destDir.listFiles();
			Assert.assertTrue(srcDirContent.length == destDirContent.length);
			Assert.assertFalse(srcDir.exists());
		} catch (final IOException e) {
			Assert.fail("no exception should occur");
			e.printStackTrace();
		}

	}

	@Test
	public void testTest() {

		final int i = 523;
		final int j = 523;
		final double k = ((double) i / j) * 100;
		final int l = (int) k;
		System.out.println(l);
	}
}
