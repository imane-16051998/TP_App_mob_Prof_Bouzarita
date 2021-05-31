package com.example.tahrimohamedguide.teacher;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tahrimohamedguide.ChefDepartement.ChefDPanel;
import com.example.tahrimohamedguide.MainActivity;
import com.example.tahrimohamedguide.Models.Users;
import com.example.tahrimohamedguide.Models.model;
import com.example.tahrimohamedguide.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tahrimohamedguide.UserProfile;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeaccherPanel extends AppCompatActivity implements ClassesAdapter.ClassesAdapterListiner
, NavigationView.OnNavigationItemSelectedListener {
    private FloatingActionButton fb;
    private RecyclerView recview;
    private  ClassesAdapter adapter;
    private String CurrentID , Department ;
    private FirebaseStorage ROOTSTORAGE = FirebaseStorage.getInstance();
    private FirebaseDatabase ROOT = FirebaseDatabase.getInstance();
    private StorageReference storageReference =ROOTSTORAGE.getReference();
    private DatabaseReference documentRef = ROOT.getReference("mydocuments");
    private ProgressDialog progressDialog;
    private NavigationView navigationView ;
    private DrawerLayout drawerLayout ;
    private ArrayList<model> downModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaccher_panel);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("enseignant");
        CurrentID =getIntent().getStringExtra("CurrentID");
        progressDialog = new ProgressDialog(this);
        navigationView = findViewById(R.id.navigationdrawerView);
        drawerLayout = findViewById(R.id.DrawerLayout);
        getUserData ();
        navigationView.setNavigationItemSelectedListener(this);

        fb = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(TeaccherPanel.this,uploadfile.class);
               intent.putExtra("CurrentID",CurrentID);
               startActivity(intent);
            }
        });

        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));


        adapter=new ClassesAdapter(this,downModels,CurrentID,1,this);

        recview.setAdapter(adapter);

        getDocList ();
/// DRAWEL LISTINER

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
/// DRAWEL LISTINER END

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }



    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        return downloadmanager.enqueue(request);
    }
    @Override
    public void onDownloadCourse(int position) {
        downloadFile(this,adapter.getElement().get(position).getFilename(),
                ".pdf",Environment.getExternalStorageDirectory().getAbsolutePath(),adapter.getElement().get(position).getFileurl());
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_amphi:
                Intent intent = new Intent(TeaccherPanel.this, UserProfile.class);
                intent.putExtra("CurrentID",CurrentID);
                intent.putExtra("FromItSelf","true");
                startActivity(intent);
                break ;
            case R.id.menu_block:
                Intent intnet2 = new Intent(TeaccherPanel.this, MainActivity.class);
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

    // GET DOCUMENT DATA
    private void getDocList (){

        documentRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                if(snapshot.exists()){
                 model helper = snapshot.getValue(model.class);
                    if(helper.getTeacher().equals(CurrentID)){
                        downModels.add(helper);
                        adapter.notifyDataSetChanged();

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
}