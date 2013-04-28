package fr.ornidroid.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class FileUtils. code from commons-io 2.4
 */
public class FileHelper {
	/** The Constant ONE_KB. */
	public static final long ONE_KB = 1024;
	/** The Constant ONE_MB. */
	public static final long ONE_MB = ONE_KB * ONE_KB;
	/** The Constant FILE_COPY_BUFFER_SIZE. */
	private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

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
		if (file.exists()) {
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
	 * Moves a directory.
	 * <p>
	 * When the destination directory is on another file system, do a
	 * "copy and delete".
	 * 
	 * @param srcDir
	 *            the directory to be moved
	 * @param destDir
	 *            the destination directory
	 * @throws IOException
	 *             if an IO error occurs moving the file
	 * @since 1.4
	 */
	public static void moveDirectory(final File srcDir, final File destDir)
			throws IOException {
		if (srcDir == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!srcDir.exists()) {
			throw new FileNotFoundException("Source '" + srcDir
					+ "' does not exist");
		}
		if (!srcDir.isDirectory()) {
			throw new IOException("Source '" + srcDir + "' is not a directory");
		}

		boolean rename = false;
		if (!destDir.exists()) {
			rename = srcDir.renameTo(destDir);
		}

		if (!rename) {
			if (destDir.getCanonicalPath()
					.startsWith(srcDir.getCanonicalPath())) {
				throw new IOException("Cannot move directory: " + srcDir
						+ " to a subdirectory of itself: " + destDir);
			}
			copyDirectory(srcDir, destDir);
			deleteDirectory(srcDir);
			if (srcDir.exists()) {
				throw new IOException("Failed to delete original directory '"
						+ srcDir + "' after copy to '" + destDir + "'");
			}
		}
	}

	/**
	 * Copies a whole directory to a new location.
	 * <p>
	 * This method copies the specified directory and all its child directories
	 * and files to the specified destination. The destination is the new
	 * location and name of the directory.
	 * <p>
	 * The destination directory is created if it does not exist. If the
	 * destination directory did exist, then this method merges the source with
	 * the destination, with the source taking precedence.
	 * <p>
	 * <strong>Note:</strong> This method tries to preserve the files' last
	 * modified date/times using {@link File#setLastModified(long)}, however it
	 * is not guaranteed that those operations will succeed. If the modification
	 * operation fails, no indication is provided.
	 * 
	 * @param srcDir
	 *            an existing directory to copy, must not be {@code null}
	 * @param destDir
	 *            the new directory, must not be {@code null}
	 * @throws IOException
	 *             if an IO error occurs during copying
	 * @since 1.1
	 */
	protected static void copyDirectory(final File srcDir, final File destDir)
			throws IOException {
		if (srcDir == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (srcDir.exists() == false) {
			throw new FileNotFoundException("Source '" + srcDir
					+ "' does not exist");
		}
		if (srcDir.isDirectory() == false) {
			throw new IOException("Source '" + srcDir
					+ "' exists but is not a directory");
		}
		if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
			throw new IOException("Source '" + srcDir + "' and destination '"
					+ destDir + "' are the same");
		}

		// Cater for destination being directory within the source directory
		// (see IO-141)
		List<String> exclusionList = null;
		if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
			final File[] srcFiles = srcDir.listFiles();

			if ((srcFiles != null) && (srcFiles.length > 0)) {
				exclusionList = new ArrayList<String>(srcFiles.length);
				for (final File srcFile : srcFiles) {
					final File copiedFile = new File(destDir, srcFile.getName());
					exclusionList.add(copiedFile.getCanonicalPath());
				}
			}
		}
		doCopyDirectory(srcDir, destDir, exclusionList);
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

	/**
	 * Internal copy directory method.
	 * 
	 * @param srcDir
	 *            the validated source directory, must not be {@code null}
	 * @param destDir
	 *            the validated destination directory, must not be {@code null}
	 * @param exclusionList
	 *            List of files and directories to exclude from the copy, may be
	 *            null
	 * @throws IOException
	 *             if an error occurs
	 * @since 1.1
	 */
	private static void doCopyDirectory(final File srcDir, final File destDir,

	final List<String> exclusionList) throws IOException {
		// recurse
		final File[] srcFiles = srcDir.listFiles();
		if (srcFiles == null) { // null if abstract pathname does not denote a
								// directory, or if an I/O error occurs
			throw new IOException("Failed to list contents of " + srcDir);
		}
		if (destDir.exists()) {
			if (destDir.isDirectory() == false) {
				throw new IOException("Destination '" + destDir
						+ "' exists but is not a directory");
			}
		} else {
			if (!destDir.mkdirs() && !destDir.isDirectory()) {
				throw new IOException("Destination '" + destDir
						+ "' directory cannot be created");
			}
		}
		if (destDir.canWrite() == false) {
			throw new IOException("Destination '" + destDir
					+ "' cannot be written to");
		}
		for (final File srcFile : srcFiles) {
			final File dstFile = new File(destDir, srcFile.getName());
			if ((exclusionList == null)
					|| !exclusionList.contains(srcFile.getCanonicalPath())) {
				if (srcFile.isDirectory()) {
					doCopyDirectory(srcFile, dstFile, exclusionList);
				} else {
					doCopyFile(srcFile, dstFile);
				}
			}
		}

	}

	/**
	 * Internal copy file method.
	 * 
	 * @param srcFile
	 *            the validated source file, must not be {@code null}
	 * @param destFile
	 *            the validated destination file, must not be {@code null}
	 * @throws IOException
	 *             if an error occurs
	 */
	private static void doCopyFile(final File srcFile, final File destFile)
			throws IOException {
		if (destFile.exists() && destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile
					+ "' exists but is a directory");
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel input = null;
		FileChannel output = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			input = fis.getChannel();
			output = fos.getChannel();
			final long size = input.size();
			long pos = 0;
			long count = 0;
			while (pos < size) {
				count = (size - pos) > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE
						: size - pos;
				pos += output.transferFrom(input, pos, count);
			}

		} finally {
			IOHelper.closeQuietly(output);
			IOHelper.closeQuietly(fos);
			IOHelper.closeQuietly(input);
			IOHelper.closeQuietly(fis);
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '"
					+ srcFile + "' to '" + destFile + "'");
		}
	}

	/**
	 * Private constructor.
	 */
	private FileHelper() {
	}

}
