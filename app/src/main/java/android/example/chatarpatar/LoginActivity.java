package android.example.chatarpatar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    TextView textView;
    EditText email,password;
    TextView signinbtn;
    FirebaseAuth auth;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait...");
        progressdialog.setCancelable(false);



        //move to regisstration page
        textView = findViewById(R.id.signup);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(i);

            }
        });


       email = findViewById(R.id.email);
       password = findViewById(R.id.password);
       signinbtn = findViewById(R.id.signin);
       String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

       signinbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               progressdialog.show();

               String gotemail = email.getText().toString();
               String gotpass = password.getText().toString();

               if(TextUtils.isEmpty(gotemail) || TextUtils.isEmpty(gotpass)){
                   progressdialog.dismiss();
                   Toast.makeText(LoginActivity.this, "Email & Password Required !!!", Toast.LENGTH_SHORT).show();
               }
               else if(!gotemail.matches(emailPattern)){
                   progressdialog.dismiss();
                   email.setError("Check Your Email Again ");
                   Toast.makeText(LoginActivity.this, "Invalid Email Id, Try Again ", Toast.LENGTH_SHORT).show();
               }
               else if(gotpass.length()<6){
                   progressdialog.dismiss();
                   password.setError("Check Password Again");
                   Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
               }

               else{
                   auth.signInWithEmailAndPassword(gotemail,gotpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful())
                           {
                               progressdialog.dismiss();
                               Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                               startActivity(i);
                           }
                           else{
                               progressdialog.dismiss();
                               Toast.makeText(LoginActivity.this, "Incorrect Email or Password", Toast.LENGTH_LONG).show();
                           }

                       }
                   });
               }

           }
       });












    }
}