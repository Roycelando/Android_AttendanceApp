package com.example.attendance;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;

public class registerActivity extends AppCompatActivity {

    Button btn_reg;
    EditText fname, lname, pass, username_tb;
    Context context = registerActivity.this;
    RadioGroup group;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        btn_reg = findViewById(R.id.btn_reg);
        fname = findViewById(R.id.et_fname2);
        lname = findViewById(R.id.et_lname2);
        pass = findViewById(R.id.et_inPassword);
        username_tb = findViewById(R.id.et_reg_username2);
        group = findViewById(R.id.radioGroup2);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radioId) {
                radioButton = (RadioButton) findViewById(radioId);
                System.out.println("[registerActivity]: radio Id -> " +  radioId);

            }
        });

        btn_reg.setOnClickListener(view -> {

            String userName = username_tb.getText().toString();
            String userFN = fname.getText().toString();
            String userLN = lname.getText().toString();
            String userPass = pass.getText().toString();


            String UserFullName = userFN + " " + userLN;

            FirebaseDatabase root = FirebaseDatabase.getInstance();
            DatabaseReference refUsers = root.getReference("Users");
            DatabaseReference refProf = root.getReference("ProfessorList");

            if(userFN.matches("[a-zA-Z]+")){
                if(userLN.matches("[a-zA-Z]+")){
                    if(userName.matches("[a-zA-Z0-9]+")) {


                        Query checkUser = refUsers.orderByChild("username").equalTo(userName);
                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    username_tb.setError("This user already exists");
                                    username_tb.requestFocus();
                                } else {
                                    DBHelper helper = new DBHelper(UserFullName, (String)radioButton.getText(), userName, userPass, null);
//                                    StudentDataModelClass studentStatus = new StudentDataModelClass(userName, "Offline");

                                    refUsers.child(userName).setValue(helper);

                                    if(((String) radioButton.getText()).contentEquals("Teacher")){
                                        refProf.child(userName).setValue("StudentList");
                                        refProf.child(userName).child("Student List").setValue("Names");

                                    }

                                    Intent intent = new Intent(context, LogInActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    else{
                        username_tb.setError("There must not be any special characters in username");
                        username_tb.requestFocus();
                    }
                }
                else{
                    lname.setError("Name not valid");
                    lname.requestFocus();
                }
            }
            else{
                fname.setError("Name not valid");
                fname.requestFocus();
            }

        });




    }
    public void getBtnSelected(View v){


    }

}
