package fr.ornidroid.ui;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import fr.ornidroid.R;
import fr.ornidroid.bo.Credits;

/**
 * The Class AboutActivity.
 */
public class AboutActivity extends AbstractOrnidroidActivity {

	/** The layout. */
	private LinearLayout layout;

	/**
	 * Instantiates a new about activity.
	 */
	public AboutActivity() {
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
		setContentView(R.layout.about);

		setTitle(R.string.about);

		printCredits();

	}

	/**
	 * Load the credits from xml.
	 * 
	 * @return the list to be used in the simple adapter
	 */
	private List<Map<String, String>> loadCredits() {
		final String[] creditsArray = this.getResources().getStringArray(
				R.array.credits);
		final Credits credits = new Credits(creditsArray);
		return credits.getListCredits();
	}

	/**
	 * Prints the credits.
	 */
	private void printCredits() {
		this.layout = (LinearLayout) findViewById(R.id.credits_layout);
		final ListView mListView = new ListView(this);
		final SimpleAdapter adapter = new SimpleAdapter(this, loadCredits(),
				R.layout.credits_list, new String[] { Credits.CREDIT_TITLE,
						Credits.CREDIT_AUTHOR, Credits.CREDIT_URL,
						Credits.CREDIT_LICENSE }, new int[] {
						R.id.credit_title, R.id.credit_author, R.id.credit_url,
						R.id.credit_license });
		mListView.setAdapter(adapter);

		mListView.setTextFilterEnabled(true);
		this.layout.addView(mListView);

	}

}
