package fr.ornidroid.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The Class FileUtils. code from commons-io 1.4
 */
public class FileHelper {
	/**
	 * Creates the empty file if it does not exist.
	 * 
	 * @param file
	 *            the file to create
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void createEmptyFile(final File file) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Deletes a file. If file is a directory, delete it and all
	 * sub-directories.
	 * <p>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>You get exceptions when a file or directory cannot be deleted.
	 * (java.io.File methods returns a boolean)</li>
	 * </ul>
	 * 
	 * @param file
	 *            file or directory to delete, must not be <code>null</code>
	 * @throws IOException
	 *             in case deletion is unsuccessful
	 */
	public static void forceDelete(final File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			final boolean filePresent = file.exists();
			if (!file.delete()) {
				if (!filePresent) {
					throw new FileNotFoundException("File does not exist: "
							+ file);
				}
				final String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}

	/**
	 * Makes a directory, including any necessary but nonexistent parent
	 * directories. If there already exists a file with specified name or the
	 * directory cannot be created then an exception is thrown.
	 * 
	 * @param directory
	 *            directory to create, must not be <code>null</code>
	 * @throws IOException
	 *             if the directory cannot be created
	 */
	public static void forceMkdir(final File directory) throws IOException {
		if (directory.exists()) {
			if (directory.isFile()) {
				final String message = "File " + directory + " exists and is "
						+ "not a directory. Unable to create directory.";
				throw new IOException(message);
			}
		} else {
			if (!directory.mkdirs()) {
				final String message = "Unable to create directory "
						+ directory;
				throw new IOException(message);
			}
		}
	}

	/**
	 * Cleans a directory without deleting it.
	 * 
	 * @param directory
	 *            directory to clean
	 * @throws IOException
	 *             in case cleaning is unsuccessful
	 */
	private static void cleanDirectory(final File directory) throws IOException {
		if (!directory.exists()) {
			final String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			final String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		final File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (int i = 0; i < files.length; i++) {
			final File file = files[i];
			try {
				forceDelete(file);
			} catch (final IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
	}

	/**
	 * Deletes a directory recursively.
	 * 
	 * @param directory
	 *            directory to delete
	 * @throws IOException
	 *             in case deletion is unsuccessful
	 */
	private static void deleteDirectory(final File directory)
			throws IOException {
		if (!directory.exists()) {
			return;
		}

		cleanDirectory(directory);
		if (!directory.delete()) {
			final String message = "Unable to delete directory " + directory
					+ ".";
			throw new IOException(message);
		}
	}

}
