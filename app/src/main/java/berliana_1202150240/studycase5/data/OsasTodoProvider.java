package berliana_1202150240.studycase5.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by arrival瑞符 on 3/24/18.
 */

/*Class OsasTodoProvider mewarisi method ContentProvider
Class ini berfungsi sebagai jembatan untuk melakukan transaction/eksekusi dari activity ke sqlite database
 */
public class OsasTodoProvider extends ContentProvider {
    //Declare seluruh objek
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private OsasTodoDBHelper mOpenHelper;

    private static final int DAFTAR = 100;
    private static final int DAFTAR_ID = 200;

    //membuat uri atau koneksi content provider
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = OsasTodoContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, OsasTodoContract.DaftarInput.TABLE_DAFTAR, DAFTAR);
        matcher.addURI(authority, OsasTodoContract.DaftarInput.TABLE_DAFTAR + "/#", DAFTAR_ID);

        return matcher;
    }

    //method yang dijalankan saat class di buat
    @Override
    public boolean onCreate() {
        mOpenHelper = new OsasTodoDBHelper(getContext()); //inisialisasi DBHelper
        return true;
    }

    //method untuk handling pengambilan data dengan berbagai case
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            //Select semua data
            case DAFTAR: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OsasTodoContract.DaftarInput.TABLE_DAFTAR,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1);
                return retCursor;
            }
            //Select data berdasarkan ID
            case DAFTAR_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OsasTodoContract.DaftarInput.TABLE_DAFTAR,
                        strings,
                        OsasTodoContract.DaftarInput._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        s1);
                return retCursor;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    //method perwarisan dari ContentProvider untuk mendapatkan path dari content provider
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            //dir atau multi data
            case DAFTAR: {
                return OsasTodoContract.DaftarInput.CONTENT_DIR_TYPE;
            }
            //item atau single data
            case DAFTAR_ID: {
                return OsasTodoContract.DaftarInput.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    //method content provider untuk insert data
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        //akses DBHelper
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            //menambahkan satu data
            case DAFTAR: {
                //fungsi insert ke dalam database dengan nilai dari "contentValues" yang didapatkan dari parameter
                long _id = db.insert(OsasTodoContract.DaftarInput.TABLE_DAFTAR, null, contentValues);
                if (_id > 0) {
                    //jika id lebih dari 0 input id
                    returnUri = OsasTodoContract.DaftarInput.buildOsasUri(_id);
                } else {
                    //jika tidak lemparkan exception
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        //notifychange atau trigger jika ada perubahan (refresh)
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    //method delete data dari ContentProvider
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        //akses SQLite DBHelper
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch (match) {
            //Delete satu table
            case DAFTAR:
                numDeleted = db.delete(
                        OsasTodoContract.DaftarInput.TABLE_DAFTAR, s, strings);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        OsasTodoContract.DaftarInput.TABLE_DAFTAR + "'");
                break;
            //Delete satu row pada table berdasarkan name
            case DAFTAR_ID:
                numDeleted = db.delete(OsasTodoContract.DaftarInput.TABLE_DAFTAR,
                        OsasTodoContract.DaftarInput.COLUMN_NAME + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        OsasTodoContract.DaftarInput.TABLE_DAFTAR + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    //method untuk melakukan update data pada ContentProvider (Tapi tidak diperlukan pada Study Case 5)
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null) {
            throw new IllegalArgumentException("content values tidak bisa kosong");
        }

        switch (sUriMatcher.match(uri)) {
            case DAFTAR: {
                numUpdated = db.update(OsasTodoContract.DaftarInput.TABLE_DAFTAR,
                        contentValues,
                        s,
                        strings);
                break;
            }
            case DAFTAR_ID: {
                numUpdated = db.update(OsasTodoContract.DaftarInput.TABLE_DAFTAR,
                        contentValues,
                        OsasTodoContract.DaftarInput._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
