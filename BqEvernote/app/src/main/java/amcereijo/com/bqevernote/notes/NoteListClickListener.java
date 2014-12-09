package amcereijo.com.bqevernote.notes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.evernote.edam.type.Note;

import amcereijo.com.bqevernote.notes.view.ViewNoteActivy;

/**
 * Created by amcereijo on 08/12/14.
 */
public class NoteListClickListener implements AdapterView.OnItemClickListener {
    private final static String TAG = NoteListClickListener.class.getName();
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Note note = (Note)adapterView.getItemAtPosition(i);
        Log.i(TAG, "note:"+note.getGuid());

        Intent intent = new Intent(view.getContext(), ViewNoteActivy.class);
        Bundle b = new Bundle();
        b.putSerializable("note", note);
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
