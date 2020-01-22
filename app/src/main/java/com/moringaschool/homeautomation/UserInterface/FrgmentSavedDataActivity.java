package com.moringaschool.homeautomation.UserInterface;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moringaschool.homeautomation.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrgmentSavedDataActivity extends Fragment {


    public FrgmentSavedDataActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frgment_saved_data, container, false);
    }

}
