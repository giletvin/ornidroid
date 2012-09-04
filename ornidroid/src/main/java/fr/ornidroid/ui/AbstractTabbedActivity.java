package fr.ornidroid.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import fr.ornidroid.R;

/**
 * The Class AbstractTabbedActivity.
 */
public abstract class AbstractTabbedActivity extends Activity {

	/** The main content. */
	private LinearLayout mainContent;

	/** The tabs. */
	private LinearLayout tabs;

	/**
	 * Gets the main content.
	 * 
	 * @return the main content
	 */
	protected LinearLayout getMainContent() {
		return this.mainContent;
	}

	/**
	 * Gets the tabs.
	 * 
	 * @return the tabs
	 */
	protected LinearLayout getTabs() {
		return this.tabs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbed_activity);
		this.tabs = (LinearLayout) findViewById(R.id.tabs);
		this.mainContent = (LinearLayout) findViewById(R.id.main_content);
	}

	/**
	 * Sets the main content.
	 * 
	 * @param mainContent
	 *            the new main content
	 */
	protected void setMainContent(final LinearLayout mainContent) {
		this.mainContent = mainContent;
	}

	/**
	 * Sets the tabs.
	 * 
	 * @param tabs
	 *            the new tabs
	 */
	protected void setTabs(final LinearLayout tabs) {
		this.tabs = tabs;
	}
}