package com.example.cropcareai;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcareai.model.UserModel;
import com.example.cropcareai.util.AndroidUtil;
import com.example.cropcareai.util.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(FirebaseUtil.isLoggedIn() && getIntent().getExtras() != null)
        {
            String userId=getIntent().getExtras().getString("userId");
            FirebaseUtil.allUserCollectionReferences().document(userId).get().addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    UserModel
                            model = task.getResult().toObject(UserModel.class);
                    Intent mainintent=new Intent(this,MainActivity.class);
                    mainintent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(mainintent);
                    Intent intent =new Intent(this, MainActivity.class);
                    AndroidUtil.passUserModelAsIntent(intent,model);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
            });

        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(FirebaseUtil.isLoggedIn())
                    {
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    }else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));



                    }
                    finish();
                }
            },1000);
        }
    }
}