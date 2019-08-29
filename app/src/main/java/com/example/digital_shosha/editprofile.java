package com.example.digital_shosha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
            mauth=FirebaseAuth.getInstance();
            user=mauth.getCurrentUser();
            currentuserid=mauth.getCurrentUser().getUid();
            database=FirebaseDatabase.getInstance();

          ref=database.getReference("users");

            userprofileimagesref= FirebaseStorage.getInstance().getReference();

        editphoto=(ImageView) findViewById(R.id.photo);
        editnamev=(EditText) findViewById(R.id.editname);
        editemailv=(EditText) findViewById(R.id.editemail);
        editemailv.setEnabled(false);
        updateprofilebutton=(Button) findViewById(R.id.updateprofile);
        editcontactnov=(EditText) findViewById(R.id.editcontactno);

        Query query=ref.orderByChild("emailid").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String name="" +ds.child("name").getValue().toString();
                   String email="" +ds.child("emailid").getValue().toString();
                   String contactnum="" +ds.child("contactnumber").getValue().toString();
                    String image="" +ds.child("image").getValue().toString();

                    editnamev.setText(name);
                   editemailv.setText(email);
                   editcontactnov.setText(contactnum);

                    try {
                        Picasso.get().load(image).into(editphoto);
                        Toast.makeText(editprofile.this, "hogya re", Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception e){
                        Picasso.get().load(R.drawable.download).into(editphoto);

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

    @Override
    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==Gallerypickup && resultCode==RESULT_OK && data!=null){
            Uri imageuri=data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode==RESULT_OK){
                    Uri resulturi=result.getUri();

                    StorageReference filepath=userprofileimagesref.child(currentuserid + ".jpg");

                    filepath.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(editprofile.this, "Profile Image uploaded Successfully", Toast.LENGTH_SHORT).show();
                               final String downloadurl= task.getResult().getUploadSessionUri().toString();


                                ref.child(currentuserid).child("image").setValue(downloadurl);
                            }
                            else
                            {  String mess=task.getException().toString();
                                Toast.makeText(editprofile.this, "Error :" + mess, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
        }
    }

}
