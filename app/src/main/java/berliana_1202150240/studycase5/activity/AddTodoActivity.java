package berliana_1202150240.studycase5.activity;

import android.content.ContentValues;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import berliana_1202150240.studycase5.R;
import berliana_1202150240.studycase5.data.OsasTodoContract;

public class AddTodoActivity extends AppCompatActivity implements View.OnClickListener {

    //Deklarasi views
    private TextInputEditText tvName;
    private TextInputEditText tvDescription;
    private TextInputEditText tvPriority;
    private Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        setUpView(); //method inisialisasi dan setup dibuat terpisah
    }

    //inisialisasi views
    private void setUpView() {
        tvName = (TextInputEditText) findViewById(R.id.tvName);
        tvDescription = (TextInputEditText) findViewById(R.id.tvDescription);
        tvPriority = (TextInputEditText) findViewById(R.id.tvPriority);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
    }

    //implementasi Interface onClick untuk tombol
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //btnAdd onClick handle
            case R.id.btnAdd:
                //pengecekan jika field ada yang kosong
                if(!validasiKosong()) {
                    Toast.makeText(this, "Harap isi seluruh field!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //jika sudah terisi semua lakukan insertData dan tampilkan pesan
                insertData();
                Toast.makeText(this, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    //method mengecek edittext yang masih kosong
    private boolean validasiKosong() {
        if(tvName.getText().toString().isEmpty() || tvDescription.getText().toString().isEmpty() ||
                tvPriority.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    //method insert data ke sqlite dengan content provider
    private void insertData() {
        ContentValues content = new ContentValues();
        content.put(OsasTodoContract.DaftarInput.COLUMN_NAME, tvName.getText().toString());
        content.put(OsasTodoContract.DaftarInput.COLUMN_DESCRIPTION, tvDescription.getText().toString());
        content.put(OsasTodoContract.DaftarInput.COLUMN_PRIORITY, tvPriority.getText().toString());
        //CONTENT_URI = alamat DB/Content Provider dan content = nilai yang di kirimkan untuk di insert
        getContentResolver().insert(OsasTodoContract.DaftarInput.CONTENT_URI, content);
    }
}
