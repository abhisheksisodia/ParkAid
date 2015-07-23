package com.parkaid.app;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parkaid.app.adapter.UserAdapter;
import com.parkaid.app.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmergencyListFragment extends Fragment {

	public EmergencyListFragment(){}
    private Uri uriContact;
    private String contactID;     // contacts unique ID
    public ArrayList<String> list;
    public ListView listview;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.fragment_emergency, container, false);

        // Construct the data source
        ArrayList<User> arrayOfUsers = new ArrayList<User>();
        // Create the adapter to convert the array to views
        UserAdapter adapter = new UserAdapter(getActivity(), arrayOfUsers);
        // Attach the adapter to a ListView
        listview = (ListView) mainView.findViewById(R.id.ListView);
        listview.setAdapter(adapter);

        adapter.add(new User("Drupadh Manjunath", "151-181-1919"));
        adapter.add(new User("Rajit Venkatesh", "151-181-1919"));
        adapter.add(new User("Irteza Arif", "151-181-1919"));
        adapter.add(new User("Ali", "151-181-1919"));
        adapter.add(new User("Abhishek", "151-181-1919"));

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

            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 1);
            return true;
        }
    };

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
//            uriContact = data.getData();
//
//            ContentResolver cr = getActivity().getContentResolver();
//            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//
//            if (cur.getCount() > 0) {
//                while (cur.moveToNext()) {
//                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
//                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//                        // This inner cursor is for contacts that have multiple numbers.
//                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
//                        pCur.close();
//                    }
//                }
//                Collections.sort(list);
//
//                final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),
//                        android.R.layout.simple_list_item_1, list);
//                listview.setAdapter(adapter);
//                listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//
//            }
//            cur.close();
//        }
//    }
}




