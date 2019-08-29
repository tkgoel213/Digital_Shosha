package com.example.digital_shosha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.InputDevice;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginactivity extends AppCompatActivity {

    EditText email, pass;

    private TextView recoverpass;
    TextView linktosignup;
    Button logink;
    private FirebaseAuth mAuth;
    ProgressBar pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.loginmail);
        pass = findViewById(R.id.loginpass);
        recoverpass = findViewById(R.id.recoverpass);
        logink = findViewById(R.id.loginbuttonf);
        linktosignup = findViewById(R.id.linktosignup);

        logink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginemail = email.getText().toString();
                String loginpass = pass.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(loginemail).matches()) {
                    email.setError("Invalid Email");
                    email.setFocusable(true);
                } else {
                    loginuser(loginemail, loginpass);
                }

            }
        });

        linktosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginactivity.this, signupactivity.class));

            }
        });

        recoverpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showrecoverpassworddialog();
            }
        });

        pd=new ProgressBar(this);
        pd.setTag("Logging in...");
    }

    private void showrecoverpassworddialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
      builder.setTitle("Recover Password");

        LinearLayout ly=new LinearLayout(this);
        final EditText email=new EditText(this);
        email.setHint("Email");
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        ly.addView(email);
        ly.setPadding(10,10,10,10);

builder.setView(ly);


        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
String emailrecoverd=email.getText().toString().trim();
beginrecovery(emailrecoverd);


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();


    }

    private void beginrecovery(String emailrecoverd) {
        mAuth.sendPasswordResetEmail(emailrecoverd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Toast.makeText(loginactivity.this, "Email sent", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(loginactivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginactivity.this, "" +e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void loginuser(String loginemail, String loginpass) {
        pd.showContextMenu();

        mAuth.signInWithEmailAndPassword(loginemail, loginpass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(loginactivity.this,Main2Activity.class));
                        finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(loginactivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(loginactivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}