package com.anicket.yogai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    //Variable Declaration
    EditText mEmail,mPassword1,mPassword2;
    Button mRegisterBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail=findViewById(R.id.email);
        mPassword1=findViewById(R.id.password1);
        mPassword2=findViewById(R.id.password2);
        mRegisterBtn=findViewById(R.id.buttonSignIn);

        //Firebase Auth Instance
        fAuth=FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password1=mPassword1.getText().toString().trim();
                String password2=mPassword2.getText().toString().trim();

                //Validation Check
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password1)){
                    mPassword1.setError("Password is required");
                    return;
                }
                if(TextUtils.isEmpty(password2)){
                    mPassword2.setError("Password is required");
                    return;
                }
                if(password1.length()<5 ){
                    mPassword1.setError("Password must be greater than 4 digit");
                    return;
                }
                if(!(password1.equals(password2))){
                    mPassword2.setError("Password not matched ");
                    return;
                }

                //Register the user in Firebase
                fAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),TemporaryActivity.class));
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
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