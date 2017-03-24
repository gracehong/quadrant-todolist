package gracehong.quadrant;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Wrapper activity for the addTask Fragment in portrait view
 */
public class AddTaskActivity extends AppCompatActivity implements AddTaskFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //Nothing
    }
}
