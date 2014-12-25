package fr.ornidroid.ui.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.ui.activity.HomeActivity_;

/**
 * The Class WikipediaFragment.
 */
@EFragment(R.layout.fragment_wikipedia)
public class WikipediaFragment extends AbstractFragment {

	@ViewById(R.id.wikipiedia_webview)
	WebView wikipediaWebView;

	@AfterViews
	void afterViews() {
		if (this.ornidroidService.getCurrentBird() == null) {
			// Github : #118
			final Intent intent = new Intent(getActivity(), HomeActivity_.class);
			startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}

		try {
			loadMediaFilesLocally();
			OrnidroidFile wikipediaPage = ornidroidService.getCurrentBird()
					.getWikipediaPage();

			if (wikipediaPage != null) {
				fragmentMainContent.setVisibility(View.VISIBLE);
				downloadBanner.setVisibility(View.GONE);
				wikipediaWebView.loadUrl("file:///" + wikipediaPage.getPath());

			} else {
				fragmentMainContent.setVisibility(View.GONE);
				downloadBanner.setVisibility(View.VISIBLE);
			}

		} catch (final OrnidroidException e) {
			Toast.makeText(
					getActivity(),
					"Error reading media files of bird "
							+ this.ornidroidService.getCurrentBird().getTaxon()
							+ " e", Toast.LENGTH_LONG).show();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.ornidroid.ui.components.AbstractFragment#getFileType()
	 */
	@Override
	public OrnidroidFileType getFileType() {
		return OrnidroidFileType.WIKIPEDIA_PAGE;
	}

}
