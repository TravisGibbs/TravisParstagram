package com.example.travisparstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PointerEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserView extends AppCompatActivity {
    List<Post> allPosts;
    _User user;
    TextView UserName;
    ImageView profilePic;
    ImageView gridImage1;
    ImageView gridImage2;
    ImageView gridImage3;
    ImageView gridImage4;
    ImageView gridImage5;
    ImageView gridImage6;
    ImageView gridImage7;
    ImageView gridImage8;
    ImageView gridImage9;
    public final static String Tag = "userView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);
        allPosts = new ArrayList<>();
        UserName = findViewById(R.id.UsernameGrid);
        profilePic = findViewById(R.id.profileImageGrid);

        gridImage1 = findViewById(R.id.gridIv1);
        gridImage2 = findViewById(R.id.gridIv2);
        gridImage3 = findViewById(R.id.gridIv3);
        gridImage4 = findViewById(R.id.gridIv4);
        gridImage5 = findViewById(R.id.gridIv5);
        gridImage6 = findViewById(R.id.girdIv6);
        gridImage7 = findViewById(R.id.gridIv7);
        gridImage8 = findViewById(R.id.gridIv8);

        String userId = getIntent().getStringExtra("Id");
        String name = getIntent().getStringExtra("userName");

        findUserName(name);

        UserName.setText(name);



    }

    protected void queryPosts(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //TODO: Find way to get user
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.whereEqualTo(Post.KEY_USER,user);
        query.setLimit(8);
        //if(allPosts.size()>0) {
        //    query.whereLessThan(Post.KEY_CREATED_AT, allPosts.get(allPosts.size() - 1).getCreatedAt());
        //}
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(Tag, "can't get posts!", e);
                    return;
                }
                allPosts.addAll(posts);
                setImages();

            }
        });

    }

    private void findUserName(String username) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    user = (_User) objects.get(0);
                    queryPosts();
                } else {
                    Log.e(Tag,"user not found",e);
                }
            }
        });
    }

    public void setImages() throws IndexOutOfBoundsException{
        if(allPosts.size()>0) {
            Glide.with(this).load(allPosts.get(0).getImage().getUrl()).into(gridImage1);
        }
        if(allPosts.size()>1) {
            Glide.with(this).load(allPosts.get(1).getImage().getUrl()).into(gridImage2);
        }
        if(allPosts.size()>2) {
            Glide.with(this).load(allPosts.get(2).getImage().getUrl()).into(gridImage3);
        }
        if(allPosts.size()>3) {
            Glide.with(this).load(allPosts.get(3).getImage().getUrl()).into(gridImage4);
        }
        if(allPosts.size()>4) {
            Glide.with(this).load(allPosts.get(4).getImage().getUrl()).into(gridImage5);
        }
        if(allPosts.size()>5) {
            Glide.with(this).load(allPosts.get(5).getImage().getUrl()).into(gridImage6);
        }
        if(allPosts.size()>6) {
            Glide.with(this).load(allPosts.get(6).getImage().getUrl()).into(gridImage7);
        }
        if(allPosts.size()>7) {
            Glide.with(this).load(allPosts.get(7).getImage().getUrl()).into(gridImage8);
        }
        if(allPosts.size()>8) {
            Glide.with(this).load(allPosts.get(8).getImage().getUrl()).into(gridImage9);
        }
        Glide.with(this).load(user.getImage().getUrl()).circleCrop().into(profilePic);

    }
}