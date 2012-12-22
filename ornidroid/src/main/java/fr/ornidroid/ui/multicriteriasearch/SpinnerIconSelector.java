package fr.ornidroid.ui.multicriteriasearch;

import fr.ornidroid.R;

/**
 * The Class SpinnerIconSelector. This is a helper that allows to display icons
 * in the spinners.
 */
public class SpinnerIconSelector {

	/**
	 * Gets the icon resource id from beak form id. This is very dependant to
	 * the sql data. Maps the beak form id to an icon.
	 * 
	 * @param beakFormId
	 *            the beak form id
	 * @return the icon resource name from beak form id
	 */
	public static int getIconResourceIdFromBeakFormId(final int beakFormId) {

		// INSERT INTO beak_form(id,name,lang)
		// VALUES(1,"autres becs droits",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(2,"épais et court",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(3,"autre",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(4,"courbé",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(5,"droit et long",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(6,"crochu",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(7,"fin et court",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(8,"canard",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(9,"mouette",'fr');

		int resourceId = 0;

		switch (beakFormId) {
		case 1:
			resourceId = R.drawable.bec_autres_becs_droits;
			break;
		case 2:
			resourceId = R.drawable.bec_epais;
			break;
		case 3:
			resourceId = R.drawable.bec_autre;
			break;
		case 4:
			resourceId = R.drawable.bec_courbe;
			break;
		case 5:
			resourceId = R.drawable.bec_droit_long;
			break;
		case 6:
			resourceId = R.drawable.bec_crochu;
			break;
		case 7:
			resourceId = R.drawable.bec_fin_court;
			break;
		case 8:
			resourceId = R.drawable.bec_canard;
			break;
		case 9:
			resourceId = R.drawable.bec_mouette;
			break;
		default:
			resourceId = R.drawable.bec_tous;
			break;
		}
		return resourceId;
	}

}
