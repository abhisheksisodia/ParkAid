package com.parkaid.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parkaid.app.R;
import com.parkaid.app.model.GaitData;
import com.parkaid.app.model.User;

import java.util.ArrayList;

/**
 * Created by abhisheksisodia on 16-03-09.
 */
public class DataAdapter extends ArrayAdapter<GaitData> {
    public DataAdapter(Context context, ArrayList<GaitData> gaitData) {
        super(context, 0, gaitData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        GaitData gaitData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_listview_row, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.userName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.userPhone);
        // Populate the data into the template view using the data object
        tvName.setText(gaitData.getEventType());
        tvHome.setText("Address: " + gaitData.getEventLocation() + " Time:" + gaitData.getEventDate());
        // Return the completed view to render on screen
        return convertView;
    }
}
