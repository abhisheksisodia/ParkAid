package com.parkaid.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parkaid.app.adapter.UserAdapter;
import com.parkaid.app.model.DatabaseHandler;
import com.parkaid.app.model.User;
import com.trnql.smart.base.SmartFragment;
import com.trnql.smart.location.AddressEntry;

import java.util.ArrayList;

public class EmergencyListFragment extends SmartFragment {

	public EmergencyListFragment(){}
    public DatabaseHandler db;
    public UserAdapter adapter;
    public ArrayList<User> arrayOfUsers;
    public ListView listview;
    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    public String userLocation;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.fragment_emergency, container, false);

        db = new DatabaseHandler(getActivity());
        // Reading all emergency contacts
        ArrayList<User> contacts = db.getAllContacts();
        if (contacts.isEmpty()){
            arrayOfUsers = new ArrayList<User>();
        } else {
            arrayOfUsers = contacts;
        }

        listview = (ListView) mainView.findViewById(R.id.ListView);
        adapter = new UserAdapter(getActivity(), arrayOfUsers);
        listview.setAdapter(adapter);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(final AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setTitle("Delete?");
                ad.setMessage("Are you sure you want to remove from emergency list?");
                final int positionToDelete = pos;
                ad.setNegativeButton("Cancel", null);
                ad.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        User deleteContact = arrayOfUsers.get(positionToDelete);
                        arrayOfUsers.remove(positionToDelete);
                        listview.setAdapter(adapter);
                        db.deleteContact(deleteContact);
                    }
                });
                ad.show();
                UserAdapter adapter = new UserAdapter(getActivity(), arrayOfUsers);
                listview.setAdapter(adapter);
                return true;
            }
        });
        listview.setLongClickable(true);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String phoneNo = arrayOfUsers.get(i).getPhoneNumber();
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, "Your friend needs help! The user is located at " + userLocation + "- ParkAid App", null, null);
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Sms Failed!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });
        return mainView;
    }

    @Override
    protected void smartAddressChange(AddressEntry address) {
        userLocation = address.toString();
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
                db.addContact(new User(name, number));
            }
        }
        adapter = new UserAdapter(getActivity(), arrayOfUsers);
        listview.setAdapter(adapter);
    }
}




