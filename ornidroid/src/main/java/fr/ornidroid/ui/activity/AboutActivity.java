package fr.ornidroid.ui.activity;

import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.Credits;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.ui.AbstractOrnidroidActivity;

/**
 * The Class AboutActivity.
 */
@EActivity(R.layout.about)
public class AboutActivity extends AbstractOrnidroidActivity {

	/** The about text. */
	@ViewById(R.id.about_ornidroid_version)
	TextView aboutText;

	/** The credits layout. */
	@ViewById(R.id.credits_layout)
	LinearLayout creditsLayout;

	/**
	 * After views.
	 */
	@AfterViews
	void afterViews() {
		final String applicationName = Constants.getApplicationName();

		aboutText.setText(applicationName + BasicConstants.BLANK_STRING
				+ Constants.getVersionName() + BasicConstants.CARRIAGE_RETURN
				+ this.getResources().getString(R.string.about_ornidroid));

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
		final ListView mListView = new ListView(this);
		final SimpleAdapter adapter = new SimpleAdapter(this, loadCredits(),
				R.layout.credits_list, new String[] { Credits.CREDIT_TITLE,
						Credits.CREDIT_AUTHOR, Credits.CREDIT_URL,
						Credits.CREDIT_LICENSE }, new int[] {
						R.id.credit_title, R.id.credit_author, R.id.credit_url,
						R.id.credit_license });
		mListView.setAdapter(adapter);

		mListView.setTextFilterEnabled(true);
		this.creditsLayout.addView(mListView);

	}

}
