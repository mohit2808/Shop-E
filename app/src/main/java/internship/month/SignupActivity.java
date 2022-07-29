package internship.month;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    EditText name, email, contact, password, confirmPassword, dob, birthTime;
    Button login, signup;
    Calendar calendar;

    int iHour, iMinute;

    RadioButton male, female, transgender;
    RadioGroup gender;
    String sGender = "";

    Spinner spinner;
    String[] cityArray = {"Ahmedabad", "Vadodara", "Surat", "Rajkot"};
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String sCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Signup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.home_name);
        email = findViewById(R.id.home_email);
        contact = findViewById(R.id.home_contact);
        password = findViewById(R.id.home_password);
        confirmPassword = findViewById(R.id.home_confirm_password);
        dob = findViewById(R.id.home_dateofbirth);

        login = findViewById(R.id.home_login);
        signup = findViewById(R.id.home_signup);

        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                dob.setText(dateFormat.format(calendar.getTime()));
            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new DatePickerDialog(SignupActivity.this, dateClick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                DatePickerDialog dialog = new DatePickerDialog(SignupActivity.this, dateClick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                //Future Date Disable
                //dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });


        birthTime = findViewById(R.id.home_time);

        TimePickerDialog.OnTimeSetListener timeClick = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                iHour = i;
                iMinute = i1;

                String sAMPM;
                if (iHour > 12) {
                    iHour -= 12; //iHour = iHour-12
                    sAMPM = "PM";
                } else if (iHour == 12) {
                    sAMPM = "PM";
                } else if (iHour == 0) {
                    iHour = 12;
                    sAMPM = "AM";
                } else {
                    sAMPM = "AM";
                }

                String sMin;
                if (iMinute < 10) {
                    sMin = "0" + iMinute;
                } else {
                    sMin = String.valueOf(iMinute);
                }

                birthTime.setText(iHour + " : " + sMin + " " + sAMPM);
            }
        };

        birthTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(SignupActivity.this, timeClick, iHour, iMinute, false).show();
            }
        });

        /*male = findViewById(R.id.signup_male);
        female = findViewById(R.id.signup_female);
        transgender = findViewById(R.id.signup_transgender);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(SignupActivity.this,male.getText().toString());
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(SignupActivity.this,female.getText().toString());
            }
        });

        transgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(SignupActivity.this,transgender.getText().toString());
            }
        });*/

        gender = findViewById(R.id.signup_radiogroup);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                sGender = rb.getText().toString();
                new CommonMethod(SignupActivity.this, sGender);
            }
        });

        spinner = findViewById(R.id.signup_spinner);
        ArrayAdapter adapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_list_item_1, cityArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sCity = (String) adapterView.getItemAtPosition(i);
                //String s = cityArray[i];
                new CommonMethod(SignupActivity.this, sCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equalsIgnoreCase("")) {
                    name.setError("Name Required");
                } else if (email.getText().toString().trim().equals("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().matches(emailPattern)) {
                    email.setError("Valid Email Id Required");
                } else if (contact.getText().toString().trim().equals("")) {
                    contact.setError("Contact No. Required");
                } else if (contact.getText().toString().length() < 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                } else if (confirmPassword.getText().toString().trim().equals("")) {
                    confirmPassword.setError("Confirm Password Required");
                } else if (!password.getText().toString().matches(confirmPassword.getText().toString())) {
                    confirmPassword.setError("Password Does Not Match");
                } else if (dob.getText().toString().trim().equals("")) {
                    dob.setError("Please Select Date Of Birth");
                } else if (birthTime.getText().toString().trim().equals("")) {
                    birthTime.setError("Please Select Birth Time");
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(SignupActivity.this, "Please Select Gender");
                } else {
                    /*new CommonMethod(SignupActivity.this, "Signup Successfully");
                    onBackPressed();*/
                    if(new ConnectionDetector(SignupActivity.this).isConnectingToInternet()){
                        //new CommonMethod(SignupActivity.this,"Internet/Wifi Connected");
                        new signupData().execute();
                    }
                    else{
                        new ConnectionDetector(SignupActivity.this).connectiondetect();
                    }
                }
                /*else if(sGender.equals("")){
                    new CommonMethod(SignupActivity.this,"Please Select Gender");
                }
                else if(male.isChecked()==false && female.isChecked()==false && transgender.isChecked()==false){

                }*/
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class signupData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SignupActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("name",name.getText().toString());
            hashMap.put("email",email.getText().toString());
            hashMap.put("contact",contact.getText().toString());
            hashMap.put("password",password.getText().toString());
            hashMap.put("gender",sGender);
            hashMap.put("city",sCity);
            hashMap.put("dob",dob.getText().toString());
            hashMap.put("birth_time",birthTime.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSp.URL+"signup.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("Status")==true){
                    new CommonMethod(SignupActivity.this,object.getString("Message"));
                    onBackPressed();
                }
                else{
                    new CommonMethod(SignupActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new CommonMethod(SignupActivity.this,e.getMessage());
            }
        }
    }
}