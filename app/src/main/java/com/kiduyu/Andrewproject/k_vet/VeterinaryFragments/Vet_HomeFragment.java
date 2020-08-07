package com.kiduyu.Andrewproject.k_vet.VeterinaryFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.kiduyu.Andrewproject.k_vet.R;

public class Vet_HomeFragment extends Fragment {
    private CardView appointment,remarks,treatment,treated,profile;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vet_home, container, false);





        appointment=view.findViewById(R.id.vet_home_appointment);
        remarks=view.findViewById(R.id.vet_home_remarks);
        treatment=view.findViewById(R.id.vet_home_treat);
        treated=view.findViewById(R.id.vet_home_treated);
        profile=view.findViewById(R.id.vet_home_profile);

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Vet_Animal_AppiontmentFragment()).commit();
            }
        });

        remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Vet_Treatment_HistoryFragment()).commit();
            }
        });

        treated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Vet_Treatment_HistoryFragment()).commit();
            }
        });

        treatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Vet_Animal_AppiontmentFragment()).commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Veterinary_Profile_Fragment()).commit();
            }
        });






        return  view;

    }
}
