package com.kiduyu.Andrewproject.k_vet.FarmerFragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kiduyu.Andrewproject.k_vet.Models.Animal;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.kiduyu.Andrewproject.k_vet.R;
import com.squareup.picasso.Picasso;


import static android.app.Activity.RESULT_OK;

public class My_Animals_Fragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST=1;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase, mbook;
    private StorageReference mStorageRef;
    private Uri MImageURI;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_animal,container,false);

        FloatingActionButton fab_add= view.findViewById(R.id.fab_add);

        mStorageRef= FirebaseStorage.getInstance().getReference().child("Animals");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Animals");
        mbook= FirebaseDatabase.getInstance().getReference().child("Bookings");
        mDatabase.keepSynced(true);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();

            }
        });
        recyclerView=view.findViewById(R.id.farmer_animals);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        return view;

    }

    public void customDialog(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Dialog_Alert);

        LayoutInflater inflater=LayoutInflater.from(getContext());
        View myview=inflater.inflate(R.layout.add_animal,null);

        final AlertDialog dialog=mydialog.create();

        dialog.setView(myview);

        final EditText name=myview.findViewById(R.id.edt_name);
        final EditText breed=myview.findViewById(R.id.edt_breed);
        final EditText description=myview.findViewById(R.id.edt_description);
        final Button image=myview.findViewById(R.id.edt_image);
        Button btnSave=myview.findViewById(R.id.btn_save);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String mname=name.getText().toString().trim();
                final String mbreed=breed.getText().toString().trim();
                final String mdescription=description.getText().toString().trim();



                if (TextUtils.isEmpty(mname)){
                    name.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mbreed)){
                    breed.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mdescription)){
                    description.setError("Required Field..");
                    return;
                }
                if (MImageURI!=null){
                    final StorageReference filereference= mStorageRef.child(System.currentTimeMillis()
                    +"."+ getFileExtension(MImageURI));
                    filereference.putFile(MImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(),"image Uploaded",Toast.LENGTH_LONG).show();
                            filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final  Uri umageuri = uri;
                                    String imge= umageuri.toString();
                                    String Owner=Prevalent.currentOnlineUser.getName();
                                    final Animal animal= new Animal(mname,mbreed,mdescription,Owner,imge);

                                    final DatabaseReference RootRef;

                                    RootRef = mDatabase;

                                    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!(dataSnapshot.child(Prevalent.currentOnlineUser.getPhone()).child(mname).exists())){
                                                RootRef.child(Prevalent.currentOnlineUser.getPhone()).child(mname).setValue(animal);
                                                Toast.makeText(getActivity(),"Animal Added Successfully",Toast.LENGTH_SHORT).show();

                                                dialog.dismiss();
                                            }else{
                                                Toast.makeText(getActivity(),"Animal Already On the List",Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    });

                }else{
                    Toast.makeText(getActivity(),"no image Selected", Toast.LENGTH_LONG).show();
                }









            }
        });


        dialog.show();

    }


    private String getFileExtension(Uri uri){
        ContentResolver cr=getActivity().getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void OpenFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK &&
        data!= null && data.getData()!=null){
            MImageURI= data.getData();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Animal> option = new FirebaseRecyclerOptions.Builder<Animal>()
                .setQuery(mDatabase.child(Prevalent.currentOnlineUser.getPhone()),Animal.class)
                .build();


        FirebaseRecyclerAdapter<Animal, AnimalViewHolder> adapter= new FirebaseRecyclerAdapter<Animal, AnimalViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final AnimalViewHolder holder, int position, @NonNull final Animal model) {
                holder.name.setText(model.getName());
                holder.breed.setText(model.getBreed());
                holder.description.setText(model.getDescription());
                String ImageView= model.getImage().toString();
                //Picasso.get().load(ImageView).into(holder.animal_image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BookAnimal();

                    }
                });
            }

            @NonNull
            @Override
            public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal, parent, false);
                return new AnimalViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void BookAnimal() {

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Dialog_Alert);

        LayoutInflater inflater=LayoutInflater.from(getContext());
        View myview=inflater.inflate(R.layout.add_appointment,null);

        final AlertDialog dialog=mydialog.create();

        dialog.setView(myview);
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder{
        TextView name,breed,description;
        ImageView animal_image ;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.animal_name);
            breed=itemView.findViewById(R.id.animal_breed);
            description=itemView.findViewById(R.id.animal_description);
            animal_image=itemView.findViewById(R.id.animal_image);




        }
    }

    }

