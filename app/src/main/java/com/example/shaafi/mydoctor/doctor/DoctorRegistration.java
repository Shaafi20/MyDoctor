package com.example.shaafi.mydoctor.doctor;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shaafi.mydoctor.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctorRegistration extends AppCompatActivity {

    View focusView = null;
    private boolean bName, bUsername, bPassword, bRePassword, bSector;
    OkHttpClient client = new OkHttpClient();


    //binding the edit text variables
    @BindView(R.id.det_name)
    EditText dName;
    @BindView(R.id.det_username)
    EditText dUsername;
    @BindView(R.id.det_password)
    EditText dPassword;
    @BindView(R.id.det_rePassword)
    EditText dRePassword;
    @BindView(R.id.det_sectors)
    EditText dSectors;
    @BindView(R.id.registrationButton)
    Button dRegistrationButton;

    //binding the image view variables
    @BindView(R.id.div_name)
    ImageView diName;
    @BindView(R.id.div_username)
    ImageView diUsername;
    @BindView(R.id.div_password)
    ImageView diPassword;
    @BindView(R.id.div_rePassword)
    ImageView diRePassword;
    @BindView(R.id.div_sectors)
    ImageView diSectors;

    @BindView(R.id.reg_progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);
        setupActionBar();
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);
        attemptRegistration();
    }

    private void attemptRegistration() {

        //name check:
        nameCheck();

        //username check
        usernameCheck();

        //password check
        passwordCheck();

        //rePassword check
        rePasswordCheck();

        //sectors check
        sectorsCheck();
    }

    private void nameCheck() {
        dName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //dName.setError(null);
                    diName.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getBaseContext(), "has focus", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), "lost focus", Toast.LENGTH_SHORT).show();
                    checkNameIfEmpty();
                }
            }
        });
    }

    private void checkNameIfEmpty() {
        if (TextUtils.isEmpty(dName.getText().toString())) {
            dName.setError(getString(R.string.enter_full_name));
            focusView = dName;
            bName = false;
        } else {
            diName.setVisibility(View.VISIBLE);
            bName = true;
        }
    }

    private void usernameCheck() {
        dUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //dUsername.setError(null);
                    diUsername.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getBaseContext(), "has focus", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), "lost focus", Toast.LENGTH_SHORT).show();
                    checkUsernameIfEmptyAndUnique();
                }
            }
        });
    }

    private void checkUsernameIfEmptyAndUnique() {
        if (TextUtils.isEmpty(dUsername.getText().toString())) {
            dUsername.setError(getString(R.string.enter_username));
            focusView = dUsername;
            bUsername = false;
        } else {
            if (hasNetworkConnection()){
            checkIfUsernameExists(dUsername.getText().toString());
            }
            else {
                Toast.makeText(this, "Network connection failed!\nCheck your internet connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void passwordCheck() {
        dPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //dPassword.setError(null);
                    diPassword.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getBaseContext(), "has focus", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), "lost focus", Toast.LENGTH_SHORT).show();
                    checkPasswordIfEmpty();
                }
            }
        });
    }

    private void checkPasswordIfEmpty() {
        if (TextUtils.isEmpty(dPassword.getText().toString())) {
            dPassword.setError(getString(R.string.enter_password));
            focusView = dPassword;
            bPassword = false;
        } else {
            diPassword.setVisibility(View.VISIBLE);
            bPassword = true;
        }
    }

    private void rePasswordCheck() {
        dRePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //dRePassword.setError(null);
                    diRePassword.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getBaseContext(), "has focus", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), "lost focus", Toast.LENGTH_SHORT).show();
                    checkRepasswordIfEmptyAndSame();
                }
            }
        });
    }

    private void checkRepasswordIfEmptyAndSame() {
        if (TextUtils.isEmpty(dRePassword.getText().toString())) {
            dRePassword.setError(getString(R.string.reenter_password));
            focusView = dRePassword;
            bRePassword = false;
        } else {
            if (dRePassword.getText().toString().equals(dPassword.getText().toString())) {
                diRePassword.setVisibility(View.VISIBLE);
                bRePassword = true;
            }
            else {
                dRePassword.setError(getString(R.string.password_dont_match));
                bRePassword = false;
            }
        }
    }

    private void sectorsCheck() {
        dSectors.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //dSectors.setError(null);
                    diSectors.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getBaseContext(), "has focus", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getBaseContext(), "lost focus", Toast.LENGTH_SHORT).show();
                    checkSectorsIfEmpty();
                }
            }
        });
    }

    private void checkSectorsIfEmpty() {
        if (TextUtils.isEmpty(dSectors.getText().toString())) {
            dSectors.setError(getString(R.string.enter_sector));
            focusView = dSectors;
            bSector = false;
        } else {
            diSectors.setVisibility(View.VISIBLE);
            bSector = true;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private boolean hasNetworkConnection() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public void registerNewUser(View view) {

        checkNameIfEmpty();
        checkUsernameIfEmptyAndUnique();
        checkPasswordIfEmpty();
        checkRepasswordIfEmptyAndSame();
        checkSectorsIfEmpty();

        Log.i("DoctorRegistration.this", bName + " "
                 + bUsername + " "
                + bPassword + " "
                + bRePassword + " "
                + bSector + " ");

        if (bName && bUsername && bPassword && bRePassword && bSector){

            if (hasNetworkConnection()) {
                registerDoctor();
            }else {
                Toast.makeText(this, "Network connection failed!\nCheck your internet connection", Toast.LENGTH_LONG).show();
            }
        }
        else {
            focusView.requestFocus();
        }
    }

    private void registerDoctor() {
        //String url = "http://192.168.13.2/my_doctor/registerDoctor.php";
        String url = "http://www.mydoctorbd.cf/registerDoctor.php";
        Log.i("okhttp", "started");
        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .add("full_name", dName.getText().toString())
                .add("user_name", dUsername.getText().toString().toLowerCase())
                .add("password", dPassword.getText().toString().toLowerCase())
                .add("sectors", dSectors.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        warringAlert("Registration failed");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    Log.i("okhttp", "result = " + result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.equals("registered")) {
                                successAlert("Registration complete.Please login with your username");
                            }
                            else {
                                warringAlert("Sorry could not register,Please try again");
                            }
                        }
                    });

                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            warringAlert("There was some problem, please try again later");
                        }
                    });
                }
            }
        });
    }

    private void warringAlert(String alert){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Ups!!")
                .setMessage(alert)
                .setPositiveButton("OK", null);

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void successAlert(String alert){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Congrats!!")
                .setMessage(alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false);

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void checkIfUsernameExists(String username) {
//        String url = "http://192.168.13.2/my_doctor/checkUserAsDoctor.php";
        String url = "http://www.mydoctorbd.cf/checkUserAsDoctor.php";
        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .add("username", username)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        warringAlert("Registration failed");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    Log.i("okhttp", "result = " + result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.equals("not found")) {
                                diUsername.setVisibility(View.VISIBLE);
                                bUsername = true;
                                dUsername.setError(null);
                            }
                            else {
                                warringAlert("Sorry username already taken, use another one");
                                dUsername.setError("Enter unique username");
                            }
                        }
                    });

                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            warringAlert("There was some problem, please try again later");
                        }
                    });
                }
            }
        });
    }
}
