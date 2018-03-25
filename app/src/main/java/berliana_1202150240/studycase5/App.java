package berliana_1202150240.studycase5;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arrival瑞符 on 3/24/18.
 */

//Class global yang dapat diakses oleh seluruh class pada project ini
public class App extends Application {

    //ambil data warna pada sharedpreference
    public static int getWarna(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getString(R.string.shared_preference_name), MODE_PRIVATE);
        int warna = prefs.getInt("warna", 0);
        return warna;
    }

    //ambil index warna pada sharedpreference
    public static int getIndex(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getString(R.string.shared_preference_name), MODE_PRIVATE);
        int index = prefs.getInt("index", 0);
        return index;
    }

    //set kode warna dan index warna yang dipilih di shape color settings pada shared preference
    public static void setWarna(Context context, int kode_warna, int index) {
        SharedPreferences.Editor editor =
                context.getSharedPreferences(context.getString(R.string.shared_preference_name), MODE_PRIVATE).edit();
        editor.putInt("warna", kode_warna);
        editor.putInt("index", index);
        editor.apply();
    }
}
