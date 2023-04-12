package com.example.semester4uts;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowLocationFragment extends Fragment {


    public ShowLocationFragment() {
        // Required empty public constructor
    }

    public static ShowLocationFragment newInstance(){
        return new ShowLocationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_show_location, container, false);
        final Button btn = rootView.findViewById(R.id.show_recom);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_mem = new Intent(rootView.getContext(), RecomDetail.class);
                startActivity(add_mem);
            }
        });
        return rootView;
    }

}