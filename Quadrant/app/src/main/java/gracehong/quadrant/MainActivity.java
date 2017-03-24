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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("myLists", MODE_PRIVATE, null);

        if (noTaskTable()){
            initializeTaskTable();
        }

        //Initializes Details fragment if in horizontal view
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

        //if Portrait: launch fragment in new activity
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("list_type", tag);
            startActivity(intent);
        }

        //if Landscape: display relevant list
        else{
           // DetailsFragment details = new DetailsFragment();
           // getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_rightFrag, details).commit();
            //details.loadList(tag);
            //fragment handles clicks to button
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
