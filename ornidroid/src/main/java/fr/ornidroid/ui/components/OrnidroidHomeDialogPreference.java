package fr.ornidroid.ui.components;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import fr.ornidroid.R;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.StringHelper;

/**
 * The Class OrnidroidHomePreference.
 * http://alexfu.tumblr.com/post/23683149440/android-dev-custom-dialogpreference
 * http
 * ://android-er.blogspot.fr/2012/07/example-of-file-explorer-in-android.html
 */
public class OrnidroidHomeDialogPreference extends DialogPreference implements
		OnItemClickListener {

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

	/** The old ornidroid home. */
	private String oldOrnidroidHome;

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

		init();
	}

	/**
	 * Gets the old ornidroid home.
	 * 
	 * @return the old ornidroid home
	 */
	public String getOldOrnidroidHome() {
		return this.oldOrnidroidHome;
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
		final String clickedFilePath = this.path.get(position);
		if (StringHelper.isNotBlank(clickedFilePath)) {
			final File file = new File(clickedFilePath);

			if (file.isDirectory()) {
				if (file.canRead()) {
					getDir(this.path.get(position));
				}
			}
		}

	}

	/**
	 * Save ornidroid home value.
	 * 
	 * @param value
	 *            the value
	 */
	public void saveOrnidroidHomeValue(final String value) {
		final Editor editor = getEditor();
		editor.putString(
				Constants
						.getStringFromXmlResource(R.string.preferences_ornidroid_home_key),
				value);
		editor.commit();
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

		this.root = BasicConstants.SLASH_STRING;// Environment.getExternalStorageDirectory().getPath();

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
		this.oldOrnidroidHome = null;
		if (positiveResult) {
			this.oldOrnidroidHome = Constants.getOrnidroidHome();
			final String newOrnidroidHome = this.mPath.getText().toString()
					+ File.separator + Constants.ORNIDROID_DIRECTORY_NAME;
			saveOrnidroidHomeValue(newOrnidroidHome);
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

}