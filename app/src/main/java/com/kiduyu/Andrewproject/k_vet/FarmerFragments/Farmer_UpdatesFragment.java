package com.kiduyu.Andrewproject.k_vet.FarmerFragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.Andrewproject.k_vet.Models.Animal;
import com.kiduyu.Andrewproject.k_vet.Models.Appointments;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.kiduyu.Andrewproject.k_vet.Models.Updates;
import com.kiduyu.Andrewproject.k_vet.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class Farmer_UpdatesFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.updats_fragments,container,false);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Updates");


        recyclerView=view.findViewById(R.id.farmer_updates_recyclerview);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return  view;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Updates> option = new FirebaseRecyclerOptions.Builder<Updates>()
                .setQuery(mDatabase, Updates.class)
                .build();


        FirebaseRecyclerAdapter<Updates, AnimalViewHolder> adapter= new FirebaseRecyclerAdapter<Updates, AnimalViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final AnimalViewHolder holder, int position, @NonNull final Updates model) {
                holder.title.setText(model.getTitle());
                holder.content.setText(model.getContent());
                holder.date.setText(model.getDate());
                holder.owner.setText(model.getOwner());
                String ImageView= model.getImage().toString();
                Picasso.get().load(ImageView).into(holder.animal_image);


            }

            @NonNull
            @Override
            public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_updates, parent, false);
                return new AnimalViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder{
        TextView title,content,date,owner;
        ImageView animal_image ;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.fetch_update_title);
            content=itemView.findViewById(R.id.fetch_update_contents);
            date=itemView.findViewById(R.id.fetch_update_date);
            owner=itemView.findViewById(R.id.fetch_update_owner);
            animal_image=itemView.findViewById(R.id.fetch_update_image);




        }
    }


}
