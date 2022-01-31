package android.example.chatarpatar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView MainUserRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView imglogout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

          auth = FirebaseAuth.getInstance();
          database =FirebaseDatabase.getInstance();
          usersArrayList = new ArrayList<>();
          imglogout = findViewById(R.id.logout);




          imglogout.setOnClickListener(new View.OnClickListener() {
             @Override
              public void onClick(View v) {
                 Dialog dialog = new Dialog(HomeActivity.this,R.style.Dialogbox);

                  dialog.setContentView(R.layout.dialog_layout);

                 TextView Yeslogout ,Nologout;

                 Yeslogout = dialog.findViewById(R.id.yesbuttonlogout);
                 Nologout = dialog.findViewById(R.id.nobuttonlogout);

                  Yeslogout.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          FirebaseAuth.getInstance().signOut();
                          Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                          startActivity(intent);
                      }
                  });


                  Nologout.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          dialog.dismiss();
                      }
                  });



                  dialog.show();


              }
          });


        DatabaseReference reference = database.getReference().child("user");
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot dataSnapshot : snapshot.getChildren())
        {
            Users users = dataSnapshot.getValue(Users.class);
            usersArrayList.add(users);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});


          MainUserRecyclerView = findViewById(R.id.mainuserrecyclerview);
          MainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
          adapter = new UserAdapter(HomeActivity.this,usersArrayList);
          MainUserRecyclerView.setAdapter(adapter);





        if(auth.getCurrentUser()==null){

            Intent i = new Intent(HomeActivity.this, RegistrationActivity.class);
            startActivity(i);

        }


    }
}