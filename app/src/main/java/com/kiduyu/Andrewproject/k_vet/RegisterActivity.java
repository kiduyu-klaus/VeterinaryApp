package com.kiduyu.Andrewproject.k_vet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.Andrewproject.k_vet.Models.Farmer;
import com.kiduyu.Andrewproject.k_vet.Models.Veterinary;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText Fullname ,PhoneNumber, Password,password_confirm;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView reg_title,signintxt, vettext,customertxt;
    private String parentDbName = "Farmers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        LoginButton = findViewById(R.id.btn_reg);
        Fullname = findViewById(R.id.fullname);
        PhoneNumber =  findViewById(R.id.phone_number);
        password_confirm =  findViewById(R.id.password_confm);
        Password =findViewById(R.id.password_reg);
        vettext=findViewById(R.id.vendor_txt);
        customertxt=findViewById(R.id.customer_txt);
        reg_title=findViewById(R.id.reg_title);
        signintxt=findViewById(R.id.signin_txt);
        loadingBar= new ProgressDialog(this);

        vettext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Veterinary Register");
                reg_title.setText("Veterinary Registration");
                parentDbName="Veterinary";
                customertxt.setVisibility(View.VISIBLE);
                vettext.setVisibility(View.INVISIBLE);
            }
        });

        customertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Farmer Register");
                reg_title.setText("Farmer Registration");
                parentDbName="Farmers";
                vettext.setVisibility(View.VISIBLE);
                customertxt.setVisibility(View.INVISIBLE);
            }
        });

        signintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateAccount();
            }
        });

    }

    private void CreateAccount()
    {
        String name = Fullname.getText().toString();
        String phone = PhoneNumber.getText().toString();
        String password = Password.getText().toString();
        String password2 = password_confirm.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Fullname.setError("Name Is Required..");
            return;
        }
        else if (TextUtils.isEmpty(phone))
        {
            PhoneNumber.setError("Phone Number Is Required..");
            return;
        }
        else if (TextUtils.isEmpty(password))
        {
            Password.setError("Password Is Required..");
            return;
        }
        else if (!password.equals(password2))
        {
            password_confirm.setError("Both Passwords Don't Match..");
            return;
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidatephoneNumber(name, phone, password);

        }


    }private void ValidatephoneNumber(final String name, final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (parentDbName.equals("Veterinary")){
                    if (!(dataSnapshot.child("Veterinary").child(phone).exists()))
                    {
                        Farmer veterinary = new Farmer(name,phone,password,"image.jpg","");

                        RootRef.child("Veterinary").child(phone).setValue(veterinary)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(RegisterActivity.this, "Congratulations Veterinary, your account has been created.", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }else if (parentDbName.equals("Farmers")){

                    if (!(dataSnapshot.child("Farmers").child(phone).exists()))
                    {

                        Farmer userdataMap = new Farmer(name,phone,password,"","");

                        RootRef.child("Farmers").child(phone).setValue(userdataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(RegisterActivity.this, "Congratulations Farmer, your account has been created.", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
