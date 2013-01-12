package fr.ornidroid.ui;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;
import fr.ornidroid.R;

/**
 * The Class AboutActivity.
 */
public class HelpActivity extends AbstractOrnidroidActivity {

	/**
	 * Instantiates a new about activity.
	 */
	public HelpActivity() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		setTitle(R.string.help);

		final TextView help = (TextView) findViewById(R.id.help_text);
		Linkify.addLinks(help, Linkify.ALL);

	}

}
