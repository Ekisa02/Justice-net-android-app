package com.example.justicenetapp;

import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    EditText emailedittext,passwordedittext;
    Button Loginaccount;
    ImageView imageView;
    ProgressBar progressbar;
    TextView SignUpbutton,txtnext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailedittext = findViewById(R.id.emailloginaccount);
        passwordedittext = findViewById(R.id.passwordloginaccount);
        Loginaccount = findViewById(R.id.btnloginaccount);
        imageView = findViewById(R.id.Imageviewlogin);
        progressbar = findViewById(R.id.loginprogressbar);
        txtnext=findViewById(R.id.txtnext);
        SignUpbutton = findViewById(R.id.btnloginaccountsignup);
        loadProfilePicture();

        Loginaccount.setOnClickListener(v -> Loginuser());
        SignUpbutton.setOnClickListener(v ->startActivity(new Intent(loginActivity.this, CreateAccountActivity.class)));
        txtnext.setOnClickListener(v ->startActivity(new Intent(loginActivity.this, HomepageActivity.class)));

    }
    private void loadProfilePicture() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String imageUrl = sharedPreferences.getString("profileImageUrl", null);
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.placeholder).into(imageView);
        }
    }

    void Loginuser(){

        String email = emailedittext.getText().toString();
        String password = passwordedittext.getText().toString();

        boolean isValidated = ValidateData(email,password);
        if (!isValidated){
            return;
        }
        LoginaccountFirebase(email,password);
    }
    void LoginaccountFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(loginActivity.this,HomepageActivity.class));

                    }
                    else {
                        Toast.makeText( loginActivity.this,"Email not verified,please verify!!",Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(loginActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressbar.setVisibility(View.VISIBLE);
            Loginaccount.setVisibility(View.GONE);
        }
        else {
            progressbar.setVisibility(View.GONE);
            Loginaccount.setVisibility(View.VISIBLE);
        }
    }
    boolean ValidateData(String email,String password){
        //validate the data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailedittext.setError("Email is invalid!");
            return false;
        }
        if (password.length()<6){
            passwordedittext.setError("Invalid password length!");
            return false;
        }
        return true;

    }

}