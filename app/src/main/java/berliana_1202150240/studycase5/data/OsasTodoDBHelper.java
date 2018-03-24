package berliana_1202150240.studycase5.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by arrival瑞符 on 3/24/18.
 */

public class OsasTodoDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "osastodo.db";
    private static final int DATABASE_VERSION = 1;

    public OsasTodoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_DAFTAR_TABLE = "CREATE TABLE " +
                OsasTodoContract.DaftarInput.TABLE_DAFTAR + "(" + OsasTodoContract.DaftarInput._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OsasTodoContract.DaftarInput.COLUMN_NAME + " TEXT NOT NULL, " +
                OsasTodoContract.DaftarInput.COLUMN_DESCRIPTION +
                " TEXT NOT NULL, " +
                OsasTodoContract.DaftarInput.COLUMN_PRIORITY +
                " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_DAFTAR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OsasTodoContract.DaftarInput.TABLE_DAFTAR);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                OsasTodoContract.DaftarInput.TABLE_DAFTAR + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}
