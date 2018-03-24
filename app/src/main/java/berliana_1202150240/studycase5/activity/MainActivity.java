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

    private FloatingActionButton btnAdd;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvData;
    private TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(0, null, this);
        setUpView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pengaturan_warna:
                showSettingDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    AlertDialog warnaDialog;
    private void showSettingDialog() {
        final CharSequence[] items = {"Green","Blue","Red"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Shape Color");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                warnaDialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(items, App.getIndex(MainActivity.this), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        App.setWarna(MainActivity.this, Color.GREEN, 0);
                        break;
                    case 1:
                        App.setWarna(MainActivity.this, Color.BLUE, 1);
                        break;
                    case 2:
                        App.setWarna(MainActivity.this, Color.RED, 2);
                        break;

                }
                loadData();
                warnaDialog.dismiss();
            }
        });

        warnaDialog = builder.create();
        warnaDialog.show();
    }

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

    private void declareSwipeRecyclerView() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Yakin hapus data ini?");

                    builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            todoAdapter.deletePositionItem(position);
                            todoAdapter.notifyItemRemoved(position);
                            loadData();
                            return;
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            todoAdapter.notifyItemRemoved(position + 1);
                            todoAdapter.notifyItemRangeChanged(position, todoAdapter.getItemCount());
                            return;
                        }
                    }).show();  //show alert dialog
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0, null, this);
    }

    public void loadData() {
        getLoaderManager().restartLoader(0, null, this);
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new CursorLoader(this,
                OsasTodoContract.DaftarInput.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        todoAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        todoAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                startActivity(new Intent(MainActivity.this, AddTodoActivity.class));
                break;
        }
    }
}
