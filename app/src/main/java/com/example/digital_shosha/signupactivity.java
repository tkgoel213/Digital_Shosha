package com.example.digital_shosha;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class signupactivity extends AppCompatActivity {

     FirebaseAuth mAuth;
     SignInButton gsignin;
    int RC_SIGN_IN = 100;
     DatabaseReference rootref;
     TextView alreadyhaveanaccount;
     Button createaccount;
     LoginButton signupfbbutton;
     ImageButton gsign;
     EditText useremail;
    EditText userpassword;
    EditText username;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user;
        ImageButton loginButtonfb;
        CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mAuth = FirebaseAuth.getInstance();
        rootref= FirebaseDatabase.getInstance().getReference();
        Initializefields();
        FacebookSdk.sdkInitialize(getApplicationContext());


        callbackManager = CallbackManager.Factory.create();
      signupfbbutton.setReadPermissions(Arrays.asList("email"));


        gsign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        alreadyhaveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sendusertologinactivity();
            }
        });


        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createnewaccount();
            }
        });
    }

    private void Sendusertologinactivity() {
        Intent loginintent=new Intent(signupactivity.this,loginactivity.class);

        startActivity(loginintent);
        finish();
    }


    private void Initializefields() {

        createaccount = (Button) findViewById(R.id.signupbutton);
        useremail = (EditText) findViewById(R.id.signupmail);
        username = (EditText) findViewById(R.id.signupname);
        userpassword = (EditText) findViewById(R.id.signuppass);
        alreadyhaveanaccount = (TextView) findViewById(R.id.linktologin);
        gsign=(ImageButton)findViewById(R.id.gmailsign);
        signupfbbutton=(LoginButton)findViewById(R.id.signupfb) ;


    }

    
    

    private void createnewaccount() {

        String email = useremail.getText().toString();
        String password =userpassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"please enter email", Toast.LENGTH_LONG);
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"please enter password", Toast.LENGTH_LONG);
        }

        else {


            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){


                                FirebaseUser user=mAuth.getCurrentUser();
                                if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                    String name= username.getText().toString();
                                    String email=user.getEmail();
                                    String uid=user.getUid();

                                    HashMap<Object,String> hashMap=new HashMap<>();
                                    hashMap.put("name",name);
                                    hashMap.put("emailid",email);
                                    hashMap.put("uid",uid);
                                    hashMap.put("image","");
                                    hashMap.put("contactnumber","");
                                    hashMap.put("favorites","");


                                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                                    DatabaseReference reference=database.getReference("users");
                                    reference.child(uid).setValue(hashMap);
                                }

                                Sendusertohomeactivity();
                                Toast.makeText(signupactivity.this,"account created",Toast.LENGTH_LONG);
                            }
                            else {
                                String message=task.getException().toString();
                                Toast.makeText(signupactivity.this,"error:" + message,Toast.LENGTH_LONG);
                            }
                        }
                    });

        }
    }

    private void Sendusertohomeactivity() {

        Intent mainintent=new Intent(signupactivity.this,homepage.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();
    }

    public void buttonclickloginfb(View v){
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(signupactivity.this, "user  succesfully logged in", Toast.LENGTH_SHORT).show();
                        handlefacebooktoken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(signupactivity.this, "user cancels", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(signupactivity.this, "user has an error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handlefacebooktoken(AccessToken accessToken) {
        AuthCredential credential= FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser myfuser=mAuth.getCurrentUser();
                            updateUI(myfuser);
                             send();
                        }
                        else
                        {
                            Toast.makeText(signupactivity.this, "could not linked to firebase", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void send() {
        Intent intent=new Intent(signupactivity.this,homepage.class);
        startActivity(intent);
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                Toast.makeText(this, "" +e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private  void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            String personName = acct.getDisplayName();
                            String email=user.getEmail();
                            String uid=user.getUid();

                            HashMap<Object,String> hashMap=new HashMap<>();
                            hashMap.put("name",personName);
                            hashMap.put("emailid",email);
                            hashMap.put("uid",uid);
                            hashMap.put("image","");
                            hashMap.put("contactnumber","");
                            hashMap.put("favs","");


                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference reference=database.getReference("users");
                            reference.child(uid).setValue(hashMap);
                            updateUI(user);
                            startActivity(new Intent(signupactivity.this,socialmedia.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(signupactivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signupactivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        if (account != null) {
            startActivity(new Intent(signupactivity.this, Main2Activity.class));
        }
        super.onStart();
    }


    private void updateUI(FirebaseUser user) {



        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
           Uri personPhoto = acct.getPhotoUrl();

            Toast.makeText(this, "Name of the user :" + personName + " user id is : " + personId, Toast.LENGTH_SHORT).show();

        }

    }
}
