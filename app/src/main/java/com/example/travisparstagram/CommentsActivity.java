package com.example.travisparstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.travisparstagram.DataTypes.Comment;
import com.example.travisparstagram.DataTypes.Post;
import com.example.travisparstagram.DataTypes._User;
import com.example.travisparstagram.Adapters.CommentsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    List<Comment> allComments;
    protected RecyclerView rvComments;
    protected CommentsAdapter commentsAdapter;
    public final static String Tag = "CommentsActivity";
    EditText userComment;
    Button postButtonComment;
    _User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        rvComments = findViewById(R.id.rvComments);
        allComments = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(this,allComments);
        rvComments.setAdapter(commentsAdapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        queryComments();

        postButtonComment = findViewById(R.id.postCommentButton);
        userComment = findViewById(R.id.eview);

        postButtonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("post"));
                if(!userComment.getText().toString().equals("")){
                    postComment(post);
                }

            }
        });

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
                commentsAdapter.notifyDataSetChanged();
                for(Comment c : allComments){
                    Log.i(Tag,(c.getText()));
                }

            }
        });

    }

    protected void postComment(Post post){
        final Comment comment = new Comment();
        comment.setText(userComment.getText().toString());
        comment.setUser(ParseUser.getCurrentUser());
        comment.setPost(post);

        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(Tag, "comment failed", e);
                }
                Log.i(Tag, "post succ");
                userComment.setText("");
                allComments.add(0,comment);
                commentsAdapter.notifyItemInserted(0);
                rvComments.smoothScrollToPosition(0);
            }
        });
    }
}