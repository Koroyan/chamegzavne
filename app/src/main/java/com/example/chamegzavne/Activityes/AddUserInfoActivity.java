package com.example.chamegzavne.Activityes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chamegzavne.InfoClass.User;
import com.example.chamegzavne.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class AddUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    IntlPhoneInput phoneInputView;
    TextInputLayout mail;
    TextInputLayout web;
    ImageView checkBtn;
    TextView phoneErrorMessage;
    Spinner countrySpnr;

    boolean mailIsValid=true;

    DatabaseReference users;
    User user;

    Toolbar toolbar;

    boolean hasInfo=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_info);

        toolbar=findViewById(R.id.add_user_info_toolbar_id);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        init();
        countrySpinnerInit();
        textInputError();
        users=FirebaseDatabase.getInstance().getReference("users");




    }
    private void init(){
        phoneInputView = findViewById(R.id.add_user_info_phone_id);
        mail = findViewById(R.id.add_user_info_mail_id);
        web = findViewById(R.id.add_user_info_web_id);
        checkBtn = findViewById(R.id.add_user_info_check_btn);
        phoneErrorMessage=findViewById(R.id.add_user_info_phone_error_id);
        checkBtn.setOnClickListener(this);
        phoneInputView.setDefault();
        hasInfo=getIntent().getBooleanExtra(MainActivity.INTENT_HAS_INFO_KEY,true);
        if(!hasInfo)
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_user_info_check_btn:
                if(!phoneInputView.isValid()){
                    Toast.makeText(AddUserInfoActivity.this,"გთხოვთ გაასწორეთ ინფორმაცია",Toast.LENGTH_LONG).show();
                    phoneErrorMessage.setText("*");
                    break;
                }else{phoneErrorMessage.setText(null);}
                if(!mailIsValid){
                    Toast.makeText(AddUserInfoActivity.this,"გთხოვთ გაასწორეთ ინფორმაცია",Toast.LENGTH_LONG).show();
                    break;
                }
                user = new User(MainActivity.userName
                        , phoneInputView.getNumber()
                        , mail.getEditText().getText().toString()
                        , countrySpnr.getSelectedItem().toString()
                        , MainActivity.userID
                        ,web.getEditText().getText().toString()
                        ,MainActivity.userProfilePhoto.toString());
                users.child(MainActivity.userID + "/" + MainActivity.userName).setValue(user);
                finish();
                break;
                default:
                    break;
        }

    }

    private void textInputError(){
        mail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("mailerror", "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("mailerror", "onTextChanged: ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()) {
                    if (!validMail(s.toString())) {
                        mail.setError("არაკორექტული მაილი");
                        mailIsValid = false;
                    } else {
                        mailIsValid = true;
                        mail.setError(null);
                    }
                }
                else{mail.setError(null);}

            }
        });
    }

   Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    boolean validMail(String input) {
       return VALID_EMAIL_ADDRESS_REGEX.matcher(input).find();

    }

    void countrySpinnerInit(){
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        countries.add("country");
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        countrySpnr = (Spinner)findViewById(R.id.add_user_info_country_spinner_id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        countrySpnr.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            if(hasInfo) {
                onBackPressed();
                finish();
                return super.onOptionsItemSelected(item);
            }
            else{
                Toast.makeText(this, "დამადებითი ინფორმაცია აუცილებელია", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(hasInfo) {
                onBackPressed();
                finish();
                return super.onKeyDown(keyCode, event);

            }
            else{
                Toast.makeText(this, "დამადებითი ინფორმაცია აუცილებელია", Toast.LENGTH_LONG).show();
            }


        }
        return false;
    }

    }

