package com.example.travisparstagram.fragments;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travisparstagram.Comment;
import com.example.travisparstagram.Post;
import com.example.travisparstagram.R;
import com.example.travisparstagram.UserView;
import com.example.travisparstagram._User;
import com.example.travisparstagram._User;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static androidx.core.content.ContextCompat.startActivity;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {


    private Context context;
    private List<Comment> comments;
    public static final String Tag = "commentsAdapt";


    CommentsAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }



    @Override
    public int getItemCount() {
        return comments.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        public String name;
        public String ID;
        _User user;
        public void bind(Comment post){
            TextView tvTimeDate = itemView.findViewById(R.id.TImeDateComment);
            ImageView ivProfile = itemView.findViewById(R.id.profileComment);
            TextView tvDescription = itemView.findViewById(R.id.textComment);
            TextView tvTopUser = itemView.findViewById(R.id.UsernameComment);
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
            tvTopUser.setText(name);
            tvDescription.setText(post.getText());

            if(user.getImage()!=null) {
                Glide.with(context).load(user.getImage().getUrl()).circleCrop().into(ivProfile);
            }

            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ID = user.getObjectId();

                    Log.i(Tag, ID);
                    goToGrid(ID, name);
                }
            });

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

    public void goToGrid(String Id, String name){
        Intent intent= new Intent(context, UserView.class);
        Log.i(Tag,"name: " + name);
        intent.putExtra("Id", Id);
        intent.putExtra("userName", name);
        startActivity(context, intent, null);
    }

}
