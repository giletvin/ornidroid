/***********************************************************************************************************************
 *
 * blueBill Core - open source birding
 * Copyright (C) 2009-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * WWW: http://bluebill.tidalwave.it
 * SCM: https://java.net/hg/bluebill~core-src
 *
 **********************************************************************************************************************/
package fr.ornidroid.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Fabrizio Giudici
 * @version $Id$
 * 
 */
public class DefaultDownloadable extends Downloadable {

	/**
	 * safe delete.
	 * 
	 * @param file
	 *            the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void safeDelete(final File file) throws IOException {
		if (!file.delete()) {
			throw new IOException("Not deleted: " + file); // Java 5 / Android
															// compatibility
		}
	}

	/**
	 * Normalized.
	 * 
	 * @param url
	 *            the url
	 * @return the string
	 */
	static String normalized(final URL url) {
		return url.toExternalForm().replaceAll("://", "/")
				.replaceAll("[:;#$?&=]", "_");
	}

	/** The proxied url. */
	public final URL proxiedUrl;

	/** The url. */
	public final URL url; // FIXME

	/** The destination path. */
	private final String destinationPath;

	/* package *//** The cached file. */
	File cachedFile;

	/** The content length. */
	int contentLength = 0;

	/* package *//** The download file. */
	File downloadFile;

	/* package *//** The timestamp file. */
	File timestampFile;

	/**
	 * Constructor.
	 * 
	 * @param url
	 *            the url
	 * @param destinationPath
	 *            the destination path
	 * @throws MalformedURLException
	 *             the malformed url exception
	 */
	public DefaultDownloadable(final URL url, final String destinationPath)
			throws MalformedURLException {
		final String x = url.toExternalForm().replaceAll(" ", "%20");
		this.destinationPath = destinationPath;
		this.url = new URL(x);
		this.proxiedUrl = new URL(x); // FIXME: can be eventually different,
										// inject a proxy configurator

		computeCachedFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.download.Downloadable#download()
	 */
	@Override
	public void download() {
		download(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.download.Downloadable#getFile()
	 */
	@Override
	public File getFile() {
		return this.cachedFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.download.Downloadable#refresh()
	 */
	@Override
	public void refresh() {
		download(true);
	}

	/**
	 * Removes the.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void remove() throws IOException {
		safeDelete(this.cachedFile);

		setStatus(Status.NOT_DOWNLOADED);
	}

	/**
	 * Compute cached file.
	 */
	private void computeCachedFile() {
		Status newStatus = null;

		synchronized (this) {
			if (this.cachedFile == null) {
				try {

					final String prefix = this.destinationPath
							+ File.separator
							+ StringUtils.substringAfterLast(
									normalized(this.url), File.separator);

					// + "/"+ normalized(url);

					this.cachedFile = new File(prefix);
					this.downloadFile = new File(prefix + ".download");
					this.timestampFile = new File(prefix + ".timestamp");

					if (this.cachedFile.exists()) {
						newStatus = Status.DOWNLOADED;
					} else {
						newStatus = Status.NOT_DOWNLOADED;
					}
				} catch (final Exception e) {
					newStatus = Status.BROKEN;
				}
			}
		}

		if (newStatus != null) {
			setStatus(newStatus);
		}
	}

	/**
	 * 
	 * 
	 * Can be overridden for testing (URLs are not mockable).
	 * 
	 * 
	 * @return the input stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private InputStream createInputStream() throws IOException {
		final URLConnection connection = this.proxiedUrl.openConnection();
		connection.connect();
		this.contentLength = connection.getContentLength(); // TODO: handle the
															// case
		// in which it's unknown
		// size
		return connection.getInputStream();

	}

	/**
	 * Download.
	 * 
	 * @param always
	 *            the always
	 */
	private synchronized void download(final boolean always) {
		computeCachedFile();

		if (always || (this.status == Status.NOT_DOWNLOADED)) {
			setStatus(Status.DOWNLOADING);
			try {
				load();
			} catch (final FileNotFoundException e) {
				setStatus(Status.BROKEN);
			} catch (final IOException e) {
				setStatus(Status.CONNECTION_PROBLEM);
			}

		}
	}

	/**
	 * load file from internet.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void load() throws IOException {
		final byte[] buffer = new byte[64 * 1024];
		this.cachedFile.getParentFile().mkdirs();

		if (this.downloadFile.exists()) {
			safeDelete(this.downloadFile);
		}

		final InputStream is = createInputStream();

		final OutputStream os = new FileOutputStream(this.downloadFile);
		// final OutputStream os =
		// fileSystem.get().openFileOutput(getCachedFile());
		// FIXME: first download in cache, at the end copy to the real
		// resource.
		int loaded = 0;

		for (;;) {
			final int n = is.read(buffer);

			if (n < 0) {
				break;
			}

			os.write(buffer, 0, n);
			loaded += n;

			if (this.contentLength > 0) {
				setDownloadProgress((1.0f * loaded) / this.contentLength);

			}
		}

		if (this.cachedFile.exists()) {
			safeDelete(this.cachedFile);
		}

		this.downloadFile.renameTo(this.cachedFile);
		final FileWriter w = new FileWriter(this.timestampFile);
		w.write("downloaded: " + System.currentTimeMillis() + "\n");

		setStatus(Status.DOWNLOADED);
	}
}
