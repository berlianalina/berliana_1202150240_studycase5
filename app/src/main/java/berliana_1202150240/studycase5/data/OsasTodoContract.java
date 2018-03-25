package berliana_1202150240.studycase5.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by arrival瑞符 on 3/24/18.
 */

/*
Contract adalah class untuk mengatur/define content author,
lokasi/path, tabel, kolom, intent actions yang diperlukan untuk implementasi ContentProvider
*/

public class OsasTodoContract {
    //inisialisasi nama content author untuk content provider yang dibuat
    public static final String CONTENT_AUTHORITY = "berliana_1202150240.studycase5.app";
    //inisialisasi lokasi content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    //Register atau implement Content Provider
    public static final class DaftarInput implements BaseColumns {
        public static final String TABLE_DAFTAR = "daftar"; //nama tabel
        public static final String _ID = "_id"; //kolom
        public static final String COLUMN_NAME = "name"; //kolom
        public static final String COLUMN_DESCRIPTION = "description"; //kolom
        public static final String COLUMN_PRIORITY = "priority"; //kolom

        // membuat content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_DAFTAR).build();
        // membuat cursor directory base type untuk multiple input
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_DAFTAR;
        // membuat cursor item base type untuk single input
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_DAFTAR;

        // membuat URI saat proses input
        public static Uri buildOsasUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
