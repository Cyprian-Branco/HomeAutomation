package com.moringaschool.homeautomation.UserInterface;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moringaschool.homeautomation.R;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrgmentSavedDataActivity extends Fragment {
    @BindView(R.id.pairingButton) Button  mPairingButton;
    @BindView(R.id.devicesListView) ListView mDevicesListView;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frgment_saved_data, container, false);
        ButterKnife.bind(this, view);
         myBluetooth = BluetoothAdapter.getDefaultAdapter();
         if (myBluetooth == null){
             Toast.makeText(getContext(), "Bluetooth device not available", Toast.LENGTH_LONG).show();
         } else if (!myBluetooth.isEnabled()){
             Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
             startActivityForResult(turnBTon, 1);
        }
         mPairingButton.setOnClickListener((v) -> {pairedDevicesList();});
        return view;
    }
    private void pairedDevicesList(){
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0){
            for (BluetoothDevice bt : pairedDevices ){
                list.add(bt.getName().toString() + "\n" + bt.getAddress().toString());
            }
        } else {
            Toast.makeText(getContext(), "No paired Bluetooth devices found", Toast.LENGTH_LONG).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        mDevicesListView.setAdapter(adapter);
        mDevicesListView.setOnItemClickListener(myListClickListener);
    }
    private AdapterView.OnItemClickListener myListClickListener = ((parent, view, position, id) -> {
        String info = ((TextView) view).getText().toString();
        String address = info.substring(info.length()-17);

        Intent intent = new Intent(getActivity(), FragmentHomeActivity.class);
        intent.putExtra(EXTRA_ADDRESS, address);
        startActivity(intent);
    });

}
