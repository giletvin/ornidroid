package fr.giletvin.ornidroid.helper;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * The Class IOHelper.
 */
public class IOHelper {

	/**
	 * Check if the size of the file is the same as the value contained in the
	 * inputStream.
	 * 
	 * @param file
	 *            the file
	 * @param isCheckSize
	 *            the is check size
	 * @return true, if size of file is the same. False in any other case
	 *         (exceptions should be caught here).
	 */
	public static boolean checkSize(final File file,
			final InputStream isCheckSize) {
		boolean checkOk = false;

		// check size to see if local db is the same as the db in assets
		try {
			final long checksize = file.length();
			final List<String> lines = IOUtils.readLines(isCheckSize);
			if (lines.size() == 1) {

				final long checksizeControl = Long.parseLong(lines.get(0));
				if (checksizeControl == checksize) {
					checkOk = true;
				}
			}

		} catch (final Exception e) {
			// exception : consider that the file is not up to date
			checkOk = false;
		}
		return checkOk;
	}

}
