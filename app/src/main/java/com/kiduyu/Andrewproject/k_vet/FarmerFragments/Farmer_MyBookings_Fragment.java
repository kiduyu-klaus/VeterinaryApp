package com.kiduyu.Andrewproject.k_vet.FarmerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.Andrewproject.k_vet.Models.Appointments;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.kiduyu.Andrewproject.k_vet.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.INVISIBLE;

public class Farmer_MyBookings_Fragment extends Fragment {
    private DatabaseReference mDatabase;
    private RecyclerView vet_appointment_animal;
                      @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_farmer_mybooking,container,false);


                          mDatabase= FirebaseDatabase.getInstance().getReference().child("mybookings");
                          vet_appointment_animal = view.findViewById(R.id.farmer_appointment_animal);
                          LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                          vet_appointment_animal.setLayoutManager(layoutManager);











        return view;
    }


    @Override
    public void onStart() {

        super.onStart();
        FirebaseRecyclerOptions<Appointments> option = new FirebaseRecyclerOptions.Builder<Appointments>()
                .setQuery(mDatabase,Appointments.class)
                .build();


        FirebaseRecyclerAdapter<Appointments,ApplicationViewHolder> adapter= new FirebaseRecyclerAdapter<Appointments, ApplicationViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final ApplicationViewHolder holder, int position, @NonNull Appointments model) {

                String Image=Prevalent.currentOnlineUser.getImage();
                if (Image.isEmpty()){
                    holder.appointment_farmer_image.setImageResource(R.drawable.profile);

                }else {
                    Picasso.get().load(Image).into(holder.appointment_farmer_image);
                }

                holder.appointment_farmer_username.setText(Prevalent.currentOnlineUser.getName());
                holder.appointment_farmer_location.setText(model.getAppointment_Address());
                holder.appointment_farmer_description.setText("Appointment made for "+model.getAnimal_name()+" showing this symptoms: "+model.getAnimal_description());
                holder.appointment_farmer_timeago.setText("scheduled for "+model.getAppiontment_date());

            }


            @NonNull
            @Override
            public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment_farmer, parent, false);
                return new ApplicationViewHolder(view);
            }
        };
        vet_appointment_animal.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder{
        TextView appointment_farmer_username,appointment_farmer_location,appointment_farmer_description,appointment_farmer_timeago;
        CircleImageView appointment_farmer_image;


        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);

            appointment_farmer_image=itemView.findViewById(R.id.appointment_farmer_image);
            appointment_farmer_timeago=itemView.findViewById(R.id.appointment_farmer_timeago);
            appointment_farmer_description=itemView.findViewById(R.id.appointment_farmer_decriptin);
            appointment_farmer_username=itemView.findViewById(R.id.appointment_farmer_username);
            appointment_farmer_location=itemView.findViewById(R.id.appointment_farmer_location);




        }


    }
}
