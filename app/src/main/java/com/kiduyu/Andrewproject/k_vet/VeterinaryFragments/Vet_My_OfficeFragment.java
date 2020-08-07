package com.kiduyu.Andrewproject.k_vet.VeterinaryFragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.kiduyu.Andrewproject.k_vet.MapsActivity;
import com.kiduyu.Andrewproject.k_vet.Models.Office;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.kiduyu.Andrewproject.k_vet.R;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class Vet_My_OfficeFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST=1;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase, mbook;
    private StorageReference mStorageRef;
    private Uri MImageURI;
    private ProgressDialog loadingBar;
    private Button locate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vet_my_office, container, false);

        FloatingActionButton fab_add= view.findViewById(R.id.fab_add);
        loadingBar = new ProgressDialog(getActivity());

        mStorageRef= FirebaseStorage.getInstance().getReference().child("Offices");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Offices");
        mDatabase.keepSynced(true);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();

            }
        });
        recyclerView=view.findViewById(R.id.vet_office);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);








        return  view;

    }

    public void customDialog(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Dialog_Alert);

        LayoutInflater inflater=LayoutInflater.from(getContext());
        View myview=inflater.inflate(R.layout.add_office,null);

        final AlertDialog dialog=mydialog.create();

        dialog.setView(myview);

        final EditText name=myview.findViewById(R.id.edt_name);
        final EditText description=myview.findViewById(R.id.edt_description);
        final Button image=myview.findViewById(R.id.edt_image);
        final Button btnSave=myview.findViewById(R.id.btn_save);
       final Spinner spinner1 =myview.findViewById(R.id.edt_location);




        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });






                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    String firstItem = String.valueOf(spinner1.getSelectedItem());
                    @Override
                    public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {
                        if (firstItem.equals(String.valueOf(spinner1.getSelectedItem()))) {

                            // ToDo when first item is selected
                            Toast.makeText(parent.getContext(),

                                    "You have selected No Location or LandMark Near Your Office",

                                    Toast.LENGTH_LONG).show();


                        } else {

                            btnSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String mname=name.getText().toString().trim();

                                    final String mlocation= parent.getItemAtPosition(position).toString();
                                    final String mdescription=description.getText().toString().trim();
                                    final String mowner= Prevalent.currentOnlineUser.getName();


                                    if (TextUtils.isEmpty(mname)){
                                        name.setError("Required Field..");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(mdescription)){
                                        description.setError("Required Field..");
                                        return;
                                    }

                                    loadingBar.setTitle("Doing Some Magic");
                                    loadingBar.setMessage("Please wait, while we are Save the Details...");
                                    loadingBar.setCanceledOnTouchOutside(false);
                                    loadingBar.show();

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
                                                        final Office office= new Office(mname,mowner,Prevalent.currentOnlineUser.getPhone(),mdescription,mlocation,imge);

                                                        final DatabaseReference RootRef;

                                                        RootRef = mDatabase;

                                                        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (!(dataSnapshot.child(mname).exists())){
                                                                    RootRef.child(mname).setValue(office);
                                                                    Toast.makeText(getActivity(),"Animal Added Successfully",Toast.LENGTH_SHORT).show();


                                                                    dialog.dismiss();
                                                                    loadingBar.dismiss();
                                                                }else{
                                                                    Toast.makeText(getActivity(),"Animal Already On the List",Toast.LENGTH_LONG).show();

                                                                    dialog.dismiss();
                                                                    loadingBar.dismiss();
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


                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

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

    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Office> option = new FirebaseRecyclerOptions.Builder<Office>()
                .setQuery(mDatabase,Office.class)
                .build();


        FirebaseRecyclerAdapter<Office, AnimalViewHolder> adapter= new FirebaseRecyclerAdapter<Office, AnimalViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final AnimalViewHolder holder, int position, @NonNull final Office model) {
                holder.name.setText(model.getOffice_name());
                holder.location.setText(model.getOffice_location());
                holder.description.setText(model.getOffice_description());
                String ImageView= model.getImage().toString();
//                Picasso.get().load(ImageView).into(holder.office_image);
            }

            @NonNull
            @Override
            public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vet_office_nert, parent, false);
                return new AnimalViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder{
        TextView name,location,description;
        ImageView office_image ,book;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.fetch_officename);
            location=itemView.findViewById(R.id.fetch_offficelocation);
            description=itemView.findViewById(R.id.fetch_officedescription);
            office_image=itemView.findViewById(R.id.fetch_officeimage);



        }
    }
}
