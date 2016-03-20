package com.parkaid.app;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parkaid.app.adapter.DataAdapter;
import com.parkaid.app.model.DatabaseHandler;
import com.parkaid.app.model.GaitData;
import com.trnql.smart.base.SmartFragment;
import com.trnql.smart.location.AddressEntry;

import java.util.ArrayList;

public class EventListFragment extends SmartFragment {

    public EventListFragment(){}
    public DatabaseHandler db;
    public DataAdapter adapter;
    public ArrayList<GaitData> arrayOfEvents;
    public ListView listview;
    public String eventLocation;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.fragment_eventlist_list, container, false);

        db = new DatabaseHandler(getActivity());
        ArrayList<GaitData> events = db.getAllEvents();
        if (events.isEmpty()){
            arrayOfEvents = new ArrayList<GaitData>();
        } else {
            arrayOfEvents = events;
        }

        listview = (ListView) mainView.findViewById(R.id.ListView);
        adapter = new DataAdapter(getActivity(), arrayOfEvents);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create fragment
                GraphFragment newFragment = new GraphFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.frame_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
        return mainView;
    }

    @Override
    protected void smartAddressChange(AddressEntry address) {
        eventLocation = address.toString();
    }
}




