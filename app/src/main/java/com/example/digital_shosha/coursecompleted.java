package com.example.digital_shosha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class coursecompleted extends AppCompatActivity {


    Button proceedtosumm;
    String keys;
    String course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursecompleted);
        Intent intent = getIntent();
        keys = intent.getStringExtra("key");
        course = intent.getStringExtra("course");
        Toast.makeText(this, "course wala="+keys+course, Toast.LENGTH_SHORT).show();

        proceedtosumm=(Button)findViewById(R.id.proceed);
        proceedtosumm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(coursecompleted.this,summary.class);
                i.putExtra("key",keys);
                i.putExtra("course",course);
                startActivity(i);
            }
        });




    }
}
