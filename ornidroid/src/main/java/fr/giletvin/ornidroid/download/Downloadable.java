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
package fr.giletvin.ornidroid.download;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * *****************************************************************************
 * ****************************************.
 * 
 * @author Fabrizio Giudici
 * @version $Id$
 * 
 *          ********************************************************************
 *          ************************************************
 */
public abstract class Downloadable {

	/** The Constant serialVersionUID. */
	private final static long serialVersionUID = 876756656347568673L;

	/** The Constant NOT_DOWNLOADED. */
	public final static int NOT_DOWNLOADED = 0;

	/** The status. */
	protected Status status = Status.NOT_DOWNLOADED;

	/**
	 * The Enum Status.
	 */
	public static enum Status {

		/** The NO t_ downloaded. */
		NOT_DOWNLOADED(false),
		/** The QUEUED. */
		QUEUED(false),
		/** The DOWNLOADING. */
		DOWNLOADING(false),
		/** The DOWNLOADED. */
		DOWNLOADED(true),
		/** The OBSOLETE. */
		OBSOLETE(false),
		/** The BROKEN. */
		BROKEN(true);

		/** The Constant BUNDLE_NAME. */
		private static final String BUNDLE_NAME = Status.class.getPackage()
				.getName().replace('.', '/')
				+ "/Bundle";

		/**
		 * Instantiates a new status.
		 * 
		 * @param b
		 *            the b
		 */
		Status(boolean b) {
			final_ = b;
		}

		/** The Constant LOCALE_COMPARATOR. */
		static final Comparator<Locale> LOCALE_COMPARATOR = new Comparator<Locale>() {
			public int compare(final Locale l1, final Locale l2) {
				return l1.getLanguage().compareTo(l2.getLanguage());
			}
		};

		/** The final_. */
		private final boolean final_;

		/**
		 * Gets the display name.
		 * 
		 * @return the display name
		 */
		public String getDisplayName() {
			return getDisplayName(Locale.getDefault());
		}

		/**
		 * Gets the display name.
		 * 
		 * @param locale
		 *            the locale
		 * @return the display name
		 */
		public String getDisplayName(final Locale locale) {
			// final ResourceBundle bundle = NbBundle.getBundle(BUNDLE_NAME,
			// locale); FIXME: crashes Android 1.5
			final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME,
					locale);
			return bundle.getString("status_" + toString());
		}

		/**
		 * Gets the display names.
		 * 
		 * @return the display names
		 */
		public Map<Locale, String> getDisplayNames() {
			final Map<Locale, String> result = new HashMap<Locale, String>();

			for (final Locale locale : getLocales()) {
				result.put(locale, getDisplayName(locale));
			}

			return result;
		}

		/**
		 * Gets the locales.
		 * 
		 * @return the locales
		 */
		public SortedSet<Locale> getLocales() {
			final TreeSet<Locale> treeSet = new TreeSet<Locale>(
					LOCALE_COMPARATOR);
			treeSet.addAll(Arrays.asList(new Locale("en"), new Locale("it"),
					new Locale("fr")));
			return treeSet;
		}

		/**
		 * Checks if is final.
		 * 
		 * @return true, if is final
		 */
		public boolean isFinal() {
			return final_;
		}
	}

	/**
	 * *************************************************************************
	 * ****************************************
	 * *********************************
	 * ******************************************
	 * *************************************.
	 */

	public static final Class<Downloadable> Downloadable = Downloadable.class;

	/** The Constant PROP_DOWNLOAD_PROGRESS. */
	public static final String PROP_DOWNLOAD_PROGRESS = "downloadProgress";

	/** The Constant PROP_STATUS. */
	public static final String PROP_STATUS = "status";

	/** The Constant PROP_CACHED. */
	public static final String PROP_CACHED = "cached";

	/** The download progress. */
	protected float downloadProgress = 0f;

	/** The cached. */
	protected boolean cached = false;

	/** The property change support. */
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	/**
	 * *************************************************************************
	 * ****************************************
	 * 
	 * 
	 * *************************************************************************
	 * ***************************************.
	 * 
	 * @return the download progress
	 */

	public float getDownloadProgress() {
		return downloadProgress;
	}

	/**
	 * *************************************************************************
	 * ****************************************
	 * 
	 * 
	 * *************************************************************************
	 * ***************************************.
	 * 
	 * @return the file
	 */

	/*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/

	public abstract File getFile();

	/**
	 * *************************************************************************
	 * ****************************************
	 * 
	 * 
	 * *************************************************************************
	 * ***************************************.
	 * 
	 * @return true, if is cached
	 */
	public boolean isCached() {
		return cached;
	}

	/**
	 * *************************************************************************
	 * ****************************************
	 * 
	 * 
	 * *************************************************************************
	 * ***************************************.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * *************************************************************************
	 * ****************************************
	 * 
	 * 
	 * *************************************************************************
	 * ***************************************.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removePropertyChangeListener(
			final PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * *************************************************************************
	 * ****************************************
	 * 
	 * 
	 * *************************************************************************
	 * ***************************************.
	 */
	public abstract void download();

	/**
	 * *************************************************************************
	 * ****************************************
	 * 
	 * 
	 * *************************************************************************
	 * ***************************************.
	 */
	public abstract void refresh();

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * *************************************************************************
	 * ****************************************
	 * 
	 * 
	 * *************************************************************************
	 * ***************************************.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public synchronized void waitUntilDownloadingCompleted()
			throws InterruptedException {
		while (getStatus() == Status.DOWNLOADING) {
			wait();
		}
	}

	/**
	 * *************************************************************************
	 * ****************************************
	 * 
	 * 
	 * *************************************************************************
	 * ***************************************.
	 * 
	 * @param downloadProgress
	 *            the new download progress
	 */
	protected void setDownloadProgress(final float downloadProgress) {
		final float oldDownloadProgress = this.downloadProgress;
		this.downloadProgress = downloadProgress;
		propertyChangeSupport.firePropertyChange(PROP_DOWNLOAD_PROGRESS,
				oldDownloadProgress, downloadProgress);
	}

	/**
	 * *************************************************************************
	 * ****************************************
	 * 
	 * 
	 * *************************************************************************
	 * ***************************************.
	 * 
	 * @param status
	 *            the new status
	 */
	protected void setStatus(final Status status) {

		final Status oldStatus = this.status;
		this.status = status;

		synchronized (this) {
			notifyAll();
		}

		propertyChangeSupport
				.firePropertyChange(PROP_STATUS, oldStatus, status);
	}

	/**
	 * *************************************************************************
	 * ****************************************
	 * 
	 * 
	 * *************************************************************************
	 * ***************************************.
	 * 
	 * @param cached
	 *            the new cached
	 */
	protected void setCached(final boolean cached) {
		final boolean oldCached = this.cached;
		this.cached = cached;
		propertyChangeSupport
				.firePropertyChange(PROP_CACHED, oldCached, cached);
	}
}
