package com.example.tahrimohamedguide.teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.dev.materialspinner.MaterialSpinner;
import com.example.tahrimohamedguide.Models.Course;
import com.example.tahrimohamedguide.Models.Users;
import com.example.tahrimohamedguide.Models.model;
import com.example.tahrimohamedguide.R;
import com.example.tahrimohamedguide.UserProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class uploadfile extends AppCompatActivity {

    ImageView imagebrowse,imageupload,filelogo,cancelfile;
    Uri filepath;

    EditText filetitle;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    private FirebaseDatabase ROOT  = FirebaseDatabase.getInstance();
    private DatabaseReference modulsRed = ROOT.getReference("modul");
    private DatabaseReference currentRef = ROOT.getReference("users");
    private ArrayList<String> MAL = new ArrayList<>();
    private MaterialSpinner modulSpinner ;
    private String CurrentID,CURRENT_TeacherName;
    private ArrayAdapter<String> mAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadfile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        modulSpinner = findViewById(R.id.Modul);
        modulSpinner.setLabel("Modul");

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("mydocuments");
        CurrentID =getIntent().getStringExtra("CurrentID");
        getCurrentData (CurrentID);
        filetitle=findViewById(R.id.filetitle);
         mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,MAL);
        modulSpinner.setAdapter(mAdapter);
        imagebrowse=findViewById(R.id.imagebrowse);
        imageupload=findViewById(R.id.imageupload);

        filelogo=findViewById(R.id.filelogo);
        cancelfile=findViewById(R.id.cancelfile);
        fillSpinner ();

        filelogo.setVisibility(View.INVISIBLE);
        cancelfile.setVisibility(View.INVISIBLE);

        cancelfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filelogo.setVisibility(View.INVISIBLE);
                cancelfile.setVisibility(View.INVISIBLE);
                imagebrowse.setVisibility(View.VISIBLE);
            }
        });


        imagebrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processupload(filepath);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            filelogo.setVisibility(View.VISIBLE);
            cancelfile.setVisibility(View.VISIBLE);
            imagebrowse.setVisibility(View.INVISIBLE);
        }
    }


    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();

        final StorageReference reference=storageReference.child("uploads/"+filetitle.getText().toString()+".pdf");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                               String modul = modulSpinner.getSpinner().getSelectedItem().toString();
                                model obj=new model(filetitle.getText().toString(),uri.toString(),modul,0,0,0);
                                 obj.setTeacher(CurrentID);
                                databaseReference.child(databaseReference.push().getKey()).setValue(obj);

                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();

                                filelogo.setVisibility(View.INVISIBLE);
                                cancelfile.setVisibility(View.INVISIBLE);
                                imagebrowse.setVisibility(View.VISIBLE);
                                filetitle.setText("");
                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :"+(int)percent+"%");
                    }
                });


    }

    private void fillSpinner (){
        modulsRed.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if(snapshot.exists()){
                    Course helper = snapshot.getValue(Course.class);
                    if(helper.getTeacher().equals(CURRENT_TeacherName)){

                        MAL.add(helper.getName());
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

    /// GET CURRENT USER DATA
    private void getCurrentData (String ID ){
        currentRef.child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(uploadfile.this, "Data of this user not found !", Toast.LENGTH_SHORT).show();
                    return ;
                }

                Users hepler = snapshot.getValue(Users.class);
                CURRENT_TeacherName= hepler.getUser_name()+" "+hepler.getUser_prename();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
    // END OF GET CURRENT USER DATA
}