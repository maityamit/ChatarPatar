package android.example.chatarpatar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    String ReceiverImage,ReceiverName,ReceiverUid,SenderUId;
    CircleImageView profileimage;
    TextView receivername;
    FirebaseDatabase database;
    FirebaseAuth firebaseauth;
    public static String senderImage;
    public static String receiverImage;

    String SenderRoom,ReceiverRoom;


    CardView sendbtn;
    EditText editmsg;

    RecyclerView MessageAdapter;

    ArrayList<Messages> messagesArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        firebaseauth = FirebaseAuth.getInstance();

        ReceiverImage = getIntent().getStringExtra("ReceiverImage");
        ReceiverName = getIntent().getStringExtra("name");
        ReceiverUid = getIntent().getStringExtra("userid");


        messagesArrayList = new ArrayList<Messages>();


        profileimage = findViewById(R.id.chatprofile);

        MessageAdapter =findViewById(R.id.messageadapter);

        sendbtn = findViewById(R.id.sendbtn);
        editmsg = findViewById(R.id.editmsg);



        Picasso.get().load(ReceiverImage).into(profileimage);
        receivername = findViewById(R.id.receivername);


        receivername.setText(" " + ReceiverName);

        SenderUId = firebaseauth.getUid();


        SenderRoom = SenderUId+ReceiverUid;
        ReceiverRoom = ReceiverUid+SenderUId;

        DatabaseReference reference=database.getReference().child("user").child(firebaseauth.getUid());

        DatabaseReference chatreference=database.getReference().child("chats").child(SenderRoom).child("messages");

        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot : snapshot.getChildren())
                {
                    Messages messages =datasnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              senderImage = snapshot.child("imageuri").getValue().toString();
              receiverImage = ReceiverImage;

          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });


      sendbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String message = editmsg.getText().toString();
              if(message.isEmpty())
              {
                  Toast.makeText(ChatActivity.this, "Enter Any Text", Toast.LENGTH_SHORT).show();
                  return;
              }
              editmsg.setText("");
              Date date = new Date();

              Messages messages = new Messages(message,SenderUId,date.getTime());

              database.getReference().child("chats")
                      .child("SenderRoom")
                      .child("messages")
                      .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {

                      database.getReference().child("chats")
                              .child("ReceiverRoom")
                              .child("messages")
                              .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {

                          }
                      });

                  }
              });


          }
      });








    }
}