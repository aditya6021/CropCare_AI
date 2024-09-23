package com.example.cropcareai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcareai.model.UserModel;
import com.example.cropcareai.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUserNameActivity extends AppCompatActivity {

    EditText usernameInput;
    Button letmein;
    ProgressBar progressBar;
    String phoneNumber;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_name);
        usernameInput=findViewById(R.id.login_username);
        letmein=findViewById(R.id.let_me_in);
        progressBar=findViewById(R.id.login_progress_bar);
        phoneNumber= getIntent().getExtras().getString("phone");
        getUsername();
        letmein.setOnClickListener(v -> {
            setUsername();
        });

    }
    void setUsername()
    {

        String username=usernameInput.getText().toString();
        if(username.isEmpty() || username.length() <3)
        {
            usernameInput.setError("user length should be at least 3 char");
            return;
        }
        setInProgress(true);
        if(userModel != null)
        {
            userModel.setUsername(username);
        }
        else {
            userModel =new UserModel(phoneNumber,username, Timestamp.now(), FirebaseUtil.currentUserId());
        }
        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful())
                {
                    Intent intent =new Intent(LoginUserNameActivity.this ,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    void getUsername()
    {
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful())
                {
                    userModel=  task.getResult().toObject(UserModel.class);
                    if(userModel!=null){
                        usernameInput.setText(userModel.getUsername());
                    }
                }
            }
        });
    }
    void setInProgress(boolean inProgress)
    {
        if(inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            letmein.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            letmein.setVisibility(View.VISIBLE);
        }

    }
}