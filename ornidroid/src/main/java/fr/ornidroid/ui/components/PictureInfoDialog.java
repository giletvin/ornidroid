package fr.ornidroid.ui.components;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFile;
import fr.ornidroid.bo.PictureOrnidroidFile;

/**
 * The Class PictureInfoDialog.
 */
public class PictureInfoDialog extends DialogFragment {

	/** The description. */
	private TextView description;

	/** The author. */
	private TextView author;

	/** The source. */
	private TextView source;

	/** The licence. */
	private TextView licence;

	/** The ornidroid file. */
	private OrnidroidFile ornidroidFile;

	/**
	 * Instantiates a new picture info dialog.
	 */
	public PictureInfoDialog() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.picture_info_dialog, container);
		description = (TextView) view
				.findViewById(R.id.dialog_picture_description);

		author = (TextView) view.findViewById(R.id.dialog_picture_author);
		source = (TextView) view.findViewById(R.id.dialog_picture_source);
		licence = (TextView) view.findViewById(R.id.dialog_picture_licence);

		if (ornidroidFile != null) {
			description
					.setText(ornidroidFile
							.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));
			author.setText(ornidroidFile
					.getProperty(PictureOrnidroidFile.IMAGE_AUTHOR_PROPERTY));
			source.setText(ornidroidFile
					.getProperty(PictureOrnidroidFile.IMAGE_SOURCE_PROPERTY));
			licence.setText(ornidroidFile
					.getProperty(PictureOrnidroidFile.IMAGE_LICENCE_PROPERTY));
		}
		getDialog().setTitle(R.string.dialog_picture_title);

		Button okButton = (Button) view.findViewById(R.id.dialog_ok_button);
		okButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				PictureInfoDialog.this.dismiss();
			}
		});
		return view;
	}

	/**
	 * Sets the ornidroid file.
	 * 
	 * @param currentMediaFile
	 *            the new ornidroid file
	 */
	public void setOrnidroidFile(OrnidroidFile currentMediaFile) {
		this.ornidroidFile = currentMediaFile;

	}

}
