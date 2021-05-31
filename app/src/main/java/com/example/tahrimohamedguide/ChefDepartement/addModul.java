package com.example.tahrimohamedguide.ChefDepartement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.materialspinner.MaterialSpinner;
import com.example.tahrimohamedguide.Admin.addChefD;
import com.example.tahrimohamedguide.Admin.adminPanel;
import com.example.tahrimohamedguide.Models.Course;
import com.example.tahrimohamedguide.Models.Users;
import com.example.tahrimohamedguide.Models.specialty;
import com.example.tahrimohamedguide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class addModul extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private EditText user_name;
    private MaterialSpinner user_birth;
    private String SName, SBirth,CurrentID,Department;
    private Button OK, Cancel;
    private FirebaseDatabase ROOT = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = ROOT.getReference("users");
    private DatabaseReference modulRef = ROOT.getReference("modul");
    private NavigationView navigationView ;
    private DrawerLayout drawerLayout ;
    private ArrayList<String> MAL = new ArrayList<>();
    private ArrayList<String> IAL = new ArrayList<>();
    private ArrayAdapter<String> mAdapter ;
    private MaterialSpinner Anne ;
    private ArrayList<String> years = new ArrayList<>();
    private ArrayAdapter<String> yearAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modul);
        CurrentID = getIntent().getStringExtra("CurrentID");
        Department = getIntent().getStringExtra("Department");

        init();
        Anne.setLabel("speciality");
        yearAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,years);
        Anne.setAdapter(yearAdapter);
        fillSpecSpinner();
           navigationView.setNavigationItemSelectedListener(this);
        user_birth.setLabel("Teacher");
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,MAL);
        fillSpinner ();
        user_birth.setAdapter(mAdapter);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ajouter un Modul");
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChefD();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(addModul.this, ModulsList.class);
                    intent.putExtra("CurrentID", CurrentID);
                    startActivity(intent);

            }
        });
    }
    private void init() {
        user_name = findViewById(R.id.AddName);
        user_birth = findViewById(R.id.addBirth);
        navigationView = findViewById(R.id.navigationdrawerView);
        drawerLayout = findViewById(R.id.DrawerLayout);
        Anne = findViewById(R.id.Spec_Spinner);
        // Buttons
        OK = findViewById(R.id.add_Chef_OkButton);
        Cancel = findViewById(R.id.addCancel);

    }
    private void fillSpinner (){
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if(snapshot.exists()){
                    Users helper = snapshot.getValue(Users.class);
                    assert helper != null;
                    if(helper.getUser_type()==3 && helper.getUser_department().equals(Department)){

                        MAL.add(helper.getUser_name()+" "+helper.getUser_prename());
                        mAdapter.notifyDataSetChanged();
                    }

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
    private void addChefD() {
        SName = user_name.getText().toString();
        int pos  = user_birth.getSpinner().getSelectedItemPosition();
        SBirth = MAL.get(pos);
        String SDep = Anne.getSpinner().getSelectedItem().toString();
        Course  modul = new Course(SName,SDep,SBirth);
        modul.setDepartment(Department);
        modulRef.child(SName).setValue(modul).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(addModul.this, "Modul Add Successfully ! ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(addModul.this, ModulsList.class);
                    intent.putExtra("CurrentID", CurrentID);
                    startActivity(intent);
                }
            }
        });

    }
    private void fillSpecSpinner(){
        DatabaseReference specRef = FirebaseDatabase.getInstance().getReference("specs");
        specRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if(snapshot.exists()){

                    specialty helper = snapshot.getValue(specialty.class);
                    if(helper.getDepartment().equals(Department)){
                        years.add(helper.getName());
                        yearAdapter.notifyDataSetChanged();
                    }

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
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        return false;
    }
}