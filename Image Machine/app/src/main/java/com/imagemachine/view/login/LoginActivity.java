package com.imagemachine.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.imagemachine.R;
import com.imagemachine.utils.CustomToast;
import com.imagemachine.view.dashboard.DashboardActivity;

public class LoginActivity extends AppCompatActivity {

    private String strUsername;
    private EditText edtUsername;
    private Button btnLogin;
    private CustomToast customToast = new CustomToast();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsernameLogin);
        btnLogin = findViewById(R.id.btnLogin);

        SharedPreferences sharedPreferences = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (!username.isEmpty()) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(v -> {
            if (!checkUsername()) {

            } else {
                SharedPreferences sharedPreferences2 = this.getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences2.edit();
                editor.putString("username", strUsername);
                editor.commit();

                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            }
        });
    }

    public boolean checkUsername() {
        strUsername = edtUsername.getText().toString();

        if (strUsername.isEmpty()) {
            customToast.customToast(this, "Username cannot be empty!");
            return false;
        }

        return true;
    }
}