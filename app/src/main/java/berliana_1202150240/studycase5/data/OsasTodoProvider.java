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

public class OsasTodoProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private OsasTodoDBHelper mOpenHelper;

    private static final int DAFTAR = 100;
    private static final int DAFTAR_ID = 200;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = OsasTodoContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, OsasTodoContract.DaftarInput.TABLE_DAFTAR, DAFTAR);
        matcher.addURI(authority, OsasTodoContract.DaftarInput.TABLE_DAFTAR + "/#", DAFTAR_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new OsasTodoDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
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

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case DAFTAR: {
                return OsasTodoContract.DaftarInput.CONTENT_DIR_TYPE;
            }
            case DAFTAR_ID: {
                return OsasTodoContract.DaftarInput.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case DAFTAR: {
                long _id = db.insert(OsasTodoContract.DaftarInput.TABLE_DAFTAR, null, contentValues);
                if (_id > 0) {
                    returnUri = OsasTodoContract.DaftarInput.buildFlavorsUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch (match) {
            case DAFTAR:
                numDeleted = db.delete(
                        OsasTodoContract.DaftarInput.TABLE_DAFTAR, s, strings);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        OsasTodoContract.DaftarInput.TABLE_DAFTAR + "'");
                break;
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

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DAFTAR:
                db.beginTransaction();

                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("content values tidak bisa kosong");
                        }
                        long _id = -1;
                        try {
                            _id = db.insertOrThrow(OsasTodoContract.DaftarInput.TABLE_DAFTAR,
                                    null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.w("Error", "Mencoba input data " +
                                    value.getAsString(
                                            OsasTodoContract.DaftarInput.COLUMN_NAME)
                                    + " tapi nilai sudah ada di database.");
                        }
                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if (numInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

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
