package com.parkaid.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parkaid.app.adapter.DataAdapter;
import com.parkaid.app.model.DatabaseHandler;
import com.parkaid.app.model.GaitData;
import com.trnql.smart.base.SmartFragment;
import com.trnql.smart.location.AddressEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EventListFragment extends SmartFragment {

    public EventListFragment(){}
    public DatabaseHandler db;
    public DataAdapter adapter;
    public ArrayList<GaitData> arrayOfEvents;
    public ListView listview;
    public String eventLocation;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
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

        return mainView;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.add("ADD")
//                .setOnMenuItemClickListener(this.mAddButtonClickListener)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//
//    }

    @Override
    protected void smartAddressChange(AddressEntry address) {
        eventLocation = address.toString();
    }

//    MenuItem.OnMenuItemClickListener mAddButtonClickListener = new MenuItem.OnMenuItemClickListener() {
//
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//
//            DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
//            String date = df.format(Calendar.getInstance().getTime());
//
//            arrayOfEvents.add(new GaitData("Fall event", eventLocation, date));
//            db.addEvent(new GaitData("Fall event", eventLocation, date));
//            adapter = new DataAdapter(getActivity(), arrayOfEvents);
//            listview.setAdapter(adapter);
//            return true;
//        }
//    };
}



