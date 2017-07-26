package com.example.jorge.lapitchat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUserList;
    private DatabaseReference mUserDatabase;
    private CircleImageView mDisplayImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar = (Toolbar) findViewById(R.id.users_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDisplayImage = (CircleImageView) findViewById(R.id.users_single_image);

        mUserList = (RecyclerView) findViewById(R.id.users_list);
        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mUserDatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, Users model, int position) {
                usersViewHolder.setName(model.getName());
                usersViewHolder.setStatus(model.getStatus());
                usersViewHolder.setImage(model.getThumb_image(), getApplicationContext());

                final String user_id = getRef(position).getKey();

                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };

        mUserList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public UsersViewHolder(View itemView){
            super(itemView);

            mView = itemView;
        }
        public void setName(String name){
            TextView mUserNameView = (TextView) mView.findViewById(R.id.user_single_name);
            mUserNameView.setText(name);
        }
        public void setStatus(String status) {
            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);
        }
        public void setImage(String image, Context context) {
            CircleImageView mUserImage = (CircleImageView) mView.findViewById(R.id.users_single_image);
            Picasso.with(context).load(image).into(mUserImage);
        }
        public void setUserImage (String thumb_image, Context context){
            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_status);
            Picasso.with(context).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);
        }
    }
}
