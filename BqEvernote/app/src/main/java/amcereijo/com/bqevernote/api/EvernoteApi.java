package amcereijo.com.bqevernote.api;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.thrift.transport.TTransportException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

import amcereijo.com.bqevernote.R;

/**
 * Created by amcereijo on 07/12/14.
 */
@Singleton
public class EvernoteApi {

    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE =
            EvernoteSession.EvernoteService.SANDBOX;
    private static final String TAG = EvernoteApi.class.getName();

    private String consumerKey;
    private String consumerSecret;
    private Application application;
    private final EvernoteSession mEvernoteSession;
    private String authToken;

    @Inject
    public EvernoteApi(Application application) {
        this.application = application;
        consumerKey = this.application.getString(R.string.consumer_key);
        consumerSecret = this.application.getString(R.string.consumer_secret);
        mEvernoteSession = EvernoteSession.getInstance(application.getApplicationContext(),
                consumerKey, consumerSecret, EVERNOTE_SERVICE, true);
    }
    public boolean login(Activity activity) {
        if(mEvernoteSession.isLoggedIn()){
            Log.i("", "already logged");
            return true;
        } else {
            mEvernoteSession.authenticate(activity);
            return false;
        }

    }

    public void getLoggedUser() {
        authToken = mEvernoteSession.getAuthToken();
    }

    public void getNotes(OnClientCallback callback) {
        try {
            mEvernoteSession.getClientFactory().createNoteStoreClient().findNotes(
                    new NoteFilter(), 0, 100, callback);
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }


    public void getNote(String guid, OnClientCallback callback) {
        try {
            mEvernoteSession.getClientFactory().createNoteStoreClient().getNoteContent(guid, callback);
        } catch (TTransportException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }

}
