package com.kiduyu.Andrewproject.k_vet.FarmerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.kiduyu.Andrewproject.k_vet.R;

public class Farmer_Home_Fragment extends Fragment {
    private CardView sick,bookings,findvet,records,profile;
                      @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_home,container,false);

        sick=view.findViewById(R.id.farmer_home_sickanimals);
        bookings=view.findViewById(R.id.farmer_home_bookings);
        findvet=view.findViewById(R.id.farmer_home_findvet);
        records=view.findViewById(R.id.farmer_home_records);
        profile=view.findViewById(R.id.farmer_home_profile);

        sick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new My_Animals_Fragment()).commit();
            }
        });

        bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Farmer_MyBookings_Fragment()).commit();
            }
        });

        findvet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Find_Vet_Fragment()).commit();
            }
        });

        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new History_Fragment()).commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Farmer_Profile_Fragment()).commit();
            }
        });










        return view;
    }
}
