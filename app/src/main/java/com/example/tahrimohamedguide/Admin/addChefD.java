package com.example.tahrimohamedguide.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.materialspinner.MaterialSpinner;
import com.example.tahrimohamedguide.ChefDepartement.ChefDPanel;
import com.example.tahrimohamedguide.ChefDepartement.StudentList;
import com.example.tahrimohamedguide.ChefDepartement.TeacherList;
import com.example.tahrimohamedguide.MainActivity;
import com.example.tahrimohamedguide.Models.Users;
import com.example.tahrimohamedguide.Models.specialty;
import com.example.tahrimohamedguide.R;
import com.example.tahrimohamedguide.UserProfile;
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

public class addChefD extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText user_name, user_prename, user_birth, user_adress, user_department,
            user_model, user_email, user_password;
    private String SName, SLName, SBirth, Sadress, Sdepartment,
            Smodel, Semail, Spassword, CurrentID, fromChefD,
            FromChefDS,Department,FromUpdateS,StudentID,Specialty,fromChefDT;

    private Button OK, Cancel;
    private FirebaseDatabase ROOT = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = ROOT.getReference("users");

    private NavigationView navigationView ;
    private DrawerLayout drawerLayout ;
    private MaterialSpinner Annees ;
    private ArrayList<String> years = new ArrayList<>();
    private ArrayAdapter<String> yearAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chef_d);
        CurrentID = getIntent().getStringExtra("CurrentID");
        fromChefD = getIntent().getStringExtra("FromChefD");
        fromChefDT = getIntent().getStringExtra("From_Teacher_List");
        FromChefDS = getIntent().getStringExtra("FromChefDS");
        FromUpdateS = getIntent().getStringExtra("FromUpdateS");
        Department = getIntent().getStringExtra("Department");
        StudentID = getIntent().getStringExtra("StudentID");
        init();
        Annees.setLabel("speciality");
        yearAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,years);
        Annees.setAdapter(yearAdapter);
        fillSpecSpinner();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ajouter");
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChefD();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromChefD != null) {
                    Intent intent = new Intent(addChefD.this, TeacherList.class);
                    intent.putExtra("CurrentID", CurrentID);
                    startActivity(intent);

                }else
                if (FromChefDS != null) {
                    Intent intent = new Intent(addChefD.this, StudentList.class);
                    intent.putExtra("CurrentID", CurrentID);
                    startActivity(intent);

                }
                else {
                    Intent intent = new Intent(addChefD.this, adminPanel.class);
                    intent.putExtra("CurrentID", CurrentID);
                    startActivity(intent);
                }

            }
        });
    }

    private void init() {
        user_name = findViewById(R.id.AddName);
        user_prename = findViewById(R.id.AddLname);
        user_birth = findViewById(R.id.addBirth);
        user_adress = findViewById(R.id.addAdress);
        user_department = findViewById(R.id.addDepartment);
        user_model = findViewById(R.id.AddModel);
        user_email = findViewById(R.id.addEmail);
        user_password = findViewById(R.id.AddPassword);
        navigationView = findViewById(R.id.navigationdrawerView);
        drawerLayout = findViewById(R.id.DrawerLayout);
        Annees = findViewById(R.id.Anné_Spinner);
        Annees.setVisibility(View.GONE);
        user_model.setVisibility(View.GONE);
        // Buttons
        OK = findViewById(R.id.add_Chef_OkButton);
        Cancel = findViewById(R.id.addCancel);
        if (FromChefDS != null || FromUpdateS != null) {

            user_department.setVisibility(View.GONE);
            Annees.setVisibility(View.VISIBLE);

        }else if(fromChefD != null || fromChefDT!=null){
            user_department.setVisibility(View.GONE);

        }
    }

    private void addChefD() {
        SName = user_name.getText().toString();
        SLName = user_prename.getText().toString();

        /*user_birth.addTextChangedListener(new TextWatcher() {
            int prevL = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevL = user_birth.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if ((prevL < length) && (length == 2 || length == 5)) {
                    editable.append("/");
                }
            }
        });*/
        SBirth = user_birth.getText().toString();
        Sadress = user_adress.getText().toString();
        Semail = user_email.getText().toString();
        Spassword = user_password.getText().toString();


        String pushKey = usersRef.push().getKey();
        long type = 2;

        if (fromChefD != null) {
            type = 3;

            Sdepartment= Department;

        } else if (FromChefDS != null) {
            type = 4;
            Specialty = Annees.getSpinner().getSelectedItem().toString();
            Sdepartment= Department;
        }
        if(FromUpdateS != null  ){

            pushKey = StudentID;
            Sdepartment= Department;
            type = 4;
        }
        if( fromChefDT != null ){
            pushKey = StudentID;
            Sdepartment= Department;
            type = 3;
        }
        if(FromChefDS==null && fromChefD==null && FromUpdateS==null){
            Sdepartment=user_department.getText().toString();
        }
        Users userHelper = new Users(pushKey, SName, SLName, SBirth, Sadress, type, ""+Sdepartment, Smodel, Semail, Spassword);
        if (FromChefDS != null) {
            userHelper.setSpeciality(Specialty);
        }

        usersRef.child(pushKey).setValue(userHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {

                    if (fromChefD != null|| fromChefDT!=null) {
                        Toast.makeText(addChefD.this, "Teacher Added Successfully ! ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(addChefD.this, TeacherList.class);
                        intent.putExtra("CurrentID", CurrentID);
                        intent.putExtra("Department", Department);
                        startActivity(intent);

                    }else if (FromChefDS != null|| FromUpdateS!=null) {
                        Toast.makeText(addChefD.this, "Student Added Successfully ! ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(addChefD.this, StudentList.class);
                        intent.putExtra("CurrentID", CurrentID);
                        intent.putExtra("Department", Department);
                        startActivity(intent);


                    } else {
                        Toast.makeText(addChefD.this, "Chef Department Added Successfully ! ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(addChefD.this, adminPanel.class);
                        intent.putExtra("CurrentID", CurrentID);
                        startActivity(intent);
                    }
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_amphi:
                Intent intent = new Intent(addChefD.this, UserProfile.class);
                intent.putExtra("CurrentID",CurrentID);
                startActivity(intent);
                break ;
            case R.id.menu_block:
                Intent intnet2 = new Intent(addChefD.this, MainActivity.class);
                intnet2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intnet2);
                break;



        }
        return true;
    }
}