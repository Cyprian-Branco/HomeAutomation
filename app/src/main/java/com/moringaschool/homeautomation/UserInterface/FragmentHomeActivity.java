package com.moringaschool.homeautomation.UserInterface;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.moringaschool.homeautomation.R;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomeActivity extends Fragment {
    @BindView(R.id.room1Button) Button mRoom1Button;
    @BindView(R.id.room2Button) Button mRoom2Button;

    String address = null;
    private ProgressDialog progressDialog;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket bluetoothSocket = null;
    private boolean isBtConnected = false;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(view);

        Intent newInt = new Intent();
        address = newInt.getStringExtra(FrgmentSavedDataActivity.EXTRA_ADDRESS);

        new ConnectBT().execute();

        mRoom1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                sendSignal("1");
            }
        });
        mRoom2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                sendSignal("2");
            }
        });
        return view;
    }
    private void sendSignal(String number){
        if(bluetoothSocket !=null){
            try{
                bluetoothSocket.getOutputStream().write(number.toString().getBytes());
            } catch (IOException e){
                msg("Error");
            }
        }
    }
    private void Disconnect(){
        if(bluetoothSocket != null){
            try{
                bluetoothSocket.close();
            }catch (IOException e){
                msg("Error");
            }
        }
    }
    private void msg(String s){
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }
    private class ConnectBT extends AsyncTask<Void, Void, Void>{
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Connecting...", "Please Wait!!");
        }
        @Override
        protected Void doInBackground(Void... devices){
            try {
                if(bluetoothSocket==null || !isBtConnected){
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice bluetoothDevice = myBluetooth.getRemoteDevice(address);
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(mUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();
                }
            } catch (IOException e){
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected  void onPostExecute(Void result){
            super.onPostExecute(result);

            if (!ConnectSuccess){
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
            } else {
                msg ("Connected");
                isBtConnected = true;
            }
            progressDialog.dismiss();
        }
    }
}
