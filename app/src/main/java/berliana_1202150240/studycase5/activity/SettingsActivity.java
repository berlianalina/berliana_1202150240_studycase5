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

public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_shape_color);

            final Preference myPref = findPreference("shape_key");

            if(App.getIndex(getActivity()) == 0)
                myPref.setSummary("Green");
            if(App.getIndex(getActivity()) == 1)
                myPref.setSummary("Blue");
            if(App.getIndex(getActivity()) == 2)
                myPref.setSummary("Red");

            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    showSettingDialog(getActivity(), myPref);
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    static AlertDialog warnaDialog;
    public static void showSettingDialog(final Context context, final Preference pref) {
        final CharSequence[] items = {"Green","Blue","Red"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Shape Color");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                warnaDialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(items, App.getIndex(context), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        App.setWarna(context, Color.GREEN, 0);
                        pref.setSummary("Green");
                        break;
                    case 1:
                        App.setWarna(context, Color.BLUE, 1);
                        pref.setSummary("Blue");
                        break;
                    case 2:
                        App.setWarna(context, Color.RED, 2);
                        pref.setSummary("Red");
                        break;

                }
                warnaDialog.dismiss();
            }
        });

        warnaDialog = builder.create();
        warnaDialog.show();
    }

}
