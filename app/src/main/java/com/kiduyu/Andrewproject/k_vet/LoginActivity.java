package com.kiduyu.Andrewproject.k_vet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import io.paperdb.Paper;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.Andrewproject.k_vet.Models.Farmer;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.rey.material.widget.CheckBox;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {
    private EditText phonenumber;
    private EditText pass;
    private CheckBox chkBoxRememberMe;
    private Button btnLogin;
    private ProgressDialog loadingBar;
    private TextView signUp,login_title;
    private String parentDbName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //transparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); //   in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        phonenumber=findViewById(R.id.number_login);
        pass=findViewById(R.id.password_login);
        btnLogin=findViewById(R.id.btn_login);
        signUp=findViewById(R.id.signup_txt);
        login_title=findViewById(R.id.login_title);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        //get passed data
        Intent intent = getIntent();

        final String id = intent.getStringExtra("choose");
        Log.d("TAG", "extra: " +id);

        if (id.equals("farmer")){
            btnLogin.setText("Farmer Login");
            login_title.setText("Farmer Login");
            parentDbName="Farmers";

        } else if (id.equals("vet")){
            btnLogin.setText("Veterinary Login");
            login_title.setText("Veterinary Login");
            parentDbName="Veterinary";
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id.equals("farmer")){
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("choose","farmer");
                    startActivity(intent);

                } else if (id.equals("vet")){
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("choose","vet");
                    startActivity(intent);
                }
               // startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

    }

    private void LoginUser() {
        String phone = phonenumber.getText().toString();
        String password = pass.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            phonenumber.setError("Phone Number Is Required..");
            FancyToast.makeText(this,"Phone Number Is Required..",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();

            return;
        }
        else if (TextUtils.isEmpty(password))
        {
            pass.setError("Password Is Required..");
            FancyToast.makeText(this,"Password Is Required..",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
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

    private void AllowAccessToAccount(final String phone, final String password) {
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
                                FancyToast.makeText(LoginActivity.this, "Welcome "+usersData.getName()+", you are logged in Successfully...", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, VetHomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Farmers"))
                            {
                                FancyToast.makeText(LoginActivity.this, "Welcome "+usersData.getName()+", you are logged in Successfully...", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
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
