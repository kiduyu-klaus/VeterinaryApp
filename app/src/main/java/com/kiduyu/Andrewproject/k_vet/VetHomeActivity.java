package com.kiduyu.Andrewproject.k_vet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.kiduyu.Andrewproject.k_vet.VeterinaryFragments.UpdatesFragment;
import com.kiduyu.Andrewproject.k_vet.VeterinaryFragments.Vet_Animal_AppiontmentFragment;
import com.kiduyu.Andrewproject.k_vet.VeterinaryFragments.Vet_HomeFragment;
import com.kiduyu.Andrewproject.k_vet.VeterinaryFragments.Vet_My_OfficeFragment;
import com.kiduyu.Andrewproject.k_vet.VeterinaryFragments.Vet_Treatment_HistoryFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class VetHomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_home);
        Toolbar toolbar=findViewById(R.id.vet_toolbar);
        setSupportActionBar(toolbar);

        drawer= findViewById(R.id.vet_drawer_layout);
        NavigationView navigationView= findViewById(R.id.vet_nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView user= headerView.findViewById(R.id.nav_header_fullname);
        TextView phone= headerView.findViewById(R.id.nav_header_email);
        CircleImageView profile_img= headerView.findViewById(R.id.user_profile_image);

        if (Prevalent.currentOnlineUser.getName().isEmpty()){
            user.setText("Username");
        }else{
            user.setText(Prevalent.currentOnlineUser.getName());
        }

        if (Prevalent.currentOnlineUser.getPhone().isEmpty()){
            phone.setText("phone");

        }else{
            phone.setText(Prevalent.currentOnlineUser.getPhone());
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                switch (Item.getItemId()) {
                    case R.id.vet_nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Vet_HomeFragment()).commit();

                        break;
                    case R.id.vet_nav_animal:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Vet_Animal_AppiontmentFragment()).commit();

                        break;

                    case R.id.vet_nav_vet2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Vet_Treatment_HistoryFragment()).commit();

                        break;

                    case R.id.updates:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new UpdatesFragment()).commit();

                        break;

                    case R.id.office:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Vet_My_OfficeFragment()).commit();

                        break;

                    case R.id.vet_nav_signout:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SignoutFragment()).commit();

                        break;

                    case R.id.vet_nav_share:

                        Toast.makeText(VetHomeActivity.this, "Share this app", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.vet_nav_send:

                        Toast.makeText(VetHomeActivity.this, "Send this app", Toast.LENGTH_SHORT).show();
                        break;

                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState== null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Vet_HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.vet_nav_home);}
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }}
}
