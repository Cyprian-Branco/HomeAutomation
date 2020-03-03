package com.moringaschool.homeautomation.UserInterface;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.moringaschool.homeautomation.R;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControlRoomActivity extends Fragment {
    @BindView(R.id.room1Button) Button button1On;
    @BindView(R.id.room1OffButton) Button button1Off;
    @BindView(R.id.room2Button) Button button2On;
    @BindView(R.id.room2OffButton) Button button2Off;
    @BindView(R.id.unPairButton) Button unPairButton;

    String address = null;
    private ProgressDialog progressDialog;
    BluetoothAdapter mBluetooth = null;
    BluetoothSocket bluetoothSocket = null;
    private boolean isConnected = false;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    public ControlRoomActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control_room, container, false);
        ButterKnife.bind(this, view);
        Intent newIntent = getActivity().getIntent();
        address = newIntent.getStringExtra(DevicesActivity.EXTRA_ADDRESS);

        new ConnectBlueTooth().execute();

        button1On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSignal("1");
            }
        });
        button1Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSignal("2");
            }
        });
        button2On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSignal("3");
            }
        });
        button2Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSignal("4");
            }
        });
        unPairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();
            }
        });

    return view;
    }
    private void sendSignal (String number){
        if(bluetoothSocket != null){
            try{
                bluetoothSocket.getOutputStream().write(number.toString().getBytes());
            } catch (IOException e){
                msg("Error");
            }
        }
    }
    private void Disconnect(){
        if(bluetoothSocket !=null){
            try {
                bluetoothSocket.close();
            }catch (IOException e){
                msg("Error");
            }
        }
        getActivity().finish();
    }
    private void msg (String s){
        Toast.makeText(getContext().getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return device.createRfcommSocketToServiceRecord(mUUID);
    }
    private class ConnectBlueTooth extends AsyncTask<Void, Void, Void >{
        private boolean ConnectSuccess = true;

        /*@Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Connecting...", "Please wait!!");
        }*/
        @Override
        protected Void doInBackground (Void... devices){
            try{
                if(bluetoothSocket==null || !isConnected){
                    mBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice bluetoothDevice = mBluetooth.getRemoteDevice(address);
                    try {
                        if (BluetoothAdapter.checkBluetoothAddress(address)) {
                            //It is a valid MAC address.
                            BluetoothDevice device = mBluetooth.getRemoteDevice(address);
                            bluetoothSocket = createBluetoothSocket(device);
                        } else {

                            Toast.makeText(getActivity().getApplicationContext(), "Invalid MAC: Address", Toast.LENGTH_LONG).show();
                        }
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(mUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();
                }
            }catch (IOException e){
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void results){
            super.onPostExecute(results);
            if(!ConnectSuccess){
                msg("Connection failed. Try again.");
                getActivity().finish();
            }else{
                msg("Connected");
                isConnected = true;
            }
            progressDialog.dismiss();
        }
    }

}
