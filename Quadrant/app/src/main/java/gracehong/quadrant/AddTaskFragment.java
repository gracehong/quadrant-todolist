package gracehong.quadrant;

import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddTaskFragment extends Fragment {

    SQLiteDatabase db;
    private OnFragmentInteractionListener mListener;

    public AddTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Initialize on Click listeners
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //initialize database:
        db = getActivity().openOrCreateDatabase("myLists", MODE_PRIVATE, null);

        //initialize onClick listener for button:
        Button button = (Button) getActivity().findViewById(R.id.addFinishedItem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFinishedItem();
            }
        });
    }

    /**
     * FUNCTION: addFinishedItem
     * function called onClick (addFinishedItem button) that reads in data from the user's input
     * and adds a task to the database. Exits activity when finished
     */
    private void addFinishedItem(){
        EditText taskInput = (EditText) getActivity().findViewById(R.id.task_input);
        String task = taskInput.getText().toString();

        if (!task.equals("")){
            String listType = parseBoxes();

            String command = "INSERT into 'tasks' VALUES (NULL,'"+task+"','"+ listType+"');";
            db.execSQL(command);

        } else{
            Toast.makeText(getContext(), "Please enter a task", Toast.LENGTH_SHORT).show();
        }
        exitFrag();
    }

    /**
     * FUNCTIOn: parseBoxes
     * helper function that parses the information from the boxes in order to determine the list type
     */
    private String parseBoxes(){
        CheckBox urgentBox = (CheckBox) getActivity().findViewById(R.id.urgent_checkbox);
        CheckBox importantBox = (CheckBox) getActivity().findViewById(R.id.important_checkbox);

        String listType = "";

        if(urgentBox.isChecked()){
            listType = listType.concat("urgent_");
        } else{
            listType = listType.concat("noturgent_");
        }

        if (importantBox.isChecked()){
            listType = listType.concat("important");
        } else{
            listType = listType.concat("notimportant");
        }

        return listType;
    }

    /**
     * FUNCTION: exitFrag
     * Function (called by the cancel button and addItem button) that exits the fragment in different
     * ways depending on screen orientation
     */
    private void exitFrag(){

        //Landscape orientation
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            DetailsFragment frag = new DetailsFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_rightFrag, frag).commit();
        }

        //Portrait orientation
        else {
            getActivity().finish();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
