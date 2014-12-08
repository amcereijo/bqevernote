package amcereijo.com.bqevernote.notes;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evernote.edam.type.Note;

import java.util.Date;
import java.util.List;

import amcereijo.com.bqevernote.R;

/**
 * Created by amcereijo on 08/12/14.
 */
public class NoteListElementAdapter extends ArrayAdapter<Note> {

    public NoteListElementAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = getItem(position);
        if(convertView == null ){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_list_element,parent, false);
        }
        ((TextView)convertView.findViewById(R.id.noteName)).setText(note.getTitle());
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(note.getUpdated())).toString();
        ((TextView)convertView.findViewById(R.id.noteDate)).setText(dateString);

        return convertView;
    }
}
