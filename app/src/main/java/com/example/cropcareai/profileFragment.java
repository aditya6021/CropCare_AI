package com.example.cropcareai;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cropcareai.model.UserModel;
import com.example.cropcareai.util.AndroidUtil;
import com.example.cropcareai.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class profileFragment extends Fragment {

    ImageView profileimg;
    TextView phnno;
    TextView username;
    Button updtbttn;
    ProgressBar profilePB;
    TextView logoutgbttn;
    UserModel currentuserModel;
    Uri selectedImageUri;
    ActivityResultLauncher<Intent> imagePickLauncher;


    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        phnno=view.findViewById(R.id.profile_phone);
        username=view.findViewById(R.id.profile_username);
        profilePB=view.findViewById(R.id.profile_progress_bar);
        updtbttn=view.findViewById(R.id.profile_update_bttn);
        logoutgbttn=view.findViewById(R.id.logout_bttn);
        getUserData();
        updtbttn.setOnClickListener((v -> {
            updateprofile();

        }));
        logoutgbttn.setOnClickListener(v -> {

                        FirebaseUtil.logout();
                        Intent intent=new Intent(getContext(), SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);



        });

        return view;

    }
    void updateprofile(){
        String newusername=username.getText().toString();
        if(newusername.isEmpty() || newusername.length() <3)
        {
            username.setError("user length should be at least 3 char");
            return;
        }
        currentuserModel.setUsername(newusername);
        setInProgress(true);
        updateTofirestore();

    }
    void updateTofirestore(){

        FirebaseUtil.currentUserDetails().set(currentuserModel).addOnCompleteListener(task -> {
            setInProgress(false);
            if(task.isSuccessful()){
                AndroidUtil.showToast(getContext(),"Updated Successfully");
            }
            else {
                AndroidUtil.showToast(getContext(),"Updated failed");
            }
        });
    }

    private void getUserData() {
        setInProgress(true);

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            currentuserModel=task.getResult().toObject(UserModel.class) ;
            username.setText(currentuserModel.getUsername());
            phnno.setText(currentuserModel.getPhone());
        });
    }
    void setInProgress(boolean inProgress)
    {
        if(inProgress) {
            profilePB.setVisibility(View.VISIBLE);
            updtbttn.setVisibility(View.GONE);
        }
        else {
            profilePB.setVisibility(View.GONE);
            updtbttn.setVisibility(View.VISIBLE);
        }

    }
}