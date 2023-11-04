package com.example.aichatbot;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aichatbot.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignActivity extends AppCompatActivity {

    //로그인
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private EditText mEt_RegId;
    private EditText mEt_RegPW;
    private EditText mEt_RegName;
    private EditText mEt_Age;
    private Button   mBtn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        FindViewID();
        SigninListener();



    }


    private void FindViewID()
    {
        mEt_RegId   = (EditText) findViewById(R.id.et_register_id);
        mEt_RegPW   = (EditText) findViewById(R.id.et_register_pw);
        mEt_RegName = (EditText) findViewById(R.id.et_register_nickname);
        mEt_Age     = (EditText) findViewById(R.id.et_register_age);
        mBtn_register = (Button) findViewById(R.id.btn_register_button);

    }

    //Click Listener
    private void SigninListener()
    {
        mBtn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEt_RegId.getText().toString().trim();
                final String password = mEt_RegPW.getText().toString().trim();
                final String nickname = mEt_RegName.getText().toString().trim();
                final String age = mEt_Age.getText().toString().trim();

                if ((email != null) && !email.isEmpty() && (password != null) && !password.isEmpty() && (nickname != null) && !nickname.isEmpty() && (age != null) && !age.isEmpty() ) {
                    mAuth.createUserWithEmailAndPassword(mEt_RegId.getText().toString(), mEt_RegPW.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful())
                                    {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put(FirebaseID.documentId, user.getUid());
                                        userMap.put(FirebaseID.email, email);
                                        userMap.put(FirebaseID.password, password);
                                        userMap.put(FirebaseID.nickname, nickname);
                                        userMap.put(FirebaseID.age, age);
                                        //현재 유저의 Uid를 이름으로 한 document 생성. 이게 없으면 사용자 컨텐츠의 이륾과 사용자id이름이 달라 사용하기 힘듬
                                        mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge());

                                        //회원가입 성공시 로그인 액티비티로 화면 전환
                                        Intent intent = new Intent(SignActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(SignActivity.this,"회원가입 오류" ,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

}