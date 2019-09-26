package com.example.chamegzavne.Activityes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.chamegzavne.InfoClass.hotLine;
import com.example.chamegzavne.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout mail;
    TextInputLayout message;
    ImageView sendBtn;

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    boolean mailIsValid=false;
    boolean messageIsValid=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        init();
        inputError();
    }
    private void init(){
        mail=findViewById(R.id.contact_us_mail_id);
        message=findViewById(R.id.contact_us_message_id);
        sendBtn=findViewById(R.id.contact_us_send_btn);
        sendBtn.setOnClickListener(this);
        bottomNavigationViewInit();
        toolbarInit();
    }
    private void bottomNavigationViewInit(){
        bottomNavigationView = findViewById(R.id.contact_us_nav_bar);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navigation_messages:
                        Intent intent = new Intent(ContactUsActivity.this, PostListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        menuItem.setChecked(true);
                        return true;
                    case R.id.navigation_profile:
                        Intent intent1=new Intent(ContactUsActivity.this,UserProfileActivity.class);
                        intent1.putExtra(MainActivity.INTENT_PROFILE_KEY,MainActivity.userID);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        menuItem.setChecked(true);

                        return true;
                    case R.id.navigation_map:
                        Intent intent2=new Intent(ContactUsActivity.this,MainActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        menuItem.setChecked(true);
                }
                return false;
            }
        });
    }
    private void toolbarInit(){
        toolbar=findViewById(R.id.contact_us_toolbar_id);
        setSupportActionBar(toolbar);

        //setActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void inputError(){
        mail.setError("*");
        mail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!validMail(s.toString())){
                    mail.setError("*");
                    mailIsValid=false;
                }
                else{
                    mail.setError(null);
                    mailIsValid=true;
                }

            }
        });
        message.setError("*");
        message.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    message.setError("*");
                    messageIsValid=false;
                }
                else{
                    message.setError(null);
                    messageIsValid=true;
                }

            }
        });
    }

    Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    boolean validMail(String input) {
        return VALID_EMAIL_ADDRESS_REGEX.matcher(input).find();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.contact_us_send_btn:
                if(mailIsValid && messageIsValid){
                    DatabaseReference hotLineReference= FirebaseDatabase.getInstance().getReference("hotLine").child("messages");
                    hotLineReference.child(MainActivity.userID)
                            .setValue(new hotLine(
                                    mail.getEditText().getText().toString()
                                    ,message.getEditText().getText().toString()
                                    ,MainActivity.userName
                                    ,MainActivity.userID));
                    finish();
                }
                else{
                    Toast.makeText(this, "გთხოვთ შეავსეთ ველები!!!", Toast.LENGTH_SHORT).show();
                }
                return;
                default:
                    return;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            onBackPressed();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
