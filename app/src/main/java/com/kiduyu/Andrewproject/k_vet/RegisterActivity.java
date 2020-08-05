package com.kiduyu.Andrewproject.k_vet;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.Andrewproject.k_vet.Models.Farmer;
import com.kiduyu.Andrewproject.k_vet.Models.Veterinary;
import com.shashank.sony.fancytoastlib.FancyToast;

public class RegisterActivity extends AppCompatActivity {
    private EditText Fullname ,PhoneNumber, Password,password_confirm;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView reg_title,signintxt;
    private String parentDbName = "Farmers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//transparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); //   in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        LoginButton = findViewById(R.id.btn_reg);
        Fullname = findViewById(R.id.fullname);
        PhoneNumber =  findViewById(R.id.phone_number);
        password_confirm =  findViewById(R.id.password_confm);
        Password =findViewById(R.id.password_reg);
        reg_title=findViewById(R.id.reg_title);
        signintxt=findViewById(R.id.signin_txt);

        loadingBar= new ProgressDialog(this);

        //get passed data
        Intent intent = getIntent();

        final String id = intent.getStringExtra("choose");
        Log.d("TAG", "extra: " +id);

        if (id.equals("farmer")){
            LoginButton.setText("Farmer Register");
            reg_title.setText("Farmer Registration");
            parentDbName="Farmers";

        } else if (id.equals("vet")){
            LoginButton.setText("Veterinary Register");
            reg_title.setText("Veterinary Registration");
            parentDbName="Veterinary";
        }
        signintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id.equals("farmer")){
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("choose","farmer");
                    startActivity(intent);

                } else if (id.equals("vet")){
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("choose","vet");
                    startActivity(intent);
                }
                //startActivity(new Intent(getApplicationContext(),LoginActivity.class));
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

    private void CreateAccount() {
        String name = Fullname.getText().toString();
        String phone = PhoneNumber.getText().toString();
        String password = Password.getText().toString();
        String password2 = password_confirm.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            FancyToast.makeText(this,"Name Is Required..",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
            return;
        }
        else if (TextUtils.isEmpty(phone))
        {
            PhoneNumber.setError("Phone Number Is Required..");
            FancyToast.makeText(this,"Phone Number Is Required..",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
            return;
        }
        else if (TextUtils.isEmpty(password))
        {
            Password.setError("Password Is Required..");
            FancyToast.makeText(this,"Password Is Required..",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
            return;
        }
        else if (!password.equals(password2))
        {
            password_confirm.setError("Both Passwords Don't Match..");
            FancyToast.makeText(this,"Both Passwords Don't Match..",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
            return;
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            FancyToast.makeText(this,"Data Received",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
            ValidatephoneNumber(name, phone, password);

        }
    }

    private void ValidatephoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (parentDbName.equals("Veterinary")){
                    if (!(dataSnapshot.child("Veterinary").child(phone).exists()))
                    {
                        Veterinary veterinary = new Veterinary(name,phone,password,"image.jpg","");

                        RootRef.child("Veterinary").child(phone).setValue(veterinary)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            FancyToast.makeText(RegisterActivity.this, "Congratulations "+name+", your account has been created.", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                            loadingBar.dismiss();

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            intent.putExtra("choose","vet");
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            loadingBar.dismiss();
                                            //Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                            FancyToast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        //Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                        FancyToast.makeText(RegisterActivity.this, "This " + phone + " already exists.", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();

                        loadingBar.dismiss();
                        FancyToast.makeText(RegisterActivity.this, "Please try again using another phone number.", FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                        //Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

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
                                            FancyToast.makeText(RegisterActivity.this, "Congratulations "+name+", your account has been created.", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                            loadingBar.dismiss();

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            intent.putExtra("choose","farmer");
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            loadingBar.dismiss();
                                            FancyToast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        //Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                        FancyToast.makeText(RegisterActivity.this, "This " + phone + " already exists.", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();

                        loadingBar.dismiss();
                        FancyToast.makeText(RegisterActivity.this, "Please try again using another phone number.", FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                        //Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

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
