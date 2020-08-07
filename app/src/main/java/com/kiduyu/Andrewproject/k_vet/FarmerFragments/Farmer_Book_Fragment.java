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
import com.kiduyu.Andrewproject.k_vet.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Farmer_Book_Fragment extends Fragment {
    private TextInputEditText finddetails;
    private TextInputEditText edt_animal_name, edt_animal_owner;
    private TextInputEditText edt_animal_description;
    private static TextInputEditText edt_booking_date;
    private TextInputEditText edt_book_address;
    private TextInputEditText edt_book_veterinary;
    private Button edt_btn_save,booking_findanimal;
    private ImageView edt_animal_image;
    private TextView Booking_head_title;
    private DatabaseReference mDatabase,mDatabase_book,mDatabase_mybook;
    private ProgressDialog loadingBar;
    private  String VetName="";
    private  String medt_animal_image="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_book,container,false);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Animals");
        mDatabase_book= FirebaseDatabase.getInstance().getReference().child("Appointments");
        mDatabase_mybook= FirebaseDatabase.getInstance().getReference().child("mybookings");


        finddetails=view.findViewById(R.id.finddetails);
        Booking_head_title=view.findViewById(R.id.Booking_head_title);

        edt_animal_name=view.findViewById(R.id.edt_animal_name);
        edt_animal_name.setEnabled(false);

        edt_animal_owner=view.findViewById(R.id.edt_animal_owner);
        edt_animal_owner.setEnabled(false);

        edt_animal_description=view.findViewById(R.id.edt_animal_description);
        edt_animal_description.setEnabled(false);

        edt_booking_date=view.findViewById(R.id.edt_booking_date);



        edt_booking_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newfragment = new SelectDateFragment();
                newfragment.show(getFragmentManager(),"DatePicker");

  }
        });
        edt_book_address=view.findViewById(R.id.edt_book_address);
        edt_book_veterinary=view.findViewById(R.id.edt_book_veterinary);
        edt_book_veterinary.setEnabled(false);
        loadingBar = new ProgressDialog(getActivity());

        Bundle bundle=this.getArguments();
        if (bundle!=null){
            String vetName=bundle.getString("vetName");
            edt_book_veterinary.setText(vetName);
            VetName=vetName;
        }


        edt_animal_image=view.findViewById(R.id.edt_animal_image);

        booking_findanimal=view.findViewById(R.id.booking_findanimal);

        edt_btn_save=view.findViewById(R.id.edt_btn_save);
        booking_findanimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mdetails = finddetails.getText().toString();
                if (TextUtils.isEmpty(mdetails)) {
                    finddetails.setError("Input is Required");
                    return;
                }else{
                loadingBar.setTitle("Finding Animal");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                final DatabaseReference RootRef;

                RootRef = mDatabase;

                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(Prevalent.currentOnlineUser.getPhone()).child(mdetails).exists()) {
                            Animal animal = dataSnapshot.child(Prevalent.currentOnlineUser.getPhone()).child(mdetails).getValue(Animal.class);

                            String animal_name = animal.getName();
                            edt_animal_name.setText(animal_name);

                            String animal_description = animal.getDescription();

                            edt_animal_description.setText(animal_description);

                            String ImageView= animal.getImage().toString();
                            Picasso.get().load(ImageView).into(edt_animal_image);

                            String medt_animal_owner = animal.getOwner();
                            edt_animal_owner.setText(medt_animal_owner);

                            String medt_animal_image = animal.getImage();




                            loadingBar.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "No Animal Found Please Try Again", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            }
        });


        edt_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Mbooking_date=edt_booking_date.getText().toString();
                final String Mbooking_address=edt_book_address.getText().toString();
                final String Mbooking_descrption=edt_animal_description.getText().toString();
                final String Mbooking_name=edt_animal_name.getText().toString();
                final String Medt_animal_owner=edt_animal_owner.getText().toString();
                final String Medt_animal_owner_phone=Prevalent.currentOnlineUser.getPhone();
                final String mVetName =VetName;
                final String animal_image=medt_animal_image;

                if (TextUtils.isEmpty(Mbooking_name)&&TextUtils.isEmpty(Medt_animal_owner) && TextUtils.isEmpty(Mbooking_descrption)){
                    Toast.makeText(getActivity(), "Find An Animal First", Toast.LENGTH_LONG).show();
                    return;

                }else if (TextUtils.isEmpty(Mbooking_date)){
                    edt_booking_date.setError("Date Is Required");
                }else if (TextUtils.isEmpty(Mbooking_address)){
                    edt_book_address.setError("Address Is Required");
                }else{

                    loadingBar.setTitle("Booking Appointment");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    final DatabaseReference RootRef_book;

                    RootRef_book = mDatabase_book.child(mVetName);

                    RootRef_book.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(Mbooking_name).exists())
                            {
                                Toast.makeText(getActivity(), "The Appointment With ."+mVetName+"Is Already Pending Approval ", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();

                            }else{
                               String muser_image= Prevalent.currentOnlineUser.getImage();
                                final Appointments appointments = new Appointments(Mbooking_name,Medt_animal_owner,Medt_animal_owner_phone,Mbooking_descrption,Mbooking_date,Mbooking_address,muser_image,animal_image,"Pending");

                                RootRef_book.child(Mbooking_name).setValue(appointments).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(getActivity(), "The Appointment With ."+mVetName+"was submitted", Toast.LENGTH_SHORT).show();
                                            final DatabaseReference RootRef_mybook;

                                            RootRef_mybook = mDatabase_mybook.child(Prevalent.currentOnlineUser.getPhone());
                                            RootRef_mybook.setValue(appointments);

                                            loadingBar.dismiss();
                                        }

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });




        return  view;

    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy= calendar.get(Calendar.YEAR);
            int mm=calendar.get(Calendar.MONTH);
            int dd=calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(),this,yy,mm,dd);
        }

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edt_booking_date.setText(month+"/"+dayOfMonth+"/"+year);

        }


    }
}
