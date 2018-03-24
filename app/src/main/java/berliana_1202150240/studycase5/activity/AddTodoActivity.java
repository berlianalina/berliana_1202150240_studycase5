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

    private TextInputEditText tvName;
    private TextInputEditText tvDescription;
    private TextInputEditText tvPriority;
    private Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        setUpView();
    }

    private void setUpView() {
        tvName = (TextInputEditText) findViewById(R.id.tvName);
        tvDescription = (TextInputEditText) findViewById(R.id.tvDescription);
        tvPriority = (TextInputEditText) findViewById(R.id.tvPriority);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                if(!validasiKosong()) {
                    Toast.makeText(this, "Harap isi seluruh field!", Toast.LENGTH_SHORT).show();
                    return;
                }

                insertData();
                Toast.makeText(this, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    private boolean validasiKosong() {
        if(tvName.getText().toString().isEmpty() || tvDescription.getText().toString().isEmpty() ||
                tvPriority.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void insertData() {
        ContentValues content = new ContentValues();
        content.put(OsasTodoContract.DaftarInput.COLUMN_NAME, tvName.getText().toString());
        content.put(OsasTodoContract.DaftarInput.COLUMN_DESCRIPTION, tvDescription.getText().toString());
        content.put(OsasTodoContract.DaftarInput.COLUMN_PRIORITY, tvPriority.getText().toString());
        getContentResolver().insert(OsasTodoContract.DaftarInput.CONTENT_URI, content);
    }
}
