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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.ornidroid.R;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.service.IOrnidroidIOService;
import fr.ornidroid.service.OrnidroidIOServiceImpl;

/**
 * The Class AddCustomMediaActivity allows to add a custom mp3 or jpg file to
 * the ornidroid data. Opens a file explorer . code found here :
 * http://android-er.blogspot.fr/2012/07/example-of-file-
 * explorer-in-android.html
 */
public class AddCustomMediaActivity extends ListActivity {
	/** The Constant DIALOG_TEXT_ENTRY. */
	private static final int DIALOG_TEXT_ENTRY = 7;
	/** The bird directory. */
	private String birdDirectory;

	/** The file type. */
	private OrnidroidFileType fileType;

	/** The item. */
	private List<String> item = null;

	/** The my path. */
	private TextView myPath;

	/** The ornidroid io service. */
	private final IOrnidroidIOService ornidroidIOService;

	/** The path. */
	private List<String> path = null;

	/** The root. */
	private String root;

	/** The selected file. */
	private File selectedFile;
	/** The selected file. */
	private String selectedFileName;

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

	/**
	 * Instantiates a new file explorer.
	 */
	public AddCustomMediaActivity() {
		super();
		this.ornidroidIOService = new OrnidroidIOServiceImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_custom_media);
		this.myPath = (TextView) findViewById(R.id.path);

		this.root = BasicConstants.SLASH_STRING;
		this.fileType = (OrnidroidFileType) getIntent().getSerializableExtra(
				OrnidroidFileType.FILE_TYPE_INTENT_PARAM_NAME);
		this.birdDirectory = (String) getIntent().getSerializableExtra(
				Constants.BIRD_DIRECTORY_PARAMETER_NAME);
		findViewById(R.id.cancel_add_custom_media).setOnClickListener(
				new OnClickListener() {
					public void onClick(final View v) {
						goBackToBirdActivity();
					}
				});
		getDir(Environment.getExternalStorageDirectory().getPath());
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

			final EditText userComment = new EditText(this);
			return new AlertDialog.Builder(this)
					.setView(userComment)
					.setTitle(R.string.add_custom_media_title)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int whichButton) {
									final String comment = userComment
											.getText().toString().trim();
									try {
										AddCustomMediaActivity.this.ornidroidIOService
												.addCustomMediaFile(
														AddCustomMediaActivity.this.birdDirectory,
														AddCustomMediaActivity.this.fileType,
														AddCustomMediaActivity.this.selectedFileName,
														AddCustomMediaActivity.this.selectedFile,
														comment);
										Toast.makeText(
												AddCustomMediaActivity.this,
												AddCustomMediaActivity.this
														.getResources()
														.getString(
																R.string.add_custom_media_success),
												Toast.LENGTH_LONG).show();
									} catch (final OrnidroidException e) {

										Toast.makeText(
												AddCustomMediaActivity.this,
												AddCustomMediaActivity.this
														.getResources()
														.getString(
																R.string.add_custom_media_error)
														+ BasicConstants.CARRIAGE_RETURN
														+ e.getSourceExceptionMessage(),
												Toast.LENGTH_LONG).show();
									}
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
			this.selectedFileName = file.getName();
			this.selectedFile = file;
			showDialog(DIALOG_TEXT_ENTRY);

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
												.getExtension(AddCustomMediaActivity.this.fileType))) {
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