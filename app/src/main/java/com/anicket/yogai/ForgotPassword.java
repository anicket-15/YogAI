package com.anicket.yogai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText mEmail;
    Button mResetBtn;
    TextInputLayout editTextEmail;
    FirebaseAuth fAuth;

    private boolean validateEmail(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        fAuth=FirebaseAuth.getInstance();
        mEmail=findViewById(R.id.email);
        editTextEmail=findViewById(R.id.editTextEmail);
        mResetBtn=findViewById(R.id.forgotPasswordBtn);

        //Reset Button
        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();

                //Validation Check
                if(!validateEmail(email)){
                    editTextEmail.setError("Valid Email is required");
                    return;
                }
                //Send Reset Password Link
                fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ForgotPassword.this, "Password Reset Link Sent to your Mail "+email, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPassword.this,LoginActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPassword.this, "Reset Link Not Sent "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

    public void backButton(View view) {
        finish();
    }

    public void buttonSignIn(View view) {
        startActivity(new Intent(ForgotPassword.this,LoginActivity.class));
        finish();
    }
}