package amcereijo.com.bqevernote.api;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.evernote.client.android.EvernoteSession;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import amcereijo.com.bqevernote.R;

/**
 * Created by amcereijo on 07/12/14.
 */
@Singleton
public class EvernoteApi {

    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE =
            EvernoteSession.EvernoteService.SANDBOX;
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



}
