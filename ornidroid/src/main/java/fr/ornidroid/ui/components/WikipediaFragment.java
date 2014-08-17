package fr.ornidroid.ui.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.OrnidroidException;

/**
 * The Class WikipediaFragment.
 */
public class WikipediaFragment extends AbstractFragment {

	/** The specific content layout. */
	private LinearLayout specificContentLayout;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.components.AbstractFragment#getOnCreateView(android.view
	 * .LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View getOnCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.specificContentLayout = new LinearLayout(getActivity());
		this.specificContentLayout.setOrientation(LinearLayout.VERTICAL);
		WebView wikipediaWebView = new WebView(getActivity());
		try {
			loadMediaFilesLocally();
		} catch (final OrnidroidException e) {
			// Log.e(Constants.LOG_TAG, "Error reading media files of bird "
			// + this.bird.getTaxon() + " e");
		}

		OrnidroidFile wikipediaPage = ornidroidService.getCurrentBird()
				.getWikipediaPage();

		if (wikipediaPage != null) {

			wikipediaWebView.loadUrl("file:///" + wikipediaPage.getPath());

		} else {
			// TODO à implementer lorsque la page wiki n 'est pas trouvée en
			// local : proposer le téléchargement
		}
		specificContentLayout.addView(wikipediaWebView);
		return specificContentLayout;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.ornidroid.ui.components.AbstractFragment#getSpecificContentLayout()
	 */
	@Override
	protected LinearLayout getSpecificContentLayout() {
		return this.specificContentLayout;
	}

}
