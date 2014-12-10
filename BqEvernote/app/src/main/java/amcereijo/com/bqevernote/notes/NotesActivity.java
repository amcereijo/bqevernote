package amcereijo.com.bqevernote.notes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Note;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Comparator;

import amcereijo.com.bqevernote.R;
import amcereijo.com.bqevernote.api.EvernoteApi;
import amcereijo.com.bqevernote.notes.newnote.HandWriteActivity;
import amcereijo.com.bqevernote.notes.newnote.NewNoteFragment;
import roboguice.activity.RoboFragmentActivity;
import roboguice.fragment.RoboFragment;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_notes)
public class NotesActivity extends RoboFragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private final static String TAG = NotesActivity.class.getName();
    private final static String CONTENT_DEFINITION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE " +
            "en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">";
    private final static String START_CONTENT = "<en-note>";
    private final static String END_CONTENT = "</en-note>";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Inject
    private EvernoteApi evernoteApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        RoboFragment fragment = null;
        switch (position) {
            case 0 : fragment = (RoboFragment)PlaceholderFragment.newInstance(position + 1); break;
            case 1 : fragment = NewNoteFragment.newInstance(); break;
            case 2 : closeSession(); break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                    .commit();
        }
    }

    private void closeSession() {
        evernoteApi.logout();
        this.finish();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.all_notes);
                break;
            case 1:
                mTitle = getString(R.string.title_new_note);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void saveNote(final View v){
        String title = ((EditText)v.getRootView().findViewById(R.id.new_note_title)).getText().toString();
        String content = ((EditText)v.getRootView().findViewById(R.id.new_note_content)).getText().toString();
        if(title == null || "".equals(title) || content == null || "".equals(content)){
            Toast.makeText(this, R.string.create_note_data_error, Toast.LENGTH_LONG).show();
        } else {
            createNote(title, content);
        }
    }

    private void createNote(String title, String content) {
        Note note = new Note();
        note.setTitle(title);
        content = new StringBuilder().append(CONTENT_DEFINITION).append(START_CONTENT).
                append(content).append(END_CONTENT).toString();
        note.setContent(content);

        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage(getString(R.string.new_note_saving));

        OnClientCallback<Note> callback = new OnClientCallback<Note>() {
            @Override
            public void onSuccess(Note data) {
                p.dismiss();
                onNavigationDrawerItemSelected(0);
            }
            @Override
            public void onException(Exception exception) {
                Log.e(TAG, exception.getMessage(), exception);
            }
        };
        p.show();
        evernoteApi.addNote(note, callback);
    }

    public void cancelNote(View v){
        onNavigationDrawerItemSelected(0);
    }

    public void toHandWrite(View v){
        Intent handWriteIntent = new Intent(this, HandWriteActivity.class);
        Bundle bundle = new Bundle();
        startActivityForResult(handWriteIntent, HandWriteActivity.RESULT_CODE, bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == HandWriteActivity.RESULT_CODE){
            if(resultCode == RESULT_OK) {
                Log.i(TAG, data.getDataString());
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends RoboFragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String TAG = PlaceholderFragment.class.getName();

        private NoteListElementAdapter noteAdapter;

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.note_list_menu, menu);
            ActionBar actionBar = getActivity().getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(getString(R.string.all_notes));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.sort_name: sortNotesByName(); break;
                case R.id.sort_date: sortNotesByDate(); break;
            }
            return true;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

            noteAdapter = new NoteListElementAdapter(this.getActivity(), R.layout.note_list_element,
                    new ArrayList<Note>());
            ListView notesListView = (ListView)rootView.findViewById(R.id.notesListView);
            notesListView.setAdapter(noteAdapter);
            notesListView.setOnItemClickListener(new NoteListClickListener());

            new NoteListAsyncTask(noteAdapter, rootView.getContext()).execute();

            return rootView;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((NotesActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        private void sortNotesByName() {
            noteAdapter.sort(new Comparator<Note>() {
                @Override
                public int compare(Note note, Note note2) {
                    return note.getTitle().compareTo(note2.getTitle());
                }
            });
            noteAdapter.notifyDataSetChanged();
        }

        private void sortNotesByDate() {
            noteAdapter.sort(new Comparator<Note>() {
                @Override
                public int compare(Note note, Note note2) {
                    return new Long(note.getUpdated()).compareTo(note2.getUpdated());
                }
            });
            noteAdapter.notifyDataSetChanged();
        }


    }



}
