package com.kiduyu.Andrewproject.k_vet.FarmerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.kiduyu.Andrewproject.k_vet.Models.Treatment;
import com.kiduyu.Andrewproject.k_vet.R;
import com.squareup.picasso.Picasso;

public class History_Fragment extends Fragment {
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_treatment_history,container,false);

        mStorageRef= FirebaseStorage.getInstance().getReference().child("Treated_Animals");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Treated_Animals");
        mDatabase.keepSynced(true);



        recyclerView=view.findViewById(R.id.farmer_animals_treatment_history);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);



        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Treatment> option = new FirebaseRecyclerOptions.Builder<Treatment>()
                .setQuery(mDatabase.child(Prevalent.currentOnlineUser.getName()),Treatment.class)
                .build();

        FirebaseRecyclerAdapter<Treatment, AnimalViewHolder> adapter= new FirebaseRecyclerAdapter<Treatment, AnimalViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final AnimalViewHolder holder, int position, @NonNull final Treatment model) {

                holder.name.setText(model.getAnimal_name());
                holder.vet_name.setText(model.getVet_name());
                holder.date_treated.setText(model.getDate_treated());
                holder.description.setText("Veterinary Remarks");
                holder.vet_remarks.setText(model.getVet_remarks());
                String ImageView= model.getAnimal_image().toString();
                if (ImageView.isEmpty()){
                    holder.animal_history_image.setImageResource(R.drawable.profile);
                }else{
                    Picasso.get().load(ImageView).into(holder.animal_history_image);
                }


            }

            @NonNull
            @Override
            public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_treatment_history, parent, false);
                return new AnimalViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    public static class AnimalViewHolder extends RecyclerView.ViewHolder{
        TextView name,vet_name,description,date_treated,vet_remarks,vet_location;
        ImageView animal_history_image ;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.user_history_animal_name);
            vet_name=itemView.findViewById(R.id.vet_name);
            description=itemView.findViewById(R.id.animal_history_description);
            date_treated=itemView.findViewById(R.id.date_treated);
            animal_history_image=itemView.findViewById(R.id.animal_history_image);
            vet_remarks=itemView.findViewById(R.id.vet_remarks);
            vet_location=itemView.findViewById(R.id.vet_location);


        }
    }
}
