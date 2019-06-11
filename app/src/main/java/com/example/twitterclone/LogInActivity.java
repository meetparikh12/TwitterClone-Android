package com.example.twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtLogInEmail, edtLogInPassword;
    private Button btnLogIn,btn2SignUp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.log_in);
        setContentView(R.layout.activity_log_in);
        if(ParseUser.getCurrentUser() != null){
            ParseUser.logOut();
        }
        edtLogInEmail = findViewById(R.id.edtLogInEmail);
        edtLogInPassword = findViewById(R.id.edtLogInPassword);
        edtLogInPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnLogIn);
                }
                return false;
            }
        });
        btn2SignUp = findViewById(R.id.btn2SignUp);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(LogInActivity.this);
        btn2SignUp.setOnClickListener(LogInActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnLogIn:
                String userEmail = edtLogInEmail.getText().toString();
                String userPassword = edtLogInPassword.getText().toString();
                if (userEmail.equals("") || userPassword.equals("")) {
                    FancyToast.makeText(LogInActivity.this,"Please Enter all the details!",Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                }
                else {
                    progressDialog = new ProgressDialog(LogInActivity.this);
                    progressDialog.setMessage("Logging in...");
                    progressDialog.show();
                    ParseUser.logInInBackground(userEmail, userPassword, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                FancyToast.makeText(LogInActivity.this, user.get("username") + " " +
                                        "logged in successfully", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                transitionToSocialMediaActivity();
                            } else {
                                FancyToast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                            }
                            progressDialog.dismiss();

                        }
                    });
                }
                break;
            case R.id.btn2SignUp:
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
                finish();
                break;
        }
    }
    public void rootLayoutTapped(View view){
        try{
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void transitionToSocialMediaActivity(){
        Intent intent = new Intent(LogInActivity.this,TwitterUsers.class);
        startActivity(intent);
        finish();
    }
}

