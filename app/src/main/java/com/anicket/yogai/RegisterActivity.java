package com.anicket.yogai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    //Variable Declaration
    EditText mEmail,mPassword1,mPassword2;
    Button mRegisterBtn;
    TextInputLayout editTextEmail, editTextPassword, editTextConfirmPassword;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    private boolean validateEmail(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail=findViewById(R.id.email);
        mPassword1=findViewById(R.id.password1);
        mPassword2=findViewById(R.id.password2);
        mRegisterBtn=findViewById(R.id.buttonSignIn);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        progressBar = findViewById(R.id.progressBar);

        //Firebase Auth Instance
        fAuth=FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password1=mPassword1.getText().toString().trim();
                String password2=mPassword2.getText().toString().trim();

                //Validation Check
                if(!validateEmail(email)){
                    editTextEmail.setError("Valid Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password1)){
                    editTextPassword.setError("Password is required");
                    return;
                }
                if(TextUtils.isEmpty(password2)){
                    editTextConfirmPassword.setError("Password is required");
                    return;
                }
                if(password1.length()<5 ){
                    editTextPassword.setError("Password must not be less than 5 characters");
                    return;
                }
                if(!(password1.equals(password2))){
                    editTextConfirmPassword.setError("Password's do not match ");
                    return;
                }

                //Progress Bar
                progressBar.setVisibility(View.VISIBLE);

                //Register the user in Firebase
                fAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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