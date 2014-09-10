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
