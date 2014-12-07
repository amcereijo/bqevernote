package amcereijo.com.bqevernote.api;

import com.evernote.client.android.EvernoteSession;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import amcereijo.com.bqevernote.application.BqEvernoteApplication;

/**
 * Created by amcereijo on 07/12/14.
 */
@Singleton
public class EvernoteApi {

    private static final String CONSUMER_KEY = "amcereijo";
    private static final String CONSUMER_SECRET = "11f45d1a3f8b1e7b";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE =
            EvernoteSession.EvernoteService.SANDBOX;

    @Inject
    private BqEvernoteApplication bqEvernoteApplication;

    private final EvernoteSession mEvernoteSession;

    public EvernoteApi() {
        mEvernoteSession = EvernoteSession.getInstance(bqEvernoteApplication.getApplicationContext(),
                CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_SERVICE, true);
    }
}
