package fr.ornidroid.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import fr.ornidroid.tests.AbstractTest;

/**
 * The Class IOHelperTest.
 */
public class IOHelperTest extends AbstractTest {

	/**
	 * Test check size.
	 * 
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	@Test
	public void testCheckSize() throws FileNotFoundException {
		final String dbPath = "./src/test/resources" + File.separator
				+ BasicConstants.DB_NAME;
		final File dbFile = new File(dbPath);
		final String checkSizeFilePath = dbPath + ".size";
		final File checkSizeFile = new File(checkSizeFilePath);
		InputStream isCheckSizeIs = new FileInputStream(checkSizeFilePath);

		Assert.assertTrue("file up to date",
				IOHelper.checkSize(dbFile, isCheckSizeIs));

		// reopen the is
		isCheckSizeIs = new FileInputStream(checkSizeFilePath);
		Assert.assertFalse("file is not the same",
				IOHelper.checkSize(checkSizeFile, isCheckSizeIs));

		Assert.assertFalse("Bad md5sum file check : should be false",
				IOHelper.checkSize(dbFile, new FileInputStream(dbPath)));

		Assert.assertFalse("Null safe check : should be false",
				IOHelper.checkSize(null, null));

		Assert.assertFalse("Null safe check : should be false",
				IOHelper.checkSize(dbFile, null));
		Assert.assertFalse("Null safe check : should be false",
				IOHelper.checkSize(null, isCheckSizeIs));

	}

	/**
	 * Test copy.
	 * 
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	@Test
	public void testCopy() throws FileNotFoundException {
		final String dbPath = "./src/test/resources" + File.separator
				+ BasicConstants.DB_NAME;
		final File dbFile = new File(dbPath);

		final File outFile = new File(AbstractTest.TEST_DIRECTORY
				+ File.separator + "outFile.txt");
		Assert.assertFalse(outFile.exists());
		final InputStream input = new FileInputStream(dbFile);
		final OutputStream output = new FileOutputStream(outFile);
		try {
			IOHelper.copy(input, output);
			Assert.assertTrue(outFile.exists());
		} catch (final IOException e) {
			Assert.fail(e.getMessage());
		}
	}
}
