package berliana_1202150240.studycase5;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arrival瑞符 on 3/24/18.
 */

public class App extends Application {
    public static int getWarna(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("pengaturan", MODE_PRIVATE);
        int warna = prefs.getInt("warna", 0);
        return warna;
    }

    public static int getIndex(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("pengaturan", MODE_PRIVATE);
        int index = prefs.getInt("index", 0);
        return index;
    }

    public static void setWarna(Context context, int kode_warna, int index) {
        SharedPreferences.Editor editor = context.getSharedPreferences("pengaturan", MODE_PRIVATE).edit();
        editor.putInt("warna", kode_warna);
        editor.putInt("index", index);
        editor.apply();
    }
}
