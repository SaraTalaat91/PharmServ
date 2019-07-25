package com.saratms.ismailiapharmserv.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saratms.ismailiapharmserv.R;

/**
 * Created by Sarah Al-Shamy on 27/04/2019.
 */

public class RareDrugsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rare_drugs, container, false);
        return view;
    }
}
