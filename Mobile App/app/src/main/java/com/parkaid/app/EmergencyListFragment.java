package com.parkaid.app;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.parkaid.app.adapter.UserAdapter;
import com.parkaid.app.model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EmergencyListFragment extends Fragment {

	public EmergencyListFragment(){}
    private Uri uriContact;
    public Gson gson;
    public ArrayList<User> arrayOfUsers;
    public ListView listview;
    static final int PICK_CONTACT_REQUEST = 1;  // The request code

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.fragment_emergency, container, false);

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String contacts = appSharedPrefs.getString("EmergencyContactList", "");
        if (contacts.isEmpty()){
            arrayOfUsers = new ArrayList<User>();
        } else {
            Type type = new TypeToken<List<User>>(){}.getType();
            arrayOfUsers = gson.fromJson(contacts, type);
        }

        listview = (ListView) mainView.findViewById(R.id.ListView);
        return mainView;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("ADD")
                .setOnMenuItemClickListener(this.mAddButtonClickListener)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    MenuItem.OnMenuItemClickListener mAddButtonClickListener = new MenuItem.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
            return true;
        }
    };

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (reqCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == getActivity().RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = getActivity().getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int phoneNumColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String number = cursor.getString(phoneNumColumn);
                String name = cursor.getString(nameColumn);
                arrayOfUsers.add(new User(name, number));
            }
        }
        UserAdapter adapter = new UserAdapter(getActivity(), arrayOfUsers);
        listview.setAdapter(adapter);
        gson = new Gson();
        String emergencyContacts = gson.toJson(arrayOfUsers);
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("EmergencyContactList", emergencyContacts);
        prefsEditor.commit();
    }
}




