package com.parkaid.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IncidentLogsFragment extends Fragment {

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.fragment_incident, container, false);

        BarChart mBarChart = (BarChart) mainView.findViewById(R.id.barchart);

        mBarChart.addBar(new BarModel("Mon", 2.3f, 0xFF123456));
        mBarChart.addBar(new BarModel("Tues", 2.f,  0xFF63CBB0));
        mBarChart.addBar(new BarModel("Wed", 3.3f, 0xFF563456));
        mBarChart.addBar(new BarModel("Thurs", 1.1f, 0xFF873F56));
        mBarChart.addBar(new BarModel("Fri", 2.7f, 0xFF56B7F1));
        mBarChart.addBar(new BarModel("Sat", 2.f,  0xFF343456));

        mBarChart.startAnimation();

        final ListView listview = (ListView) mainView.findViewById(R.id.ListView);
        String[] values = new String[] { "Today", "This Week", "Last Week",
                "Last Month", "Past Year"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                // Create fragment
                EventListFragment newFragment = new EventListFragment();

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

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }


}