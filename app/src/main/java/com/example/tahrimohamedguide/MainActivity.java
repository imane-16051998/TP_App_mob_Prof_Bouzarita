package com.example.tahrimohamedguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tahrimohamedguide.Admin.adminPanel;
import com.example.tahrimohamedguide.ChefDepartement.ChefDPanel;
import com.example.tahrimohamedguide.Models.Users;
import com.example.tahrimohamedguide.Student.StudentPanel;
import com.example.tahrimohamedguide.Student.studentviewpdf;
import com.example.tahrimohamedguide.teacher.TeaccherPanel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
   private   FirebaseDatabase Root = FirebaseDatabase.getInstance();
    private DatabaseReference UserLoginRef = Root.getReference("users");

    private EditText user_email, user_password ;
    private Button login ;
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        user_password=findViewById(R.id.adminSignInPassET);
        user_email=findViewById(R.id.adminSignInEmailET);
        login=findViewById(R.id.adminLoginBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });

    }


    private void login (){

        email = user_email.getText().toString().trim();
        password = user_password.getText().toString().trim();
        if(email!=null){
        UserLoginRef.orderByChild("user_email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this, "dataSnapshot does not exist", Toast.LENGTH_SHORT).show();
                    return ;
                }
                Users userHelper ;
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    userHelper = ds.getValue(Users.class);
                    if(userHelper.getUser_password().equals(password)){
                        long user_Type = userHelper.getUser_type();
                        if(user_Type ==1){
                            // TO ADMINL PANEL
                            Intent intent = new Intent(MainActivity.this, adminPanel.class);
                            intent.putExtra("CurrentID",userHelper.getUser_id());
                            startActivity(intent);
                        }else if (user_Type==2){
                            // TO DEPARTMENT CHEF PANEL
                            Intent intent = new Intent(MainActivity.this, ChefDPanel.class);
                            intent.putExtra("CurrentID",userHelper.getUser_id());
                            intent.putExtra("Department",userHelper.getUser_department());
                            startActivity(intent);
                        }else if ( user_Type==3) {
                            // TO TEACHER PANEL
                            Intent intent = new Intent(MainActivity.this, TeaccherPanel.class);
                            intent.putExtra("CurrentID",userHelper.getUser_id());
                            intent.putExtra("Department",userHelper.getUser_department());
                            startActivity(intent);
                        }else {
                            // TO STUDENT PANEL
                            Intent intent = new Intent(MainActivity.this, StudentPanel.class);
                            intent.putExtra("CurrentID",userHelper.getUser_id());
                            intent.putExtra("Department",userHelper.getUser_department());
                            startActivity(intent);
                        }



                    }else {
                        Toast.makeText(MainActivity.this, "Email or password is incorrect ! ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "error response :"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MainActivity","DatabaseError :"+databaseError.getMessage());
            }
        });
        }else Toast.makeText(this, "L'email null", Toast.LENGTH_SHORT).show();
    }
}