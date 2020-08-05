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

import com.google.android.material.navigation.NavigationView;
import com.kiduyu.Andrewproject.k_vet.Farmer.Fragments.Farmer_Home_Fragment;
import com.kiduyu.Andrewproject.k_vet.Models.Prevalent;
import com.shashank.sony.fancytoastlib.FancyToast;

public class UserHomeActivity extends AppCompatActivity {
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer= findViewById(R.id.drawer_layout);
        NavigationView navigationView= findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView user= headerView.findViewById(R.id.nav_header_name);
        TextView phone= headerView.findViewById(R.id.nav_header_phone);

        user.setText(Prevalent.currentOnlineUser.getName());
        phone.setText(Prevalent.currentOnlineUser.getPhone());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                switch (Item.getItemId()) {
                    case R.id.user_nav_home:
                      /*  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Farmer_Home_Fragment()).commit();

                       */

                        break;
                    case R.id.user_nav_animal:
                     //   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                       //         new My_Animals_Fragment()).commit();

                        break;
                    case R.id.user_nav_updates:
                     //   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                       //         new Farmer_UpdatesFragment()).commit();

                        break;

                    case R.id.user_nav_vet:
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                         //       new Find_Vet_Fragment()).commit();

                        break;

                    case R.id.user_nav_book:
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                           //     new Farmer_MyBookings_Fragment()).commit();

                        break;

                    case R.id.user_nav_vet2:
                       // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                          //      new History_Fragment()).commit();

                        break;

                    case R.id.user_nav_profile:
                      //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                           //     new Farmer_Profile_Fragment()).commit();

                        break;

                    case R.id.customer_nav_signout:
                       // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                         //       new SignoutFragment()).commit();

                        break;

                    case R.id.customer_nav_share:

                        FancyToast.makeText(UserHomeActivity.this, "Share this app", FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                        break;

                    case R.id.customer_nav_send:

                       // Toast.makeText(UserHomeActivity.this, "Send this app", Toast.LENGTH_SHORT).show();
                        FancyToast.makeText(UserHomeActivity.this, "Send this app", FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();

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
                    new Farmer_Home_Fragment()).commit();
            navigationView.setCheckedItem(R.id.vet_nav_home);}


    }
}
