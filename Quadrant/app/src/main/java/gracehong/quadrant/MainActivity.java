package gracehong.quadrant;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.R.attr.button;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("myLists", MODE_PRIVATE, null);

        if (noTaskTable()){
            initializeTaskTable();
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

        }
    }
}
