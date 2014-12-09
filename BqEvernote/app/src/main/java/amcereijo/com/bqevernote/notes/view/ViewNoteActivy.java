package amcereijo.com.bqevernote.notes.view;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.evernote.edam.type.Note;

import java.util.Date;

import amcereijo.com.bqevernote.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_view_note)
public class ViewNoteActivy extends RoboActivity {

    private static final String TAG = ViewNoteActivy.class.getName();

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
        dateTextView.setText(dateString);

        new ViewNoteAsyncTask(contentTextView, contentTextView.getRootView().getContext(),
                note.getGuid()).execute();

    }
}
