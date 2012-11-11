package fr.ornidroid.ui.multicriteriasearch;

import android.content.Context;

/**
 * The Class BeakFormsHelper.
 */
public class BeakFormsHelper {

	/** The Constant BEC_AUTRE. */
	private static final String BEC_AUTRE = "bec_autre_30";

	/** The Constant BEC_CANARD. */
	private static final String BEC_CANARD = "bec_canard_30";

	/** The Constant BEC_COURBE. */
	private static final String BEC_COURBE = "bec_courbe_30";

	/** The Constant BEC_CROCHU. */
	private static final String BEC_CROCHU = "bec_crochu_30";

	/** The Constant BEC_EPAIS_ICON. */
	private static final String BEC_EPAIS_ICON = "bec_epais_30";

	/** The Constant BEC_FIN_COURT. */
	private static final String BEC_FIN_COURT = "bec_fin_court_30";

	/** The Constant BEC_FIN_LONG_ICON. */
	private static final String BEC_FIN_LONG_ICON = "bec_fin_long_30";

	/** The Constant BEC_GREBE. */
	private static final String BEC_GREBE = "bec_grebe_30";

	/** The Constant BEC_MOUETTE. */
	private static final String BEC_MOUETTE = "bec_mouette_30";

	/** The Constant BEC_TOUS. */
	private static final String BEC_TOUS = "bec_tous";
	/** The Constant DRAWABLE. */
	private static final String DRAWABLE = "drawable";

	/**
	 * Gets the drawable.
	 * 
	 * @param context
	 *            the context
	 * @param name
	 *            the name
	 * @return the drawable
	 */
	public static int getDrawable(final Context context, final String name) {
		int idDrawable = 0;
		if ((name != null) && (context != null)) {

			idDrawable = context.getResources().getIdentifier(name, DRAWABLE,
					context.getPackageName());
		}
		return idDrawable;
	}

	/**
	 * Gets the icon resource name from beak form id. This is very dependant to
	 * the sql data. Maps the beak form id to an icon.
	 * 
	 * @param beakFormId
	 *            the beak form id
	 * @return the icon resource name from beak form id
	 */
	public static String getIconResourceNameFromBeakFormId(final int beakFormId) {
		// INSERT INTO beak_form(id,name,lang) VALUES(1,"fin long",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(2,"épais",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(3,"autre",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(4,"courbé",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(5,"grèbe",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(6,"crochu",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(7,"fin court",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(8,"canard",'fr');
		// INSERT INTO beak_form(id,name,lang) VALUES(9,"mouette",'fr');

		String resourceName = null;

		switch (beakFormId) {
		case 1:
			resourceName = BEC_FIN_LONG_ICON;
			break;
		case 2:
			resourceName = BEC_EPAIS_ICON;
			break;
		case 3:
			resourceName = BEC_AUTRE;
			break;
		case 4:
			resourceName = BEC_COURBE;
			break;
		case 5:
			resourceName = BEC_GREBE;
			break;
		case 6:
			resourceName = BEC_CROCHU;
			break;
		case 7:
			resourceName = BEC_FIN_COURT;
			break;
		case 8:
			resourceName = BEC_CANARD;
			break;
		case 9:
			resourceName = BEC_MOUETTE;
			break;
		default:
			resourceName = BEC_TOUS;
			break;
		}
		return resourceName;
	}

}
