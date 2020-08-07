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
import io.paperdb.Paper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.Andrewproject.k_vet.Models.Farmer;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.rey.material.widget.CheckBox;

public class LoginActivity extends AppCompatActivity {
    private EditText phonenumber;
    private EditText pass;
    private CheckBox chkBoxRememberMe;
    private Button btnLogin;
    private ProgressDialog loadingBar;
    private TextView signUp,login_title,customer_txt,vet_txt;
    private String parentDbName = "Farmers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        phonenumber=findViewById(R.id.number_login);
        pass=findViewById(R.id.password_login);
        btnLogin=findViewById(R.id.btn_login);
        signUp=findViewById(R.id.signup_txt);
        login_title=findViewById(R.id.login_title);
        customer_txt=findViewById(R.id.customer_txt);
        vet_txt=findViewById(R.id.vet_txt);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        Paper.init(this);


        vet_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setText("Veterinary Login");
                login_title.setText("Veterinary Login");
                parentDbName="Veterinary";
                customer_txt.setVisibility(View.VISIBLE);
                vet_txt.setVisibility(View.INVISIBLE);
            }
        });

        customer_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setText("Farmer Login");
                login_title.setText("Farmer Login");
                parentDbName="Farmers";
                vet_txt.setVisibility(View.VISIBLE);
                customer_txt.setVisibility(View.INVISIBLE);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }

    private void LoginUser()
    {
        String phone = phonenumber.getText().toString();
        String password = pass.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            phonenumber.setError("Phone Number Is Required..");
            return;
        }
        else if (TextUtils.isEmpty(password))
        {
            pass.setError("Password Is Required..");
            return;
        }
        else
        {
            loadingBar.setTitle("Logging Into The VetFinder App");
            loadingBar.setMessage("Please wait, while we are checking the credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);


        }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Farmer usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Farmer.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Veterinary"))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome Vet, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, VetHomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Farmers"))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome , you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
