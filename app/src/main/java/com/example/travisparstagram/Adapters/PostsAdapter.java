package com.example.travisparstagram.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travisparstagram.CommentsActivity;
import com.example.travisparstagram.DataTypes.Post;
import com.example.travisparstagram.R;
import com.example.travisparstagram.UserView;
import com.example.travisparstagram.DataTypes._User;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static androidx.core.content.ContextCompat.startActivity;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {


    private Context context;
    private List<Post> posts;
    public static final String Tag = "postAdapt";


    public PostsAdapter(Context context, List<Post> posts){
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
        private ImageButton buttonComment;
        private ImageButton buttonLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBotUser = itemView.findViewById(R.id.tvUserBot);
            tvTopUser = itemView.findViewById(R.id.tvUserTop);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImagePost = itemView.findViewById(R.id.ivPost);
            ivProfile =itemView.findViewById(R.id.ivProfile);
            tvTimeDate = itemView.findViewById(R.id.TimeData);
            buttonComment = itemView.findViewById(R.id.commentButton);
        }
        public String name;
        public String ID;
        _User user;
        public void bind(final Post post){
            tvDescription.setText(post.getKeyDescription());
            user = post.getUser();
            int radius = 200;
            int margin = 22;
            name = "";

            Log.i("posts",user.getObjectId());
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

            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ID = user.getObjectId();

                    Log.i(Tag, ID);
                    goToGrid(ID, name);
                }
            });

            buttonComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToComments(post);
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

    public void goToComments(Post post){
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra("post", Parcels.wrap(post));
        startActivity(context, intent, null);
    }

}
