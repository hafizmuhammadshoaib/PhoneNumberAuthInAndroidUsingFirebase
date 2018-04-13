package com.example.shoai.phonenumberauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifCodeActivity extends AppCompatActivity {
    String verifId;
    Button sendCodeButton;
    EditText codeEditText;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif_code);
        sendCodeButton = (Button) findViewById(R.id.sendBtn);
        codeEditText = (EditText) findViewById(R.id.editTextCode);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        verifId = intent.getStringExtra("verifId");
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = codeEditText.getText().toString();
                sendCodeButton.setEnabled(false);
                progressDialog = new ProgressDialog(VerifCodeActivity.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifId, code);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(VerifCodeActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(VerifCodeActivity.this, secondActivity.class);
                    startActivity(intent);
                    finish();


                }
                else {
                    sendCodeButton.setEnabled(true);
                    // Sign in failed, display a message and update the UI
                    Log.w("Invalid", "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Log.i("Invalid","The verification code entered was invalid");
                    }
                }
            }
        });
    }
}
