package fr.ornidroid.ui.multicriteriasearch;

import java.util.ArrayList;
import java.util.List;

public class MultiCriteriaSeachMenuItem {
	private String label;
	private Integer count;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		String result = (null == count ? "" : " (" + count + ")");
		return label + result;
	}

	public static List<MultiCriteriaSeachMenuItem> initList(
			List<String> listString) {
		List<MultiCriteriaSeachMenuItem> list = new ArrayList<MultiCriteriaSeachMenuItem>();
		for (String label : listString) {
			MultiCriteriaSeachMenuItem searchMenuItem = new MultiCriteriaSeachMenuItem();
			searchMenuItem.setLabel(label);
			list.add(searchMenuItem);
		}
		return list;
	}

}
