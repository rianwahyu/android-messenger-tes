package com.depobangunan.depomessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ResetPasswordActivity extends AppCompatActivity {

    ImageButton btnBackLogin;

    Button btnReset;

    FirebaseAuth firebaseAuth;

    MaterialEditText et_email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        btnBackLogin = findViewById(R.id.btnBackLogin);

        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                finish();
            }
        });

        btnReset = findViewById(R.id.btnConfirm);
        et_email = findViewById(R.id.et_email);

        firebaseAuth =  FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();

                if (email.equals("")){
                    Toast.makeText(getApplicationContext(),"Mohon Memasukkan Email", Toast.LENGTH_LONG).show();
                }else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Cek Email Anda", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                                finish();
                            }else {
                                String error = task.getException().getMessage().toString();
                                Toast.makeText(getApplicationContext(),error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
