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
}
