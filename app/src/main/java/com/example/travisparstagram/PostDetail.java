package com.example.travisparstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.travisparstagram.DataTypes.Post;
import com.example.travisparstagram.DataTypes._User;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PostDetail extends AppCompatActivity {

    private TextView tvTopUser;
    private TextView tvBotUser;
    private TextView tvDescription;
    private ImageView ivProfile;
    private ImageView ivImagePost;
    private TextView tvTimeDate;
    private TextView tvLikes;
    private ImageButton buttonComment;
    private ImageButton buttonLike;
    public String name;
    public String ID;
    _User user;
    public String Tag = "PostDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        tvBotUser = findViewById(R.id.tvUserBotDetail);
        tvTopUser = findViewById(R.id.tvUserTopDetail);
        tvDescription = findViewById(R.id.tvDescriptionDetail);
        ivImagePost = findViewById(R.id.ivPostDetail);
        ivProfile = findViewById(R.id.ivProfileDetail);
        tvTimeDate = findViewById(R.id.TimeDataDetail);
        tvLikes = findViewById(R.id.likesTextDetail);
        buttonComment = findViewById(R.id.commentButtonDetail);
        buttonLike = findViewById(R.id.heartButtonDetail);

        Post post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("post"));

        tvDescription.setText(post.getKeyDescription());
        user = post.getUser();
        name = "";

        String URL = "";
        try {
            name = user.fetchIfNeeded().getString("username");
        } catch (ParseException e) {
            Log.v(Tag, e.toString());
            e.printStackTrace();
        }
        tvTimeDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
        tvLikes.setText(post.getLike());
        tvTopUser.setText(name);
        tvBotUser.setText(name);
        if(post.getImage()!=null) {
            Glide.with(this).load(post.getImage().getUrl()).into(ivImagePost);
        }
        if(user.getImage()!=null) {
            Glide.with(this).load(user.getImage().getUrl()).circleCrop().into(ivProfile);
        }
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}