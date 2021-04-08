package com.depobangunan.depomessenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText etEmail, etPassword;
    TextView textDaftar;
    Button btnLogin;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    TextView textForgot;

    ProgressDialog prog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prog = new ProgressDialog(LoginActivity.this);
        prog.setMessage("Check email & password...");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser !=null){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        etEmail = (MaterialEditText) findViewById(R.id.et_email);
        etPassword = (MaterialEditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        textDaftar = (TextView) findViewById(R.id.textDaftar);
        textForgot = (TextView) findViewById(R.id.text_forgot);

        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_email = etEmail.getText().toString();
                String s_password = etPassword.getText().toString();

                if (TextUtils.isEmpty(s_email) || TextUtils.isEmpty(s_password)) {
                    Toast.makeText(LoginActivity.this,"Mohon diisi kabeh", Toast.LENGTH_LONG).show();
                }else {
                    prog.show();
                    firebaseAuth.signInWithEmailAndPassword(s_email,s_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        prog.dismiss();
                                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        prog.dismiss();
                                        Toast.makeText(LoginActivity.this,"Gagal",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        textDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}