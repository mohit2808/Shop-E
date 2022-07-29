package internship.month;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button login;
    TextView createAccount;
    EditText email, password;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSp.PREFERENCE,MODE_PRIVATE);
        login = findViewById(R.id.main_login);
        createAccount = findViewById(R.id.main_create_account);

        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().equals("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().matches(emailPattern)) {
                    email.setError("Valid Email Id Required");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                } else if (password.getText().toString().length() < 8) {
                    password.setError("Min. 8 Character Required");
                } else {
                    /*System.out.println("Login Successfully");
                    Log.d("RESPONSE", "Login Successfully");
                    Log.e("RESPONSE", "Login Successfully");
                    //Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                    new CommonMethod(MainActivity.this, "Login Successfully");
                    //Snackbar.make(view,"Login Successfully",Snackbar.LENGTH_LONG).show();
                    new CommonMethod(view, "Login Successfully");

                    sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                    new CommonMethod(MainActivity.this,HomeActivity.class);*/
                    if(new ConnectionDetector(MainActivity.this).isConnectingToInternet()){
                        new doLogin().execute();
                    }
                    else{
                        new ConnectionDetector(MainActivity.this).connectiondetect();
                    }
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Exit Alert");
        builder.setMessage("Are You Sure Want To Exit!");

        builder.setCancelable(false);

        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //finish();
                finishAffinity();
                //System.exit(0);
            }
        });
        builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                new CommonMethod(MainActivity.this,"Rate Us");
            }
        });
        builder.show();
    }

    private class doLogin extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("email",email.getText().toString());
            hashMap.put("password",password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSp.URL+"login.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("Status")==true){
                    new CommonMethod(MainActivity.this,object.getString("Message"));
                    JSONArray array = object.getJSONArray("UserData");
                    for (int i=0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        String sId = jsonObject.getString("id");
                        String sName = jsonObject.getString("name");
                        String sEmail = jsonObject.getString("email");
                        String sContact = jsonObject.getString("contact");
                        String sGender = jsonObject.getString("gender");
                        String sCity = jsonObject.getString("city");
                        String sDob = jsonObject.getString("dob");
                        String sBirthTime = jsonObject.getString("birth_time");

                        sp.edit().putString(ConstantSp.ID,sId).commit();
                        sp.edit().putString(ConstantSp.NAME,sName).commit();
                        sp.edit().putString(ConstantSp.EMAIL,sEmail).commit();
                        sp.edit().putString(ConstantSp.CONTACT,sContact).commit();
                        sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                        sp.edit().putString(ConstantSp.CITY,sCity).commit();
                        sp.edit().putString(ConstantSp.DOB,sDob).commit();
                        sp.edit().putString(ConstantSp.BIRTH_TIME,sBirthTime).commit();

                        new CommonMethod(MainActivity.this,HomeActivity.class);
                    }
                }
                else{
                    new CommonMethod(MainActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new CommonMethod(MainActivity.this,e.getMessage());
            }
        }
    }
}