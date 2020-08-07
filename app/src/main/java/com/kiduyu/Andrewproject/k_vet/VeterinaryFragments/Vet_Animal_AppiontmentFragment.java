package com.kiduyu.Andrewproject.k_vet.VeterinaryFragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.kiduyu.Andrewproject.k_vet.Models.Treatment;
import com.kiduyu.Andrewproject.k_vet.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Vet_Animal_AppiontmentFragment extends Fragment {
    private DatabaseReference mDatabase,mDatabase_treat,mDatabase_report,mDatabase_history;
    private  RecyclerView vet_appointment_animal;
    private ProgressDialog loadingBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vet_animal_appointment, container, false);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Appointments").child(Prevalent.currentOnlineUser.getName());
        mDatabase.keepSynced(true);
        vet_appointment_animal = view.findViewById(R.id.vet_appointment_animal);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        vet_appointment_animal.setLayoutManager(layoutManager);
        loadingBar= new ProgressDialog(getActivity());




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
            protected void onBindViewHolder(@NonNull final ApplicationViewHolder holder, int position, @NonNull final Appointments model) {
                String Image=model.getUser_image();
                if (Image.isEmpty()){
                    holder.appointment_farmer_image.setImageResource(R.drawable.profile);

                }else {
                    Picasso.get().load(Image).into(holder.appointment_farmer_image);
                }

                holder.appointment_farmer_username.setText(model.getAnimal_owner());
                holder.appointment_farmer_location.setText(model.getAppointment_Address());
                holder.appointment_farmer_description.setText("I would Like to make an Appointment With You On "+model.getAppiontment_date()+
                        "Regarding My Sick Animal that show the following System. My Phone Number Is "+model.getAnimal_owner_phone());

                Query query=mDatabase.child(model.getAnimal_name()).orderByChild("status");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.equals("Approved")) {
                            holder.appointment_farmer_reject.setVisibility(INVISIBLE);
                            holder.appointment_farmer_accept.setVisibility(INVISIBLE);
                            Toast.makeText(getActivity(), "done", Toast.LENGTH_LONG).show();


                        } else {
                            holder.appointment_farmer_reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDatabase.child(model.getAnimal_name()).child("status").setValue("Rejected").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            holder.appointment_farmer_reject.setVisibility(INVISIBLE);
                                            holder.appointment_farmer_accept.setVisibility(INVISIBLE);
                                            holder.appointment_farmer_dlayed.setText("Rejected");
                                            holder.appointment_farmer_dlayed.setBackgroundColor(getResources().getColor(R.color.red_300));

                                        }
                                    });
                                }
                            });

                            holder.appointment_farmer_accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDatabase.child(model.getAnimal_name()).child("status").setValue("Approved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            holder.appointment_farmer_reject.setVisibility(INVISIBLE);
                                            holder.appointment_farmer_accept.setVisibility(INVISIBLE);
                                            holder.appointment_farmer_dlayed.setText("Approved");
                                            holder.appointment_farmer_dlayed.setBackgroundColor(getResources().getColor(R.color.green));
                                            holder.appointment_farmer_treat.setVisibility(VISIBLE);

                                        }
                                    });
                                }
                            });


                            holder.appointment_farmer_dlayed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Dialog_Alert);

                                    LayoutInflater inflater=LayoutInflater.from(getContext());
                                    View myview=inflater.inflate(R.layout.delay,null);

                                    final AlertDialog dialog=mydialog.create();

                                    dialog.setView(myview);
                                    final EditText delay_input=myview.findViewById(R.id.txt_input_delay);
                                    Button button=myview.findViewById(R.id.btn_lay_save);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (TextUtils.isEmpty(delay_input.getText().toString())){
                                                delay_input.setError("required");
                                                return;
                                            }
                                            mDatabase.child(model.getAnimal_name()).child("status").setValue("Delayed Till "+delay_input.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            holder.appointment_farmer_reject.setVisibility(INVISIBLE);
                                                            holder.appointment_farmer_dlayed.setVisibility(INVISIBLE);
                                                            holder.appointment_farmer_reject.setText("Delayed Till "+delay_input.getText().toString());
                                                            holder.appointment_farmer_reject.setBackgroundColor(getResources().getColor(R.color.red_300));
                                                            holder.appointment_farmer_reject.setEnabled(false);
                                                            dialog.dismiss();

                                                        }
                                                    });

                                        }
                                    });




                                    dialog.show();
                                }
                            });
                            holder.appointment_farmer_treat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert);

                                    LayoutInflater inflater=LayoutInflater.from(getContext());
                                    View myview=inflater.inflate(R.layout.add_treated_animal,null);

                                    final AlertDialog dialog=mydialog.create();

                                    dialog.setView(myview);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    final EditText animal_name=myview.findViewById(R.id.edt_appointment_name);
                                    animal_name.setText(model.getAnimal_name());
                                    animal_name.setEnabled(false);
                                    final EditText farmer_phone=myview.findViewById(R.id.edt_phone);
                                    farmer_phone.setText(model.getAnimal_owner());
                                    farmer_phone.setEnabled(false);
                                    final EditText date_added=myview.findViewById(R.id.edt_date);
                                    final String date= DateFormat.getDateInstance().format(new Date());
                                    date_added.setText(date);
                                    date_added.setEnabled(false);
                                    final EditText vet_location=myview.findViewById(R.id.edt_vet_location);
                                    vet_location.setText(model.getAppointment_Address());
                                    vet_location.setEnabled(false);
                                    final EditText vet_remarks=myview.findViewById(R.id.edt_vet_remarks);
                                    Button btnSave=myview.findViewById(R.id.btn_save);


                                    btnSave.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final String mvet_remarks=vet_remarks.getText().toString().trim();

                                            if (TextUtils.isEmpty(mvet_remarks)){
                                                vet_remarks.setError("Remarks is A Required Field ..");
                                                vet_remarks.setHint("input Remarks");
                                                return;
                                            }
                                            loadingBar.setTitle("Treating "+model.getAnimal_name());
                                            loadingBar.setMessage("Please wait, ...");
                                            loadingBar.setCanceledOnTouchOutside(false);
                                            loadingBar.show();

                                            mDatabase_treat= FirebaseDatabase.getInstance().getReference().child("Treated_Animals");
                                            mDatabase_treat.keepSynced(true);


                                            mDatabase_treat.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (!(dataSnapshot.child(model.getAnimal_owner()).child(model.getAnimal_name()).exists())){

                                                        final Treatment animal= new Treatment(model.getAnimal_name(),model.getAnimal_owner()
                                                                ,Prevalent.currentOnlineUser.getName(),model.getAnimal_image(),date,mvet_remarks);

                                                        mDatabase_treat.child(model.getAnimal_owner()).child(model.getAnimal_name()).setValue(animal)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(getActivity(),"Treatment Done Successfully",Toast.LENGTH_SHORT).show();

                                                                        mDatabase_report=FirebaseDatabase.getInstance().getReference().child("Reports");
                                                                        mDatabase_treat.keepSynced(true);

                                                                        mDatabase_report.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                if (!(dataSnapshot.child(model.getAnimal_owner()).child(model.getAnimal_name()).exists()))
                                                                                {
                                                                                    HashMap<String, Object> userdataMap = new HashMap<>();
                                                                                    userdataMap.put("Animal_Name", model.getAnimal_name());
                                                                                    userdataMap.put("Remarks", mvet_remarks);
                                                                                    userdataMap.put("vet_Name", Prevalent.currentOnlineUser.getName());

                                                                                    mDatabase_report.child(model.getAnimal_owner()).child(model.getAnimal_name()).setValue(userdataMap)
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    Toast.makeText(getActivity(),"Report Done Successfully",Toast.LENGTH_SHORT).show();
                                                                                                    mDatabase_history=FirebaseDatabase.getInstance().getReference().child("Treatment_History").child(Prevalent.currentOnlineUser.getPhone());
                                                                                                    mDatabase_history.keepSynced(true);



                                                                                                    mDatabase_history.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                            if (!(dataSnapshot.child(model.getAnimal_name()).exists())){
                                                                                                                HashMap<String, Object> treathistory = new HashMap<>();
                                                                                                                treathistory.put("Animal_Name", model.getAnimal_name());
                                                                                                                treathistory.put("Remarks", mvet_remarks);
                                                                                                                treathistory.put("Date", date);

                                                                                                                mDatabase_history.child(model.getAnimal_name()).setValue(treathistory)
                                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                Toast.makeText(getActivity(),"Done",Toast.LENGTH_LONG).show();
                                                                                                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                                                                                                        new Vet_Treatment_HistoryFragment()).commit();
                                                                                                                                        loadingBar.dismiss();



                                                                                                                            }
                                                                                                                        });
                                                                                                            }

                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                        }
                                                                                                    });


                                                                                                    loadingBar.dismiss();
                                                                                                }
                                                                                            });

                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                                        loadingBar.dismiss();


                                                                    }
                                                                });

                                                    }else{
                                                        Toast.makeText(getActivity(),"Treatment Already Done",Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                        }
                                    });

                                    dialog.show();

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                }
            

            @NonNull
            @Override
            public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment_vet, parent, false);
                return new ApplicationViewHolder(view);
            }
        };
        vet_appointment_animal.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder{
        TextView appointment_farmer_username,appointment_farmer_location,appointment_farmer_description,appointment_farmer_timeago;
        CircleImageView appointment_farmer_image;
        Button appointment_farmer_accept, appointment_farmer_reject,appointment_farmer_treat,appointment_farmer_dlayed;


        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);

            appointment_farmer_image=itemView.findViewById(R.id.appointment_farmer_image);
            appointment_farmer_timeago=itemView.findViewById(R.id.appointment_farmer_timeago);
            appointment_farmer_description=itemView.findViewById(R.id.appointment_farmer_decriptin);
            appointment_farmer_username=itemView.findViewById(R.id.appointment_farmer_username);
            appointment_farmer_location=itemView.findViewById(R.id.appointment_farmer_location);
            appointment_farmer_dlayed=itemView.findViewById(R.id.appointment_farmer_dlayed);


            appointment_farmer_accept=itemView.findViewById(R.id.appointment_farmer_accept);
            appointment_farmer_reject=itemView.findViewById(R.id.appointment_farmer_reject);
            appointment_farmer_treat=itemView.findViewById(R.id.appointment_farmer_treat);




        }


    }
}
