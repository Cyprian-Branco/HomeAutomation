package com.moringaschool.homeautomation.UserInterface;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.moringaschool.homeautomation.R;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesActivity extends Fragment {
    @BindView(R.id.pairButton)
    Button buttonPaired;
    @BindView(R.id.devicesListView)
    ListView deviceList;

    private BluetoothAdapter mBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    /*public DevicesActivity() {
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_devices, container, false);
        ButterKnife.bind(this, view);

        mBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (mBluetooth == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Bluetooth device not available", Toast.LENGTH_LONG).show();
            getActivity().finish();
        } else if (mBluetooth.isEnabled()) {
            Intent turnBluetoothOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBluetoothOn, 1);
        }
        buttonPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });
        return view;
    }

    private void pairedDevicesList() {
        pairedDevices = mBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName().toString() + "\n" + bt.getAddress().toString());
            }
        } else {
            Toast.makeText(getContext().getApplicationContext(), "No paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }
        Context context;
        final ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);

            Intent intent = new Intent(getActivity(), ControlRoomActivity.class);
            intent.putExtra(EXTRA_ADDRESS, address);
            startActivity(intent);
        }
    };

}
