package fr.ornidroid.ui.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.webkit.WebView;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;

/**
 * The Class WikipediaFragment.
 */
@EFragment(R.layout.fragment_wikipedia)
public class WikipediaFragment extends AbstractFragment {

	@ViewById(R.id.wikipiedia_webview)
	WebView wikipediaWebView;

	@AfterViews
	void afterViews() {

		if (commonAfterViews()) {
			OrnidroidFile wikipediaPage = ornidroidService.getCurrentBird()
					.getWikipediaPage();
			wikipediaWebView.loadUrl("file:///" + wikipediaPage.getPath());
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
