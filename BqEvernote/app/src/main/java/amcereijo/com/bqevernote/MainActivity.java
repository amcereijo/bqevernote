package amcereijo.com.bqevernote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.google.inject.Inject;

import amcereijo.com.bqevernote.api.EvernoteApi;
import amcereijo.com.bqevernote.notes.NotesActivity;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    private final static String TAG = MainActivity.class.getName();
    
    @Inject
    private EvernoteApi evernoteApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void login(View v){
        if(evernoteApi.login(this)) {
            this.startApplication();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case EvernoteSession.REQUEST_CODE_OAUTH:
                if (resultCode == Activity.RESULT_OK) {
                    this.startApplication();
                }
                break;
            default:
                Toast.makeText(this, R.string.login_error, Toast.LENGTH_LONG);
        }
    }

    private void startApplication() {
        Log.i(TAG, "logged");
        evernoteApi.getLoggedUser();
        Intent intentNotes = new Intent(this, NotesActivity.class);
        this.startActivity(intentNotes);
    }
}

