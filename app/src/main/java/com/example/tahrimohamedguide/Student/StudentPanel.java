package com.example.tahrimohamedguide.Student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tahrimohamedguide.MainActivity;
import com.example.tahrimohamedguide.Models.Course;
import com.example.tahrimohamedguide.Models.Users;
import com.example.tahrimohamedguide.R;
import com.example.tahrimohamedguide.UserProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentPanel extends AppCompatActivity implements ModulAdapter.adminListener
, NavigationView.OnNavigationItemSelectedListener {
     private RecyclerView RCV ;
     private ArrayList<Course> CAL = new ArrayList<>();
     private ModulAdapter mAdapter ;
     private FirebaseDatabase ROOT = FirebaseDatabase.getInstance();
     private DatabaseReference courseRef = ROOT.getReference("modul");
     private String CurrentID , Department , Speciality ;
    private NavigationView navigationView ;
    private DrawerLayout drawerLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_panel);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Etudiant");

        CurrentID = getIntent().getStringExtra("CurrentID");
        Department = getIntent().getStringExtra("Department");
        init();
        getUserData ();
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView.LayoutManager LM = new GridLayoutManager(this,2);
        RCV.setLayoutManager(LM);
        RCV.setAdapter(mAdapter);
        fillModules();
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

    private void init (){
        RCV = findViewById(R.id.modulsRecyclerView);
        mAdapter = new ModulAdapter(this,CAL,this);
        navigationView = findViewById(R.id.navigationdrawerView);
        drawerLayout = findViewById(R.id.DrawerLayout);

    }


    private void  fillModules(){

        courseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if(!snapshot.exists()){
                    Toast.makeText(StudentPanel.this, "Nothing exist !", Toast.LENGTH_SHORT).show();
                }
                Course helper = snapshot.getValue(Course.class);
                if(helper.getSpeciality().equals(Speciality)){

                    CAL.add(helper);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onCourseClick(int position) {
      Intent intent = new Intent(StudentPanel.this,studentviewpdf.class);
      intent.putExtra("CurrentID",CurrentID);
      intent.putExtra("Modul",CAL.get(position).getName());
      startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_amphi:
                Intent intent = new Intent(StudentPanel.this, UserProfile.class);
                intent.putExtra("CurrentID",CurrentID);
                intent.putExtra("FromItSelf","true");
                startActivity(intent);
                break ;
            case R.id.menu_block:
                Intent intnet2 = new Intent(StudentPanel.this, MainActivity.class);
                intnet2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intnet2);
                break;



        }
        return true;
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
                    Speciality = helper.getSpeciality();
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