package gracehong.quadrant;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    SQLiteDatabase db;
    ArrayAdapter<String> adapter;
    ArrayList<String> list;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
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
     * FUNCTION: onActivityCreated
     * overrides onActivityCreated in order to get any relevant parameters passed in (if buttons
     * are clicked)
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = getActivity().openOrCreateDatabase("myLists", MODE_PRIVATE, null);
        final TextView listTitle = (TextView) getActivity().findViewById(R.id.list_title);
        String listType = "urgent_important"; //sets default listType to urgent_important

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = getActivity().getIntent();
            if (intent.hasExtra("list_type")) {
                listType = intent.getStringExtra("list_type");
            }
        } else {
            Button UrgImpt = (Button) getActivity().findViewById(R.id.urgent_important_button);
            UrgImpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadList("urgent_important");
                    listTitle.setText("Urgent|Important");
                }
            });

            Button UrgNotImpt = (Button) getActivity().findViewById(R.id.urgent_notimportant_button);
            UrgNotImpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadList("urgent_notimportant");
                    listTitle.setText("Urgent|Not Important");
                }
            });

            Button NotUrgImpt = (Button) getActivity().findViewById(R.id.noturgent_important_button);
            NotUrgImpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadList("noturgent_important");
                    listTitle.setText("Not Urgent|Important");
                }
            });

            Button NotUrgNotImpt = (Button) getActivity().findViewById(R.id.noturgent_notimportant_button);
            NotUrgNotImpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadList("noturgent_notimportant");
                    listTitle.setText("Not Urgent|Not Important");
                }
            });
        }

        if (listTitle != null) {
            listTitle.setText(listType);
        }
        loadList(listType);
    }

    /**
     * FUNCTION: loadList
     * @param listType (which list to load from database)
     *
     */
    public void loadList(String listType){

        if(db == null){
            db = getActivity().openOrCreateDatabase("myLists", MODE_PRIVATE, null);
        }

        //query's database
        list = new ArrayList<>();
        System.out.println("the list name is "+ listType);

        String command = "SELECT content FROM tasks WHERE listType = '"+listType+"';";
        Cursor cursor = db.rawQuery(command, null);
        while(cursor.moveToNext()){
            list.add(cursor.getString(0));

        }

        //loads list into array and initializes long click listener
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView) getActivity().findViewById(R.id.task_list);
        if (listView != null) {
            listView.setAdapter(adapter);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String content = list.get(position);
                    db.delete("tasks","content=?",new String[]{content});
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                    return false;
                }
            });
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
