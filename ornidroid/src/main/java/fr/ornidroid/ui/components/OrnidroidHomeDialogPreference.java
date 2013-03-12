package fr.ornidroid.ui.components;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.ornidroid.R;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.FileHelper;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.helper.StringHelper;
import fr.ornidroid.ui.multicriteriasearch.HelpDialog;

/**
 * The Class OrnidroidHomePreference.
 * http://alexfu.tumblr.com/post/23683149440/android-dev-custom-dialogpreference
 */
public class OrnidroidHomeDialogPreference extends DialogPreference implements
		OnItemClickListener {

	/** The exception caught during move. */
	private Exception exceptionCaughtDuringMove = null;
	/** The file helper. */
	private final FileHelper fileHelper;
	/** The item. */
	private List<String> item = null;

	/** The m list view. */
	private ListView mListView;

	/** The m path. */
	private TextView mPath;

	/** The m text. */
	private String mText;

	/** The my view. */
	private View myView;

	/** The path. */
	private List<String> path = null;

	/** The root. */
	private String root;

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
	 * Instantiates a new ornidroid home preference.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public OrnidroidHomeDialogPreference(final Context context,
			final AttributeSet attrs) {
		super(context, attrs);
		this.fileHelper = new FileHelper();
		init();
	}

	/**
	 * Instantiates a new ornidroid home preference.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 * @param defStyle
	 *            the def style
	 */
	public OrnidroidHomeDialogPreference(final Context context,
			final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		this.fileHelper = new FileHelper();
		init();
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return this.mText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	public void onItemClick(final AdapterView<?> arg0, final View arg1,
			final int position, final long arg3) {

		final File file = new File(this.path.get(position));

		if (file.isDirectory()) {
			if (file.canRead()) {
				getDir(this.path.get(position));
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.preference.DialogPreference#onBindDialogView(android.view.View)
	 */
	@Override
	protected void onBindDialogView(final View view) {
		super.onBindDialogView(view);

		this.mPath = (TextView) view.findViewById(R.id.ornidroid_home_path);

		// find the stored value and print it
		this.mPath.setText(Constants.getOrnidroidHome());

		// TODO : a changer ?
		this.root = Environment.getExternalStorageDirectory().getPath();

		// start point : current ornidroid home
		getDir(Constants.getOrnidroidHome());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.DialogPreference#onCreateDialogView()
	 */
	@Override
	protected View onCreateDialogView() {
		this.myView = super.onCreateDialogView();
		this.mPath = (TextView) this.myView
				.findViewById(R.id.ornidroid_home_path);
		this.mListView = (ListView) this.myView
				.findViewById(R.id.ornidroid_home_file_list);
		this.mListView.setOnItemClickListener(this);

		return this.myView;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.DialogPreference#onDialogClosed(boolean)
	 */
	@Override
	protected void onDialogClosed(final boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			final String newOrnidroidHome = this.mPath.getText().toString()
					+ File.separator + Constants.ORNIDROID_DIRECTORY_NAME;
			try {
				setNewOrnidroidHome(Constants.getOrnidroidHome(),
						newOrnidroidHome);
				final Editor editor = getEditor();
				editor.putString(
						Constants
								.getStringFromXmlResource(R.string.preferences_ornidroid_home_key),
						newOrnidroidHome);
				editor.commit();
			} catch (final OrnidroidException e) {

				Log.e(Constants.LOG_TAG, e.getSourceExceptionMessage());
				HelpDialog
						.showInfoDialog(
								this.getContext(),
								this.getContext()
										.getResources()
										.getString(
												R.string.help_change_ornidroid_home_title),
								e.getSourceExceptionMessage());
			}
		}
	}

	// TODO : le long toast ne fonctionne pas du tout
	/**
	 * Fire long toast.
	 * 
	 * @param toast
	 *            the toast
	 * @throws OrnidroidException
	 */
	private void fireLongToast() throws OrnidroidException {
		try {
			while ((OrnidroidHomeDialogPreference.this.fileHelper
					.getCopyPercentProgress() != 100)
					&& (OrnidroidHomeDialogPreference.this.exceptionCaughtDuringMove == null)) {
				final Toast toast = Toast
						.makeText(
								OrnidroidHomeDialogPreference.this.getContext(),
								OrnidroidHomeDialogPreference.this
										.getContext()
										.getResources()
										.getString(
												R.string.preferences_ornidroid_home_copying_files)
										+ OrnidroidHomeDialogPreference.this.fileHelper
												.getCopyPercentProgress() + "%",
								Toast.LENGTH_SHORT);

				toast.show();
				Thread.sleep(1850);
			}
		} catch (final Exception e) {
			Log.e(Constants.LOG_TAG, e.getMessage(), e);
		}
		if (this.exceptionCaughtDuringMove != null) {
			throw new OrnidroidException(OrnidroidError.CHANGE_ORNIDROID_HOME,
					this.exceptionCaughtDuringMove);
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
		this.mPath.setText(dirPath);
		this.item = new ArrayList<String>();
		this.path = new ArrayList<String>();
		final File f = new File(dirPath);
		final File[] files = f.listFiles();

		if (!dirPath.equals(this.root)) {
			this.item.add("../");
			this.path.add(f.getParent());
		}
		if (files != null) {
			Arrays.sort(files, this.filecomparator);

			for (int i = 0; i < files.length; i++) {
				final File file = files[i];

				if (!file.isHidden() && file.canRead()) {
					this.path.add(file.getPath());
					if (file.isDirectory()) {
						this.item.add(file.getName() + File.separator);
					} else {
						this.item.add(file.getName());
					}
				}
			}
		}

		final ArrayAdapter<String> fileList = new ArrayAdapter<String>(
				Constants.getCONTEXT(), R.layout.file_explorer_row, this.item);
		this.mListView.setAdapter(fileList);
	}

	/**
	 * Inits the dialog from constructor.
	 */
	private void init() {
		setDialogLayoutResource(R.layout.ornidroid_home_dialog_preference);
		setPersistent(true);
	}

	/**
	 * Sets the new ornidroid home. Copy files from previous installation
	 * directory to the new location
	 * 
	 * @param newOrnidroidHome
	 *            the new new ornidroid home
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	private void setNewOrnidroidHome(final String oldOrnidroidHome,
			final String newOrnidroidHome) throws OrnidroidException {
		this.exceptionCaughtDuringMove = null;
		if (!StringHelper.equals(oldOrnidroidHome, newOrnidroidHome)) {

			final Thread t = new Thread() {
				@Override
				public void run() {
					OrnidroidHomeDialogPreference.this.fileHelper
							.initMoveOperation();
					try {
						final File destDir = new File(newOrnidroidHome);
						final File srcDir = new File(
								Constants.getOrnidroidHome());
						OrnidroidHomeDialogPreference.this.fileHelper
								.moveDirectory(srcDir, destDir);
					} catch (final Exception e) {
						OrnidroidHomeDialogPreference.this.exceptionCaughtDuringMove = e;
						Log.e(Constants.LOG_TAG, e.getMessage(), e);
					} finally {
						OrnidroidHomeDialogPreference.this.fileHelper
								.setMoveComplete();
					}
				}
			};
			t.start();

			fireLongToast();

		}

	}
}