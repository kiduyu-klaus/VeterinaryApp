package com.kiduyu.Andrewproject.k_vet.VeterinaryFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kiduyu.Andrewproject.k_vet.Models.History;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.kiduyu.Andrewproject.k_vet.R;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

public class Vet_Treatment_HistoryFragment extends Fragment {
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vet_treatment_history, container, false);

        recyclerView=view.findViewById(R.id.vet_treatment_animals);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Treatment_History").child(Prevalent.currentOnlineUser.getPhone());
        mDatabase.keepSynced(true);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;

    }

    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<History> option = new FirebaseRecyclerOptions.Builder<History>()
                .setQuery(mDatabase,History.class)
                .build();


        FirebaseRecyclerAdapter<History, AnimalViewHolder> adapter= new FirebaseRecyclerAdapter<History, AnimalViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final AnimalViewHolder holder, int position, @NonNull final History model) {
                holder.name.setText("Animal Name: "+model.getAnimal_Name());
                holder.remarks.setText("Remarks Given: "+model.getRemarks());
                holder.date.setText("Treatment Date: "+model.getDate());



            }

            @NonNull
            @Override
            public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vet_appointment_history, parent, false);
                return new AnimalViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder{
        TextView name,date,remarks;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.vet_history_animal_name);
            remarks=itemView.findViewById(R.id.vet_history_remarks);
            date=itemView.findViewById(R.id.vet_history_date);





        }
    }
}
