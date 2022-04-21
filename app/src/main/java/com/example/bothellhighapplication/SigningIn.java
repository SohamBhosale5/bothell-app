package com.example.bothellhighapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//SigningIn Fragment
public class SigningIn extends AppCompatActivity {
    Window window;
    Button btn_login;
    EditText et_email;
    EditText et_password;
    String email;
    String password;
    private FirebaseAuth mAuth;
    @Override
    //Creating view for onCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.bothell_blue));
        }
        setContentView(R.layout.activity_signing_in);
        et_email = findViewById(R.id.edit_text_email);
        et_password = findViewById(R.id.edit_text_password);
        if(Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        btn_login = (Button)findViewById(R.id.ButtonLogin);
        mAuth = FirebaseAuth.getInstance();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if(email.isEmpty() || email.equals("")) {
                    Toast.makeText(SigningIn.this, "Email required", Toast.LENGTH_LONG).show();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(SigningIn.this, "Enter a Valid Email", Toast.LENGTH_LONG).show();
                } else if(password.isEmpty() || password.equals("")) {
                    Toast.makeText(SigningIn.this, "Password required", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String userID = mAuth.getCurrentUser().getUid();
                                Intent intent = new Intent(SigningIn.this, NavigationDrawer.class);
                                intent.putExtra("id", userID);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SigningIn.this, "Failed to Login Please Check Your Credentials", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


    }
}


