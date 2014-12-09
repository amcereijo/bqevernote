package amcereijo.com.bqevernote.notes;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.notestore.NoteList;
import com.google.inject.Inject;

import amcereijo.com.bqevernote.R;
import amcereijo.com.bqevernote.api.EvernoteApi;
import roboguice.util.RoboAsyncTask;

/**
 * Created by amcereijo on 09/12/14.
 */
public class NoteListAsyncTask extends RoboAsyncTask<Void> {

    private final static String TAG = NoteListAsyncTask.class.getName();
    private ProgressDialog progressDialog;
    @Inject
    private EvernoteApi evernoteApi;
    private NoteListElementAdapter adapter;

    private final OnClientCallback<NoteList> notesCallback = new OnClientCallback<NoteList>() {
        @Override
        public void onSuccess(NoteList data) {
            adapter.addAll(data.getNotes());
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
        @Override
        public void onException(Exception exception) {
            Log.e(TAG, exception.getMessage(), exception);
        }
    };

    public NoteListAsyncTask(NoteListElementAdapter adapter, Context context) {
        super(context);
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setTitle(context.getString(R.string.title_activity_notes));
        this.progressDialog.setMessage(context.getString(R.string.notes_loading));
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    public Void call() throws Exception {
        evernoteApi.getNotes(notesCallback);
        return null;
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        Log.e(TAG, e.getMessage(), e);
        super.onException(e);
    }

}
