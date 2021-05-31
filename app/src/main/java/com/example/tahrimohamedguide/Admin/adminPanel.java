package com.example.tahrimohamedguide.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tahrimohamedguide.MainActivity;
import com.example.tahrimohamedguide.Models.Users;
import com.example.tahrimohamedguide.R;
import com.example.tahrimohamedguide.UserProfile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class adminPanel extends AppCompatActivity implements AdminAdapter.adminListener
        ,NavigationView.OnNavigationItemSelectedListener{
    private FirebaseDatabase ROOT = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = ROOT.getReference("users");
    private RecyclerView rcv ;
    private ArrayList<Users> UAL = new ArrayList<>();
    private String CurrentID;
    private AdminAdapter mAdapter ;
    private FloatingActionButton addChefD;
    private NavigationView navigationView ;
    private DrawerLayout drawerLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        CurrentID  =getIntent().getStringExtra("CurrentID");
        init();
        getUserData();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Admin");
        navigationView.setNavigationItemSelectedListener(this);
        mAdapter = new AdminAdapter(this,UAL,this);
        RecyclerView.LayoutManager LL = new LinearLayoutManager(this);
        rcv.setLayoutManager(LL);
        rcv.setAdapter(mAdapter);
        DisplaychefDeparts ();
        addChefD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminPanel.this,addChefD.class);
                intent.putExtra("CurrentID",CurrentID);
                startActivity(intent);
            }
        });
    }

  private  void init (){
        rcv = findViewById(R.id.adminPanelRecyclerView);
        addChefD = findViewById(R.id.addChefDButton);
      navigationView = findViewById(R.id.navigationdrawerView);
      drawerLayout = findViewById(R.id.DrawerLayout);
  }

  private void DisplaychefDeparts (){

        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                    if(!snapshot.exists()){
                        Toast.makeText(adminPanel.this, "Nothin exist !", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    Users helper = snapshot.getValue(Users.class);
                    if(helper.getUser_type()==2) {
                        UAL.add(helper);
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
    public void onChefClick(int position) {
       Intent intent  = new Intent(adminPanel.this, UserProfile.class);
       intent.putExtra("CurrentID",UAL.get(position).getUser_id());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_amphi:
                Intent intent = new Intent(adminPanel.this, UserProfile.class);
                intent.putExtra("CurrentID",CurrentID);
                intent.putExtra("FromItSelf","true");
                startActivity(intent);
                break ;
            case R.id.menu_block:
                Intent intnet2 = new Intent(adminPanel.this, MainActivity.class);
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