package berliana_1202150240.studycase5.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;

import berliana_1202150240.studycase5.App;
import berliana_1202150240.studycase5.R;
import berliana_1202150240.studycase5.data.OsasTodoContract;

/**
 * Created by arrival瑞符 on 3/24/18.
 */

public class TodoAdapter extends RecyclerViewCursorAdapter<TodoAdapter.ViewHolder> {

    private static final String LOG_TAG = TodoAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        setViewHolder(holder);
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }

    public class ViewHolder extends RecyclerViewCursorViewHolder {
        public final LinearLayout rlRow;
        public final TextView tvName;
        public final TextView tvDescription;
        public final TextView tvPriorityId;

        ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            tvPriorityId = (TextView) view.findViewById(R.id.tvPriorityId);
            rlRow = (LinearLayout) view.findViewById(R.id.rlRow);
        }

        @Override
        public void bindCursor(Cursor cursor) {
            rlRow.setBackgroundColor(Color.GREEN);
            rlRow.setBackgroundColor(App.getWarna(mContext));

            int nameIndex = cursor.getColumnIndex(OsasTodoContract.DaftarInput.COLUMN_NAME);
            final String name = cursor.getString(nameIndex);
            tvName.setText(name);

            int descIndex = cursor.getColumnIndex(OsasTodoContract.DaftarInput.COLUMN_DESCRIPTION);
            final String desc = cursor.getString(descIndex);
            tvDescription.setText(desc);

            int priorityIndex = cursor.getColumnIndex(OsasTodoContract.DaftarInput.COLUMN_PRIORITY);
            final int priority = cursor.getInt(priorityIndex);
            tvPriorityId.setText(priority + "");
        }
    }

    public TodoAdapter(Context context) {
        super(context);
        mContext = context;
        setupCursorAdapter(null, 0, R.layout.row_todo, false);
    }

    public void deletePositionItem(int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        int nameIndex = mCursorAdapter.getCursor().getColumnIndex(OsasTodoContract.DaftarInput._ID);
        final int id = mCursorAdapter.getCursor().getInt(nameIndex);
        mContext.getContentResolver().delete(OsasTodoContract.DaftarInput.CONTENT_URI, "_id = "+id, null);
    }
}
