package fr.giletvin.ornidroid.data;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fr.giletvin.ornidroid.helper.Constants;

/**
 * Pompé sur
 * http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android
 * -applications/
 * 
 * @author hamlet
 * 
 */
public class OrnidroidDatabaseOpenHelper extends SQLiteOpenHelper {

	/** The my data base. */
	private static SQLiteDatabase myDataBase;

	/**
	 * Instantiates a new ornidroid database open helper.
	 * 
	 * @param context
	 *            the context
	 */
	public OrnidroidDatabaseOpenHelper(Context context) {
		super(context, Constants.DB_NAME, null, 1);
	}

	/**
	 * Open or create database.
	 * 
	 * @return the sQ lite database
	 */
	public SQLiteDatabase openDatabase() {

		String dbPath = Constants.getOrnidroidDbPath();

		File fileDb = new File(dbPath);
		if (fileDb.exists()) {
			Log.i(Constants.LOG_TAG, "Fichier sqlite  trouve " + dbPath);
			myDataBase = SQLiteDatabase.openDatabase(dbPath, null,
					SQLiteDatabase.OPEN_READWRITE
							| SQLiteDatabase.NO_LOCALIZED_COLLATORS);

		} else {
			Log.e(Constants.LOG_TAG, "Fichier sqlite non trouve " + dbPath);
		}

		// myDataBase= SQLiteDatabase.openOrCreateDatabase(myPath,null);
		// myDataBase = SQLiteDatabase.openDatabase(myPath, null,
		// SQLiteDatabase.OPEN_READONLY);
		return myDataBase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#close()
	 */

	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

}