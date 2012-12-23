package fr.ornidroid.ui.multicriteriasearch;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import fr.ornidroid.R;

/**
 * The Class HelpDialog.
 */
public class HelpDialog extends Dialog implements OnClickListener {

	/** The dialog instance. */
	private static HelpDialog DIALOG_INSTANCE;

	/**
	 * Gets the single instance of HelpDialog.
	 * 
	 * @param context
	 *            the context
	 * @return single instance of HelpDialog
	 */
	public static HelpDialog getInstance(final Context context) {
		if (DIALOG_INSTANCE == null) {
			DIALOG_INSTANCE = new HelpDialog(context);
		}
		return DIALOG_INSTANCE;
	}

	/** The dialog content. */
	private final TextView dialogContent;

	/** The dialog title. */
	private final TextView dialogTitle;

	/** The ok button. */
	private final Button okButton;

	/**
	 * Instantiates a new help dialog.
	 * 
	 * @param context
	 *            the context
	 */
	private HelpDialog(final Context context) {
		super(context);
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.multicriteriasearch_help_dialog);
		this.dialogTitle = (TextView) findViewById(R.id.mcs_help_dialog_title);
		this.dialogContent = (TextView) findViewById(R.id.mcs_help_dialog_content);
		this.okButton = (Button) findViewById(R.id.multicriteriasearch_help_dialog_ok_button);
		this.okButton.setOnClickListener(this);
	}

	/**
	 * Gets the dialog content.
	 * 
	 * @return the dialog content
	 */
	public TextView getDialogContent() {
		return this.dialogContent;
	}

	/**
	 * Gets the dialog title.
	 * 
	 * @return the dialog title
	 */
	public TextView getDialogTitle() {
		return this.dialogTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(final View v) {
		if (v == this.okButton) {
			dismiss();
		}
	}
}