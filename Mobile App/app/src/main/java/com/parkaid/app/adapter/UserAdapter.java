package com.parkaid.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parkaid.app.R;
import com.parkaid.app.model.User;

import java.util.ArrayList;

/**
 * Created by abhisheksisodia on 15-07-22.
 */
public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_listview_row, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.userName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.userPhone);
        // Populate the data into the template view using the data object
        tvName.setText(user.getName());
        tvHome.setText("Phone: " + user.getPhoneNumber());
        // Return the completed view to render on screen
        return convertView;
    }
}