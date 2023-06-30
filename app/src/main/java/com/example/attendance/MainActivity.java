//Push Test
// Pushed that back
// Pushed again
// Quadruple push back
// quintuple push back

package com.example.attendance;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    String user, role, fullName; // used to get database values

    private DrawerLayout drawer;
    ImageView img_drawer;
    LocationManager location;
    double longitude;
    double latitude;

    double tlongitude;
    double llattidue;

    EditText et_code, et_profID;
    RelativeLayout rel_teacher_layout, rel_student_layout, after_code, before_code;
    double x1;
    double y1;
    double x2;
    double y2;

    Button goback;

    Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        role = intent.getStringExtra("role");
        fullName = intent.getStringExtra("fullname");



        //username passed from login comes here as "user string"

        set_drawer();  // function to set drawer "3 horizontal line on the top left of display
        // where it shows logout option"
        all_find(); //this function finds everything from the design


        //here be very careful while coding for student and teacher because we have to set visibility
        //according to data passed. If not passed correctly it will display both teacher and student design.

        if (role.equals("Teacher")) { //it checks whether its student or teacher
            rel_student_layout.setVisibility(View.GONE);  //here is have passed "t" for teacher so this line hides students display

            teacher();   // everything will be handled by this function for teacher.

        } else if(role.equals("Student")) {
            rel_teacher_layout.setVisibility(View.GONE);  // here for students i havent passed anything so when
            //nothing is pressed it shows students view.
            before_code.setVisibility(View.VISIBLE);  // code display view
            student();  // everything will be handled by this function for student.
        }


    }

    /**
     * This method is used to generate the list of absent and present students
     * this list is used to update the firebase database
     *
     * @param i
     */
    private void getStudentList(int i){
        ArrayList<StudenDataModelClass> absentList = new ArrayList<>(); // holds the list of absent users
        ArrayList<StudenDataModelClass> presentList = new ArrayList<>(); // holds the list of present students
        RecyclerView student_view_present, student_view_absent;
        student_view_absent = findViewById(R.id.student_view_absent);
        student_view_present = findViewById(R.id.student_view_present);


        FirebaseDatabase fire = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = fire.getReference("ProfessorList").child(user).child("Student List").child("Names");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<StudenDataModelClass> present = new ArrayList<>();
                ArrayList<StudenDataModelClass> absent = new ArrayList<>();
                if(snapshot.exists()){
                    System.out.println(snapshot);


                    for(DataSnapshot data : snapshot.getChildren()){
                        StudenDataModelClass student = data.getValue(StudenDataModelClass.class);
                        System.out.println("Status:" + student.getStatus());

                        if(student.getStatus().contentEquals("Offline")) {
                            System.out.println( student.getName() + "-> Offline");
                            absent.add(student);
                        }

                        else if(student.getStatus().contentEquals("Online")) {
                            System.out.println( student.getName() + "-> Online");
                            present.add(student);
                        }


                    }

                    student_view_present.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false));
                    CustomAdapter customAdapter = new CustomAdapter(present, context,1);
                    student_view_present.setNestedScrollingEnabled(false);
                    student_view_present.setAdapter(customAdapter);


                    student_view_absent.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false));
                    CustomAdapter customAdapter1 = new CustomAdapter(absent, context,1);
                    student_view_absent.setNestedScrollingEnabled(false);
                    student_view_absent.setAdapter(customAdapter1);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void setStudentsOffline(){
        FirebaseDatabase fire = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = fire.getReference("ProfessorList").child(user).child("Student List").child("Names");
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("[setStudentsOffline]: someone called me");
                if (snapshot.exists()) {

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        StudenDataModelClass student = snap.getValue(StudenDataModelClass.class);
                        student.setStatus("Offline");
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("status", "Offline");
                        dataRef.child(snap.getKey()).updateChildren(map);


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void getPresentStudents(){



    }

    private void teacher() {
        TextView tv = (TextView) findViewById(R.id.profName);
        tv.setText(fullName);
        getStudentList(1);
        //Switch here for online offline option

        SwitchCompat s = findViewById(R.id.toggle);
        TextView v = findViewById(R.id.codeText);

        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference ref = root.getReference("ProfessorList");
        ref.child(user).child("Code").setValue(null);
        s.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String code = "" + ((int)(Math.random()*9000)+1000);
                ref.child(user).child("Code").setValue(code);
                String ss = "Login Code: " + code;
                v.setText(ss);
            } else {
                String ss = "Offline";
                ref.child(user).child("Code").setValue(null);
                v.setText(ss);
                setStudentsOffline();

            }

        });

    }



    private void student() {
        TextView text_for_student=findViewById(R.id.text_for_student);
        TextView tv = (TextView) findViewById(R.id.profName);
        tv.setText(fullName);

        goback = (Button)findViewById(R.id.go_back);

        goback.setOnClickListener(view -> {
            after_code.setVisibility(view.GONE);
            before_code.setVisibility(view.VISIBLE);
        });

        et_code.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (et_code.length() == 4) {  // if 4 digit otp condition true
                    validateCode(et_code);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        et_profID.addTextChangedListener(new TextWatcher() {


            // This method gets professor ID from text box and checks if the professor exists
            // If the professor exists the the student is added to the student list of that professor
            public void afterTextChanged(Editable s) {
                FirebaseDatabase fire = FirebaseDatabase.getInstance();
                DatabaseReference datRef = fire.getReference("ProfessorList");
                datRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println("[afterTextChangedListener]: " + snapshot);
                        if(snapshot.exists()){
                            for(DataSnapshot snap :  snapshot.getChildren()){
                                if(snap.getKey().contentEquals(et_profID.getText().toString())){

                                    StudenDataModelClass student = new StudenDataModelClass(fullName, "Offline");
                                    datRef.child(et_profID.getText().toString()).child("Student List").child("Names").child(user).setValue(student);
                                    et_profID.setText("");


                                }

                            }

                        }
                        else{
                            Toast.makeText(context, "Code not found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

    }

    //all_find function finds everything
    private void all_find() {
        et_code = findViewById(R.id.et_code);
        et_profID = findViewById(R.id.et_prof_txt);
        rel_student_layout = findViewById(R.id.rel_student_layout);
        rel_teacher_layout = findViewById(R.id.rel_teacher_layout);
        before_code = findViewById(R.id.before_code);
        after_code = findViewById(R.id.after_code);
    }

    //set_drawer helps to set everthing in top left corner
    private void set_drawer() {
        drawer = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);


        img_drawer = findViewById(R.id.img_drawer);

        img_drawer.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.LEFT);
            } else {
                drawer.openDrawer(Gravity.LEFT);
            }

        });

        TextView nav_logout = findViewById(R.id.nav_logout);
        TextView nav_all_student = findViewById(R.id.nav_all_student);
        if (!role.equals("Teacher")) {
            nav_all_student.setVisibility(View.GONE);
        }

        // logout code
        nav_logout.setOnClickListener(view -> {
            Intent intent = new Intent(context, LogInActivity.class);
            startActivity(intent);
            finish();
        });

        nav_all_student.setOnClickListener(view -> {
            Intent intent = new Intent(context, AllStudentActivity.class);
            startActivity(intent);
        });


    }

    // This gets "Code" from database and verifies it
    private void validateCode(EditText code){
        FirebaseDatabase node = FirebaseDatabase.getInstance();
        DatabaseReference reference = node.getReference("ProfessorList");
        String c = code.getText().toString();
        Query checkCode = reference.orderByChild("Code").equalTo(c);

        checkCode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    System.out.println("[Validate code]: snapshot:" + snapshot );

                    //Add professor here
                    String dCodeEarl = snapshot.child("Earl").child("Code").getValue(String.class);
                    String dCodeCorn = snapshot.child("CorneliusC").child("Code").getValue(String.class);
                    System.out.println("[Validate code]: Earls code: " + dCodeEarl);
                    System.out.println("[Validate code]: Cronlius' code " + dCodeCorn + "CornCode");
                    System.out.println("[Validate code]: typed code " +c);

                    for(DataSnapshot snap: snapshot.getChildren()){
                        if(snap.child("Code").getValue(String.class) != null && snap.child("Code").getValue(String.class).contentEquals(c)){
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("status","Online");
                            reference.child(snap.getKey()).child("Student List").child("Names").child(user).updateChildren(map);

                            before_code.setVisibility(View.GONE);
                            after_code.setVisibility(View.VISIBLE);


                            TextView tv = (TextView) findViewById(R.id.text_for_student);
                            String te = "Earl Roxwell's lecture - Present";
                            tv.setText(te);

                        }

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getLocation(String prof){

        Toast.makeText(context,"Checking Location",
                Toast.LENGTH_SHORT).show();

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);

        }

        location = (LocationManager) getApplication().getSystemService(LOCATION_SERVICE);
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1,MainActivity.this);
        getEuclideanDistance(prof);

    }

    public double getEuclideanDistance(String prof){
        String TAG = "value";
        double euclidean =-1;

        double y = longitude;
        double x = latitude;


        FirebaseDatabase fire = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = fire.getReference("ProfessorList").child(prof);
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    System.out.println("Hellooooooooooooo");

                    String coordinates = snapshot.child("co-ordinates").getValue(String.class);
                    String[] splitVals = coordinates.split(",");

                    double yy = Double.parseDouble(splitVals[0]);
                    double xx = Double.parseDouble(splitVals[1]);
                    System.out.println(splitVals[0]);
                    System.out.println(splitVals[1]);
                    assingVals(x,y,xx,yy);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println(x1 +" " + y1 + " " + x2 + " " + y2);

        return calcEuclid(x1,y1,x2,y2);
    }

    private double calcEuclid(double x1, double x2, double y1, double y2){
        return 0.0;

    }

    public void assingVals(double x11, double y11, double x22, double y22){
        x1 = x11;
        y1 = y11;
        x2 = x22;
        y2 = y22;

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Geocoder geocoder = new Geocoder(MainActivity.this,Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            System.out.println(address);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            System.out.println("Longitude: " + longitude + " Latitude: " +  latitude);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {

    }

    @Override
    public void onFlushComplete(int requestCode) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
