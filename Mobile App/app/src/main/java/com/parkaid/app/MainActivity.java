package com.parkaid.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parkaid.app.adapter.NavDrawerListAdapter;
import com.parkaid.app.model.NavDrawerItem;
import com.trnql.smart.base.SmartActivity;

import java.util.ArrayList;

public class MainActivity extends SmartActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private boolean fallDetected = false;
	private boolean	btEnabled = false;

	private static final int REQUEST_ENABLE_BT = 3;
	// Tag for logging
	private static final String TAG = "MainActivity";

	// MAC address of remote Bluetooth device
	// Replace this with the address of your own module
	private final String address = "20:15:05:05:10:81";

	// The thread that does all the work
	BluetoothThread btt;

	// Handler for writing messages to the Bluetooth connection
	Handler writeHandler;

	// Member fields
	private BluetoothAdapter mBtAdapter;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;

    private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getAppData().setApiKey("bf44571f-413b-4fd0-a37a-bb2a058b3c8d");
		startSmartServices(false);
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();


		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

		// Register mMessageReceiver to receive messages.
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
				new IntentFilter("fall-event"));
	}

	@Override
	public void onResume() {
		super.onResume();

		// Register mMessageReceiver to receive messages.
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
				new IntentFilter("fall-event"));
	}

	@Override
	protected void onPause() {
		// Unregister since the activity is not visible
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
		fallDetected = false;
		super.onPause();
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem miExit) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(miExit)) {
			return true;
		}
		// Handle action bar actions click
		switch (miExit.getItemId()) {
			case R.id.action_connect:
				mBtAdapter = BluetoothAdapter.getDefaultAdapter();
				checkBTState();
				// Set result CANCELED in case the user backs out
				setResult(Activity.RESULT_CANCELED);
				if(btEnabled){
					connectButtonPressed();
				}
				return true;
			case R.id.action_disconnect:
				disconnectButtonPressed();
				return true;
			default:
				return super.onOptionsItemSelected(miExit);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		return drawerOpen;
	}

	/**
	 * Displaying fragment view for selected nav drawer list item
	 * */
	public void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		ConnectivityManager cm = (ConnectivityManager)MainActivity.this.getSystemService(Activity.CONNECTIVITY_SERVICE);

    	if(cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()){
		switch (position) {
		case 0:
            fragment = new IncidentLogsFragment();
			break;
		case 1:
			fragment = new EmergencyListFragment().newInstance(fallDetected);
			break;
            case 2:
                fragment = new SettingsFragment();
                break;
		default:
			break;
			}
    	}
	    else{
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
	 
				// set title
				alertDialogBuilder.setTitle("Internet Disconnected");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Please check internet connection and try again")
					.setCancelable(false)
					.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	
					// show it
					alertDialog.show();   
	    }
    	
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	// handler for received Intents for the "fall-event"
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			fallDetected = true;
			displayView(1);
		}
	};

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Launch the Bluetooth thread.
	 */
	public void connectButtonPressed() {
		Log.v(TAG, "Connect button pressed.");
		Toast msg = Toast.makeText(getBaseContext(), "Connecting device", Toast.LENGTH_LONG);
		msg.show();

		// Only one thread at a time
		if (btt != null) {
			Log.w(TAG, "Already connected!");
			Toast errmsg = Toast.makeText(getBaseContext(), "Already connected!", Toast.LENGTH_SHORT);
			errmsg.show();
			return;
		}

		// Initialize the Bluetooth thread, passing in a MAC address
		// and a Handler that will receive incoming messages
		btt = new BluetoothThread(address, new Handler() {

			@Override
			public void handleMessage(Message message) {

				String s = (String) message.obj;

				// Do something with the message
				if (s.equals("CONNECTED")) {
					Toast msg = Toast.makeText(getBaseContext(), "Device Connected", Toast.LENGTH_SHORT);
					msg.show();
				} else if (s.equals("DISCONNECTED")) {
					Toast msg = Toast.makeText(getBaseContext(), "Device Disconnected", Toast.LENGTH_SHORT);
					msg.show();
				} else if (s.equals("CONNECTION FAILED")) {
					Toast msg = Toast.makeText(getBaseContext(), "Connection Failed", Toast.LENGTH_SHORT);
					msg.show();
				} else {
					if (!fallDetected) {
						Intent intent = new Intent("fall-event");
						LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
					}
				}
			}
		});

		// Get the handler that is used to send messages
		writeHandler = btt.getWriteHandler();

		// Run the thread
		btt.start();
	}

	/**
	 * Kill the Bluetooth thread.
	 */
	public void disconnectButtonPressed() {
		if(btt != null) {
			btt.interrupt();
			btt = null;
		}
	}

	private void checkBTState() {
		// Check for Bluetooth support and then check to make sure it is turned on
		// Emulator doesn't support Bluetooth and will return null
		if(mBtAdapter == null) {
			Toast msg = Toast.makeText(getBaseContext(), "Fatal Error" + " - " + "Bluetooth Not supported. Aborting.", Toast.LENGTH_SHORT);
			msg.show();
		} else {
			if (mBtAdapter.isEnabled()) {
				Toast msg = Toast.makeText(getBaseContext(), "Bluetooth is Enabled", Toast.LENGTH_SHORT);
				msg.show();
				btEnabled = true;
			} else {
				//Prompt user to turn on Bluetooth
				Intent enableBtIntent = new Intent(mBtAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_ENABLE_BT:
				if (resultCode == Activity.RESULT_OK) {
					Toast msg = Toast.makeText(getBaseContext(), "Connecting device", Toast.LENGTH_LONG);
					msg.show();
					btEnabled = true;
					connectButtonPressed();
				}
				else if (resultCode == Activity.RESULT_CANCELED) {
					Toast msg = Toast.makeText(getBaseContext(), "Unable to connect. Bluetooth is off", Toast.LENGTH_LONG);
					msg.show();
				}
		}
	}
}
