package fr.ornidroid.ui.components;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import fr.ornidroid.bo.SimpleBird;

/**
 * The Class OrnidroidAutoCompleteAdapter.
 */
public class OrnidroidAutoCompleteAdapter extends ArrayAdapter<SimpleBird> {

	/** The m birds list. */
	private List<SimpleBird> mBirdsList;

	/**
	 * Instantiates a new ornidroid auto complete adapter.
	 * 
	 * @param context
	 *            the context
	 * @param resource
	 *            the resource
	 * @param birdsList
	 *            the birds list
	 */
	public OrnidroidAutoCompleteAdapter(Context context, int resource,
			List<SimpleBird> birdsList) {
		super(context, resource, birdsList);
		this.mBirdsList = birdsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getFilter()
	 */
	@Override
	public Filter getFilter() {
		Filter myFilter = new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				// Do not filter anything. Everything is okay : the filtering is
				// done is the SQL query
				// the user can type 'mesange' --> we should not drop 'mésange'.
				FilterResults results = new FilterResults();
				results.values = mBirdsList;
				results.count = mBirdsList.size();
				return results;

			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// Now we have to inform the adapter about the new list filtered
				if (results.count == 0)
					notifyDataSetInvalidated();
				else {
					mBirdsList = (List<SimpleBird>) results.values;
					notifyDataSetChanged();
				}

			}

		};
		return myFilter;
	}
}
