package berliana_1202150240.studycase5.activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import berliana_1202150240.studycase5.App;
import berliana_1202150240.studycase5.R;
import berliana_1202150240.studycase5.adapter.TodoAdapter;
import berliana_1202150240.studycase5.data.OsasTodoContract;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    //Deklarasi views
    private FloatingActionButton btnAdd;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvData;
    private TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(0, null, this); //inisialisasi loader untuk get data dari database sqlite
        setUpView(); //method inisialisasi semua view yang digunakan
    }

    //method untuk attach menu dari xml menu ke activity ini
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //method untuk handle menu yang di klik
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pengaturan_warna:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //method inisialisasi semua view dan konfigurasi awal
    private void setUpView() {
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        rvData = (RecyclerView) findViewById(R.id.rvData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvData.setLayoutManager(linearLayoutManager);

        todoAdapter = new TodoAdapter(this);
        rvData.setAdapter(todoAdapter);
        declareSwipeRecyclerView();

        btnAdd.setOnClickListener(this);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    //Method untuk handling swipe pada item recyclerview dengan memanfaatkan "ItemTouchHelper"
    private void declareSwipeRecyclerView() {
        //Callback jika item di swipe/tarik ke arah kiri
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            //handling saat item bergerak
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            //handling saat item sudah di swipe full ke arah kiri
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                //ambil index item yang di swipe
                final int position = viewHolder.getAdapterPosition();

                //validasi hanya jika swipe ke kiri
                if (direction == ItemTouchHelper.LEFT) {
                    //munculkan pesan dialog konfirmasi
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Yakin hapus data ini?");

                    //jika klik "Hapus"
                    builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            todoAdapter.deletePositionItem(position); //item di hapus dari adapter dan database
                            todoAdapter.notifyItemRemoved(position); //refresh adapter dengan item yang sudah dihapus
                            loadData(); //load data ulang untuk memastikan data telah berhasil dihapus
                            return;
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            todoAdapter.notifyItemRemoved(position + 1);
                            todoAdapter.notifyItemRangeChanged(position, todoAdapter.getItemCount());
                            //jika cancel tidak ada data yang dihapus hanya melakukan refresh adapter
                            return;
                        }
                    }).show();  //show alert dialog
                }
            }
        };
        //inisialisasi callback itemtouchhelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        //attach atau dimplementasikan kedalam recyclerview data (rvData)
        itemTouchHelper.attachToRecyclerView(rvData);
    }

    //jika kondisi aplikasi pada state onResume, data akan di load ulang
    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0, null, this);
    }

    //method untuk load data to do
    public void loadData() {
        getLoaderManager().restartLoader(0, null, this);
        swipeRefresh.setRefreshing(false);
    }

    //method untuk membuat loader content provider untuk mengakses data
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new CursorLoader(this,
                OsasTodoContract.DaftarInput.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    //handling dari loader saat load selesai
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        todoAdapter.swapCursor(cursor); //refresh cursor
    }

    //handling dari loader saat load reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        todoAdapter.swapCursor(null); //refresh cursor
    }

    //implementasi interface onClick untuk btnAdd
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                startActivity(new Intent(MainActivity.this, AddTodoActivity.class));
                break;
        }
    }
}
