package com.example.tahrimohamedguide.ChefDepartement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tahrimohamedguide.Admin.addChefD;
import com.example.tahrimohamedguide.MainActivity;
import com.example.tahrimohamedguide.Models.Users;
import com.example.tahrimohamedguide.R;
import com.example.tahrimohamedguide.UserProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChefDPanel extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   private  CardView Student ,Teacher,Modul,SPec ;
   private String CurrentID,Department;
    private NavigationView navigationView ;
    private DrawerLayout drawerLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_dpanel);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CurrentID = getIntent().getStringExtra("CurrentID");
        Department = getIntent().getStringExtra("Department");
        init();
        getUserData();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chef De Departement");
        navigationView.setNavigationItemSelectedListener(this);
        Teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(ChefDPanel.this, TeacherList.class);
                intent.putExtra("CurrentID",CurrentID);
                intent.putExtra("Department",Department);
                intent.putExtra("FromChefD","true");
                startActivity(intent);
            }
        });
        Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(ChefDPanel.this, StudentList.class);
                intent.putExtra("CurrentID",CurrentID);
                intent.putExtra("Department",Department);
                intent.putExtra("FromChefDS","true");
                startActivity(intent);
            }
        });
        Modul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(ChefDPanel.this, ModulsList.class);
                intent.putExtra("CurrentID",CurrentID);
                intent.putExtra("Department",Department);

                startActivity(intent);
            }
        });
        SPec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(ChefDPanel.this, SpecList.class);
                intent.putExtra("CurrentID",CurrentID);
                intent.putExtra("Department",Department);

                startActivity(intent);
            }
        });
        //DRAWER HANDLE
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull @NotNull View drawerView) {



            }

            @Override
            public void onDrawerClosed(@NonNull @NotNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        //DRAWER HANDLE END

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if(!drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.openDrawer(GravityCompat.START);

                }else {
                    drawerLayout.closeDrawer(GravityCompat.START);

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }
    private void init(){
        Student=findViewById(R.id.Student);
        Teacher=findViewById(R.id.Teacher);
        Modul=findViewById(R.id.Modul);
        SPec=findViewById(R.id.Spec);
        navigationView = findViewById(R.id.navigationdrawerView);
        drawerLayout = findViewById(R.id.DrawerLayout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_amphi:
                Intent intent = new Intent(ChefDPanel.this, UserProfile.class);
                intent.putExtra("CurrentID",CurrentID);
                intent.putExtra("FromItSelf","true");
                startActivity(intent);
                break ;
            case R.id.menu_block:
                Intent intnet2 = new Intent(ChefDPanel.this, MainActivity.class);
                intnet2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intnet2);
                break;



        }
        return true;
    }
    public void getUserData (){

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.child(CurrentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    TextView name =  navigationView.getHeaderView(0).findViewById(R.id.DrawerName);
                    TextView email =  navigationView.getHeaderView(0).findViewById(R.id.DrawerEmail);
                    CircleImageView profile =  navigationView.getHeaderView(0).findViewById(R.id.DrawerProfilePic);
                    Users helper = snapshot.getValue(Users.class);
                    name.setText("Name : "+helper.getUser_name());
                    email.setText("Email : "+helper.getUser_email());
                    if(helper.getImageid()!=null){
                        Picasso.get().load(helper.getImageid()).into(profile);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}