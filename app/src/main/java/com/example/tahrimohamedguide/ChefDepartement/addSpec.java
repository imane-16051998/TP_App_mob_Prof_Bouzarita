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
import java.util.NavigableMap;

public class addSpec extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText user_name;
;
    private String SName, SBirth,CurrentID,Department;
    private Button OK, Cancel;
    private FirebaseDatabase ROOT = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = ROOT.getReference("users");
    private DatabaseReference modulRef = ROOT.getReference("specs");
    private NavigationView navigationView ;
    private DrawerLayout drawerLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spec);
        CurrentID = getIntent().getStringExtra("CurrentID");
        Department = getIntent().getStringExtra("Department");

        init();
        navigationView.setNavigationItemSelectedListener(this);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ajouter une spécialité");
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChefD();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(addSpec.this, SpecList.class);
                intent.putExtra("CurrentID", CurrentID);
                startActivity(intent);

            }
        });
    }
    private void init() {
        user_name = findViewById(R.id.AddName);

        navigationView = findViewById(R.id.navigationdrawerView);
        drawerLayout = findViewById(R.id.DrawerLayout);
        // Buttons
        OK = findViewById(R.id.add_Chef_OkButton);
        Cancel = findViewById(R.id.addCancel);

    }



    private void addChefD() {
        SName = user_name.getText().toString();
        specialty specialty = new specialty(SName,Department);
        modulRef.child(SName).setValue(specialty).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(addSpec.this, "Speciality Added Successfully ! ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(addSpec.this, SpecList.class);
                    intent.putExtra("CurrentID", CurrentID);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        return false;
    }
}