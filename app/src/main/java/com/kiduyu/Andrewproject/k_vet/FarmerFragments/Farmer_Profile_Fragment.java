package com.kiduyu.Andrewproject.k_vet.FarmerFragments;

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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rey.material.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.kiduyu.Andrewproject.k_vet.R;

public class Farmer_Profile_Fragment extends Fragment  {
    private StorageReference mStorageRef;
    private Uri MImageURI;
    Button edt_save,edt_profile;
    CircleImageView profile_img;
    EditText Fullname,PhoneNumber,Email;
    private static final int PICK_IMAGE_REQUEST=1;
    private ProgressDialog loadingBar;
    private Spinner spinner1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=  inflater.inflate(R.layout.fragment_user_profile,container,false);
        Fullname = view.findViewById(R.id.edt_profile_fullname);
        mStorageRef= FirebaseStorage.getInstance().getReference().child("Profile");
        PhoneNumber =  view.findViewById(R.id.edt_profile_phone);
        Email =  view.findViewById(R.id.edt_profile_email);
        profile_img =  view.findViewById(R.id.edt_profile_image);
        profile_img.setEnabled(false);

        Fullname.setText(Prevalent.currentOnlineUser.getName());
        Fullname.setEnabled(false);
        Email.setText(Prevalent.currentOnlineUser.getAddress());
        Email.setEnabled(false);
        PhoneNumber.setText(Prevalent.currentOnlineUser.getPhone());
        PhoneNumber.setEnabled(false);

        if (Prevalent.currentOnlineUser.getImage().isEmpty()){
            profile_img.setImageResource(R.drawable.profile);
        }else {
            //Picasso.get().load(Prevalent.currentOnlineUser.getImage()).into(profile_img);
        }

        loadingBar= new ProgressDialog(getActivity());

        edt_profile=view.findViewById(R.id.edt_profile_edit);
        edt_save=view.findViewById(R.id.edt_profile_save);
        edt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_profile.setVisibility(View.INVISIBLE);
                edt_save.setVisibility(View.VISIBLE);
                Fullname.setText("");
                Fullname.setHint("Enter New Name");
                Fullname.setEnabled(true);
                PhoneNumber.setText("");
                PhoneNumber.setHint("Enter New Phone");
                PhoneNumber.setEnabled(true);
                Email.setText("");
                Email.setHint("Enter New Address");
                Email.setEnabled(true);
                profile_img.setEnabled(true);
            }
        });

        edt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mfullname=Fullname.getText().toString();
                String memail=Email.getText().toString();
                String mphone=PhoneNumber.getText().toString();

                if (TextUtils.isEmpty(mfullname))
                {
                    Fullname.setError("Name Is Required..");
                    return;
                }
                else if (TextUtils.isEmpty(memail))
                {
                    Email.setError("Email Is Required..");
                    return;
                }
                else if (TextUtils.isEmpty(mphone))
                {
                    PhoneNumber.setError("Phone number is Required");
                } else{
                    loadingBar.setTitle("Updating Profile");
                    loadingBar.setMessage("Please wait, ...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    AllowAccessToAccount(mfullname, memail,mphone);

                }
            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });


        return  view;
    }

    private void AllowAccessToAccount(final String mfullname, final String memail, final String mphone) {
        if (MImageURI!=null){
            final StorageReference filereference= mStorageRef.child(System.currentTimeMillis()
                    +"."+ getFileExtension(MImageURI));
            filereference.putFile(MImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final  Uri umageuri = uri;
                            final String imge= umageuri.toString();

                            final DatabaseReference RootRef;
                            RootRef = FirebaseDatabase.getInstance().getReference().child("Farmers").child(Prevalent.currentOnlineUser.getPhone());
                            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists())
                                    {
                                        HashMap<String, Object> userdataMap = new HashMap<>();
                                        userdataMap.put("name", mfullname);
                                        userdataMap.put("address", memail);
                                        userdataMap.put("phone", mphone);
                                        userdataMap.put("image", imge);

                                        RootRef.updateChildren(userdataMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getActivity(), "Congratulations " + mfullname + ", your profile was updated.", Toast.LENGTH_SHORT).show();
                                                            Prevalent.currentOnlineUser.setName(mfullname);

                                                            Prevalent.currentOnlineUser.setAddress(memail);

                                                            Prevalent.currentOnlineUser.setPhone(mphone);
                                                            profile_img.setEnabled(false);
                                                            Fullname.setEnabled(false);
                                                            Email.setEnabled(false);
                                                            PhoneNumber.setEnabled(false);
                                                            edt_profile.setVisibility(View.VISIBLE);
                                                            edt_save.setVisibility(View.INVISIBLE);

                                                            loadingBar.dismiss();
                                                        }else
                                                        {
                                                            loadingBar.dismiss();
                                                            Toast.makeText(getActivity(), "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
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
                    });

                }
            }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();

                }
            });
        }else{
            Toast.makeText(getActivity(),"no image Selected", Toast.LENGTH_LONG).show();
        }






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
            profile_img.setImageURI(MImageURI);

        }

    }
}
