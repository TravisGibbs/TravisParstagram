package com.example.travisparstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.travisparstagram.fragments.CommentsAdapter;
import com.example.travisparstagram.fragments.PostsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    List<Comment> allComments;
    protected RecyclerView rvPosts;
    protected CommentsAdapter postsAdapter;
    protected List<Comment> AllComments;
    public final static String Tag = "CommentsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

    }

    protected void queryComments(){
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e != null) {
                    Log.e(Tag, "can't get posts!", e);
                    return;
                }
                allComments.addAll(comments);
                //postsAdapter.notifyDataSetChanged();
                for(Comment c : allComments){
                    Log.i(Tag,(c.getText()));
                }
            }
        });

    }
}