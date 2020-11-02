package com.anicket.yogai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText mEmail,mPassword1;
    Button mSignInBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail=findViewById(R.id.email);
        mPassword1=findViewById(R.id.password1);
        mSignInBtn=findViewById(R.id.btnSignIn);

        fAuth=FirebaseAuth.getInstance();

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password1=mPassword1.getText().toString().trim();

                //Validation Check
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password1)){
                    mPassword1.setError("Password is required");
                    return;
                }
                if(password1.length()<5 ){
                    mPassword1.setError("Password must be greater than 4 digit");
                    return;
                }

                //Authenticate User
                fAuth.signInWithEmailAndPassword(email,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Logged-in Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),TemporaryActivity.class));
                        }
                        else {
                            mPassword1.setError(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });
            }
        });


    }

    public void backButton(View view) {
        finish();
    }
}