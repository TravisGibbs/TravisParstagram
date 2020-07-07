package com.example.travisparstagram.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;

import com.example.travisparstagram.Post;
import com.example.travisparstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class ProfileFragment extends PostsFragment {



    @Override
    protected void queryPosts(){
        {
            ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
            query.setLimit(20);
            query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
            query.addDescendingOrder(Post.KEY_CREATED_AT);
            query.findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> posts, ParseException e) {
                    if (e != null) {
                        Log.e(Tag, "can't get posts!", e);
                        return;
                    }
                    for (Post post : posts) {
                        Log.i(Tag, "post called: " + post.getKeyDescription());
                    }
                    allPosts.addAll(posts);
                    postsAdapter.notifyDataSetChanged();
                }
            });

        }
    }

    public ProfileFragment() {
        // Required empty public constructor
    }





}