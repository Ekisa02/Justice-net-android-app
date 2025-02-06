package com.example.justicenetapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;


public class CreateAccountActivity extends AppCompatActivity {
    EditText emailedittext,passwordedittext,confirmpasswordedittext;
    ImageView imageView;
    Button createaccount;
    ProgressBar progressbar;
    TextView loginbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        emailedittext = findViewById(R.id.emailcreateaccount);
        passwordedittext = findViewById(R.id.passwordcreateaccount);
        confirmpasswordedittext = findViewById(R.id.confirmpasswordcreateaccount);
        createaccount = findViewById(R.id.btncreateaccount);
        progressbar = findViewById(R.id.createaccountprogressbar);
        loginbutton = findViewById(R.id.btncreateaccountlogin);
        imageView = findViewById(R.id.createaccountimageview);
        createaccount.setOnClickListener(v -> createAccount());
        loginbutton.setOnClickListener(v -> finish());
        loadProfilePicture();

        imageView.setOnClickListener(v -> startActivity(new Intent(CreateAccountActivity.this, ProfileActivity.class)));

    }
    private void loadProfilePicture() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String imageUrl = sharedPreferences.getString("profileImageUrl", null);
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.placeholder).into(imageView);
        }
    }
    void createAccount(){
        String email = emailedittext.getText().toString();
        String password = passwordedittext.getText().toString();
        String confirmpassword = confirmpasswordedittext.getText().toString();

        boolean isValidated = ValidateData(email,password,confirmpassword);
        if (!isValidated){
            return;
        }
        createAccountInFirebase(email,password);
    }
    void createAccountInFirebase(String email,String password){
    changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    Toast.makeText(CreateAccountActivity.this,"Successfully created an account please check email to verify!",Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }
                else {
                    Toast.makeText(CreateAccountActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressbar.setVisibility(View.VISIBLE);
            createaccount.setVisibility(View.GONE);
        }
        else {
            progressbar.setVisibility(View.GONE);
            createaccount.setVisibility(View.VISIBLE);
        }
    }
    boolean ValidateData(String email,String password,String confirmpassword){
        //validate the data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailedittext.setError("Email is invalid!");
            return false;
        }
        if (password.length()<6){
            passwordedittext.setError("Invalid password length!");
            return false;
        }
        if (!password.equals(confirmpassword)){
            confirmpasswordedittext.setError("Password don`t match!");
            return false;
        }
        return true;

    }
}