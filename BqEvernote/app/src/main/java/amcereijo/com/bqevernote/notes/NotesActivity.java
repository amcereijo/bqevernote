package amcereijo.com.bqevernote.notes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import amcereijo.com.bqevernote.R;
import amcereijo.com.bqevernote.api.EvernoteApi;
import roboguice.activity.RoboActivity;
import roboguice.activity.RoboFragmentActivity;
import roboguice.fragment.RoboFragment;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_notes)
public class NotesActivity extends RoboFragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

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
        // update the main content by replacing fragments
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
            .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
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
