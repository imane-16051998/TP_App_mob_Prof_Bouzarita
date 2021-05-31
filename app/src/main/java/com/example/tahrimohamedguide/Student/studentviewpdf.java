package com.example.tahrimohamedguide.Student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tahrimohamedguide.ChefDepartement.ChefDPanel;
import com.example.tahrimohamedguide.MainActivity;
import com.example.tahrimohamedguide.Models.DownModel;
import com.example.tahrimohamedguide.Models.Users;
import com.example.tahrimohamedguide.Models.model;
import com.example.tahrimohamedguide.R;
import com.example.tahrimohamedguide.UserProfile;
import com.example.tahrimohamedguide.teacher.ClassesAdapter;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class studentviewpdf extends AppCompatActivity implements ClassesAdapter.ClassesAdapterListiner
, NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fb;
    RecyclerView recview;
    ClassesAdapter adapter;
    FirebaseFirestore db;
    ArrayList<DownModel> downModelArrayList = new ArrayList<>();
    private String CurrentID , Modul ;
    private FirebaseStorage ROOTSTORAGE = FirebaseStorage.getInstance();
    private StorageReference storageReference =ROOTSTORAGE.getReference();
    private FirebaseDatabase ROOT = FirebaseDatabase.getInstance();
    private DatabaseReference documentRef = ROOT.getReference("mydocuments");
    private ProgressDialog progressDialog;
    private NavigationView navigationView ;
    private DrawerLayout drawerLayout ;
    private ArrayList<model> downModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentviewpdf);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CurrentID = getIntent().getStringExtra("CurrentID");
        Modul = getIntent().getStringExtra("Modul");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Modul+" Courses");
        progressDialog = new ProgressDialog(this);
        navigationView = findViewById(R.id.navigationdrawerView);
        drawerLayout = findViewById(R.id.DrawerLayout);
        navigationView.setNavigationItemSelectedListener(this);
        getUserData ();
        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        checkPermission();
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("mydocuments"), model.class)
                        .build();

        adapter=new ClassesAdapter(this,downModels,Modul,this);
        recview.setAdapter(adapter);
        getDocList ();
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
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    private void downloadLink (int position) throws IOException {
        File rootPath = new File(Environment.getExternalStorageDirectory(), adapter.getElement().get(position).getFilename()+".pdf");
        if(!rootPath.exists()) {
            rootPath.createNewFile();
        }



        storageReference.child("uploads/"+adapter.getElement().get(position).getFilename()+".pdf").getFile(rootPath)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        Log.e("firebase ",";local tem file created  created " +rootPath.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e("firebase ",";local tem file not created  created " +e.toString());
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull FileDownloadTask.TaskSnapshot snapshot) {
                //calculating progress percentage
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Downloading ... "+((int) progress)+"%");
                progressDialog.show();
                if(progress==100){
                    progressDialog.hide();
                    progressDialog.dismiss();
                }
            }
        });
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
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2);


        }else {
          
                Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();


            } else {

                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==2) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();


            } else {

                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_amphi:
                Intent intent = new Intent(studentviewpdf.this, UserProfile.class);
                intent.putExtra("CurrentID",CurrentID);
                intent.putExtra("FromItSelf","true");
                startActivity(intent);
                break ;
            case R.id.menu_block:
                Intent intnet2 = new Intent(studentviewpdf.this, MainActivity.class);
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
                    if(helper.getModul().equals(Modul)){
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