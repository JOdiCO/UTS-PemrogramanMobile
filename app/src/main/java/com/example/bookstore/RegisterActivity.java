package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText TxEmail, TxUsername, TxPassword;
    Button BtnRegister;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        TxEmail = (EditText)findViewById(R.id.txEmailReg);
        TxPassword = (EditText)findViewById(R.id.txPasswordReg);
        TxUsername = (EditText)findViewById(R.id.txUsernameReg);
        BtnRegister = (Button)findViewById(R.id.btnRegister);

        TextView tvRegister = (TextView)findViewById(R.id.tvRegister);

        tvRegister.setText(fromHtml("Back to " +
                "</font><font color='#3b5998'>Login</font>"));

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = TxEmail.getText().toString().trim();
                String username = TxUsername.getText().toString().trim();
                String password = TxPassword.getText().toString().trim();

                ContentValues values = new ContentValues();


                if (email.equals("")){
                    TxEmail.setError("Email Tidak Boleh Kosong!");
                }
                if (username.equals("")) {
                    TxUsername.setError("Nama Tidak Boleh Kosong!");
                }
                if (password.equals("")) {
                    TxPassword.setError("Password Tidak Boleh Kosong!");
                }else if (TxPassword.length() < 6) {
                    TxPassword.setError("Password Harus 6 Karakter atau Lebih!");
                }else {
                    values.put(DBHelper.row_Email, email);
                    values.put(DBHelper.row_username, username);
                    values.put(DBHelper.row_password, password);
                    dbHelper.insertData(values);

                    Toast.makeText(RegisterActivity.this, "Berhasil membuat akun, silahkan Login", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    public static Spanned fromHtml(String html){
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}