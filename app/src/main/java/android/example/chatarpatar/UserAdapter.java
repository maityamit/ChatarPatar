package android.example.chatarpatar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context homeActivity;
    ArrayList<Users> usersArrayList;


    public UserAdapter(HomeActivity homeActivity, ArrayList<Users> usersArrayList) {
        this.homeActivity = homeActivity;
        this.usersArrayList = usersArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.item_userrow,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = usersArrayList.get(position);

        holder.user_name.setText(users.name);
        holder.user_status.setText(users.status);

        Picasso.get().load(users.ImageUri).into(holder.user_profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity,ChatActivity.class);
                Toast.makeText(homeActivity, "hi", Toast.LENGTH_LONG).show();
               intent.putExtra("name" , users.getName());
                intent.putExtra("ReceiverImage",users.getImageUri());
                intent.putExtra("userid",users.getUid());

                homeActivity.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
      return usersArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView user_profile;
        TextView user_name;
        TextView user_status;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            user_profile = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
           user_status = itemView.findViewById(R.id.user_status);

        }
    }
}
