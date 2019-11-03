package com.example.digital_shosha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;



public class editprofile extends AppCompatActivity {

    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference ref;
    ImageView editphoto;
    private static final int Gallerypickup=1;
    private FirebaseAuth mauth;
    private String currentuserid;
    private Button updateprofilebutton;
    private EditText editnamev,editemailv,editcontactnov;

    private StorageReference  userprofileimagesref;
    private Uri imageuri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
            mauth=FirebaseAuth.getInstance();
            user=mauth.getCurrentUser();
            currentuserid=mauth.getUid();
            database=FirebaseDatabase.getInstance();

          ref=database.getReference("users").child(currentuserid);

            userprofileimagesref= FirebaseStorage.getInstance().getReference("uploads");

        editphoto=(ImageView) findViewById(R.id.ephoto);
        editnamev=(EditText) findViewById(R.id.editname);
        editemailv=(EditText) findViewById(R.id.editemail);
        editemailv.setEnabled(false);
        updateprofilebutton=(Button) findViewById(R.id.updateprofile);
        editcontactnov=(EditText) findViewById(R.id.editcontactno);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name="" +dataSnapshot.child("name").getValue();
                String email="" +dataSnapshot.child("emailid").getValue();
                editnamev.setText(name);
                editemailv.setText(email);

                if(dataSnapshot.child("image").getValue().toString().isEmpty()){
                    editphoto.setImageResource(R.drawable.badge1c);
                }

                else {
                    String uri = (dataSnapshot.child("image").getValue().toString());
                    if(!uri.isEmpty()){
                        Picasso.get().load(uri).into(editphoto);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        updateprofilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatesettings();
            }
        });


        editphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent=new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,Gallerypickup);
            }
        });
    }

//    private  String getfileextension(Uri uri){
//        ContentResolver contentResolver=getApplicationContext().getContentResolver();
//        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//
//    }

    private void updatesettings() {

        String setname=editnamev.getText().toString();
        String setcontact=editcontactnov.getText().toString();

        if(TextUtils.isEmpty(setname)){
            Toast.makeText(this, "Please Write Your Name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(setcontact)){
            Toast.makeText(this, "Please Give Your Contact number", Toast.LENGTH_SHORT).show();
        }
        else{
            ref.child(currentuserid).child("name").setValue(setname);
            ref.child(currentuserid).child("contactnumber").setValue(setcontact);
            startActivity(new Intent(editprofile.this,homepage.class));
            finish();
        }
    }

//


    @Override
    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==Gallerypickup && resultCode==RESULT_OK && data!=null){
            imageuri=data.getData();


                    final StorageReference filepath=userprofileimagesref.child(currentuserid + ".jpg");

            filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            final String downloadurl=task.getResult().toString();
                                ref.child("image").setValue(downloadurl);
                            Toast.makeText(editprofile.this, ""+downloadurl, Toast.LENGTH_SHORT).show();


                        }
                    });

                }
            });

        }
//
//
//
    }

}
