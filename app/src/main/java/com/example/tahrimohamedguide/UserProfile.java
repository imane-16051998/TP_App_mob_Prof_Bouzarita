package com.example.tahrimohamedguide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tahrimohamedguide.Admin.addChefD;
import com.example.tahrimohamedguide.Models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    private CircleImageView profile ;
    private TextView profileName,profileEmail,profilePhone;
    private FirebaseDatabase ROOT=FirebaseDatabase.getInstance();
    private DatabaseReference currentRef = ROOT.getReference("users");
    private String CurrentID ,FromChefDT,FromItSelf,Department,StudentID;
    private Button OK;
    private Uri ImageUri ;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    public StorageTask uploadtask;
    private ProgressDialog progressDialog;
    private DatabaseReference modulsRed = ROOT.getReference("modul");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        CurrentID = getIntent().getStringExtra("CurrentID");
        FromItSelf = getIntent().getStringExtra("FromItSelf");
        FromChefDT = getIntent().getStringExtra("From_Teacher_List");
        Department = getIntent().getStringExtra("Department");
        StudentID = getIntent().getStringExtra("StudentID");
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference("Ppics");
        init();

        getCurrentData(CurrentID);
    }

    private void init(){
        profile = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhone = findViewById(R.id.profilePhone);
        OK = findViewById(R.id.okUpdate);

        if( FromItSelf!=null){
            OK.setVisibility(View.INVISIBLE);

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chekPermission();

                }
            });
        }else {
            profileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            profileEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            profilePhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(UserProfile.this, addChefD.class);
                    if(FromChefDT!=null){
                    intent.putExtra("From_Teacher_List","true");
                    }else { intent.putExtra("FromUpdateS","true");}
                    intent.putExtra("Department",Department);
                    intent.putExtra("StudentID",StudentID);
                    intent.putExtra("CurrentID",CurrentID);
                    startActivity(intent);

                }
            });



        }

    }
    private void uploadPiture ( ){


    }
    private void chekPermission(){
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(i, 1);

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageUri=data.getData();

            profile.setImageURI(ImageUri);
            fileUploader();

        }
    }
    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void fileUploader() {
        String ImageID = System.currentTimeMillis() + "." + getExtension(ImageUri);
        StorageReference Ref = storageReference.child(ImageID);

        uploadtask = Ref.putFile(ImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Task<Uri> downloadURL = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                currentRef.child(CurrentID).child("imageid").setValue(uri.toString());
                                Toast.makeText(getApplicationContext(), "profile picture uploaded successfully", Toast.LENGTH_LONG).show();
                            }
                        });



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
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

    private void getCurrentData (String ID ){
        currentRef.child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(UserProfile.this, "Data of this user not found !", Toast.LENGTH_SHORT).show();
                    return ;
                }

                Users hepler = snapshot.getValue(Users.class);
                profileName.setText(hepler.getUser_name()+" "+hepler.getUser_prename());
                profileEmail.setText(hepler.getUser_email());
                profilePhone.setText(hepler.getUser_department());
                if(hepler.getImageid()!=null){
                    Picasso.get().load(hepler.getImageid()).into(profile);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}