package amcereijo.com.bqevernote.notes;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Note;
import com.google.inject.Inject;

import java.util.Date;

import amcereijo.com.bqevernote.R;
import amcereijo.com.bqevernote.api.EvernoteApi;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_view_note)
public class ViewNote extends RoboActivity {

    private static final String TAG = ViewNote.class.getName();

    @Inject
    private EvernoteApi evernoteApi;
    @InjectView(R.id.note_view_title)
    private TextView titleTextView;
    @InjectView(R.id.note_view_date)
    private TextView dateTextView;
    @InjectView(R.id.note_view_content)
    private TextView contentTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Note note = (Note)getIntent().getExtras().get("note");
        titleTextView.setText(note.getTitle());
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(note.getUpdated())).toString();
        dateTextView.setText(getString(R.string.date) + ": " + dateString);


        OnClientCallback callbackForString = new OnClientCallback() {
            @Override
            public void onSuccess(Object data) {
                contentTextView.setText(Html.fromHtml((String)data));
            }

            @Override
            public void onException(Exception exception) {
                Log.e(TAG, "Error:"+exception.getMessage(), exception);
            }
        };
        evernoteApi.getNote(note.getGuid(), callbackForString);
        Log.i(TAG, "Note title:" + note.getTitle()+" date:"+note.getUpdated() + " content:" +note.getContent());
    }
}
