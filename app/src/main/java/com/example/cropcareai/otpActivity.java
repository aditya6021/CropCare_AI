package com.example.cropcareai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcareai.util.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class otpActivity extends AppCompatActivity {

    String phoneNumber;
    EditText otpInput;
    Button nextBttn;
    ProgressBar progressBar;
    TextView resendOtpTextView;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    Long timeoutseconds =60L;
    String Verificationcode;
    PhoneAuthProvider.ForceResendingToken ResendingToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpInput=findViewById(R.id.login_otp);
        nextBttn=findViewById(R.id.verify_otp_button);
        progressBar=findViewById(R.id.login_progress_bar);
        resendOtpTextView=findViewById(R.id.resend_otp_textview);
        phoneNumber=getIntent().getExtras().getString("phone");
        senOtp(phoneNumber,false);
        nextBttn.setOnClickListener(v->{
            String enteredOtp=otpInput.getText().toString();
            PhoneAuthCredential credential= PhoneAuthProvider.getCredential(Verificationcode,enteredOtp);
            signIn(credential);
            setInProgress(true);

        });
        resendOtpTextView.setOnClickListener(v -> {
            senOtp(phoneNumber,true);
        });

    }
    void senOtp(String phoneNumber,boolean isResend)
    {
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder=
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeoutseconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtil.showToast(getApplicationContext(),"OTP verfication failed");
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                Verificationcode=s;
                                ResendingToken=forceResendingToken;
                                AndroidUtil.showToast(getApplicationContext(),"OTP send successfully ");
                                setInProgress(false);

                            }
                        });
        if(isResend)
        {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());

        }
        else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }
    void setInProgress(boolean inProgress)
    {
        if(inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            nextBttn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            nextBttn.setVisibility(View.VISIBLE);
        }

    }
    void signIn(PhoneAuthCredential phoneAuthCredential)
    {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    Intent intent =new Intent(otpActivity.this,LoginUserNameActivity.class);
                    intent.putExtra("phone",phoneNumber);
                    startActivity(intent);
                } else {
                    AndroidUtil.showToast(getApplicationContext(),"OTP verification Failed");
                }
            }
        });
    }
    void startResendTimer()
    {
        resendOtpTextView.setEnabled(false);
        Timer timer =new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutseconds--;
                resendOtpTextView.setText("Resend OTP in " + timeoutseconds+" seconds");
                if(timeoutseconds<=0)
                {
                    timeoutseconds=60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        resendOtpTextView.setEnabled(true);
                    });
                }

            }
        },0,1000);
    }
}