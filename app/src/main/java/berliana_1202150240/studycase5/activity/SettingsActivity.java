package berliana_1202150240.studycase5.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import berliana_1202150240.studycase5.App;
import berliana_1202150240.studycase5.R;

//SettingsActivity mewarisi method AppCompatPreferenceActivity
public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //menampilkan tombol back pada toolbar

        //load fragment setting
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    //Class konten dari preference/settings
    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //koneksikan dengan xml pref yang sudah dibuat
            addPreferencesFromResource(R.xml.pref_shape_color);

            //inisialisasi key menu di pref
            final Preference myPref = findPreference("shape_key");

            /*pengecekan kondisi index yang tersimpan pada sharedpreference
            0 = Green, 1 = Blue, 2 = Red
             */
            if(App.getIndex(getActivity()) == 0)
                myPref.setSummary("Green");
            if(App.getIndex(getActivity()) == 1)
                myPref.setSummary("Blue");
            if(App.getIndex(getActivity()) == 2)
                myPref.setSummary("Red");

            //Handler saat menu myPref di klik
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //Tampilkan dialog shape color
                    showSettingDialog(getActivity(), myPref);
                    return true;
                }
            });
        }
    }

    //handler saat tombol back toolbar di klik
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //Declare alertdialog
    static AlertDialog warnaDialog;
    //Method untuk membuat dialog shape color
    public static void showSettingDialog(final Context context, final Preference pref) {
        //Membuat array daftar warna
        final CharSequence[] items = {"Green","Blue","Red"};

        // inisialisasi, build dan konfigurasi AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Shape Color");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                warnaDialog.dismiss();
            }
        });

        //membuat views radio button option
        builder.setSingleChoiceItems(items, App.getIndex(context), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        App.setWarna(context, Color.GREEN, 0); //update nilai ke sharedpreference
                        pref.setSummary("Green"); //update text summary pada preference
                        break;
                    case 1:
                        App.setWarna(context, Color.BLUE, 1); //update nilai ke sharedpreference
                        pref.setSummary("Blue"); //update text summary pada preference
                        break;
                    case 2:
                        App.setWarna(context, Color.RED, 2); //update nilai ke sharedpreference
                        pref.setSummary("Red"); //update text summary pada preference
                        break;

                }
                warnaDialog.dismiss(); //exit dialog
            }
        });

        warnaDialog = builder.create(); //create dialog
        warnaDialog.show();//tampilkan dialog
    }

}
