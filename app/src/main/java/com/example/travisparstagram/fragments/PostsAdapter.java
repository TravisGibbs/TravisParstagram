package com.example.travisparstagram.fragments;

import android.content.Context;
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
import com.example.travisparstagram.Post;
import com.example.travisparstagram.R;
import com.example.travisparstagram._User;
import com.example.travisparstagram._User;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {


    private Context context;
    private List<Post> posts;
    public static final String Tag = "postAdapt";


    PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTopUser;
        private TextView tvBotUser;
        private TextView tvDescription;
        private ImageView ivProfile;
        private ImageView ivImagePost;
        private TextView tvTimeDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBotUser = itemView.findViewById(R.id.tvUserBot);
            tvTopUser = itemView.findViewById(R.id.tvUserTop);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImagePost = itemView.findViewById(R.id.ivPost);
            ivProfile =itemView.findViewById(R.id.ivProfile);
            tvTimeDate = itemView.findViewById(R.id.TimeData);
        }

        public void bind(Post post){
            tvDescription.setText(post.getKeyDescription());
            _User user = post.getUser();
            int radius = 200;
            int margin = 22;

            Log.i("posts",user.getObjectId());
            String name = "";
            String URL = "";
            try {
                name = user.fetchIfNeeded().getString("username");
            } catch (ParseException e) {
                Log.v(Tag, e.toString());
                e.printStackTrace();
            }
                tvTimeDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
                tvTopUser.setText(name);
                tvBotUser.setText(name);
            if(post.getImage()!=null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivImagePost);
            }
            if(user.getImage()!=null) {
                Glide.with(context).load(user.getImage().getUrl()).circleCrop().into(ivProfile);
            }

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
