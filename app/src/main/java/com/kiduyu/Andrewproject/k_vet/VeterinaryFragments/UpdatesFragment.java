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
import com.kiduyu.Andrewproject.k_vet.Models.Office;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.kiduyu.Andrewproject.k_vet.Models.Updates;
import com.kiduyu.Andrewproject.k_vet.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class UpdatesFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase, mbook;
    private StorageReference mStorageRef;
    private ProgressDialog loadingBar;
    private Uri MImageURI;
    ImageView imageView;
    private static final int PICK_IMAGE_REQUEST=1;
    private FloatingActionButton floatingActionButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vet_updates, container, false);

        floatingActionButton= view.findViewById(R.id.fab_add);

        mStorageRef= FirebaseStorage.getInstance().getReference().child("Updates");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Updates");
        mbook= FirebaseDatabase.getInstance().getReference().child("myupdates");
        mDatabase.keepSynced(true);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUpdates();
            }
        });

        recyclerView=view.findViewById(R.id.vet_updates_recyclerview);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);






        return  view;

    }

    private void AddUpdates() {

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Dialog_Alert);

        LayoutInflater inflater=LayoutInflater.from(getContext());
        View myview=inflater.inflate(R.layout.item_update,null);

        final AlertDialog dialog=mydialog.create();


        dialog.setView(myview);

        final EditText title=myview.findViewById(R.id.vet_updates_title);
        final EditText content =myview.findViewById(R.id.vet_updates_content);
        final Button post=myview.findViewById(R.id.vet_updates_post);
        loadingBar = new ProgressDialog(getActivity());

        imageView = myview.findViewById(R.id.vet_updates_image);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mtitle=title.getText().toString().trim();
                final String mcontent=content.getText().toString().trim();
                final String mowner= Prevalent.currentOnlineUser.getName();
                final String date= DateFormat.getDateInstance().format(new Date());

                if (TextUtils.isEmpty(mtitle)){
                    title.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mcontent)){
                    content.setError("Required Field..");
                    return;
                }else {

                    if (MImageURI!=null) {

                        loadingBar.setTitle("Saving Post");
                        loadingBar.setMessage("Please wait, while we are Save the Details...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        final StorageReference filereference = mStorageRef.child(System.currentTimeMillis()
                                + "." + getFileExtension(MImageURI));
                        filereference.putFile(MImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final  Uri umageuri = uri;
                                        String imge= umageuri.toString();

                                        final Updates updates= new Updates(mtitle,mcontent,mowner,date,imge);

                                        final DatabaseReference RootRef;

                                        RootRef = mDatabase;

                                        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (!(dataSnapshot.child(mtitle).exists())){
                                                    RootRef.child(mtitle).setValue(updates);
                                                    Toast.makeText(getActivity(),"Updates Added Successfully",Toast.LENGTH_SHORT).show();

                                                    final DatabaseReference RootRef1;
                                                    RootRef1 = mbook;

                                                    RootRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            RootRef1.child(Prevalent.currentOnlineUser.getPhone()).child(mtitle).setValue(updates);

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });



                                                    loadingBar.dismiss();
                                                }else{
                                                    Toast.makeText(getActivity(),"Updates Already On the List",Toast.LENGTH_LONG).show();


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

                    }else {
                        Toast.makeText(getActivity(),"no imgr",Toast.LENGTH_LONG).show();


                    }

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
            imageView.setImageURI(MImageURI);


        }

    }

    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Updates> option = new FirebaseRecyclerOptions.Builder<Updates>()
                .setQuery(mbook.child(Prevalent.currentOnlineUser.getPhone()),Updates.class)
                .build();


        FirebaseRecyclerAdapter<Updates, AnimalViewHolder> adapter= new FirebaseRecyclerAdapter<Updates, AnimalViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final AnimalViewHolder holder, int position, @NonNull final Updates model) {
                holder.name.setText(model.getTitle());
                holder.breed.setText(model.getDate());
                holder.description.setText(model.getContent());
                String ImageView= model.getImage().toString();
                //Picasso.get().load(ImageView).into(holder.animal_image);
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
