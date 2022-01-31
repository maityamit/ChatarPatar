package android.example.chatarpatar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {
    
    TextView textView;
    CircleImageView imageView;
    EditText regname,regemail,regpass,regcnfrmpass;
    TextView regsignupbtn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    Uri imageuri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imagetokenuri;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
     textView = findViewById(R.id.signin);
     textView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
             startActivity(i);
         }
     });
     auth = FirebaseAuth.getInstance();
     database =FirebaseDatabase.getInstance();
     storage = FirebaseStorage.getInstance();


     imageView = findViewById(R.id.profile_image);
     regname = findViewById(R.id.regname);
     regemail = findViewById(R.id.regemail);
     regpass = findViewById(R.id.regpass);
     regcnfrmpass = findViewById(R.id.regconfrmpass);
     regsignupbtn = findViewById(R.id.regsignupbtn);


     progressdialog = new ProgressDialog(this);
     progressdialog.setMessage("Please Wait...");
     progressdialog.setCancelable(false);



      regsignupbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              progressdialog.show();
                    String gotregname = regname.getText().toString();
                    String gotregemail = regemail.getText().toString();
                    String gotregpass = regpass.getText().toString();
                    String gotregcnfrmpass = regcnfrmpass.getText().toString();
                   String status="Hey I am Available to Talk ";



                    if(TextUtils.isEmpty(gotregname) || TextUtils.isEmpty(gotregemail) || TextUtils.isEmpty(gotregpass) || TextUtils.isEmpty(gotregcnfrmpass)){
                        progressdialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Something is Missing", Toast.LENGTH_SHORT).show();
                    }
                    else if(!gotregemail.matches(emailPattern))
                    {
                        progressdialog.dismiss();
                        regemail.setError("Check Your Email Again ");
                        Toast.makeText(RegistrationActivity.this, "Please Enter Correct Email Address", Toast.LENGTH_SHORT).show();
                    }

                    else if(!(gotregpass.length()>6)){
                        progressdialog.dismiss();
                        regpass.setError("Password Must Greater than 6 Characters");
                    }

                    else if(!gotregpass.equals(gotregcnfrmpass)   ){
                        progressdialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Password Does not Matched", Toast.LENGTH_SHORT).show();
                    }


                    else{
                        // firebase create new user
                        auth.createUserWithEmailAndPassword(gotregemail,gotregpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(RegistrationActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                                    DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                    StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                    if(imageuri!=null)
                                    {

                                        storageReference.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                if(task.isSuccessful()){
                                                  storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                      @Override
                                                      public void onSuccess(Uri uri) {
                                                         imagetokenuri = uri.toString();
                                                         Users users = new Users(auth.getUid(),gotregname,gotregemail,imagetokenuri,status);
                                                         reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {
                                                                 if(task.isSuccessful())
                                                                 {
                                                                     progressdialog.dismiss();
                                                                     Intent i = new Intent(RegistrationActivity.this,HomeActivity.class);
                                                                     startActivity(i);
                                                                 }
                                                                 else {
                                                                     progressdialog.dismiss();
                                                                     Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                                 }
                                                             }
                                                         });
                                                      }

                                                  });
                                                }
                                            }
                                        });

                                    }
                                    else{




                                        imagetokenuri = "https://firebasestorage.googleapis.com/v0/b/chatar-patar-3a77f.appspot.com/o/filename.jpg?alt=media&token=0ae39099-add8-49b2-a0c2-2e1851eb72f5";
                                        Users users = new Users(auth.getUid(),gotregname,gotregemail,imagetokenuri,status);
                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    progressdialog.dismiss();
                                                    Intent intent = new Intent(RegistrationActivity.this,HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                                else{
                                                    progressdialog.dismiss();
                                                    Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                }
                                else{
                                    progressdialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                }



                            }
                        });
                    }
          }
      });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(data!=null)
            {
                imageuri=data.getData();
                imageView.setImageURI(imageuri);
            }
        }
    }
}