package it.ads.app.bluetoothfinder;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> mDeviceList = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private String TAG = "kopo88";
    //get access to location permission
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        progressBar = findViewById(R.id.llProgressBar);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Button fab = findViewById(R.id.button1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanDevices();
                Toast.makeText(getApplicationContext(),
                        "Scanning for Bluetooth devices...",Toast.LENGTH_LONG).show();
            }
        });

        if (!mBluetoothAdapter.isEnabled()) {
            startBluetooth();
        }

        locationPermission();

    }

    /**
     * From API level 23 (Android 6.0 Marshmallow) we need to ask Run-time
     * permission from the user-end
     */
    private void locationPermission(){

        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }else{
                scanDevices();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scanDevices();
                } else {
                    // Permission for location Denied
                    Toast.makeText( this,"Well cant help you then!" ,
                            Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * If Bluetooth is off, prompt user to swith BT on
     */
    private void startBluetooth(){
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 0);
    }

    private void scanDevices(){

        if (!mBluetoothAdapter.isEnabled()) {
            startBluetooth();
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        try {
            getApplicationContext().registerReceiver(mReceiver, filter);
            Log.i(TAG,"Registered receiver");
        } catch(Exception e) {
            Log.i(TAG,"Failed to register receiver");
            e.printStackTrace();
        }

        if(mBluetoothAdapter != null){
            mBluetoothAdapter.startDiscovery();
            Log.i(TAG,"Scanning for Bluetooth devices...");
//            Toast.makeText(getApplicationContext(),
//                    "Scanning for Bluetooth devices...",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),
                    "Something wrong!",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        Log.i(TAG, "BLE Broadcast Reciever destroyed ");
        super.onDestroy();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "Received Broadcast ");
            if (BluetoothDevice.ACTION_FOUND.equalsIgnoreCase(action)) {

                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(TAG, "Found Device - "+device.getAddress()+" - "+device.getName());

                if(!mDeviceList.contains("Name: "+device.getName()
                        + "\nMAC: " + device.getAddress())){
                    mDeviceList.add("Name: "+device.getName()
                            + "\nMAC: " + device.getAddress());
                    Log.i("BT", device.getName() + "\n" + device.getAddress());
                }else{
                    Log.i(TAG, "Avoiding duplicates (hackjob but work for now ;))");
                }

                listView.setAdapter(new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1, mDeviceList));

            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equalsIgnoreCase(action)){

                int devicesFound = mDeviceList.size();
                Log.i(TAG ,"Discovery finished "+devicesFound);
                if(devicesFound == 0){
                    Toast.makeText(getApplicationContext(), "No devices found",
                            Toast.LENGTH_LONG).show();
                }
                mBluetoothAdapter.cancelDiscovery();
                progressBar.setVisibility(View.INVISIBLE);


            }else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equalsIgnoreCase(action)){

                Log.i(TAG ,"Trying to discover BLE devices...");
                progressBar.setVisibility(View.VISIBLE);

            }
        }
    };
}