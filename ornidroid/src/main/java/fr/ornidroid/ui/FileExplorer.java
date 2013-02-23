package fr.ornidroid.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFileType;

/**
 * The Class FileExplorer.
 * http://android-er.blogspot.fr/2012/07/example-of-file-
 * explorer-in-android.html
 */
public class FileExplorer extends ListActivity {

	/** The Constant DIALOG_TEXT_ENTRY. */
	private static final int DIALOG_TEXT_ENTRY = 7;

	/** The file type. */
	private OrnidroidFileType fileType;

	/** The item. */
	private List<String> item = null;

	/** The my path. */
	private TextView myPath;

	/** The path. */
	private List<String> path = null;

	/** The root. */
	private String root;

	/** The selected file. */
	private String selectedFile;
	/** The filecomparator. */
	Comparator<? super File> filecomparator = new Comparator<File>() {

		public int compare(final File file1, final File file2) {

			if (file1.isDirectory()) {
				if (file2.isDirectory()) {
					return String.valueOf(file1.getName().toLowerCase())
							.compareTo(file2.getName().toLowerCase());
				} else {
					return -1;
				}
			} else {
				if (file2.isDirectory()) {
					return 1;
				} else {
					return String.valueOf(file1.getName().toLowerCase())
							.compareTo(file2.getName().toLowerCase());
				}
			}

		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_explorer);
		this.myPath = (TextView) findViewById(R.id.path);

		this.root = Environment.getExternalStorageDirectory().getPath();
		this.fileType = (OrnidroidFileType) getIntent().getSerializableExtra(
				OrnidroidFileType.FILE_TYPE_INTENT_PARAM_NAME);

		findViewById(R.id.cancel_add_custom_media).setOnClickListener(
				new OnClickListener() {
					public void onClick(final View v) {
						goBackToBirdActivity();
					}
				});
		getDir(this.root);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(final int id) {
		switch (id) {
		case DIALOG_TEXT_ENTRY:
			// This example shows how to add a custom layout to an AlertDialog
			final LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(
					R.layout.alert_dialog_text_entry, null);
			return new AlertDialog.Builder(FileExplorer.this)

					.setTitle(this.selectedFile)
					.setView(textEntryView)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int whichButton) {

									/* User clicked OK so do some stuff */
									// TODO
								}
							}).setNegativeButton(R.string.cancel, null)
					.create();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
		final File file = new File(this.path.get(position));

		if (file.isDirectory()) {
			if (file.canRead()) {
				getDir(this.path.get(position));
			} else {
				new AlertDialog.Builder(this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle(
								"[" + file.getName()
										+ "] folder can't be read!")
						.setPositiveButton("OK", null).show();
			}
		} else {
			this.selectedFile = file.getName();
			showDialog(DIALOG_TEXT_ENTRY);
			// new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher)
			// .setTitle("[" + file.getName() + "]")
			// .setPositiveButton("OK", null).show();

		}
	}

	/**
	 * Gets the dir.
	 * 
	 * @param dirPath
	 *            the dir path
	 * @return the dir
	 */
	private void getDir(final String dirPath) {
		this.myPath.setText("Location: " + dirPath);
		this.item = new ArrayList<String>();
		this.path = new ArrayList<String>();
		final File f = new File(dirPath);
		final File[] files = f.listFiles(new FileFilter() {
			public boolean accept(final File pathName) {
				// only accept directories and files ending with .jpg or .mp3
				if (!pathName.isDirectory()
						&& !pathName
								.getAbsolutePath()
								.endsWith(
										OrnidroidFileType
												.getExtension(FileExplorer.this.fileType))) {
					return false;
				}
				return true;
			}
		});

		if (!dirPath.equals(this.root)) {
			this.item.add(this.root);
			this.path.add(this.root);
			this.item.add("../");
			this.path.add(f.getParent());
		}

		Arrays.sort(files, this.filecomparator);

		for (int i = 0; i < files.length; i++) {
			final File file = files[i];

			if (!file.isHidden() && file.canRead()) {
				this.path.add(file.getPath());
				if (file.isDirectory()) {
					this.item.add(file.getName() + "/");
				} else {
					this.item.add(file.getName());
				}
			}
		}

		final ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
				R.layout.file_explorer_row, this.item);
		setListAdapter(fileList);
	}

	/**
	 * Go back to bird activity.
	 */
	private void goBackToBirdActivity() {
		final Intent intentBirdInfo = new Intent(getApplicationContext(),
				BirdActivity.class);

		// put the uri so that the BirdInfoActivity
		// reloads correctly the
		// bird
		// intentBirdInfo.setData(this.getIntent().getData());
		// put an extra info to let the
		// BirdInfoActivity know which tab to
		// open.
		intentBirdInfo.putExtra(BirdActivity.INTENT_TAB_TO_OPEN,
				OrnidroidFileType.getCode(this.fileType));
		startActivity(intentBirdInfo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

	}

}