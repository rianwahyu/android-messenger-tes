package com.depobangunan.depomessenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText etUsername, etEmail, etPassword;
    Button btnRegister;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog prog;

    ImageButton btnBackLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        prog = new ProgressDialog(RegisterActivity.this);
        prog.setMessage("Create Account...");

        etUsername = (MaterialEditText) findViewById(R.id.et_username);
        etEmail = (MaterialEditText) findViewById(R.id.et_email);
        etPassword = (MaterialEditText) findViewById(R.id.et_password);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnBackLogin = findViewById(R.id.btnBackLogin);

        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_username = etUsername.getText().toString();
                String s_password = etPassword.getText().toString();
                String s_email = etEmail.getText().toString();

                if (TextUtils.isEmpty(s_username) || TextUtils.isEmpty(s_password) || TextUtils.isEmpty(s_email)){
                    Toast.makeText(RegisterActivity.this, "Mohon diisi semua ya", Toast.LENGTH_LONG).show();
                }else if (s_password.length() < 6){
                    Toast.makeText(RegisterActivity.this,"Password minimal 6 karakter", Toast.LENGTH_LONG).show();
                }else{
                    register(s_username,s_email,s_password);
                }
            }
        });

    }

    void register(final String username, String email, String password ){
        prog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("imageUrl", "default");
                            hashMap.put("status", "Offline");
                            hashMap.put("search", username.toLowerCase());

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        prog.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }else{
                            prog.dismiss();
                            Toast.makeText(RegisterActivity.this,"blm Sukses",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
