package gracehong.quadrant;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.R.attr.button;

public class MainActivity extends AppCompatActivity implements AddTaskFragment.OnFragmentInteractionListener, DetailsFragment.OnFragmentInteractionListener{

    SQLiteDatabase db;
    boolean addDummyValues = true; //TODO: this is for testing purposes. Remove and rest database!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("myLists", MODE_PRIVATE, null);

        if (noTaskTable()){
            initializeTaskTable();
        }

        if (addDummyValues){
            //TODO: remove this, it initializes it to a default for testing
            String init1 = "INSERT INTO tasks VALUES (NULL,'Today my urgent and important thing is applying for jobs','urgent_important');";
            String init2 = "INSERT INTO tasks VALUES (NULL,'Today my noturgent and important thing is working on this app','noturgent_important');";
            String init3 = "INSERT INTO tasks VALUES (NULL,'Today my urgent and notimportant thing is replying to a lot of emails','urgent_notimportant');";
            String init4 = "INSERT INTO tasks VALUES (NULL,'Today my noturgent and notimportant thing is returning library books','noturgent_notimportant');";
            db.execSQL(init1);
            db.execSQL(init2);
            db.execSQL(init3);
            db.execSQL(init4);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            DetailsFragment frag = new DetailsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.mainActivity_rightFrag, frag).commit();
        }

    }

    /**
     * FUNCTION: initializeTaskTable
     * sets up the taskTable in the database
     */
    private void initializeTaskTable(){
        String command = "CREATE TABLE tasks (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "content TEXT," +
                "listType TEXT);";
        db.execSQL(command);


    }

    /**
     * FUNCTION: noListTable
     * Private helper function to check if the list table is present
     * @return
     */
    private boolean noTaskTable(){
        Cursor tablesCursor = db.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' AND name='tasks';",
                null);
        return tablesCursor.getCount() == 0;
    }


    public void onShowList(View view) {
        Button button = (Button) view;
        String tag = button.getTag().toString();

        System.out.println("The activity selected was "+ tag);

        //if Portrait: launch fragment in new activity
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("list_type", tag);
            startActivity(intent);
        }

        //if Landscape: display relevant list
        else{
            DetailsFragment details = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
            details.loadList(tag);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //Nothing
    }

    /**
     * FUNCTION: onAddNewItem
     * Launches addNewTask activity in portrait view, or replaces the detailsFragment with an addTask
     * fragment in horizontal view
     * @param view
     */
    public void onAddNewItem(View view) {

        //if Portrait: launch fragment in new activity
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            Intent intent = new Intent(this, AddTaskActivity.class);
            startActivity(intent);
        }

        //if Landscape: display relevant list
        else{
            AddTaskFragment frag = new AddTaskFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_rightFrag, frag).commit();
        }
    }
}
