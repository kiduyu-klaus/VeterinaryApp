package com.kiduyu.Andrewproject.k_vet.FarmerFragments;

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
import com.kiduyu.Andrewproject.k_vet.MapsActivity;
import com.kiduyu.Andrewproject.k_vet.Models.Office;
import com.kiduyu.Andrewproject.k_vet.R;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

public class Find_Vet_Fragment extends Fragment {
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_find_vet,container,false);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Offices");

        recyclerView=view.findViewById(R.id.user_find_vet);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        return view;
    }

    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Office> option = new FirebaseRecyclerOptions.Builder<Office>()
                .setQuery(mDatabase,Office.class)
                .build();


        FirebaseRecyclerAdapter<Office, AnimalViewHolder> adapter= new FirebaseRecyclerAdapter<Office, AnimalViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final AnimalViewHolder holder, int position, @NonNull final Office model) {
                holder.name.setText("Office Name: " + model.getOffice_name());
                holder.owner.setText("Office Owned By: " + model.getOffice_owner());
                holder.owner_phone.setText("Owner Phone Number: " + model.getOffice_number());
                holder.location.setText("Situated At: " + model.getOffice_location());
                holder.description.setText(model.getOffice_description());
                String ImageView = model.getImage().toString();
               // Picasso.get().load(ImageView).into(holder.office_image);
                holder.office_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MapsActivity.class);
                        intent.putExtra("location", model.getOffice_location());
                        intent.putExtra("owner", model.getOffice_owner());
                        startActivity(intent);

                    }
                });
                holder.Office_book_appointment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        Bundle arguments = new Bundle();
                        arguments.putString("vetName",model.getOffice_owner());
                        arguments.putString("Office_Location",model.getOffice_location());
                        Farmer_Book_Fragment book_fragment = new Farmer_Book_Fragment();
                        book_fragment.setArguments(arguments);
                        fm.beginTransaction().replace(R.id.fragment_container,book_fragment).commit();


                    }
                });


            }

            @NonNull
            @Override
            public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vet_office, parent, false);
                return new AnimalViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder{
        TextView name,location,description,owner,owner_phone;
        ImageView office_image;
        Button Office_book_appointment;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.fetch_officename);
            owner=itemView.findViewById(R.id.fetch_offficeowner);
            owner_phone=itemView.findViewById(R.id.fetch_offficeowner_phone);
            location=itemView.findViewById(R.id.fetch_offficelocation);
            description=itemView.findViewById(R.id.fetch_officedescription);
            office_image=itemView.findViewById(R.id.fetch_officeimage);
            Office_book_appointment =itemView.findViewById(R.id.Office_book_appointment);





        }
    }
}
