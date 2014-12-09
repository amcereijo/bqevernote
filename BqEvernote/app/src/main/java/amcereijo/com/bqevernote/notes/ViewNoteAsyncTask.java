package amcereijo.com.bqevernote.notes;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.evernote.client.android.OnClientCallback;
import com.google.inject.Inject;

import amcereijo.com.bqevernote.R;
import amcereijo.com.bqevernote.api.EvernoteApi;
import roboguice.util.RoboAsyncTask;

/**
 * Created by amcereijo on 09/12/14.
 */
public class ViewNoteAsyncTask extends RoboAsyncTask<Void> {

    final static String TAG = ViewNoteAsyncTask.class.getName();
    final OnClientCallback<String> callbackForString = new OnClientCallback<String>() {
         @Override
         public void onSuccess(String data) {
             contentTextView.setText(Html.fromHtml(data));
             progressDialog.dismiss();
         }

         @Override
         public void onException(Exception exception) {
             Log.e(TAG, "Error:" + exception.getMessage(), exception);
         }
    };
    private TextView contentTextView;
    @Inject
    private EvernoteApi evernoteApi;
    private String guid;
    private ProgressDialog progressDialog;


    public ViewNoteAsyncTask(TextView contentTextView, Context context, String guid){
        super(context);
        this.contentTextView = contentTextView;
        this.guid = guid;
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setTitle(context.getString(R.string.title_activity_notes));
        this.progressDialog.setMessage(context.getString(R.string.notes_note_loading));
    }

    @Override
    protected void onPreExecute() throws Exception {
        progressDialog.show();
    }

    @Override
    public Void call() throws Exception {
        evernoteApi.getNote(guid, callbackForString);
        return null;
    }
}
