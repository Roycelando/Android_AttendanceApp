package com.example.attendance;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    Button btn_log_in, btn_register;
    EditText et_username, et_password;
    Context context = LogInActivity.this;
    FirebaseDatabase fire;
    DatabaseReference  dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btn_log_in = findViewById(R.id.btn_log_in);
        et_username = findViewById(R.id.et_fname);
        et_password = findViewById(R.id.et_lname);
        btn_register = findViewById(R.id.register_button);

        btn_register.setOnClickListener(view -> {

            Intent register_activity = new Intent(context, registerActivity.class);
            startActivity(register_activity);

        });

        //when login button is pressed onclick it comes here
        btn_log_in.setOnClickListener(v -> {
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();


                checklogin(username, password);// on click it checks email and password and takes in check login function



        });

    }
    //checklogin function
    private void checklogin(String username, String password) {

           isUser(username,password);


    }

    private void isUser(String username, String password){

        Intent intent = new Intent(context, MainActivity.class);//takes screen to ,main activity
        fire = FirebaseDatabase.getInstance(); // get an instance of firebase database
        dataRef = fire.getReference("Users"); // references a column in the data base
        Query checkUser = dataRef.orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    System.out.println(snapshot);
                    et_username.setError(null);
                    //et_username.setVisibility(View.VISIBLE);
                    String dpPassword = snapshot.child(username).child("password").getValue(String.class);


                    if(dpPassword.equals(password)){
                        System.out.println("Crash Test: dpPassword --> " + password );

                        String role = snapshot.child(username).child("role").getValue(String.class);
                        System.out.println("Crash Test: role --> " + role );
                        String fullName = snapshot.child(username).child("name").getValue(String.class);
                        System.out.println("Crash Test: fullName --> " + fullName);



                        intent.putExtra("role", role);
                        intent.putExtra("fullname", fullName);
                        intent.putExtra("password",dpPassword);
                        intent.putExtra("username", username);


                        startActivity(intent);
                        finish();



                    }
                    else{
                        et_password.setError("Wrong Password");
                        et_password.requestFocus();
                    }
                }
                else {
                      et_username.setError("This user does not exist");
                      et_username.requestFocus();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
      });
    }

    private boolean validateUserName(String username){
        return true;
    }

    private boolean validatePasswordName(String username, String Password){
        return true;
    }


}